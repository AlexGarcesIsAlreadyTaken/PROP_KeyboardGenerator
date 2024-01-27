package edu.upc.prop.cluster21.domain.classes;

import edu.upc.prop.cluster21.exceptions.*;

import java.util.*;

/**
 * Alphabet és la classe encarregada de representar alfabets
 */

public class Alphabet  {

    /**
     * Identificador d'alfabet
     */
    private String _name;

    /**
     * Contenidor dels símbols de cada alfabet
     */
    private HashSet<Character> symbols;

    /**
     * Creadora d'alfabet
     * @param name Identificador que pren el objecte
     * @param text Text a partir del qual s'extreuen els símbols de l'alfabet a crear
     * @throws MaxCapacityExceeded Es llença si s'intenta crear un teclat amb massa símbols
     */
    public Alphabet(String name, String text) throws MaxCapacityExceeded {
        _name = name;
        symbols = new HashSet<>();
        setWordsAlph(text);
    }

    public Alphabet(String name, HashSet<Character> symb){
        _name = name;
        symbols = symb;
    }

    /**
     * Traductora de text a símbols per als alfabets.
     * Funcionament: Per a cada paraula continguda a l'string, abstreu cada char per
     * separat i l'afegeix al hashset de l'objecte alphabet per guardar-lo.
     * @param text String del que se n'extreuen els símbols per l'alfabet
     * @throws MaxCapacityExceeded Es llença si es supera el llindar de símbols a l'objecte
     */
    private void setWordsAlph(String text) throws MaxCapacityExceeded {
        text.toLowerCase();
        StringTokenizer chain = new StringTokenizer(text, " .,:;");
        while (chain.hasMoreTokens()) {
            String word = chain.nextToken();
            for (int i = 0; i < word.length(); ++i) {
                Character letter = word.charAt(i);
                if (symbols.size() < 56) {
                    if (!symbols.contains(letter)) {
                        symbols.add(letter);
                    }
                }
                else {
                    throw new MaxCapacityExceeded(56);
                }
            }
        }
    }

    /**
     * Getter del Hashset dels símbols
     * @return Hashset còpia dels símbols de l'objecte
     */
    public final HashSet<Character> getSymbols() {
        return new HashSet<>(symbols);
    }

    /**
     * Getter de l'identificador de l'objecte alphabet
     * @return String còpia de l'identificador de l'objecte alphabet
     */
    public String getName() {
        return new String(_name);
    }

    /**
     * Funció que permet afegir nous símbols a l'objecte alphabet sempre que no es superi el llindar
     * @param letter Símbol/lletra que es vol afegir
     * @throws ObjectAlreadyExistException Es llança si es vol afegir una lletra ja existent al conjunt
     * @throws MaxCapacityExceeded Es llança si es vol afegir una lletra i el conjunt és ple
     */
    public void addLetter(Character letter) throws ObjectAlreadyExistException, MaxCapacityExceeded {
        if (!symbols.contains(letter)) {
            if (symbols.size() < 56) {
                symbols.add(letter);
            }
            else {
                throw new MaxCapacityExceeded(56);
            }
        }
        else {
            throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.CHARACTER, letter.toString());
        }
    }

    /**
     * Funció que permet eliminar un dels símbols a l'objecte alphabet sempre que aquest la contingui
     * i, quan l'elimini, no baixi del mínim.
     * @param letter Lletra que es vol eliminar del conjunt de símbols
     * @throws ObjectDoesNotExistException Es llança si la lletra que es vol eliminar no existeix al Hashset
     */
    public void deleteLetter(Character letter) throws ObjectDoesNotExistException, MinimumCapacityReached {
        if (symbols.contains(letter)) {

            if (symbols.size() > 1) symbols.remove(letter);
            else throw new MinimumCapacityReached(ObjectExistenceException.TypeObject.ALPHABET, getName());
        }
        else {
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.CHARACTER, letter.toString());
        }
    }

    /**
     * Funció que passa el conjunt de lletres de tipus hashset a un SetTree, per tal que l'alfabet es retorni ordenat.
     * @param symbols Conjunt de lletres el qual es vol ordenar
     * @return TreeSet amb les lletres del hashset ordenades
     */
    public Set<Character> convertToOrderedSet(HashSet<Character> symbols) {
        Iterator<Character> it = symbols.iterator();
        Set<Character> alphOrdenat = new TreeSet<>();
        while (it.hasNext()) {
            alphOrdenat.add(it.next());
        }
        return alphOrdenat;
    }
}
