package edu.upc.prop.cluster21.presentation.components;
import edu.upc.prop.cluster21.presentation.panels.ManagerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Panel que representa l'element d'una llista
 */
public class ElementList extends JPanel {
    private JLabel nameLabel;
    private JButton visualizeButton;
    private JButton removeButton;
    private JButton exportButton;
    private ManagerPanel parent;

    /**
     * Constructora de la classe
     * @param name nom de l'element que representen
     * @param parent instància que els ha creat
     */
    public ElementList(String name, ManagerPanel parent) {
        super(new GridBagLayout());
        this.parent = parent;
        initializeComponents(name);
        initializeListeners();
    }

    /**
     * Inicialitza els components de la instància
     * @param name nom de l'element que representa
     */
    void initializeComponents(String name) {
        GridBagConstraints grid = new GridBagConstraints();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //this.setPreferredSize(this.getMaximumSize());

        grid.gridy = 0;
        grid.gridx = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        grid.insets = new Insets(0, 5, 0, 0);
        grid.anchor = GridBagConstraints.WEST;
        nameLabel = new JLabel(name);
        this.add(nameLabel, grid);

        ++grid.gridx;
        grid.weightx = 1.0;
        grid.weighty = 1.0;
        grid.insets = new Insets(0, 0, 0, 0);
        this.add(new JSeparator(), grid);

        ++grid.gridx;
        grid.weightx = 0.0;
        grid.weighty = 0.0;
        grid.insets = new Insets(0, 0, 0, 2);
        exportButton = new JButton("Exportar \uD83D\uDD0E");
        this.add(exportButton, grid);

        ++grid.gridx;
        grid.weightx = 0.0;
        grid.weighty = 0.0;
        grid.insets = new Insets(0, 0, 0, 2);
        visualizeButton = new JButton("Visualitzar \uD83D\uDD0E");
        visualizeButton.setForeground(Color.BLUE);
        this.add(visualizeButton, grid);

        ++grid.gridx;
        grid.insets = new Insets(0, 3, 0, 5);
        removeButton = new JButton("Eliminar \uD83D\uDDD1️");
        removeButton.setForeground(Color.RED);
        this.add(removeButton, grid);
    }

    /**
     * Inicialitza els EventListeners de la classe
     */
    private void initializeListeners() {
        this.removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.removeElement(nameLabel.getText());
            }
        });

        this.visualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.visualizeElement(nameLabel.getText());
            }
        });

        this.exportButton.addActionListener(e -> {
            parent.exportElement(nameLabel.getText());
        });
    }

    /**
     * @return Nom de l'element que representen
     */
    public String getName() {return this.nameLabel.getName();}
}
