package edu.upc.prop.cluster21.presentation.views;

import java.io.FileNotFoundException;

import edu.upc.prop.cluster21.domain.classes.Alphabet;
import edu.upc.prop.cluster21.domain.classes.FrequencyList;
import edu.upc.prop.cluster21.domain.classes.Keyboard;
import edu.upc.prop.cluster21.exceptions.MaxCapacityExceeded;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;

import javax.swing.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    //Inicialitzem el programa en versio terminal (vista terminal, nomes per la primera entrega)
    CtrlPresentation.getInstance().paintLoginView();
  }
}
