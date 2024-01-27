package edu.upc.prop.cluster21.presentation.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe que reimplementa la classe JTextField per a que mostri un text d'ajuda quan l'usuari no ha escrit res a la instancia
 */
public class MyTextField extends JTextField {
    private final Color colorLost = Color.GRAY;
    private final Color colorGained = Color.BLACK;
    private String hint;

    /**
     * Constructora
     * @param hint Text d'ajuda de la instancia
     */
    public MyTextField(String hint) {
        super();
        this.hint = hint;
        this.setHint();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (getText().isEmpty()) {
                    setForeground(colorGained);
                    setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (getText().isEmpty()) setHint();
            }
        });
    }

    /**
     * La instancia passa a mostrar el text d'ajuda
     */
    public void setHint() {
        setForeground(colorLost);
        setText(this.hint);
    }

    /**
     *
     * @return Text buit en cas que es mostri l'ajuda, el text imprés en cas contrari
     */
    @Override
    public String getText() {
        if (getForeground().equals(colorLost)) return "";
        return super.getText();
    }
}
