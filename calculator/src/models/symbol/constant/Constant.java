package models.symbol.constant;

import models.symbol.Symbol;

public class Constant extends Symbol {
    private final double value;
    public Constant(String symbols , double value) {
        super(symbols);
        this.value = value ;
    }

    public double getValue() {
        return value;
    }
}
