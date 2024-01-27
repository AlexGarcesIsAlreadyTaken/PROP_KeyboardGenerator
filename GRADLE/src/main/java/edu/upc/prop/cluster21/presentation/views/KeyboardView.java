package edu.upc.prop.cluster21.presentation.views;

import edu.upc.prop.cluster21.domain.controllers.CtrlKeyboard;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyJOptionPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
/**
 * JFrame que representa una finestra de gestió de teclats
 */
public class KeyboardView extends JFrame{

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH * 9 / 16;

    private JPanel mainPanel;
    private JPanel matrixPanel;
    private JLabel keyboardNameLabel;
    private JButton addKeyButton;
    private JButton[][] keyboardButtons;

    private JLabel costLabel;

    public KeyboardView(String name, Character[][] keyboard) {
        super("Keyboard View");
        initializeFrameComponents();
        initializeMatrixComponents(keyboard);
        initializePanelComponents(name);
        initializeListeners();
        setContentPane(mainPanel);
    }

    /**
     * Inicialitza els Listeners
     */
    void initializeListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int optionSelected = JOptionPane.showConfirmDialog(null, "Segur que vols tancar el visualitzador?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (optionSelected != JOptionPane.YES_OPTION) return;
                CtrlPresentation.getInstance().closeKeyboardVisualizer();
                dispose();
            }
        });

        addKeyButton.addActionListener(e -> {
            Character answer = MyJOptionPane.addKeyOptionPane();
            if (answer != null) try {
                CtrlPresentation.getInstance().addKey(keyboardNameLabel.getText(), answer);
                Character[][] keyboardDistribution = CtrlPresentation.getInstance().getKeyboardDistribution(keyboardNameLabel.getText());
                for (int i = 0; i < keyboardDistribution.length; i++)
                    for (int j = 0; j < keyboardDistribution[i].length; j++)
                        // si no concorden les tecles és perque no hi ha lletra a la interficie pero en la capa de domini sí
                        // per tant és aquesta la lletra que s'ha de canviar
                        if (keyboardDistribution[i][j] != null && !keyboardButtons[i][j].getText().equals(keyboardDistribution[i][j].toString())) {
                            keyboardButtons[i][j].setEnabled(true);
                            keyboardButtons[i][j].setText(keyboardDistribution[i][j].toString());
                            initializeKeyboardButtonListener(keyboardButtons[i][j], i, j);
                        }
            } catch (ObjectDoesNotExistException ex) {
                JOptionPane.showMessageDialog(null, "No existeix el teclat " + ex.getName(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ObjectAlreadyExistException ex) {
                JOptionPane.showMessageDialog(null, "La tecla " + ex.getName() + " ja és al teclat", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (MaxCapacityExceeded ex) {
                JOptionPane.showMessageDialog(null, "El teclat és ple, no pots afegir més tecles", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InputException | OutputException ex){
                JOptionPane.showMessageDialog(null, "Problemes amb el sistema de fitxers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        initializeKeyboardButtonListeners();
    }

    /**
     * Inicialitza la finestra
     */
    private void initializeFrameComponents() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(getMinimumSize());
        setResizable(true);

        setLocationRelativeTo(null); //Center frame
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    /**
     * Inicialitzen els components del panel
     * @param name nom del teclat
     */
    private void initializePanelComponents(String name) {
        this.mainPanel = new JPanel(new GridBagLayout());
        this.mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.weightx = 0.0;
        grid.fill = GridBagConstraints.BOTH;

        grid.gridwidth = 5;
        grid.weighty = 0.1;
        this.keyboardNameLabel = new JLabel(name);
        this.keyboardNameLabel.setHorizontalAlignment(JLabel.CENTER);
        this.keyboardNameLabel.setFont(this.keyboardNameLabel.getFont().deriveFont(16.0f));
        this.mainPanel.add(this.keyboardNameLabel, grid);

        grid.gridy++;
        grid.weighty = 0.3;
        grid.gridwidth = 5;
        this.mainPanel.add(new JSeparator(SwingConstants.VERTICAL), grid);

        grid.gridy++;
        grid.gridwidth = 1;
        grid.weightx = 0.4;
        grid.weighty = 0.;
        this.mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), grid);

        grid.weightx = 0.8;
        grid.gridx++;
        grid.gridwidth = 3;
        mainPanel.add(matrixPanel, grid);

        grid.gridx++;
        grid.gridwidth = 1;
        grid.weightx = 0.1;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), grid);

        grid.gridy++;
        grid.gridwidth = 5;
        grid.gridx = 0;
        grid.weighty = 0.0;
        costLabel = new JLabel("AVÍS: Optimització no computable");
        this.costLabel.setHorizontalAlignment(JLabel.CENTER);
        this.costLabel.setFont(this.costLabel.getFont().deriveFont(16.0f));
        costLabel.setForeground(Color.RED);
        costLabel.setVisible(CtrlPresentation.getInstance().getKeyboardImprovement(this.keyboardNameLabel.getText()).isNaN());
        mainPanel.add(costLabel, grid);

        grid.gridy++;
        grid.weighty = 0.3;
        grid.weightx = 0.0;
        mainPanel.add(new JSeparator(SwingConstants.VERTICAL), grid);

        grid.weighty = 0.0;
        grid.gridy++;
        grid.weightx = 1.0;
        grid.gridwidth = 1;
        grid.gridx = 0;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), grid);
        ++grid.gridx;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), grid);

        grid.gridx++;
        grid.weightx = 0.0;
        addKeyButton = new JButton("Afegir tecla");
        mainPanel.add(addKeyButton, grid);

        grid.gridx++;
        grid.weightx = 1.0;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), grid);
        grid.gridx++;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), grid);
    }

    /**
     * Inicialitza els components que representen el teclat
     * @param keyboard distribució a representar
     */
    private void initializeMatrixComponents(Character[][] keyboard) {
        matrixPanel = new JPanel(new GridLayout(3, 10));
        this.keyboardButtons = new JButton[keyboard.length][];
        for (int i = 0; i < keyboard.length; i++) {
            this.keyboardButtons[i] = new JButton[keyboard[i].length];
            for (int j = 0; j < keyboard[i].length; j++) {
                // Representa una tecla del teclat
                String text = "";
                if (keyboard[i][j] != null) text = keyboard[i][j].toString();

                this.keyboardButtons[i][j] = new JButton(text);
                if (keyboard[i][j] == null) this.keyboardButtons[i][j].setEnabled(false);
                else if (i == 2 && (j == 0 || j == 9)) this.keyboardButtons[i][j].setEnabled(false);
                matrixPanel.add(this.keyboardButtons[i][j]);
            }
        }
    }

    /**
     * Inicialitza els Listeners de les tecles del teclat
     */
    private void initializeKeyboardButtonListeners() {

        // inicialitzem totes les tecles que tenen lletres i són modificables
        for (int i = 0; i < keyboardButtons.length; i++) {
            for (int j = 0; j < keyboardButtons[i].length; j++) {
                if (keyboardButtons[i][j].isEnabled()) initializeKeyboardButtonListener(keyboardButtons[i][j], i, j);
            }
        }
    }

    /**
     * Inicialitza els Listeners d'una tecla del teclat
     * @param button tecla a inicialitzar
     * @param row fila de la tecla
     * @param col columna de la tecla
     */
    private void initializeKeyboardButtonListener(JButton button, int row, int col) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem changeKey = new JMenuItem("canviar lletra");
        JMenuItem swapKeys = new JMenuItem("intercanviar lletres");
        JMenuItem deleteKey = new JMenuItem("eliminar lletra");

        char c = button.getText().charAt(0);
        popupMenu.add(changeKey);
        popupMenu.add(swapKeys);
        popupMenu.add(deleteKey);
        button.addActionListener(e -> {
            popupMenu.show(button, button.getWidth(), 0);
        });

        changeKey.addActionListener(e -> {
            Character answer = MyJOptionPane.changeKeyOptionPane(c);
            if (answer != null) {
                try {
                    CtrlPresentation.getInstance().changeKey(keyboardNameLabel.getText(), row, col, answer);
                    computeCost();
                    button.setText(answer.toString());
                    pack();
                } catch (ObjectDoesNotExistException ex) {
                    if (ex.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
                        JOptionPane.showMessageDialog(null, "No existeix la lletra " + ex.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    else JOptionPane.showMessageDialog(null, "No existeix el teclat " + ex.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ObjectAlreadyExistException ex) {
                    JOptionPane.showMessageDialog(null, "La lletra " + ex.getName() + " ja és al teclat.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InputException | OutputException ex) {
                    JOptionPane.showMessageDialog(null, "Problemes amb el sistema de fitxers", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        swapKeys.addActionListener(e -> {
            //Character[] keys = new Character[keyboardButton.length*keyboard[0].length - 1];
            ArrayList<Character> keys = new ArrayList<>();
            int i = 0;
            for (JButton[] buttons : keyboardButtons) for (JButton b : buttons) {
                if (b.isEnabled() && !button.getText().equals(b.getText())) keys.add(b.getText().charAt(0)); // el botó és actiu si treballem amb ell i conté una lletra
            }
            Character answer = MyJOptionPane.swapKeysOptionPane(button.getText().charAt(0), keys);
            if (answer == null) return;
            try {
                CtrlPresentation.getInstance().swapKeys(keyboardNameLabel.getText(), button.getText().charAt(0), answer);

                //No trenquem el bucle ja que sabem que hi ha 30
                for (JButton[] buttons : keyboardButtons) for (JButton b : buttons) if (b.getText().equals(answer.toString())) b.setText(button.getText());
                button.setText(answer.toString());
                computeCost();
                pack();
            } catch (ObjectDoesNotExistException ex) {
                if (ex.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
                    JOptionPane.showMessageDialog(null, "La lletra " + ex.getName() + " no és al teclat.", "Error", JOptionPane.ERROR_MESSAGE);
                else JOptionPane.showMessageDialog(null, "No existeix el teclat " + ex.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (OutputException | InputException ex){
                JOptionPane.showMessageDialog(null, "Problemes amb el sistema de fitxers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteKey.addActionListener(e -> {
            int optionSelected = JOptionPane.showConfirmDialog(
                    null, "Segur vols eliminar la lletra " + button.getText() + "? \n" +
                            "<html>Si elimines una de les que són al text o llista amb que <br>has creat el teclat ja no podràs calcular la millora.</html>",
                    "eliminar lletra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );
            if (optionSelected == JOptionPane.YES_OPTION) {
                try {
                    CtrlPresentation.getInstance().deleteKey(keyboardNameLabel.getText(), button.getText().charAt(0));
                    button.setText("");
                    button.setEnabled(false);
                    computeCost();
                    pack();
                } catch (ObjectDoesNotExistException ex) {
                    if (ex.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
                        JOptionPane.showMessageDialog(null, "La tecla " + ex.getName() + " no és al teclat.", "Error", JOptionPane.ERROR_MESSAGE);
                    else JOptionPane.showMessageDialog(null, "No existeix el teclat " + ex.getName() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InputException | OutputException ex){
                    JOptionPane.showMessageDialog(null, "Problemes amb el sistema de fitxers", "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    /**
     * Mostra per pantalla l'empitjorament o la millora del teclat respecta l'ultima versió en cas que sigui computable
     */
    private void computeCost() {
        Double cost = CtrlPresentation.getInstance().getKeyboardImprovement(keyboardNameLabel.getText());
        if (cost.isNaN()) costLabel.setVisible(true);
        else if (cost < 0.0) JOptionPane.showMessageDialog(
                null, "El teclat ha empitjorat un " + String.format("%.2f", -cost) + "%", "Cost de " + keyboardNameLabel.getText(),
                JOptionPane.INFORMATION_MESSAGE);
        else if (cost > 0.0)  JOptionPane.showMessageDialog(
                null, "El teclat ha millorat un " + String.format("%.2f", cost) + "%", "Cost de " + keyboardNameLabel.getText(),
                JOptionPane.INFORMATION_MESSAGE);
        else JOptionPane.showMessageDialog(
                null, "El teclat no ha millorat ni empitjorat", "Cost de " + keyboardNameLabel.getText(),
                    JOptionPane.INFORMATION_MESSAGE);
    }
}
