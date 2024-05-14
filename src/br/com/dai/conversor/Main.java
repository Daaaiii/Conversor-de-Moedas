package br.com.dai.conversor;

import br.com.dai.conversor.apiConnection.ConectApi;
import br.com.dai.conversor.models.CurrencyConversionResponse;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String baseCurrency = "";
            String targetCurrency = "";

            System.out.println("""
                Seja bem-vindo ao conversor de moedas!
                
                1) Dólar => Peso Argentino
                2) Peso Argentino => Dólar
                3) Dólar => Real Brasileiro
                4) Real Brasileiro => Dólar
                5) Dólar => Peso Colombiano
                6) Peso Colombiano => Dólar
                7) Sair
                Escolha uma opção:
                """);

            int option = scanner.nextInt();
            boolean validOption = true;

            switch (option) {
                case 1 -> {
                    System.out.println("Dólar => Peso Argentino");
                    baseCurrency = "USD";
                    targetCurrency = "ARS";
                }
                case 2 -> {
                    System.out.println("Peso Argentino => Dólar");
                    baseCurrency = "ARS";
                    targetCurrency = "USD";
                }
                case 3 -> {
                    System.out.println("Dólar => Real Brasileiro");
                    baseCurrency = "USD";
                    targetCurrency = "BRL";
                }
                case 4 -> {
                    System.out.println("Real Brasileiro => Dólar");
                    baseCurrency = "BRL";
                    targetCurrency = "USD";
                }
                case 5 -> {
                    System.out.println("Dólar => Peso Colombiano");
                    baseCurrency = "USD";
                    targetCurrency = "COP";
                }
                case 6 -> {
                    System.out.println("Peso Colombiano => Dólar");
                    baseCurrency = "COP";
                    targetCurrency = "USD";
                }
                case 7 -> {
                    System.out.println("Saindo...");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Opção inválida. Tente novamente.");
                    System.out.println("\n********************************************");
                    validOption = false;
                }
            }

            if (validOption) {
                ConectApi api = new ConectApi();
                try {
                    CurrencyConversionResponse response = api.fetchCurrencyConversion(baseCurrency.toUpperCase(), targetCurrency.toUpperCase());
                    if (response != null) {
                        System.out.println("Taxa de "+ baseCurrency + " para " + targetCurrency + " : " + response.getRateForCurrency(targetCurrency.toUpperCase()));
                        System.out.println("\n********************************************");
                    } else {
                        System.out.println("Falha ao obter a resposta da API.");
                        System.out.println("\n********************************************");
                    }
                } catch (RuntimeException e) {
                    System.err.println("Erro ao buscar a conversão de moedas: " + e.getMessage());
                    System.out.println("\n********************************************");
                }
            }
        }
    }
}
