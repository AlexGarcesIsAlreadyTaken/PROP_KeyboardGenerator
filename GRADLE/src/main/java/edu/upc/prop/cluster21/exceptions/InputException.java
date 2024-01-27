package edu.upc.prop.cluster21.exceptions;

public class InputException extends Exception {
    public InputException() {
        super("S'ha produit un error llegint del fitxer");
    }
}