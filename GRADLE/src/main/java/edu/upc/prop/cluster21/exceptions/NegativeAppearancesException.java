package edu.upc.prop.cluster21.exceptions;

public class NegativeAppearancesException extends Exception{
    public NegativeAppearancesException() {
        super("No es pot tenir un nombre negatiu d'aparicions");
    }

    public NegativeAppearancesException(String s) {
        super(s);
    }
}