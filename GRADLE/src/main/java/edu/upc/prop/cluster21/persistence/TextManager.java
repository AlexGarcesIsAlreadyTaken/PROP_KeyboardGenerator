package edu.upc.prop.cluster21.persistence;

import edu.upc.prop.cluster21.exceptions.InputException;

import java.io.*;
/**
 * Classe que s'encarrega de la lectura de textos desde memòria
 */
public class TextManager {
    /**
     * La instància actual
     */
    private static TextManager current;

    /**
     * Getter de la única instància seguint el patró singleton
     * @return La instància actual
     */
    public static TextManager getInstance() {
        if (current == null) {
            current = new TextManager();
        }
        return current;
    }

    /**
     * Funció per a llegir un fitxer interprentant-lo com a text pla
     * @param path Direcció d'on s'ha de llegir el fitxer
     * @return El text que s'ha llegit del fitxer
     * @throws InputException En cas d'error de lectura
     */
    public String readText(String path) throws InputException{
        File f = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String ret = "", aux;
            while ((aux = br.readLine()) != null) {
                ret = ret + " " + aux;
            }
            return ret;
        }
        catch (IOException e) {
            throw new InputException();
        }
    }
}
