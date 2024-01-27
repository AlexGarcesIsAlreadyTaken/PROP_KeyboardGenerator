package edu.upc.prop.cluster21.presentation.panels;

import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyJOptionPane;
import edu.upc.prop.cluster21.presentation.views.ManagerView;

import javax.swing.*;
import java.util.ArrayList;

/**
 * JPanel que representa el panell principal d'un gestor de teclats
 */
public class KeyboardPanel extends ManagerPanel{

        /**
         * Constructora de la classe
         * @param names noms dels teclats que es llisten
         * @param parent instància que l'ha creat
         */
    public KeyboardPanel(ArrayList<String> names, ManagerView parent) {
        super(names, parent);
    }

    /**
     * Inicia l'execució per visualitzar un teclat
     * @param name nom del teclat que es vol visualitzar
     */
    @Override
    public void visualizeElement(String name) {
        try {
            CtrlPresentation.getInstance().visualizeKeyboard(name);
        } catch (ObjectDoesNotExistException e) {
            JOptionPane.showMessageDialog(null, "No existeix el teclat " + e.getName() + ".");
        }
    }

    /**
     * Inicia l'execució per eliminar un teclat
     * @param name nom del teclat que es vol eliminar
     */
    @Override
    public void removeElement(String name)  {
        int optionSelected = JOptionPane.showOptionDialog(null, "Segur que vols eliminar el teclat " + name + "?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (optionSelected == JOptionPane.YES_OPTION) try {
            this.parent.deleteKeyboard(name);
        } catch (ObjectDoesNotExistException e) {
            throw new RuntimeException(e);
        }catch (InputException | OutputException ex){
            JOptionPane.showMessageDialog(null, "Error amb el sistema de fitxers");
        }
    }


    /**
     * Mostra per pantalla un conjunt de preguntes per l'usuari i en funció d'aquestes
     * crea, o no, un teclat seguint les condicions establertes
     */
    @Override
    protected void createElement() {
        String aux = "Vols crear-lo a partir d'una llista de freqüencies ja existent o d'un text?";
        String[] options = {"Llista de freqüencies", "Text", "Cancelar"};
        int optionSelected = JOptionPane.showOptionDialog(null, aux, "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        System.out.println(optionSelected);
        if (optionSelected == JOptionPane.CANCEL_OPTION) return;
        if (optionSelected == JOptionPane.YES_OPTION) createKeyboardWithFreqList();
        else createKeyboardWithText();


    }

    private void createKeyboardWithFreqList() {
        String[] answers = MyJOptionPane.createKeyboardWithListOptionPane();
        if (answers == null) return;
        if (answers[0].isEmpty()) {
            JOptionPane.showMessageDialog(null, "Posa-li un nom al teclat", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (answers[0].contains(" ")) {
            JOptionPane.showMessageDialog(null, "No posis espais al nom", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int algorithm;
        if (answers[3].equals("Simulated Annealing")) algorithm = 2;
        else {
            algorithm = 1;
            int confirmation = JOptionPane.showConfirmDialog(
                    null,"<html>Has seleccionat Branch & Bound <br>Aquest algoritme es molt lent a partir de 10 lletres <br> Segur vols utilitzar-lo?</html>",
                    "", JOptionPane.YES_NO_OPTION
            );
            if (confirmation != JOptionPane.YES_OPTION) return;
        }
        try {
            parent.createKeyboardWithList(answers[0], answers[1], answers[2], algorithm);
        } catch (ObjectAlreadyExistException e) {
            JOptionPane.showMessageDialog(
                    null, "Ja existeix el teclat " + e.getName() + ".",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        } catch (ObjectDoesNotExistException e) {
            if (e.getTypeObject() == ObjectExistenceException.TypeObject.ALPHABET)
                JOptionPane.showMessageDialog(
                        null, "No existeix l'alfabet " + e.getName() + ".",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            else if (e.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
                JOptionPane.showMessageDialog(
                        null, "La lletra " + e.getName() + " no pertany a l'alfabet.",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
            else JOptionPane.showMessageDialog(
                        null, "No existeix la llista de freqüencies " + e.getName() + ".",
                        "Error", JOptionPane.ERROR_MESSAGE
                );
        }
    }

    private void createKeyboardWithText() {
        String[] answers = MyJOptionPane.createKeyboardWithTextOptionPane();
        if (answers == null) return;
        if (answers[0].isEmpty()) {
            JOptionPane.showMessageDialog(null, "Posa-li un nom al teclat", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (answers[0].contains(" ")) {
            JOptionPane.showMessageDialog(null, "No posis espais al nom", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int algorithm;
        if (answers[3].equals("Simulated Annealing")) algorithm = 2;
        else {
            algorithm = 1;
            int confirmation = JOptionPane.showConfirmDialog(
                    null,"<html>Has seleccionat Branch & Bound <br>Aquest algoritme es molt lent a partir de 10 lletres <br> Segur vols utilitzar-lo?</html>",
                    "", JOptionPane.YES_NO_OPTION
            );
            if (confirmation != JOptionPane.YES_OPTION) return;
        }
        try {
            parent.createKeyboardWithText(answers[0], answers[1], answers[2], algorithm);
        } catch (ObjectAlreadyExistException e) {
            JOptionPane.showMessageDialog(
                    null, "Ja existeix el teclat " + e.getName() + ".",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        } catch (ObjectDoesNotExistException e) {
            if (e.getTypeObject() == ObjectExistenceException.TypeObject.ALPHABET) JOptionPane.showMessageDialog(
                    null, "No existeix l'alfabet " + e.getName() + ".",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            else if (e.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
                if (e.getName().equals("\n")) System.out.println("cagada monumental");
            JOptionPane.showMessageDialog(
                    null, "La lletra " + e.getName() + " no pertany a l'alfabet.",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Inicia l'execució per importar un teclat
     */
    @Override
    protected void importElement() {
        super.importElement();
        if (importFilePath != null) this.parent.importKeyboard(importFilePath);
    }

    /**
     * Inicia l'execució per exportar un teclat
     * @param name Nom del teclat que s'exporta
     */
    @Override
    public void exportElement(String name) {
        super.exportElement(name);
        if (exportFilePath != null) this.parent.exportKeyboard(name, exportFilePath);
    }
}
