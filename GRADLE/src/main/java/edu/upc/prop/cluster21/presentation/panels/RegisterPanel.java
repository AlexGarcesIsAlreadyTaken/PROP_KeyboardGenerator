package edu.upc.prop.cluster21.presentation.panels;
import edu.upc.prop.cluster21.exceptions.IncorrectPasswordException;
import edu.upc.prop.cluster21.exceptions.InputException;
import edu.upc.prop.cluster21.exceptions.ObjectAlreadyExistException;
import edu.upc.prop.cluster21.exceptions.OutputException;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyPasswordField;
import edu.upc.prop.cluster21.presentation.components.MyTextField;
import edu.upc.prop.cluster21.presentation.views.LoginView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;


/**
 * JPanel que representa el panell principal d'una finestra per crear un compte d'usuari
 */
public class RegisterPanel extends JPanel{
    private MyTextField usernameTextField;
    private MyPasswordField passwordField;
    private MyPasswordField confirmPasswordField;
    private JButton registerButton;
    private JLabel hasAlreadyAccountLabel;
    private JLabel loginLabel;
    private LoginView parent;

    /**
     * Constructora de la classe
     * @param parent instància que l'ha creat
     */
    public RegisterPanel(LoginView parent) {
        super(new GridBagLayout());
        this.parent = parent;
        initializeComponents();
        initializeListeners();
    }

    /**
     * Inicialitza els components de la classe
     */
    private void initializeComponents() {
        GridBagConstraints grid = new GridBagConstraints();
        grid.gridx = 0;
        grid.gridy = 0;
        grid.weighty = 0.0;
        grid.weightx = 0.0;
        grid.fill = GridBagConstraints.HORIZONTAL;

        grid.gridwidth = 1;
        grid.gridheight = 6;
        grid.weightx = 0.25;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), grid);
        grid.gridx = 2;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.gridheight = 1;
        grid.weightx = 0.5;

        // Agreguem els components
        grid.gridy++;
        this.usernameTextField = new MyTextField("Nom d'usuari");
        this.add(this.usernameTextField, grid);

        grid.gridy++;
        this.passwordField = new MyPasswordField("Contrasenya");
        this.add(this.passwordField, grid);

        grid.gridy++;
        this.confirmPasswordField = new MyPasswordField("Confirma Contrasenya");
        this.add(this.confirmPasswordField, grid);

        grid.gridy++;
        this.registerButton = new JButton("Crear Compte");
        this.add(this.registerButton, grid);

        grid.gridy++;
        grid.insets = new Insets(10, 0, 0, 0);
        this.hasAlreadyAccountLabel = new JLabel("Ja tens compte?");
        this.hasAlreadyAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.hasAlreadyAccountLabel, grid);

        grid.gridy++;
        grid.insets = new Insets(0, 0, 0, 0);
        this.loginLabel = new JLabel("Inicia Sessió");
        this.loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.loginLabel.setForeground(Color.BLUE);
        this.add(this.loginLabel, grid);
    }

    /**
     * Inicialitza els EventListeners de la classe
     */
    private void initializeListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = Arrays.toString(passwordField.getPassword());
                String confirmPassword = Arrays.toString(confirmPasswordField.getPassword());
                try {
                    parent.createAccount(username, password, confirmPassword);
                } catch (ObjectAlreadyExistException ex) {
                    JOptionPane.showMessageDialog(null, "L'usuari " + username + " ja existeix");
                } catch (IncorrectPasswordException ex) {
                    JOptionPane.showMessageDialog(null, "Les contrasenyes no coincideixen");
                }catch (InputException | OutputException ex) {
                    JOptionPane.showMessageDialog(null, "Problema amb el sistema de fitxers");
                }
            }
        });


        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                parent.changeContentPanel(LoginView.PANEL.LOGINPANEL);
            }
        });
    }

    /**
     * Reinicialitza els valors de la classe a com eren al moment de finalitzar la constructora
     */
    public void restartValues() {
        usernameTextField.setHint();
        passwordField.setHint();
        confirmPasswordField.setHint();
    }
}
