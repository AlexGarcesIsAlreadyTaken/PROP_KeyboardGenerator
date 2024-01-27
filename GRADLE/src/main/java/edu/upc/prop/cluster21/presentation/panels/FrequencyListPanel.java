package edu.upc.prop.cluster21.presentation.panels;

import edu.upc.prop.cluster21.exceptions.MaxCapacityExceeded;
import edu.upc.prop.cluster21.exceptions.ObjectAlreadyExistException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import edu.upc.prop.cluster21.exceptions.OutputException;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyJOptionPane;
import edu.upc.prop.cluster21.presentation.views.ManagerView;

import javax.swing.*;
import java.util.ArrayList;

/**
 * JPanel que representa el panell principal d'un gestor de llistes de freqüències
 */
public class FrequencyListPanel extends ManagerPanel{

    public FrequencyListPanel(ArrayList<String> names, ManagerView parent) {
        super(names, parent);
    }

    /**
     * Inicia l'execució per visualitzar una llista de freqüències
     * @param name nom de la llista de freqüències que es vol visualitzar
     */
    @Override
    public void visualizeElement(String name) {
        try {
            CtrlPresentation.getInstance().visualizeFreqList(name);
        } catch (ObjectDoesNotExistException e) {
            JOptionPane.showMessageDialog(null, "No existeix la llista de freqüències " + e.getName() + ".");
        }
    }

    /**
     * Inicia l'execució per eliminar una llista de freqüències
     * @param name nom de la llista de freqüències que es vol eliminar
     */
    @Override
    public void removeElement(String name) {
        int optionSelected = JOptionPane.showOptionDialog(null, "Segur que vols eliminar la llista de freqüències " + name + "?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (optionSelected == JOptionPane.YES_OPTION) try {
            this.parent.deleteFrequencyList(name);
        } catch (ObjectDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mostra per pantalla un conjunt de preguntes per l'usuari i en funció d'aquestes
     * crea, o no, una llista de freqüències seguint les condicions establertes
     */
    @Override
    protected void createElement() {
        String[] questions = {
                "Introdueix el nom que tindrà la llista de freqüències:",
                "Introdueix el text:"
        };
        String[] answers = MyJOptionPane.createFrequencyListOptionPane();
        if (answers == null) return;
        if (answers[0].isEmpty()) JOptionPane.showMessageDialog(
                null, "Posa-li un nom, va home va."
        );
        else try {
            this.parent.createFrequencyList(answers[0], answers[1]);
        } catch (OutputException | ObjectAlreadyExistException e) {
            JOptionPane.showMessageDialog(null, "La llista de freqüencies ja existeix.\n Proba amb un altre nom");
        }
    }

    /**
     * Inicia l'execució per importar una llista de freqüències
     */
    @Override
    protected void importElement() {
        super.importElement();
        if (importFilePath != null ) this.parent.importFrequencyList(importFilePath);
    }

    /**
     * Inicia l'execució per exportar una llista de freqüències
     * @param name Nom de la llista de freqüències que s'exporta
     */
    @Override
    public void exportElement(String name) {
        super.exportElement(name);
        if (exportFilePath != null) {
            try {
                this.parent.exportFrequencyList(name, exportFilePath);
            } catch (OutputException e) {
                JOptionPane.showMessageDialog(null, "Hi ha hagut algun error d'escriptura", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ObjectDoesNotExistException e) {
                JOptionPane.showMessageDialog(null, "No existeix la llista de freqüències " + e.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
