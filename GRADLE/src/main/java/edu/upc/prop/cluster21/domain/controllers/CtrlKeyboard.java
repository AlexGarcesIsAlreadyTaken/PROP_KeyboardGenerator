package edu.upc.prop.cluster21.domain.controllers;
import edu.upc.prop.cluster21.domain.classes.Alphabet;
import edu.upc.prop.cluster21.domain.classes.Keyboard;
import edu.upc.prop.cluster21.domain.classes.User;
import edu.upc.prop.cluster21.domain.classes.Utilities;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.persistence.AlphabetManager;
import edu.upc.prop.cluster21.persistence.KeyboardManager;

import java.security.Key;
import java.util.*;

/**
 * Classe Controlador especifica per les funcionalitats dels teclats a la capa de domini
 */
public class CtrlKeyboard {
    /**
     * Patro singleton de ctrlKeyboard
     */
    private static CtrlKeyboard current;

    /**
     * Conjunt que guarda tots els teclats de l'usuari
     */
    private static HashMap<String, Keyboard> keyboards;

    /**
     *
     * @return Retorna el ctrlKeyboard amb que es treballarà
     */
    public static CtrlKeyboard getInstance() {
        if (current == null) {
            current = new CtrlKeyboard();
            keyboards = new HashMap<>();
        }
        return current;
    }

    public void importKeyboard(String filePath) throws InputException, OutputException {
        Utilities.Pair<String, Character[][]> keyboard = KeyboardManager.getInstance().importKeyboard(filePath);

        Keyboard newKeyboard = new Keyboard(keyboard.first, keyboard.second, null);
        keyboards.put(keyboard.first, newKeyboard);

        String userName = User.getInstance().getUser();
        KeyboardManager.getInstance().saveKeyboard(userName, keyboard.first, keyboard.second, null);
    }

    public void exportKeyboard(String name, String path) throws OutputException {
        Character[][] distribution = keyboards.get(name).getKeyboard();
        KeyboardManager.getInstance().exportKeyboard(name, distribution, path);
    }


    public void loadKeyboards(String user) throws InputException {
        keyboards.clear();
        HashMap<String, Character[][]> k = KeyboardManager.getInstance().loadKeyboards(user);
        for (String name : k.keySet()) {
            try {

                keyboards.put(name, new Keyboard(name, k.get(name), KeyboardManager.getInstance().loadKeyboardFreq(user, name)));
                System.out.println("Yes: " + name);
            }
            catch (ObjectDoesNotExistException e){
                System.out.println("No: " + name);
                keyboards.put(name, new Keyboard(name, k.get(name), null));
            }
        }
    }

    public void rmKeyboards(){
        keyboards.clear();
    }

    /**
     * Cambia la lletra de la posició i-j-èssima del teclat amb identificador kName per key
     * @param kName teclat que es vol modificar
     * @param i fila de la tecal a modificar
     * @param j columna de la lletra a modificar
     * @param key nova lletra que ocuparà la posició i-j-essima del teclat
     * @throws ObjectDoesNotExistException El teclat amb identificador kName no existeix
     * @throws ArrayIndexOutOfBoundsException Es vol canviar la lletra d'una posició impossible del teclat
     * @throws ObjectAlreadyExistException La tecla key ja és al teclat
     */
    public void changeKey(String kName, int i, int j, Character key) throws ArrayIndexOutOfBoundsException, ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
        if (!keyboards.containsKey(kName)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.KEYBOARD, kName);
        keyboards.get(kName).changeKey(i, j, key);

        String userName = User.getInstance().getUser();
        KeyboardManager.getInstance().modifyKeyboard(userName, kName, keyboards.get(kName).getKeyboard());
    }

    /**
     * Crea un teclat amb identificador name
     * @param name identificador del teclat
     * @param pairFrequency frequencia de parells de lletres
     * @param symbols alfabet amb que es treballarà
     * @param generationAlgorithm 1: Branch and Bound
     *                            2: SimulatedAnnealing
     * @throws ObjectDoesNotExistException Una lletra de pairFrequency no existeix dins el conjunt de symbols
     * @throws ObjectAlreadyExistException Ja existeix un teclat amb identificador name
     */
    public void createKeyboard(String name, HashMap<String, Integer> pairFrequency, HashSet<Character> symbols, int generationAlgorithm) throws ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
        if (keyboards.containsKey(name)) throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.KEYBOARD, name);
        Keyboard k = new Keyboard(name, pairFrequency, symbols, generationAlgorithm);
        keyboards.put(name, k);
        String userName = User.getInstance().getUser();
        KeyboardManager.getInstance().saveKeyboard(userName, name, k.getKeyboard(), pairFrequency);
    }

    /**
     * Afegeix una lletra en el teclat amb identificador kName
     * @param kName identificador del teclat
     * @param key lletra que es vol afegir
     * @throws MaxCapacityExceeded S'intenta afegir una lletra i el teclat és ple.
     * @throws ObjectDoesNotExistException No existeix el teclat amb identificador kName.
     * @throws ObjectAlreadyExistException El teclat amb identificador kName té una tecla amb valor key.
     */
    public void addKey(String kName, Character key) throws ObjectDoesNotExistException, ObjectAlreadyExistException, MaxCapacityExceeded, InputException, OutputException {
        if (!keyboards.containsKey(kName)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.KEYBOARD, kName);
        keyboards.get(kName).addKey(key);
        String userName = User.getInstance().getUser();
        KeyboardManager.getInstance().modifyKeyboard(userName, kName, keyboards.get(kName).getKeyboard());
    }

    /**
     * Elimina la tecla key del teclat amb identificador kName
     * @param kName Identificador del teclat
     * @param key Lletra a eliminar
     * @throws ObjectDoesNotExistException En funció de typeObject
     *                                     <ul>
     *                                         <li> KEYBOARD: No existeix el teclat amb identificador kName
     *                                         <li> CHARACTER: No apareix la lletra key al teclat
     *                                     </ul>
     */
    public void deleteKey(String kName, Character key) throws ObjectDoesNotExistException, InputException, OutputException {
        if (!keyboards.containsKey(kName)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.KEYBOARD, kName);
        keyboards.get(kName).removeKey(key);
        String userName = User.getInstance().getUser();
        KeyboardManager.getInstance().modifyKeyboard(userName, kName, keyboards.get(kName).getKeyboard());
    }

    /**
     * S'intercanvien les posicions de les teclaes key1 i key2 del teclat amb identificador kName
     * @param kName Identificador del teclat
     * @param key1 Lletra 1
     * @param key2 Lletra 2
     * @throws ObjectDoesNotExistException En funció de typeObject:
     *                                      <ul>
     *                                          <li> KEYBOARD: No existeix el teclat amb identificador kName
     *                                          <li> CHARACTER: No existeix al menys una de les dues lletres al teclat
     *                                      </ul>
     */
    public void swapKeys(String kName, Character key1, Character key2) throws ObjectDoesNotExistException, InputException, OutputException {
        if (!keyboards.containsKey(kName)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.KEYBOARD, kName);
        keyboards.get(kName).swapKeys(key1, key2);
        String userName = User.getInstance().getUser();
        KeyboardManager.getInstance().modifyKeyboard(userName, kName, keyboards.get(kName).getKeyboard());
    }

    /**
     * Retorna els identificadors dels teclats que té l'usuari
     * @return Conjunt d'identificadors de teclat
     */
    public final ArrayList<String> getKeyboardsNames() {
        return new ArrayList<>(keyboards.keySet());
    }

    /**
     * Retorna la distribució del teclat amb identificador kName
     * @param kName Identificador del teclat
     * @return Distribució del teclat
     * @throws ObjectDoesNotExistException No existeix el teclat amb identificador kName
     */
    public final Character[][] getKeyboardDistribution(String kName) throws ObjectDoesNotExistException {
        if (!keyboards.containsKey(kName)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.KEYBOARD, kName);
        return keyboards.get(kName).getKeyboard();
    }

    /**
     * Elimina el teclat kName de l'usuari que ha fet la crida
     * @param kName Identificador del teclat a eliminar
     * @throws ObjectDoesNotExistException El teclat kName no existeix
     */
    public final void removeKeyboard(String kName) throws ObjectDoesNotExistException, InputException, OutputException {
        if (keyboards.remove(kName) == null) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.KEYBOARD, kName);
        KeyboardManager.getInstance().deleteKeyboard(User.getInstance().getUser(), kName);
        KeyboardManager.getInstance().deleteKeyboardFreq(User.getInstance().getUser(), kName);
    }

    public double getKeyboardImprovement(String kName) {
        return keyboards.get(kName).getKeyboardImprovement();
    }

    public void deleteKeyboardFreq(String name) {
        String user = User.getInstance().getUser();
        KeyboardManager.getInstance().deleteKeyboardFreq(user, name);

    }
}
