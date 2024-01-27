package edu.upc.prop.cluster21.presentation.components;

import edu.upc.prop.cluster21.exceptions.InputException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe que hereda de JOptionPane per mostrar un conjunt de preguntes a l'usuari
 * adequades a les necessitats del projecte
 */
public class MyJOptionPane  extends JOptionPane {

    /**
     * Mostra les preguntes necessaries per crear un alfabet
     * @return respostes a les preguntes :
     *                            <ol>
     *                            <li> nom de l'alfabet
     *                            <li> text per crear l'alfabet
     *                            </ol>
     */
    public static String[] createAlphabetOptionPane() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 1.0;

        panel.add(new JLabel("Introdueix el nom de l'alfabet"), grid);

        grid.gridy++;
        JTextField nameTextField = new JTextField();
        panel.add(nameTextField, grid);

        grid.gridy++;
        panel.add(new JLabel("Introdueix els símbols de l'alfabet (de qualsevol forma, junts o amb espais):"), grid);

        grid.gridy++;
        JTextField alphabetTextField = new JTextField();
        panel.add(alphabetTextField, grid);

        int optionSelected = showConfirmDialog(null, panel, "", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION)
            return new String[]{
                    nameTextField.getText(),
                    alphabetTextField.getText()
            };
        return null;
    }

    /**
     * Mostra les preguntes necessaries per crear una llista de freqüències
     * @return respostes a les preguntes :
     *                            <ol>
     *                            <li> nom de la llista
     *                            <li> text per crear la llista
     *                            </ol>
     */
    public static String[] createFrequencyListOptionPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 1.0;

        panel.add(new JLabel("Introdueix el nom de la llista de freqüències"), grid);

        grid.gridy++;
        JTextField nameTextField = new JTextField();
        panel.add(nameTextField, grid);

        ++grid.gridy;
        panel.add(new JLabel("Introdueix el text o carrega'l"), grid);
        ++grid.gridy;
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setLineWrap(true);
        grid.weighty = 1.0;
        JScrollPane jScrollPane = new JScrollPane(textArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(jScrollPane, grid);

        grid.weighty = 0.0;
        ++grid.gridy;
        final JButton loadTextButton = loadTextButton(textArea);
        panel.add(loadTextButton, grid);

        int optionSelected = showConfirmDialog(null, panel, "", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION)
            return new String[] {
                    nameTextField.getText(),
                    textArea.getText()
            };

        return null;
    }

    /**
     * @param textArea textarea on es carregarà el text
     * @return Botó per carregar un text
     */
    private static JButton loadTextButton(JTextArea textArea) {
        JButton carregarTextButton = new JButton("Carregar Text");
        carregarTextButton.addActionListener(e -> {
            String userDirLocation = System.getProperty("user.dir");
            File userDir = new File(userDirLocation);
            JFileChooser fileChooser = new JFileChooser(userDir);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String text = CtrlPresentation.getInstance().loadText(fileChooser.getSelectedFile().getAbsolutePath());
                    textArea.setText(text);
                } catch (InputException ex) {
                    JOptionPane.showMessageDialog(null, "Hi ha hagut algun error de lectura.");
                }
            }
        });
        return carregarTextButton;
    }

    /**
     * Mostra les preguntes necessaries per crear un teclat amb text
     * @return respostes a les preguntes :
     *                            <ol>
     *                            <li> nom del teclat
     *                            <li> nom de l'alfabet
     *                            <li> text
     *                            <li> algoritme
     *                            </ol>
     */
    public static String[] createKeyboardWithTextOptionPane() {
        ArrayList<String> aNames = CtrlPresentation.getInstance().getAlphabetsNames();
        if (aNames.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "No tens cap alfabet. \nNecessitaràs un per crear el teclat. "
            );
            return null;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.BOTH;
        grid.weightx = 1.0;

        panel.add(new JLabel("Introdueix el nom del nou teclat"), grid);
        ++grid.gridy;
        JTextField keyboardName = new JTextField();
        panel.add(keyboardName, grid);

        ++grid.gridy;
        panel.add(new JLabel("Selecciona un dels alfabets"), grid);
        ++grid.gridy;
        String[] aNamesArray = new String[aNames.size()];
        for (int i = 0; i < aNamesArray.length; i++) aNamesArray[i] = aNames.get(i);
        JComboBox<String> comboBoxAlphabet = new JComboBox<String>(aNamesArray);
        panel.add(comboBoxAlphabet, grid);

        ++grid.gridy;
        panel.add(new JLabel("Introdueix el text o carrega'l"), grid);
        ++grid.gridy;
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setLineWrap(true);
        grid.weighty = 1.0;
        JScrollPane jScrollPane = new JScrollPane(textArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(jScrollPane, grid);

        grid.weighty = 0.0;
        ++grid.gridy;
        JButton loadTextButton = loadTextButton(textArea);
        panel.add(loadTextButton, grid);

        ++grid.gridy;
        panel.add(new JLabel("Selecciona un dels algoritmes"), grid);
        ++grid.gridy;
        JComboBox<String> comboBoxAlgorithm = new JComboBox<>(new String[]{"Simulated Annealing", "Branch & Bound - lent a partir de 10 tecles"});
        panel.add(comboBoxAlgorithm, grid);
        int optionSelected = showConfirmDialog(null, panel, "", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION)
            return new String[]{
                    keyboardName.getText(),
                    (String) comboBoxAlphabet.getSelectedItem(),
                    textArea.getText(),
                    (String) comboBoxAlgorithm.getSelectedItem()
            };

        return null;
    }

    /**
     * Mostra les preguntes necessaries per crear un teclat amb llistes de freqüències
     * @return respostes a les preguntes :
     *                            <ol>
     *                            <li> nom del teclat
     *                            <li> nom de l'alfabet
     *                            <li> nom de la llista
     *                            <li> algoritme
     *                            </ol>
     */
    public static String[] createKeyboardWithListOptionPane() {
        ArrayList<String> aNames = CtrlPresentation.getInstance().getAlphabetsNames();
        if (aNames.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "No tens cap alfabet. \nNecessitaràs un per crear el teclat. "
            );
            return null;
        }
        ArrayList<String> fNames = CtrlPresentation.getInstance().getFrequencyListsNames();
        if (fNames.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "No tens cap llista de freqüències. \nNecessitaràs una per crear el teclat. "
            );
            return null;
        }


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 1.0;

        panel.add(new JLabel("Introdueix el nom del nou teclat"), grid);
        ++grid.gridy;
        JTextField keyboardName = new JTextField();
        panel.add(keyboardName, grid);

        ++grid.gridy;
        panel.add(new JLabel("Selecciona un dels alfabets"), grid);
        ++grid.gridy;
        String[] aNamesArray = new String[aNames.size()];
        for (int i = 0; i < aNamesArray.length; i++) aNamesArray[i] = aNames.get(i);
        JComboBox<String> comboBoxAlphabet = new JComboBox<String>(aNamesArray);
        panel.add(comboBoxAlphabet, grid);

        ++grid.gridy;
        panel.add(new JLabel("Selecciona una de les llistes de freqüencies"), grid);
        ++grid.gridy;
        String[] fNamesArray = new String[fNames.size()];
        for (int i = 0; i < fNamesArray.length; i++) fNamesArray[i] = fNames.get(i);
        JComboBox<String> comboBoxFreqList = new JComboBox<String>(fNamesArray);
        panel.add(comboBoxFreqList, grid);

        ++grid.gridy;
        panel.add(new JLabel("Selecciona un dels algoritmes"), grid);
        ++grid.gridy;
        JComboBox<String> comboBoxAlgorithm = new JComboBox<>(new String[]{"Simulated Annealing", "Branch & Bound - lent a partir de 10 tecles"});
        panel.add(comboBoxAlgorithm, grid);
        int optionSelected = showConfirmDialog(null, panel, "", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION)
            return new String[]{
                    keyboardName.getText(),
                    (String) comboBoxAlphabet.getSelectedItem(),
                    (String) comboBoxFreqList.getSelectedItem(),
                    (String) comboBoxAlgorithm.getSelectedItem()
            };

        return null;
    }

    /** Mostra les preguntes necessaries per canviar la lletra d'un teclat
     * @param key lletra a cambiar
     * @return lletra per la que es vol canviar key
     */
    public static Character changeKeyOptionPane(char key) {
        JTextField answerField = new JTextField(1);
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (!answerField.getText().isEmpty()) {
                    char c = answerField.getText().charAt(0);
                    if (answerField.getText().length() > 1) answerField.setText(Character.toString(c));
                }
            }
        });
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        panel.add(new JLabel("Quina lletra vols en canvi de " + key + "?"), grid);
        ++grid.gridy;

        panel.add(new JLabel("<html>Si la canvies per una que no és al text o llista amb que has <br>creat el teclat ja no es podra calcular la millora.</html>"), grid);
        grid.gridy++;

        panel.add(answerField, grid);
        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "canvia lletra", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION && !answerField.getText().isEmpty())
            return answerField.getText().charAt(0);
        return null;
    }

    /**
     * Mostra les preguntes necessaries per afegir una lletra a un teclat
     * @return Lletra que es vol afegir
     */
    public static Character addKeyOptionPane() {
        String question = "Quina lletra vols afegir al teclat?";
        JTextField answerField = new JTextField(1);
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (!answerField.getText().isEmpty()) {
                    char c = answerField.getText().charAt(0);
                    if (answerField.getText().length() > 1) answerField.setText(Character.toString(c));
                }
            }
        });
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        JLabel questionLabel = new JLabel(question);
        questionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        panel.add(questionLabel, grid);
        ++grid.gridy;
        panel.add(answerField, grid);
        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "afegir lletra", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION )
            if (answerField.getText().isEmpty()) JOptionPane.showMessageDialog(null, "No has indicat quina lletra vols afegir", "Error", JOptionPane.ERROR_MESSAGE);
            else return answerField.getText().charAt(0);
        return null;
    }

    /**
     * Mostra les preguntes necessàries per intercanviar dues lletres d'un teclat
     * @param key lletra que es vol intercanviar
     * @param keys conjunt de lletres amb que es pot intercanviar
     * @return lletra amb que s'intercanviarà
     */
    public static Character swapKeysOptionPane(Character key, ArrayList<Character> keys) {

        //Panel contenidor de la pregunta
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.gridx = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 0.0;
        grid.weighty = 0.0;

        grid.gridwidth = 3;
        JLabel questionLabel = new JLabel("Quina lletra vols intercanviar amb " + key + "?");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(questionLabel, grid);

        grid.gridy++;
        grid.gridwidth = 1;
        grid.weightx = 1.0/3.0;
        JLabel keyLabel = new JLabel(key.toString());
        keyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(keyLabel, grid);
        ++grid.gridx;
        JLabel symbolLabel = new JLabel("↔");
        symbolLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(symbolLabel, grid);
        ++grid.gridx;
        //Aqui mostrem totes les tecles amb que podem intercanviar key
        Character[] aux = new Character[keys.size()];
        for (int i = 0; i < aux.length; i++) aux[i] = keys.get(i);
        JComboBox<Character> optionsComboBox = new JComboBox<Character>(aux);
        optionsComboBox.setAlignmentX(0.5f);
        panel.add(optionsComboBox, grid);

        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "intercanvia lletres", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION)  return (Character) optionsComboBox.getSelectedItem();
        return null;
    }

    /**
     * Funció que crea la finestra necessària per realitzar l'acció d'afegir un símbol a un alfabet
     * La finestra es conforma d'un missatge indicatiu i un textfield que només permet afegir 1 símbol
     * La acció només es realaitzarà si s'ha introduit un caràcter
     * @return Es retorna la lletra introduida per l'usuari perquè la view pugui fer els canvis pertinents
     */
    public static Character addLetterOptionPane() {
        String question = "Quina lletra vols afegir a l'alfabet?";
        JTextField answerField = new JTextField(1);
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (!answerField.getText().isEmpty()) {
                    char c = answerField.getText().charAt(0);
                    if (answerField.getText().length() > 1) answerField.setText(Character.toString(c));
                }
            }
        });
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        JLabel questionLabel = new JLabel(question);
        questionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        panel.add(questionLabel, grid);
        ++grid.gridy;
        panel.add(answerField, grid);
        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "afegir lletra", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION && !answerField.getText().isEmpty())
            return answerField.getText().charAt(0);
        return null;
    }

    /**
     * Funció que crea la finestra necessària per eliminar un símbol del conjunt de l'alfabet actual
     * Per tal d'evitar errors per part de l'usuari, es mostra una comboBox amb totes les posibles opcions a eliminar
     * així l'usuari en selecciona una i l'elimina.
     * @param name Nom de l'alfabet que s'està modificant
     * @return Es retorna el símbol seleccionat per eliminar, la View s'encarregará d'acabar la acció
     */
    public static Character deleteLetterOptionPane(String name) {
        String question = "Quina lletra vols eliminar de l'alfabet?";

        Set<Character> aux = null;
        try {
            aux = CtrlPresentation.getInstance().getAlphabet(name);
        } catch (ObjectDoesNotExistException e) {
            JOptionPane.showMessageDialog(null, "L'alfabet " + e.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Character[] vaux = new Character[aux.size()];
        int i = 0;
        for (Character symbol: aux) {
            vaux[i] = symbol;
            i++;
        }
        JComboBox<Character> eraseOption = new JComboBox<>(vaux);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        JLabel questionLabel = new JLabel(question);
        questionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        panel.add(questionLabel, grid);
        ++grid.gridy;
        eraseOption.setAlignmentX(0.5f);
        panel.add(eraseOption, grid);
        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "Elimina la lletra seleccionada", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION) {
            int confirmErase = JOptionPane.showConfirmDialog(
                    null, "Segur vols eliminar la lletra?",
                    "Eliminar lletra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );
            if (confirmErase == JOptionPane.YES_OPTION) return (Character) eraseOption.getSelectedItem();
        }
        return null;
    }

    /**
     * Funció que genera la finestra necessària per afegir una paraula i la seva freqüència a la llista actual
     * Es composa de dos textos indicatius i dos textFields, un per introduir la paraula i la segona per afegir la seva freqüència
     * @return Retorna una entrada (String, Integer) que servirá per afegir-ho a la llista actual
     */
    public static Map.Entry<String, Integer> addWordOptionPane() {
        String question = "Quina paraula vols afegir a la llista?";
        JLabel questionLabel = new JLabel(question);
        JTextField answerField = new JTextField(1);
        String question2 = "Quina freqüència tindrà?";
        JLabel questionLabel2 = new JLabel(question2);
        JTextField answerField2 = new JTextField(1);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        panel.add(questionLabel, grid);
        ++grid.gridy;
        panel.add(answerField, grid);
        ++grid.gridy;
        panel.add(questionLabel2, grid);
        ++grid.gridy;
        panel.add(answerField2, grid);

        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "afegir paraula i la freqüència", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION && !answerField.getText().isEmpty()) {
            String aux1 = answerField.getText();
            Integer aux2 = null;
            try {
                aux2 = Integer.parseInt(answerField2.getText());
            } catch (NumberFormatException nf) {
                JOptionPane.showMessageDialog(null, "Afegir un Integer com a freqüència, siusplau");
            }
            if (aux2 != null) {
                Map.Entry<String, Integer> entry = Map.entry(aux1, aux2);
                return entry;
            }
            return null;
        }
        return null;
    }

    /**
     * Funció que genera la finestra necessària per eliminar una paraula de la llista
     * @param name Nom de la llista que s'està modificant
     * @return retorna un string amb la paraula que s'ha d'eliminar de la llista
     */
    public static String deleteWordOptionPane(String name) {
        String question = "Quina paraula vols eliminar de la llista?";

        HashMap<String, Integer> aux = null;
        try {
            aux = CtrlPresentation.getInstance().getWordFreq(name);
        } catch (ObjectDoesNotExistException ex) {
            JOptionPane.showMessageDialog(null, "La Llista de Freqüències " + ex.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String[] vaux = new String[aux.size()];
        int i = 0;
        for (String key : aux.keySet()) {
            vaux[i] = key;
            i++;
        }
        JComboBox<String> eraseOption = new JComboBox<>(vaux);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        JLabel questionLabel = new JLabel(question);
        questionLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        panel.add(questionLabel, grid);
        ++grid.gridy;
        eraseOption.setAlignmentX(0.5f);
        panel.add(eraseOption, grid);

        int optionSelected = JOptionPane.showConfirmDialog(null, panel, "Elimina la paraula seleccionada", OK_CANCEL_OPTION, QUESTION_MESSAGE);
        if (optionSelected == JOptionPane.OK_OPTION) {
            int confirmErase = JOptionPane.showConfirmDialog(
                    null, "Segur vols eliminar la paraula?",
                    "Eliminar lletra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );
            if (confirmErase == JOptionPane.YES_OPTION) return (String) eraseOption.getSelectedItem();
        }
        return null;
    }
}
