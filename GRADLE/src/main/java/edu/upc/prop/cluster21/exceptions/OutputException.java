package edu.upc.prop.cluster21.exceptions;

public class OutputException extends Exception {
    public OutputException() {
        super("S'ha produit un error escrivint al fitxer");
    }
}