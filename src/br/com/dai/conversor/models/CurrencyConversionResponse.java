package br.com.dai.conversor.models;

import java.util.Collections;


public record CurrencyConversionResponse(
        String result,
        String documentation,
        String terms_of_use,
        long time_last_update_unix,
        String time_last_update_utc,
        long time_next_update_unix,
        String time_next_update_utc,
        String base_code,
        ConversionRates conversion_rates
) {
    public CurrencyConversionResponse {
        if (conversion_rates == null) {
            conversion_rates = new ConversionRates( Collections.emptyMap());
        }
    }

    public Double getRateForCurrency(String currencyCode) {
        if (conversion_rates == null || conversion_rates.rates() == null) {
            System.out.println("conversion_rates ou conversion_rates.rates() é null.");
            return null;
        }

        Double rate = conversion_rates.rates().get(currencyCode);
        if (rate == null) {
            System.out.println("Taxa para " + currencyCode + " não encontrada.");
        }
        return rate;
    }


    public void printAllRates() {
        if (conversion_rates == null || conversion_rates.rates() == null) {
            System.out.println("conversion_rates ou conversion_rates.rates() é null.");
            return;
        }

        conversion_rates.rates().forEach((currency, rate) -> System.out.println(currency + ": " + rate));
    }
}

