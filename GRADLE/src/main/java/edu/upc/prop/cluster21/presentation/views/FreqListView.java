package edu.upc.prop.cluster21.presentation.views;

import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyJOptionPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/**
 * JFrame que representa una finestra de gestió de llistes de freqüències
 */
public class FreqListView extends JFrame {
    final static int WIDTH = 800;
    final static int HEIGHT = WIDTH * 9 / 16;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel freqListPanel;
    private JScrollPane scrollPane;
    private JLabel titleLabel;
    private JButton afegirButton;
    private JButton eliminarButton;
    private HashMap<String, JSpinner> spinnersMap;


    /**
     * Això és el visualitzador d'una llista de freqüències per a comprovar-la i modificar-la. La funció de getFreqs estarà
     * a la pantalla anterior
     * @param freqs Conjunt de paraules amb les seves freqüències que conformen la llista
     * @param name Nom de la llista que es visualitza
     */
    public FreqListView(String name, HashMap<String, Integer> freqs) {
        super("FreqList View");
        initializeComponents(name, freqs);
        initializeListeners(name);
        initializeFreqListListeners(name);
        initializeFrameComponents();
    }

    /**
     * Funció que inicialitza el frame de la view i estableix les seves característiques
     */
    void initializeFrameComponents() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(getMinimumSize());
        setResizable(true);

        setLocationRelativeTo(null); //Center frame
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Funció que inicialitza els diferents panells i botons que conformaran la view
     * @param name Nom de la llista a visualitzar
     * @param freqs Conjunt de paraules i les seves freqüències que conformen la llista
     */
    private void initializeComponents(String name, HashMap<String, Integer> freqs) {
        // Panel para organizar los componentes
        this.mainPanel = new JPanel(new BorderLayout());

        // Título centrado en la parte superior
        this.titleLabel = new JLabel(name, SwingConstants.CENTER);
        this.titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.mainPanel.add(titleLabel, BorderLayout.NORTH);

        initializeFreqListComponents(name, freqs);
        this.scrollPane = new JScrollPane(this.freqListPanel);
        mainPanel.add(scrollPane);

        afegirButton = new JButton("Afegir Paraula");
        eliminarButton = new JButton("Eliminar Paraula");
        buttonPanel = new JPanel(new FlowLayout());
        this.buttonPanel.add(afegirButton);
        this.buttonPanel.add(eliminarButton);
        this.mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Agrega el panel al JFrame
        add(mainPanel);
    }

    /**
     * Funció encarregada de generar un ScrollPane amb totes les paraules i un spinner que permeti modificar les seves
     * freqüències
     * @param name Nom de la llista a visualitzar
     * @param freqs Conjunt de paraules i les seves freqüències que conformen la llista
     */
    private void initializeFreqListComponents(String name, HashMap<String, Integer> freqs) {
        this.freqListPanel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.fill = GridBagConstraints.BOTH;
        grid.weightx = 0.0;
        grid.insets = new Insets(5, 0, 5, 0);
        spinnersMap = new HashMap<>();

        ArrayList<String> keysSorted = new ArrayList<>(freqs.keySet());
        Collections.sort(keysSorted);

        for (String key : keysSorted) {
            //System.out.println(key);

            JLabel keyLabel = new JLabel(key + "  \uDB81\uDF98  ", SwingConstants.RIGHT);

            JSpinner spinner = new JSpinner();
            SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
            spinnerNumberModel.setMinimum(1);   // Posem el minim a 1, si vol ficar-ho a zero ho eliminará mitjançant el deleteWordButton
            spinner.setModel(spinnerNumberModel);
            spinner.setValue(freqs.get(key));   // Li designem el valor de la freqüència
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setColumns(4); //Fem que pugui arribar a veure numeros de 4 digits

            spinnersMap.put(key, spinner); 

            grid.gridx = 0;
            this.freqListPanel.add(keyLabel, grid);
            grid.gridx = 1;
            this.freqListPanel.add(spinner, grid);
            grid.gridy++;
        }
    }

    /**
     * Funció que inicialitza les funcionalitats dels spinners i dels botons que té la view
     * @param name Nom de la llista que es modifica dintre de la view
     */
    private void initializeListeners(String name) {
        this.afegirButton.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map.Entry<String, Integer> answer = MyJOptionPane.addWordOptionPane();
                if (answer != null) {
                    try {
                        CtrlPresentation.getInstance().addWordToList(name, answer.getKey(), answer.getValue());
                        HashMap<String, Integer> aux = CtrlPresentation.getInstance().getWordFreq(name);
                        initializeComponents(name, aux);
                        initializeListeners(name);
                        initializeFreqListListeners(name);
                        setContentPane(mainPanel);
                        pack();
                    } catch (ObjectAlreadyExistException ex) {
                        JOptionPane.showMessageDialog(null, "La paraula " + ex.getName() + " ja és a la llista", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ObjectDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(null, "La Llista de Freqüències " + ex.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (OutputException | InputException ex){
                        JOptionPane.showMessageDialog(null, "Error guardant en disc la llista", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        this.eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String answer = MyJOptionPane.deleteWordOptionPane(name);
                if (answer != null) {
                    try {
                        CtrlPresentation.getInstance().deleteWordToList(name, answer);
                        HashMap<String, Integer> aux = CtrlPresentation.getInstance().getWordFreq(name);
                        initializeComponents(name, aux);
                        initializeListeners(name);
                        initializeFreqListListeners(name);
                        setContentPane(mainPanel);
                        pack();
                    } catch (ObjectDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(null, "La paraula " + ex.getName() + " no és a la llista", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (OutputException | InputException ex){
                        JOptionPane.showMessageDialog(null, "Error guardant en disc la llista", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CtrlPresentation.getInstance().closeAlphabetVisualizer();
            }
        });

    }

    void initializeFreqListListeners(String name) {
        for (String s : spinnersMap.keySet()) {
            JSpinner spinner = spinnersMap.get(s);
            spinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    try{
                        CtrlPresentation.getInstance().modifyFrequencyList(name, s, (int)spinner.getValue());
                    } catch (NegativeAppearancesException | ObjectDoesNotExistException | InputException | OutputException ex){
                        JOptionPane.showMessageDialog(null, "Error no es pot accedir al fitxer", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        }
    }

    /**
     * Funció que permet visualitzar el frame per pantalla
     */
    public void makeVisible() {
        pack();
        setVisible(true);
    }
}
