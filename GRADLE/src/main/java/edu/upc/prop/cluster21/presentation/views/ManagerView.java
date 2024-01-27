package edu.upc.prop.cluster21.presentation.views;

import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * JFrame que representa una finestra de gestió
 */
public class ManagerView extends JFrame {

    public enum PANEL {
        KEYBOARD_PANEL,
        ALPHABET_PANEL,
        FREQUENCY_LIST_PANEL,
    }

    final static int WIDTH = 1200;
    final static int HEIGHT = WIDTH * 9 / 16;
    final static double NUM_AUREO = 1.6180339887;

    private JSplitPane jSplitPane;
    private SidebarPanel sidebarPanel;
    private KeyboardPanel keyboardPanel;
    private AlphabetPanel alphabetPanel;
    private FrequencyListPanel frequencyListPanel;

    /**
     * Constructora de la classe
     * @param user nom d'usuari que ha iniciat sessió
     */
    public ManagerView(String user) {
        super("ManagerViewFrame");
        initializeComponents(user);
        initializeListeners();
    }

    /**
     * Inicialitza els EventListeners de la instància
     */
    private void initializeListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });
    }


    /**
     * Inicialitza els components de la classe
     * @param user usuari que ha iniciat sessió
     */
    private void initializeComponents(String user) {

        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(getMinimumSize());
        setResizable(true);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.sidebarPanel = new SidebarPanel(this, user);
        //this.keyboardPanel = new KeyboardPanel(CtrlPresentation.getInstance().getKeyboardsNames(), this);

        this.jSplitPane = new JSplitPane();
        this.jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.jSplitPane.setLeftComponent(this.sidebarPanel);
        changeManagerPanel(PANEL.ALPHABET_PANEL);

        this.jSplitPane.setDividerLocation(Math.abs(NUM_AUREO - 1)/NUM_AUREO);
        this.jSplitPane.setDividerSize(0);

        setContentPane(this.jSplitPane);
    }

    /**
     * Crida a tancar sessió
     */
    public void logout() {
        int optionSelected = JOptionPane.showConfirmDialog(null, "Segur que vols tancar sessió?", "", JOptionPane.YES_NO_OPTION);
        if (optionSelected != JOptionPane.YES_OPTION) return;
        CtrlPresentation.getInstance().logout();
        CtrlPresentation.getInstance().paintLoginView();
        dispose();
    }

    /**
     * reinicia la instància
     * @param user usuario
     */
    public void restart(String user) {
        sidebarPanel.restart(user);
        changeManagerPanel(PANEL.ALPHABET_PANEL);
    }

    /**
     * Canvia el panel principal que mostra la finestra
     * @param panel panel a mostrar
     */
    public void changeManagerPanel(PANEL panel) {
        switch (panel) {
            case ALPHABET_PANEL:
                alphabetPanel = new AlphabetPanel(CtrlPresentation.getInstance().getAlphabetsNames(), this);
                this.jSplitPane.setRightComponent(alphabetPanel);
                break;
            case FREQUENCY_LIST_PANEL:
                frequencyListPanel = new FrequencyListPanel(CtrlPresentation.getInstance().getFrequencyListsNames(), this);
                this.jSplitPane.setRightComponent(frequencyListPanel);
                break;
            case KEYBOARD_PANEL:
                keyboardPanel = new KeyboardPanel(CtrlPresentation.getInstance().getKeyboardsNames(), this);
                this.jSplitPane.setRightComponent(keyboardPanel);
        }
    }

    /**
     * Crida a eliminar un teclat
     * @param name teclat a eliminar
     * @throws ObjectDoesNotExistException no existeix el teclat
     */
    public void deleteKeyboard(String name) throws ObjectDoesNotExistException, InputException, OutputException {
        CtrlPresentation.getInstance().deleteKeyboard(name);
        changeManagerPanel(PANEL.KEYBOARD_PANEL);
    }

    /**
     * Crida a eliminar un alfabet
     * @param name alfabet a eliminar
     * @throws ObjectDoesNotExistException no existeix l'alfabet
     */
    public void deleteAlphabet(String name) throws ObjectDoesNotExistException, InputException, OutputException {
        CtrlPresentation.getInstance().deleteAlphabet(name);
        changeManagerPanel(PANEL.ALPHABET_PANEL);
    }

    /**
     * Crida a eliminar una llista de freqüències
     * @param name llista de freqüències a eliminar
     * @throws ObjectDoesNotExistException no existeix la llista de freqüències
     */
    public void deleteFrequencyList(String name) throws ObjectDoesNotExistException {
        CtrlPresentation.getInstance().deleteFrequencyList(name);
        changeManagerPanel(PANEL.FREQUENCY_LIST_PANEL);
    }

    /**
     * Crida a crear un teclat amb text
     * @param name nom del teclat
     * @param aName nom de l'alfabet
     * @param text text amb que es crea el teclat
     * @param algorithm algoritme a executar
     * @throws ObjectAlreadyExistException El teclat ja existeix
     * @throws ObjectDoesNotExistException L'alfabet no existex
     */
    public void createKeyboardWithText(String name, String aName, String text, int algorithm) throws ObjectAlreadyExistException, ObjectDoesNotExistException {
        try{
            CtrlPresentation.getInstance().createKeyboardWithText(name, aName, text, algorithm);
        }catch (InputException | OutputException ex){
            JOptionPane.showMessageDialog(null, "Error al sistema de fitxers", "Error", JOptionPane.ERROR_MESSAGE);
        }
        changeManagerPanel(PANEL.KEYBOARD_PANEL);
    }

    /**
     * Crida a crear un teclat amb llista de freqüències
     * @param name nom del teclat
     * @param aName nom de l'alfabet
     * @param fName nom de la llista de freqüències
     * @param algorithm algoritme a executar
     * @throws ObjectAlreadyExistException El teclat ja existeix
     * @throws ObjectDoesNotExistException En funció del tipus o no existeix l'alfabet, o no existeix la llista de freqüencies
     */
    public void createKeyboardWithList(String name, String aName, String fName, int algorithm) throws ObjectAlreadyExistException, ObjectDoesNotExistException {
        try{
            CtrlPresentation.getInstance().createKeyboardWithList(name, aName, fName, algorithm);
        }catch (InputException | OutputException ex){
            JOptionPane.showMessageDialog(null, "Error al sistema de fitxers", "Error", JOptionPane.ERROR_MESSAGE);
        }
        changeManagerPanel(PANEL.KEYBOARD_PANEL);
    }

    /**
     * Crida a crear un alfabet
     * @param name nom de l'alfabet
     * @param text text amb que es crea l'alfabet
     * @throws ObjectAlreadyExistException Ja existeix l'alfabet
     * @throws MaxCapacityExceeded S'ha superat la capacitat maxima de lletres que conté l'alfabet
     */
    public void createAlphabet(String name, String text) throws ObjectAlreadyExistException, MaxCapacityExceeded {
        try {
            CtrlPresentation.getInstance().createAlphabet(name, text);
        } catch (OutputException e) {
            JOptionPane.showMessageDialog(null, "Error a l'intentar crear l'alfabet", "Error", JOptionPane.ERROR_MESSAGE);
        }
        changeManagerPanel(PANEL.ALPHABET_PANEL);
    }

    /**
     * Crida a crear una llista de freqüències
     * @param name nom de la llista de freqüències
     * @param text text amb que es crea la llista de freqüències
     * @throws ObjectAlreadyExistException Ja existeix la llista de freqüències
     * @throws OutputException Hi ha hagut un error escrivint al fitxer de llistes de freqüencies
     */
    public void createFrequencyList(String name, String text) throws ObjectAlreadyExistException, OutputException {
        CtrlPresentation.getInstance().createFrequencyList(name, text);
        changeManagerPanel(PANEL.FREQUENCY_LIST_PANEL);
    }

    /**
     * Crida a importar un alfabet
     * @param filePath direcció on es troba l'alfabet a importar
     */
    public void importAlphabet(String filePath) {
        try {
            CtrlPresentation.getInstance().importAlphabet(filePath);
            changeManagerPanel(PANEL.ALPHABET_PANEL);
        } catch (InputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar importar. Comprova que el format del fitxer sigui l'adequat.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (OutputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar guardar l'alfabet importat", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crida a importar una llista de freqüències
     * @param filePath direcció on es troba la llista de freqüències a importar
     */
    public void importFrequencyList(String filePath) {
        try {
            CtrlPresentation.getInstance().importFrequencyList(filePath);
            changeManagerPanel(PANEL.FREQUENCY_LIST_PANEL);
        }
        catch (InputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar importar. Comprova que el format del fitxer sigui l'adequat.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (OutputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar guardar la llista de freqüències importada", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crida a importar un teclat
     * @param filePath direcció on es troba el teclat a importar
     */
    public void importKeyboard(String filePath) {
        try {
            CtrlPresentation.getInstance().importKeyboard(filePath);
            changeManagerPanel(PANEL.KEYBOARD_PANEL);
        } catch (InputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar importar. Comprova que el format del fitxer sigui l'adequat.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (OutputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar guardar l'alfabet importat", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crida a exportar un alfabet
     * @param name nom de l'alfabet que es vol exportar
     * @param filePath direcció on es vol exportar l'alabet
     */
    public void exportAlphabet(String name, String filePath) {
        try {
            CtrlPresentation.getInstance().exportAlphabet(name, filePath);
        } catch (OutputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crida a exportar una llista de freqüències
     * @param name nom de la llista de freqüències que es vol exportar
     * @param filePath direcció on es vol exportar la llista de freqüències
     */
    public void exportFrequencyList(String name, String filePath) throws OutputException, ObjectDoesNotExistException {
        CtrlPresentation.getInstance().exportFrequencyList(name, filePath);
    }

    /**
     * Crida a exportar un teclat
     * @param name nom del teclat que es vol exportar
     * @param filePath direcció on es vol exportar el teclat
     */
    public void exportKeyboard(String name, String filePath) {
        try {
            CtrlPresentation.getInstance().exportKeyboard(name, filePath);
        } catch (OutputException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar exportar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
