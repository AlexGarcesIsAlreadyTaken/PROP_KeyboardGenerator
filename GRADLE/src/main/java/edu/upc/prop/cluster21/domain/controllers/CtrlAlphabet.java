package edu.upc.prop.cluster21.domain.controllers;

import edu.upc.prop.cluster21.domain.classes.Alphabet;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.persistence.AlphabetManager;
import edu.upc.prop.cluster21.domain.classes.User;
import edu.upc.prop.cluster21.domain.classes.Utilities;
import java.util.*;

/**
 * CtrlAlphabet és la classe controlador específica per les funcionalitats dels alfabets a la capa de domini
 */

public class CtrlAlphabet {

    /**
     * Instància del controlador en ús
     */
    private static CtrlAlphabet current;

    /**
     * Conjunt que guarda tots els alfabets de l'usuari
     */
    private static HashMap<String, Alphabet> alphabets;

    /**
     * Funció que obté la instància del controlador amb la que treballa l'usuari
     * @return Retorna el CtrlAlphabet amb què es treballarà
     */
    public static CtrlAlphabet getInstance() {
        if (current == null) {
            current = new CtrlAlphabet();
            alphabets = new HashMap<>();
        }
        return current;
    }

    public void importAlphabet(String filePath) throws InputException, OutputException {
        //Primer carreguem l'alfabet amb el gestor d'alfabets, si hi ha algun error es llança excepció
        Utilities.Pair<String, HashSet<Character>> alphabet = AlphabetManager.getInstance().importAlphabet(filePath);

        //Ara guardem l'alfabet al conjunt d'alfabets de l'usuari.
        Alphabet newAlphabet = new Alphabet(alphabet.first, alphabet.second);
        alphabets.put(alphabet.first, newAlphabet);

        String userName = User.getInstance().getUser();
        AlphabetManager.getInstance().saveAlphabet(userName, alphabet.first, alphabet.second);
    }

    public void exportAlphabet(String name, String path) throws OutputException {
        //Nomes guardem el .json de l'alfabet al path indicat, amb nom nomAlfabet.json
        HashSet<Character> symbols = alphabets.get(name).getSymbols();
        AlphabetManager.getInstance().exportAlphabet(name, symbols, path);
    }

    public void loadAlphabets(String user) throws InputException {
        alphabets.clear();
        HashMap<String, HashSet<Character>> aux = AlphabetManager.getInstance().loadAlphabets(user);
        for (HashMap.Entry<String, HashSet<Character>> fq : aux.entrySet()){
            alphabets.put(fq.getKey(), new Alphabet(fq.getKey(), fq.getValue()));
        }
    }

    public void rmAlphabets(){
        alphabets.clear();
    }

    /**
     * Funció que permet crear a l'usuari un nou alfabet i afegir-ho al seu conjunt
     * @param name Nom del nou teclat a crear
     * @param text Text a partir del qual es crearà el teclat
     * @throws ObjectAlreadyExistException Es llença si el teclat ja existeix al conjunt
     * @throws MaxCapacityExceeded Es llença si l'alfabet a crear supera el llindar de símbols
     * @throws OutputException Hi ha hagut un error escribint al fitxer on es guarda el teclat
     */
    public void createAlphabet(String name, String text) throws ObjectAlreadyExistException, MaxCapacityExceeded, OutputException{
        if (!alphabets.containsKey(name)) {
            Alphabet newAlphabet = new Alphabet(name, text);
            alphabets.put(name, newAlphabet);
            String userName = User.getInstance().getUser();
            AlphabetManager.getInstance().saveAlphabet(userName, name, newAlphabet.getSymbols());

        }
        else {
            throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.ALPHABET, name);
        }
    }

    /**
     * Funció que permet eliminar a l'usuari un dels alfabets
     * @param name Nom de l'alfabet a eliminar
     */
    public void deleteAlphabet(String name) throws InputException, OutputException {
        alphabets.remove(name);
        AlphabetManager.getInstance().deleteAlphabet(User.getInstance().getUser(), name);
    }

    /**
     * Funció per afegir una lletra a un dels alfabets del conjunt de l'usuari
     * @param name Nom de l'alfabet que es vol modificar
     * @param letter Lletra que es vol afegir a l'alfabet
     * @throws ObjectDoesNotExistException Es llença quan l'alfabet que es vol modificar no forma part del conjunt
     * @throws ObjectAlreadyExistException Es llença quan la lletra que es vol afegir ja pertany a l'alfabet name
     * @throws MaxCapacityExceeded Es llença quan l'alfabet name ja està ple
     */
    public void AddLetter(String name, Character letter) throws ObjectAlreadyExistException, MaxCapacityExceeded, ObjectDoesNotExistException, InputException, OutputException {
        if (alphabets.containsKey(name)) {
            alphabets.get(name).addLetter(letter);
            AlphabetManager.getInstance().modifyAlphabet(User.getInstance().getUser(), name, alphabets.get(name).getSymbols());
        }
        else {
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.ALPHABET, name);
        }
    }

    /**
     * Funció per eliminar una lletra a un dels alfabets del conjunt de l'usuari
     * @param name Nom de l'alfabet del qual es vol eliminar una lletra
     * @param letter Lletra que es vol eliminar
     * @throws ObjectDoesNotExistException Es llença quan l'alfabet name no pertany al conjunt de l'usuari o
     * quan la lletra letter no pertany a l'alfabet name
     *
     */
    public void DeleteLetter(String name, Character letter) throws ObjectDoesNotExistException, MinimumCapacityReached, InputException, OutputException {
        if (alphabets.containsKey(name)) {
            Alphabet a = alphabets.get(name);
            a.deleteLetter(letter);
            AlphabetManager.getInstance().modifyAlphabet(User.getInstance().getUser(), name, a.getSymbols());
        }
        else {
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.ALPHABET, name);
        }
    }

    /**
     * Funció que retorna un llistat dels noms dels alfabets que té l'usuari
     * @return Un arraylist amb els noms del conjunt d'alfabets de l'usuari
     */
    public ArrayList<String> getAlphabetsNames() {
        return new ArrayList<>(alphabets.keySet());
    }

    /**
     * Funció que retorna els caràcters de l'alfabet name. Es retorna en forma de Set per tal que aquests
     * s'ordenin i no retornar un alfabet desordenat
     * @param name Nom de l'alfabet del qual se'n volen les lletres
     * @return Set de Characters que conté les lletres de l'alfabet name
     * @throws ObjectDoesNotExistException Es llença quan l'alfabet name no pertany al conjunt de l'usuari
     */
    public Set<Character> getAlphabet(String name) throws ObjectDoesNotExistException {
        if (alphabets.containsKey(name)) return alphabets.get(name).convertToOrderedSet(alphabets.get(name).getSymbols());
        else throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.ALPHABET, name);
    }

}
