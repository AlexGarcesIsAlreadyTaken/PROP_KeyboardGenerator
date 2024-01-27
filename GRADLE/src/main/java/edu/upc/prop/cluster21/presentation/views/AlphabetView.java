package edu.upc.prop.cluster21.presentation.views;

import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyJOptionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

/**
 * JFrame que representa una finestra de gestió d'alfabets
 */
public class AlphabetView extends JFrame{
    final static int WIDTH = 800;
    final static int HEIGHT = WIDTH * 9 / 16;
    private JPanel mainPanel;
    private JPanel alphabetPanel;
    private JPanel buttonPanel;
    private JLabel tituloLabel;
    private JButton eliminarBoton;
    private JButton anadirBoton;

    /**
     * Això és el visualitzador d'un alfabet per a comprovar-lo i modificar-lo. La funció de getSymbols estarà
     * a la pantalla anterior
     * @param symbols Conjunt de símbols que conformen l'alfabet
     * @param name Nom de l'alfabet que es visualitza
     */
    public AlphabetView(String name, Set<Character> symbols) {
        super("Alphabet View");
        initializeComponents(name, symbols);
        initializeListeners(name, symbols);
        initializeFrameComponents();
    }

    /**
     * Funció que inicialitza el frame amb les característiques desitjades
     */
    void initializeFrameComponents() {
        setMinimumSize(new Dimension((int) (WIDTH/1.5), (HEIGHT/3)));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(null); //Center frame
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Funció utilitzada per inicialitzar els diferents components dintre de la finestra de la view
     * @param name Nom de l'alfabet que es visualitza a la view
     * @param symbols Conjunt de símbols de l'alfabet que es visualitza a la view
     */
    private void initializeComponents(String name, Set<Character> symbols) {

        // Panel para organizar los componentes
        this.mainPanel = new JPanel(new BorderLayout());

        // Título centrado en la parte superior
        this.tituloLabel = new JLabel(name, SwingConstants.CENTER);
        this.tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        this.mainPanel.add(tituloLabel, BorderLayout.NORTH);

        // Panel central con GridLayout para la matriz
        SetAlphabetGrid(symbols); //Función que crea el panel para los símbolos
        this.mainPanel.add(alphabetPanel
                , BorderLayout.CENTER);

        // Panel para los botones en la parte inferior
        this.buttonPanel = new JPanel(new FlowLayout());
        this.anadirBoton = new JButton("Afegir símbol");
        this.eliminarBoton = new JButton("Eliminar símbol");
        this.buttonPanel.add(this.anadirBoton);
        this.buttonPanel.add(this.eliminarBoton);
        this.mainPanel.add(this.buttonPanel, BorderLayout.SOUTH);

        // Agrega el panel al JFrame
        add(mainPanel);
    }

    /**
     * Funció que permet visualitzar el frame per pantalla
     */
    public void makeVisible() {
        pack();
        setVisible(true);
    }

    /**
     * Funció que crea el panel per visualitzar l'alfabet en fila horitzontal
     * @param symbols Hashset de simbols que es mostren per pantalla
     * @return Panel amb grid que servirà per ensenyar l'alfabet per pantalla
     */
    private void SetAlphabetGrid(Set<Character> symbols) {

        this.alphabetPanel
                = new JPanel(new FlowLayout());
        for (Character symbol : symbols) {
            JLabel label = new JLabel(String.valueOf(symbol), SwingConstants.LEFT);
            alphabetPanel.add(label);
        }
    }

    /**
     * Funció que dóna les funcionalitats als botóns "Afegir símbol" i "Eliminar Símbol" de la view
     */
    private void initializeListeners(String name, Set<Character> symbols) {
        //En las funciones de los botones habrá que ver cómo ir llamando a los modificadores de alphabet
        this.anadirBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Character answer = MyJOptionPane.addLetterOptionPane();
                if  (answer != null) {
                    try {
                        CtrlPresentation.getInstance().addLetter(name, answer);
                    } catch (ObjectAlreadyExistException ex) {
                        JOptionPane.showMessageDialog(null, "La lletra " + ex.getName() + " ja és a l'alfabet", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (MaxCapacityExceeded ex) {
                        JOptionPane.showMessageDialog(null, "L'alfabet ja és ple, no pots afegir més lletres/símbols", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ObjectDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(null, "L'alfabet " + ex.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
                    }catch (InputException | OutputException ex){
                        JOptionPane.showMessageDialog(null, "Error amb el sistema de fitxers");
                    }
                    Set<Character> aux = null;
                    try {
                        aux = CtrlPresentation.getInstance().getAlphabet(name);
                    } catch (ObjectDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(null, "L'alfabet " + ex.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    //Falta revisar com fer-ho més eficient
                    initializeComponents(name, aux);
                    makeVisible();
                }
            }
        });

        this.eliminarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Character answer = MyJOptionPane.deleteLetterOptionPane(name);
                if (answer != null) {
                    try {
                        CtrlPresentation.getInstance().deleteLetter(name, answer);
                    } catch (ObjectDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(null, "L'alfabet " + ex.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (MinimumCapacityReached ex) {
                        JOptionPane.showMessageDialog(null, "L'alfabet " + ex.getName() + " no pot quedar buit", "Error", JOptionPane.ERROR_MESSAGE);
                    }catch (InputException | OutputException ex){
                        JOptionPane.showMessageDialog(null, "Error amb el sistema de fitxers");
                    }

                    Set<Character> aux = null;
                    try {
                        aux = CtrlPresentation.getInstance().getAlphabet(name);
                    } catch (ObjectDoesNotExistException ex) {
                        JOptionPane.showMessageDialog(null, "L'alfabet " + ex.getName() + " no existeix", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    initializeComponents(name, aux);
                    makeVisible();
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
}
