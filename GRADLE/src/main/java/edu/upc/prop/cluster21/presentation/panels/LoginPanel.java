package edu.upc.prop.cluster21.presentation.panels;
import edu.upc.prop.cluster21.exceptions.IncorrectPasswordException;
import edu.upc.prop.cluster21.exceptions.InputException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.components.MyPasswordField;
import edu.upc.prop.cluster21.presentation.components.MyTextField;
import edu.upc.prop.cluster21.presentation.views.LoginView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;


/**
 * JPanel que representa el panell principal d'una finestra per iniciar sessió
 */
public class LoginPanel extends JPanel {
    private MyTextField usernameTextField;
    private MyPasswordField passwordTextField;
    private JButton loginButton;
    private JLabel hasNoAccountLabel;
    private JLabel createAccountLabel;
    private LoginView parent;

    /**
     * Constructora de la classe
     * @param parent Instancia LoginView que l'ha creat
     */
    public LoginPanel(LoginView parent) {
        super(new GridBagLayout());
        this.parent = parent;
        initializeComponents();
        initializeListeners();
        //exceptionLabel.setVisible(false);
        //exceptionLabel.setSize(new Dimension(exceptionLabel.getWidth(), 100));
    }


    /**
     * Inicialitza els listeners del components de la classe
     */
    private void initializeListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = Arrays.toString(passwordTextField.getPassword());
                try {
                    parent.login(username, password);
                } catch (IncorrectPasswordException ex) {
                    JOptionPane.showMessageDialog(null, "L'usuari o la contrasenya són incorrectes", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (ObjectDoesNotExistException ex) {
                    JOptionPane.showMessageDialog(null, "L'usuari " + username + " no existeix.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InputException ex) {
                    JOptionPane.showMessageDialog(null, "Hi ha hagut un error de lectura.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        createAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                parent.changeContentPanel(LoginView.PANEL.REGISTERPANEL);
            }
        });
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
        grid.gridheight = 5;
        grid.weightx = 0.25;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), grid);
        grid.gridx = 2;
        this.add(new JSeparator(SwingConstants.HORIZONTAL), grid);

        grid.gridx = 1;
        grid.gridwidth = 1;
        grid.gridheight = 1;
        grid.weightx = 0.5;

        //Afegim els components
        grid.gridy++;
        this.usernameTextField = new MyTextField("nom d'usuari");
        this.add(this.usernameTextField, grid);

        grid.gridy++;
        this.passwordTextField = new MyPasswordField("contrasenya");
        this.add(this.passwordTextField, grid);

        grid.gridy++;
        this.loginButton = new JButton("Iniciar Sessió");
        this.add(this.loginButton, grid);

        grid.gridy++;
        grid.insets = new Insets(10, 0, 0, 0); // Espaciado superior
        this.hasNoAccountLabel = new JLabel("No tens cap compte?");
        this.hasNoAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this .add(this.hasNoAccountLabel, grid);

        grid.gridy++;
        grid.insets = new Insets(0, 0, 0, 0); // Restablece el espaciado
        this.createAccountLabel = new JLabel("Registra't");
        this.createAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.createAccountLabel.setForeground(Color.BLUE);
        this.add(this.createAccountLabel, grid);
    }

    /**
     * Reinicia els valors dels components a com estaban al moment d'acabar la construcció
     */
    public void restartValues() {
        this.usernameTextField.setHint();
        this.passwordTextField.setHint();
    }


}
