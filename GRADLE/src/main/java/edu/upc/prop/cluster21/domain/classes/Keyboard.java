package edu.upc.prop.cluster21.domain.classes;

import edu.upc.prop.cluster21.domain.controllers.CtrlKeyboard;
import edu.upc.prop.cluster21.exceptions.*;

import java.util.*;

/**
 * Classe encarregada de representar teclats
 */
public class Keyboard {

    /*
        Classe per ordenar en ordre descendent una cua de prioritat de parells de String i enters en el valor de l'enter
     */

    /**
     * Identificador del teclat
     */
    private String name;

    /**
     * Distribució de tecles del teclat
     */
    private Character[][] keyboard;

    private HashMap<String, Integer> freqs;

    private double previousCost;

    private double currentCost;

    private boolean canShowCost;

    /**
     * Crea un subconjunt de symbols de com a molt 28 elements basat en la freqüencia dels parells de lletres de pairFreqs
     * tal que el parells que tenen major freqüencia, les lletres que la formen són al subconjunt.
     * @param symbols Conjunt de simbols amb que és treballa
     * @param pairFreqs Diccionari de parell de lletres i freqüencies en què apareixen
     * @return Subconjunt de symbols de mida com a màxim 28
     */
    private HashSet<Character> FirstPrioritaryLetter(HashSet<Character> symbols, HashMap<String, Integer> pairFreqs){
        HashSet<Character> ret = new HashSet<>();

        // PriorityQueue en orden descendente de e.first
        PriorityQueue<Utilities.Pair<String, Integer>> pq = new PriorityQueue<>(new Utilities.SortBySecond());
        Set<String> pairSet = pairFreqs.keySet();

        for (String s : pairSet) pq.add(new Utilities.Pair<>(s, pairFreqs.get(s)));

        while (!pq.isEmpty()) {
            // Sacamos el par con mayor frecuencia de pairFreqs
            String s = pq.poll().first;
            Character s1 = s.charAt(0);
            Character s2 = s.charAt(1);
            ret.add(s1);
            if (ret.size() >= Utilities.KeysPerKeyboard) return ret;
            ret.add(s2);
        }
        return ret;
    }

    /**
     * Constructora de la classe Keyboard
     * @param name Identificador que pendra el objecte
     * @param pairFreqs Parell de freqüencies en què es basa per fer la distribució de tecles inicial
     * @param alphabet Alfabet en que és basa per la distribució de tecles inicials
     * @param generationAlgorithm Algorisme escollit
     *                            <ul>
     *                            <li> 1 = Branch and Bound
     *                            <li> 2 = SimulatedAnnealing
     *                            </ul>
     * @throws ObjectDoesNotExistException Alguna de les lletres que apareix a pairFreqs no ho fa a symbols
     */
    public Keyboard(String name, final HashMap<String, Integer> pairFreqs, final HashSet<Character> alphabet, int generationAlgorithm) throws ObjectDoesNotExistException{
        this.name = name;

        for (String s : pairFreqs.keySet()) for (char c : s.toCharArray()) if (!alphabet.contains(c)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(c));

        this.freqs = pairFreqs;

        for (String s : freqs.keySet()) System.out.println(s);

        HashSet<Character> aux = new HashSet<>(alphabet);

        if (aux.isEmpty()) this.keyboard = new Character[3][10];
        else {
            if (aux.size() > Utilities.KeysPerKeyboard) aux = FirstPrioritaryLetter(alphabet, pairFreqs);
            switch (generationAlgorithm) {
                case 1:
                    this.keyboard = BranchAndBound.getInstance().generate_optimized_keyboard(aux, pairFreqs);
                    break;
                case 2:
                    this.keyboard = SimulatedAnnealing.getInstance().generate_optimized_keyboard(aux, pairFreqs);
                    break;
                default:
                    break;
            }
            this.keyboard[2][0] = '↑';
            this.keyboard [2][9] = '⌫';
        }
        previousCost = BranchAndBound.getInstance().computeCost(this.keyboard, this.freqs);
        currentCost = previousCost;
        this.canShowCost = true;
    }

    public Keyboard(String name, Character[][] distribution, HashMap<String, Integer> fq) {
        this.name = name;
        this.keyboard = distribution;
        this.canShowCost = false;
        if (fq != null) {
            this.canShowCost = true;
            // JSON carrega automaticament com a Long, per no canviar tot el codi em implementat això
            // Transforma d'objecte arbitrari a Long i de Long a integer
            // Com teoricament es Integer no deixaria compilar si fiquessim que fos Long
            freqs = new HashMap<>();
            for (String s : fq.keySet()) {
                Object aux = fq.get(s);
                int freq = (aux == null) ? 0 : ((Long) aux).intValue();
                freqs.put(s, freq);
            }
            currentCost = BranchAndBound.getInstance().computeCost(this.keyboard, this.freqs);
        }
    }

    /**
     * Canvia la lletra que hi havia en la posició i-j-èssima del teclat
     * @param i fila en que es troba la tecla a canviar
     * @param j columna en que es troba la tecla a canviar
     * @param newKey nou valor que prendrà la tecla
     * @throws ArrayIndexOutOfBoundsException S'intenta accedir a una posició impossible del teclat
     */
    private void setKey(int i, int j, Character newKey) throws ArrayIndexOutOfBoundsException {
        if ((j == 0 && i == 2) || (i==2 && j == 9)) throw new ArrayIndexOutOfBoundsException();
        keyboard[i][j] = newKey;
    }

    /**
     * Intercanvia 2 lletres del teclat
     * @param key1 Lletra del teclat
     * @param key2 Lletra del teclat
     * @throws ObjectDoesNotExistException Una de les dues lletres no és al teclat
     */
    public void swapKeys(Character key1, Character key2) throws ObjectDoesNotExistException {
        int key1i, key1j;
        int key2i, key2j;
        key1i = key1j = key2i = key2j = -1;
        boolean found1 = false, found2 = false;
        for (int i = 0; i < this.keyboard.length && !(found1 && found2); i++)
            for (int j = 0; j < this.keyboard[i].length && !(found1 && found2); j++)
                if (keyboard[i][j] != null) {
                    if (keyboard[i][j].equals(key1)) {
                        found1 = true;
                        key1i = i;
                        key1j = j;
                    } else if (keyboard[i][j].equals(key2)) {
                        found2 = true;
                        key2i = i;
                        key2j = j;
                    }
                }

        //Si key1i vale -1 significa que no hemos encontrado key1 (no existe en el alfabeto), lo mismo ocurre con key2
        if (key1i == -1) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(key1.charValue()));
        if (key2i == -1) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(key2.charValue()));
        this.setKey(key1i, key1j, key2);
        this.setKey(key2i, key2j, key1);

        updateCost();
    }

    /**
     * Insereix la lletra key al teclat si té espai i key no és ja al teclat
     * @param key Lletra que es vol afegir al teclat
     * @throws ObjectAlreadyExistException Ja és la lletra key al teclat
     * @throws MaxCapacityExceeded S'intenta afegir una lletra i el teclat és ple
     */
    public void addKey(Character key) throws ObjectAlreadyExistException, MaxCapacityExceeded {
        if (hasKey(key)) throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(key.charValue()));
        //if (!alphabet.contains(key)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(key.charValue()));
        for (int i = 0; i < Utilities.keysPos.length; i++) {
            int n = Utilities.keysPos[i];
            if (keyboard[n / 10][n % 10] == null) {
                setKey(n / 10, n % 10, key);
                updateCost();
                return;
            }
        }
        throw new MaxCapacityExceeded(Utilities.KeysPerKeyboard);
    }

    /**
     * Comprova si la lletra key apareix o no al teclat
     * @param key Lletra a comprovar
     * @return True si key és al teclat, false altrament
     */
    private final boolean hasKey(Character key) {
        for (Character[] chars : keyboard) for (Character c : chars) if (c != null && c.equals(key)) return true;
        return false;
    }

    /**
     * Elimina la lletra key del teclat
     * @param key Lletra que es vol eliminar
     * @throws ObjectDoesNotExistException La lletra a eliminar no és a l'alfabet
     */
    public void removeKey(Character key) throws ObjectDoesNotExistException {
        for (int i = 0; i < keyboard.length; i++) {
            for (int j = 0; j < keyboard[i].length; j++) {
                if (keyboard[i][j] != null && keyboard[i][j].equals(key)) {
                    keyboard[i][j] = null;
                    if (canShowCost) {
                        for (String s : this.freqs.keySet()) {

                            System.out.println(s + " contains " + key + "?");
                            if (s.contains(key.toString())) {
                                canShowCost = false;
                                updateCost();
                                break;
                            }
                        }
                        if (!canShowCost) CtrlKeyboard.getInstance().deleteKeyboardFreq(this.name);
                    }
                    return;
                }
            }
        }
        throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(key.charValue()));
    }

    /**
     * Consultora de l'identificador del teclat
     * @return copia de l'identificador del teclat
     */
    public final String getName() {
        return new String(this.name);
    }

    /**
     * Consultora de la distribució del teclat
     * @return copia de la distribució del teclat
     */
    public final Character[][] getKeyboard() {
        return this.keyboard.clone();
    }

    /**
     * Canvia la lletra a la posició i-j-essima per key
     * @param i Fila del teclat
     * @param j Columna del teclat
     * @param key Lletra que es vol que aparegui a la posició i-j-essima
     * @throws ObjectAlreadyExistException La lletra key ja es al teclat
     * @throws ArrayIndexOutOfBoundsException S'intenta accedir a una posició impossible del teclat
     */
    public void changeKey(int i, int j, Character key) throws ObjectAlreadyExistException, ArrayIndexOutOfBoundsException {
        if (hasKey(key)) throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.CHARACTER, String.valueOf(key.charValue()));
        boolean canModifyShowCost = false;

        if (this.keyboard[i][j] != null && canShowCost) for (String s : this.freqs.keySet()) {
            if (s.contains(this.keyboard[i][j].toString())) canModifyShowCost = true;
            updateCost();
            break;
        }
        setKey(i, j, key);
        if (canModifyShowCost && canShowCost) {
            canShowCost = false;
            for (String s : this.freqs.keySet()) {
                if (s.contains(key.toString())) canShowCost = true;
                updateCost();
                break;
            }
            if (!canShowCost) CtrlKeyboard.getInstance().deleteKeyboardFreq(this.name);
        }
    }

    private void updateCost() {
        previousCost = currentCost;
        if (freqs != null) currentCost = BranchAndBound.getInstance().computeCost(this.keyboard, this.freqs);
    }

    public double getKeyboardImprovement() {
        if (canShowCost) {
            if (currentCost <= previousCost) return (100 - currentCost/previousCost * 100.0);
            else return -(100 - (previousCost/currentCost * 100.0));
        }
        else return Double.NaN;
    }
}
