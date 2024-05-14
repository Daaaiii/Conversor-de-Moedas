package br.com.dai.conversor.models;

import java.util.Collections;
import java.util.Map;

public record ConversionRates(Map<String, Double> rates) {
    public ConversionRates {
        if (rates == null) {
            rates = Collections.emptyMap();
        }
    }
}

