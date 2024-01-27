package edu.upc.prop.cluster21.presentation.panels;

import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyJOptionPane;
import edu.upc.prop.cluster21.presentation.views.ManagerView;

import javax.swing.*;
import java.util.ArrayList;

/**
 * JPanel que representa el panell principal d'un gestor d'alfabets
 */
public class AlphabetPanel extends ManagerPanel{
    public AlphabetPanel(ArrayList<String> names, ManagerView parent) {
        super(names, parent);
    }

    /**
     * Inicia l'execució per visualitzar un alfabet
     * @param name nom de l'alfabet que es vol visualitzar
     */
    @Override
    public void visualizeElement(String name) {
        try {
            CtrlPresentation.getInstance().visualizeAlphabet(name);
        } catch (ObjectDoesNotExistException e) { //Això no hauria de passar mai
            JOptionPane.showMessageDialog(null, "L'alfabet " + e.getName() + " no existeix.");
        }
    }

    /**
     * Inicia l'execució per eliminar un alfabet
     * @param name nom de l'alfabet que es vol eliminar
     */
    @Override
    public void removeElement(String name) {
        int optionSelected = JOptionPane.showOptionDialog(null, "Segur que vols eliminar l'alfabet " + name + "?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (optionSelected == JOptionPane.YES_OPTION) try {
            this.parent.deleteAlphabet(name);
        } catch (ObjectDoesNotExistException e) {
            JOptionPane.showMessageDialog(null, "L'alfabet " + e.getName() + " no existeix.");
        }catch (InputException | OutputException ex){
            JOptionPane.showMessageDialog(null, "Error amb el sistema de fitxers");
        }
        ;
    }

    /**
     * Mostra per pantalla un conjunt de preguntes per l'usuari i en funció d'aquestes
     * crea, o no, un alfabet seguint les condicions establertes
     */
    @Override
    protected void createElement() {
        String[] answers = MyJOptionPane.createAlphabetOptionPane();
        if (answers == null) return;
        if (answers[0].isEmpty()) JOptionPane.showMessageDialog(
                null, "Posa-li un nom, va home va."
        );
        else try {
            this.parent.createAlphabet(answers[0], answers[1]);
        } catch (ObjectAlreadyExistException e) {
            JOptionPane.showMessageDialog(null, "L'alfabet " + e.getName() + " ja existeix.\n Proba amb un altre nom.");
        } catch (MaxCapacityExceeded e) {
            JOptionPane.showMessageDialog(null, "Un alfabet no pot tenir més de " + e.getCapacity() + " lletres.");
        }
    }

    /**
     * Inicia l'execució per importar un alfabet
     */
    @Override
    protected void importElement() {
        super.importElement();
        if (importFilePath != null) this.parent.importAlphabet(this.importFilePath);
    }

    /**
     * Inicia l'execució per exportar un alfabet
     * @param name Nom de l'alfabet que s'exporta
     */
    @Override
    public void exportElement(String name) {
        super.exportElement(name);
        if (exportFilePath != null) this.parent.exportAlphabet(name, exportFilePath);
    }
}
