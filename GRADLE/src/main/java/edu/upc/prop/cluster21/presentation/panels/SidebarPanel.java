package edu.upc.prop.cluster21.presentation.panels;

import edu.upc.prop.cluster21.presentation.views.ManagerView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * Classe que representa la barra lateral de la finestra principal
 */
public class SidebarPanel extends JPanel{
    private JButton alphabetButton;
    private JButton frequencyListButton;
    private JButton keyboardButton;
    private JButton logoutButton;
    private JLabel usernameLabel;

    private final ManagerView parent;

    /**
     * Constructora de la classe
     * @param parent instancia que l'ha creat
     * @param user nom de l'usuari que ha iniciat sessió
     */
    public SidebarPanel(ManagerView parent, String user) {
        super(new GridBagLayout());
        initializeComponents(user);
        initializeListeners();
        this.parent = parent;
    }

    /**
     * Inicialitza els EventListeners de la classe
     */
    private void initializeListeners() {
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.logout();
            }
        });
        keyboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.changeManagerPanel(ManagerView.PANEL.KEYBOARD_PANEL);
            }
        });
        frequencyListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.changeManagerPanel(ManagerView.PANEL.FREQUENCY_LIST_PANEL);
            }
        });
        alphabetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.changeManagerPanel(ManagerView.PANEL.ALPHABET_PANEL);
            }
        });
    }

    /**
     * Inicialitza els components de la classe
     * @param user nom de l'usuari que ha iniciat sessió
     */
    private void initializeComponents(String user) {
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridy = 0;
        grid.gridx = 0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        this.usernameLabel = new JLabel(user);

        this.usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Map atributtes = this.usernameLabel.getFont().getAttributes();
        atributtes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        this.usernameLabel.setFont(this.usernameLabel.getFont().deriveFont(atributtes));
        grid.insets = new Insets(5, 2, 5, 2);
        grid.anchor = GridBagConstraints.NORTH; // El coloquem a la part alta del panel
        add(this.usernameLabel, grid);

        ++grid.gridy;
        grid.weighty = 1.0;
        grid.anchor = GridBagConstraints.CENTER;
        add(new JSeparator(SwingConstants.VERTICAL), grid);

        ++grid.gridy;
        grid.weighty = 0.0;
        grid.insets = new Insets(0, 0, 0, 0);
        this.alphabetButton = new JButton("Gestionar Alfabets");
        add(this.alphabetButton, grid);

        ++grid.gridy;
        this.frequencyListButton = new JButton("Gestionar LListes de Freqüències");
        add(this.frequencyListButton, grid);

        ++grid.gridy;
        this.keyboardButton = new JButton("Gestionar Teclats");
        add(this.keyboardButton, grid);

        ++grid.gridy;
        grid.weighty = 1.0;
        grid.anchor = GridBagConstraints.CENTER;
        add(new JSeparator(SwingConstants.VERTICAL), grid);


        ++grid.gridy;
        grid.weighty = 0.0;
        grid.anchor = GridBagConstraints.SOUTH;
        grid.insets = new Insets(5, 0, 10, 0);
        this.logoutButton = new JButton("Tancar Sessió");
        add(this.logoutButton, grid);
    }

    /**
     * Reinicia els valors de la classe a com eren al moment posterior a la construcció
     * @param user usuari que ha iniciat sessió
     */
    public void restart(String user) {
        usernameLabel.setText(user);
    }
}
