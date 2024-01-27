package edu.upc.prop.cluster21.presentation.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe que reimplementa la classe JPasswordField per a que mostri un text d'ajuda quan és buida la instància
 */
public class MyPasswordField extends JPasswordField {
    private final Color colorLost = Color.GRAY;
    private final Color colorGained = Color.BLACK;
    private String hint;

    /**
     * Constructora de la classe
     * @param hint Text d'ajuda que tindra la instància
     */
    public MyPasswordField(String hint) {
        super();
        this.hint = hint;
        this.setHint();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (getForeground().equals(colorLost)) {
                    setForeground(colorGained);
                    setText("");
                    setEchoChar('•');
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (getText().isEmpty()) setHint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!getText().isEmpty()) setEchoChar('•');
            }
        });
    }

    /**
     * Imprimeix l'ajuda al marc de la instància
     */
    public void setHint() {
        setForeground(colorLost);
        setText(this.hint);
        setEchoChar((char) 0);
    }

    @Override
    public String getText() {
        if (getForeground().equals(colorLost)) return "";
        return super.getText();
    }
}
