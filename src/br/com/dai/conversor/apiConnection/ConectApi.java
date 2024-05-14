package br.com.dai.conversor.apiConnection;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.com.dai.conversor.models.ConversionRates;
import br.com.dai.conversor.models.CurrencyConversionResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConectApi {
    private static final String API_KEY = "5c40137979425f8374bb2db5";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Set<String> FILTERED_CURRENCIES = Set.of("ARS", "BOB", "BRL", "CLP", "COP", "USD");

    public CurrencyConversionResponse fetchCurrencyConversion(String baseCurrency, String targetCurrency) {
        String url = BASE_URL + API_KEY + "/pair/" + baseCurrency + "/" + targetCurrency;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();


        try {
            HttpResponse<String> response = CLIENT.send(request, BodyHandlers.ofString());
            return parseCurrencyConversionResponse(response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch currency conversion", e);
        }
    }

    private CurrencyConversionResponse parseCurrencyConversionResponse(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        String result = jsonObject.get("result").getAsString();
        String documentation = jsonObject.get("documentation").getAsString();
        String termsOfUse = jsonObject.get("terms_of_use").getAsString();
        long timeLastUpdateUnix = jsonObject.get("time_last_update_unix").getAsLong();
        String timeLastUpdateUtc = jsonObject.get("time_last_update_utc").getAsString();
        long timeNextUpdateUnix = jsonObject.get("time_next_update_unix").getAsLong();
        String timeNextUpdateUtc = jsonObject.get("time_next_update_utc").getAsString();
        String baseCode = jsonObject.get("base_code").getAsString();
        Map<String, Double> conversionRatesMap = new HashMap<>();

        if (jsonObject.has("conversion_rates")) {
            JsonObject conversionRatesJson = jsonObject.get("conversion_rates").getAsJsonObject();
            for (String key : FILTERED_CURRENCIES) {
                if (conversionRatesJson.has(key)) {
                    conversionRatesMap.put(key, conversionRatesJson.get(key).getAsDouble());
                }
            }
        } else if (jsonObject.has("conversion_rate")) {
            String targetCode = jsonObject.get("target_code").getAsString();
            double conversionRate = jsonObject.get("conversion_rate").getAsDouble();
            conversionRatesMap.put(targetCode, conversionRate);
        }

        ConversionRates conversionRates = new ConversionRates(conversionRatesMap);

        return new CurrencyConversionResponse(
                result,
                documentation,
                termsOfUse,
                timeLastUpdateUnix,
                timeLastUpdateUtc,
                timeNextUpdateUnix,
                timeNextUpdateUtc,
                baseCode,
                conversionRates
        );
    }
}
