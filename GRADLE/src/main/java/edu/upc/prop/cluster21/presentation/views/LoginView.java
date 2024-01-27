package edu.upc.prop.cluster21.presentation.views;

import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;
import edu.upc.prop.cluster21.presentation.panels.LoginPanel;
import edu.upc.prop.cluster21.presentation.panels.RegisterPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * JFrame que representa una pantalla d'inici de sessió
 */
public class LoginView extends JFrame{

    public enum PANEL {
        LOGINPANEL,
        REGISTERPANEL
    };

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private JPanel myPanel;

    private static final int WIDTH = 700;
    private static final int HEIGHT = 400;

    /**
     * Constructora de la classe
     */
    public LoginView() {
        super("Inici de sessió");
        initializeComponents();
        initializeListeners();
    }

    /**
     * Inicialitza els EventListeners de la presentació
     */
    private void initializeListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int optionSelected = JOptionPane.showConfirmDialog(null, "Segur que vols tancar l'aplicació", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (optionSelected == JOptionPane.YES_OPTION) dispose();

            }
        });
    }

    /**
     * Inicialitza els components de la classe
     */
    void initializeComponents() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(getMinimumSize());
        setResizable(false);

        setLocationRelativeTo(null); //Center frame
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        myPanel = new JPanel(new GridLayout(1, 2));

        ImageIcon logoIcon = new ImageIcon("logo.jpg");
        Image scaledImage = logoIcon.getImage().getScaledInstance(350, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);

        myPanel.add(new JLabel(scaledLogoIcon));

        loginPanel = new LoginPanel(this);
        myPanel.add(loginPanel);
        setContentPane(myPanel);
        pack();
    }

    /**
     * Canvia el JPanel que es mostra
     * @param panel panel a mostrar
     */
    public void changeContentPanel(PANEL panel) {
        switch (panel) {
            case LOGINPANEL:
                if (loginPanel == null) loginPanel = new LoginPanel(this);
                else loginPanel.restartValues();
                myPanel.remove(1);
                myPanel.add(loginPanel);
                break;
            case REGISTERPANEL:
                if (registerPanel == null) registerPanel = new RegisterPanel(this);
                else registerPanel.restartValues();
                myPanel.remove(1);
                myPanel.add(registerPanel);
                break;
        }
        pack();
        repaint();
    }

    /**
     * Crida a iniciar sessió
     * @param username nom de l'usuari
     * @param password contrasenya de l'usuari
     * @throws IncorrectPasswordException password no coincideix amb la contrasenya de l'usuari username
     * @throws ObjectDoesNotExistException No existeix l'usuari amb nom d'usuari username
     * @throws InputException S'ha produit un error llegint un fitxer
     */
    public void login(String username, String password) throws IncorrectPasswordException, ObjectDoesNotExistException, InputException {
        CtrlPresentation.getInstance().login(username, password);
        CtrlPresentation.getInstance().paintManagerView(username);
        dispose();
    }

    /**
     * Crea un usuari
     * @param username nom de l'usuari
     * @param password contrasenya de l'usuari
     * @param confirmPassword contrasenya de l'usuari
     * @throws ObjectAlreadyExistException Ja existeix un usuari amb nom d'usuari username
     * @throws IncorrectPasswordException password i confirmPassword no coincideixen
     */
    public void createAccount(String username, String password, String confirmPassword) throws ObjectAlreadyExistException, IncorrectPasswordException, InputException, OutputException {
        if (!password.equals(confirmPassword)) throw new IncorrectPasswordException();
        CtrlPresentation.getInstance().createAccount(username, password);
        CtrlPresentation.getInstance().paintManagerView(username);
        dispose();
    }

    /**
     * Reinicia la instancia de la classe a just despres de la constructora
     */
    public void restart() {
        loginPanel.restartValues();
        changeContentPanel(PANEL.LOGINPANEL);
    }

}
