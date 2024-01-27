package edu.upc.prop.cluster21.presentation.panels;

import edu.upc.prop.cluster21.presentation.components.ElementList;
import edu.upc.prop.cluster21.presentation.views.ManagerView;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * JPanel que representa el panell principal d'un gestor
 */
public abstract class ManagerPanel extends JPanel{
    //private JPanel mainPanel;
    private JSplitPane jSplitPane;
    private JPanel northPanel;
    private JButton createButton;
    private JButton importButton;
    private JButton infoButton;
    private JScrollPane managerScrollPane;
    private JPanel managerListPanel;
    private JLabel title;
    protected ManagerView parent;
    protected String importFilePath;
    protected String exportFilePath;

    /**
     * Constructora de la classe
     * @param names noms dels elements de la llista
     * @param parent instància que l'ha creat
     */
    public ManagerPanel(ArrayList<String> names, ManagerView parent) {
        super(new GridBagLayout());

        initializeComponents(names);
        initializeListeners();
        this.parent = parent;
    }

    /**
     * Inicialitza els listenerEvents dels components
     */
    protected void initializeListeners() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createElement();
            }
        });

        importButton.addActionListener(e -> {
            importElement();
        });

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String info = "A la part esquerre hi ha disponibles els tres botons per canviar de gestor.\n A cada gestor, hi ha una barra horitzontal" +
                        " per cada element amb els botons Exportar, Visualitzar i Eliminar, \nque permeten fer el que el nom indica amb l'element en qüestió. " +
                        "(Per modificar cal visualitzar). \n" +
                        "Per importar o crear disposem dels botons que es troben a la part superior dreta de la finestra. \nPer tancar sessió hi ha un botó a la part inferior esquerre.";
                JOptionPane.showMessageDialog(null, info, "Informació", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Inicialitza els components de la classe
     * @param names noms dels elements que es volen llistar
     */
    private void initializeComponents(ArrayList<String> names) {
        initializeNorthPanelComponents();
        initializeElementListComponents(names);
        GridBagConstraints grid = new GridBagConstraints();

        this.jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Separem per una part la llista dels teclats i per una altra el buscador i el butó d'afegir.

        this.jSplitPane.setTopComponent(northPanel);
        this.jSplitPane.setBottomComponent(managerScrollPane);
        this.jSplitPane.setDividerSize(0);


        grid.gridy = 0;
        grid.gridx = 0;
        // Volem que el splitPanel ocupi tot el panel.
        grid.fill = GridBagConstraints.BOTH;
        grid.weightx = 1.0;
        grid.weighty = 1.0;
        this.add(this.jSplitPane, grid);
    }

    /**
     * Inicialitza els components de la part nord del panel
     */
    private void initializeNorthPanelComponents() {
        this.northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.gridx = 0;
        grid.fill = GridBagConstraints.BOTH;

        ++grid.gridx;
        grid.weightx = 0.0;
        grid.insets = new Insets(0, 2, 0, 2);

        this.infoButton = new JButton("\u24D8");

        this.infoButton.setFocusPainted(false);
        this.infoButton.setFont(new Font("Arial", Font.PLAIN, 18));

        this.northPanel.add(infoButton, grid);

        // Separador después del botón de información
        ++grid.gridx;
        grid.weightx = 1.0; // El JSeparator ha de tenir pés maxim en el Panel per poder actuar correctament
        grid.insets = new Insets(0, 0, 0, 0);
        this.northPanel.add(new JSeparator(), grid);

        // Botón "Importar"
        ++grid.gridx;
        grid.weightx = 0.0;
        grid.insets = new Insets(0, 2, 0, 2);
        this.importButton = new JButton("Importar");
        this.northPanel.add(this.importButton, grid);

        // Botón "Crear"
        ++grid.gridx;
        grid.insets = new Insets(0, 2, 0, 4);
        this.createButton = new JButton("Crear");
        this.northPanel.add(this.createButton, grid);
    }

    /**
     * Inicialitza els components de la llista
     * @param names noms dels elements que es vol a la llista
     */
    private void initializeElementListComponents(ArrayList<String> names) {
        managerListPanel = new JPanel(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.anchor = GridBagConstraints.NORTH;
        grid.weightx = 1.0; // Volem que els ElementList ocupin tota l'amplada del panel
        grid.gridx = 0;
        grid.gridy = 0;

        title = new JLabel();
        title.setFont(title.getFont().deriveFont(16.0f));
        title.setFont(title.getFont().deriveFont(Font.BOLD));

        if (this.getClass() == KeyboardPanel.class) title.setText("GESTOR DE TECLATS");
        else if (this.getClass() == AlphabetPanel.class) title.setText("GESTOR D'ALFABETS");
        else title.setText("GESTOR DE LLISTES DE FREQÜÈNCIES");

        managerListPanel.add(title);
        grid.gridy++;

        for (String name : names) {
            managerListPanel.add(new ElementList(name, this), grid);
            ++grid.gridy;
        }
        ++grid.gridy;
        grid.weightx = 0.0;
        grid.weighty = 1.0;
        managerListPanel.add(new JSeparator(SwingConstants.VERTICAL), grid);

        managerScrollPane = new JScrollPane(managerListPanel);
        managerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }


    /**
     * Visualitza un element qualsevol
     * @param name nom de l'element a visualitzar
     */
    public abstract void visualizeElement(String name);

    /**
     * Elimina un element qualsevol
     * @param name nom de l'element a eliminar
     */
    public abstract void removeElement(String name);

    /**
     * Crea un element qualsevol
     */
    protected abstract void createElement();

    /**
     * Importa un element qualsevol
     */
    protected void importElement() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fileChooser = new JFileChooser(userDir);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getAbsolutePath().endsWith(".json");
            }

            @Override
            public String getDescription() {
                return "JSon files (*.json)";
            }
        });
        int optionSelected = fileChooser.showOpenDialog(this);
        if (optionSelected == JFileChooser.APPROVE_OPTION && !fileChooser.getSelectedFile().isDirectory()) importFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        else importFilePath = null;
    }

    /**
     * Exporta un element qualsevol
     * @param name Nom de l'element a exportar
     */
    public void exportElement(String name) {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fileChooser = new JFileChooser(userDir);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int optionSelected = fileChooser.showOpenDialog(this);
        if (optionSelected == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory()) exportFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        else exportFilePath = null;
    }
}

