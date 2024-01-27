package edu.upc.prop.cluster21.exceptions;

public class MatrixIsNotSquare extends Exception {
    public MatrixIsNotSquare() {
        super("La matriu passada al Hungarian Algorithm no es cuadrada");
    }
}