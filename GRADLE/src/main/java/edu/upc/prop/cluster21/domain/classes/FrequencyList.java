package edu.upc.prop.cluster21.domain.classes;

import edu.upc.prop.cluster21.exceptions.NegativeAppearancesException;
import edu.upc.prop.cluster21.exceptions.ObjectAlreadyExistException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import edu.upc.prop.cluster21.exceptions.ObjectExistenceException;


import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

/**
 * Classe encaregada de crear les llistes de freqüències
 */
public class FrequencyList {
    /**
     * Nom de la llista de frequència. Un usuari no te dues llistes de frequències amb el mateix nom.
     */
    private String _name;
    /**
     * Hashmap on guardem la llista en concret.
     */
    private HashMap<String, Integer> _WordFreq;
    
    //CONSTRUCTORS

    /**
     *
     * @param name Identificador que pendra el objecte
     * @param text Text a partir del qual es construirà la llista de frequències
     */
    public FrequencyList(String name, String text) {
        _name = name;
        _WordFreq = new HashMap<>();
        setWordsFreq(text);
    }

    public FrequencyList(String name, HashMap<String, Integer> hm) {
        _name = name;
        _WordFreq = new HashMap<>();
        for (String key : hm.keySet()) {
            Object aux = hm.get(key);
            _WordFreq.put(key, ((Long)aux).intValue());
        }
    }


    //GETTERS Y SETTERS

    /**
     *
     * @return Nom de la llista de frequències en concret.
     */
    public String getName(){
        return new String(_name);
    }

    /**
     * Dona valor a la llista de frequència a partir d'un text.
     * @param text Text a partir del qual es construirà la llista
     */
    private void setWordsFreq(String text) {
        text = text.toLowerCase();
        StringTokenizer stream = new StringTokenizer(text, " ,.:;\n");
        while (stream.hasMoreTokens()) {
            String word = stream.nextToken();
            if (_WordFreq.containsKey(word)){
                int count = _WordFreq.get(word);
                _WordFreq.put(word, count +1);
            }
            else
                _WordFreq.put(word, 1);
        }
    }

    /**
     * Funció que permet afegir una nova paraula amb la seva freqüència a la llista
     * @param word Paraula que es vol afegir
     * @param freq Freqüència que tindrà la paraula
     * @throws ObjectAlreadyExistException Es llança si la paraula ja pertanyia a la llista
     */
    public void setNewWord(String word, Integer freq) throws ObjectAlreadyExistException {
        if (!_WordFreq.containsKey(word)) {
            _WordFreq.put(word, freq);
        }
        else throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.WORD, word);
    }

    /**
     * Funció que permet eliminar una de les paraules de la llista
     * @param word Paraula que es vol eliminar
     * @throws ObjectDoesNotExistException Es llança si la paraula que es vol eliminar no pertany a la llista
     */
    public void deleteWord(String word) throws ObjectDoesNotExistException {
        if (_WordFreq.containsKey(word)) {
            _WordFreq.remove(word);
        }
        else throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.WORD, word);
    }

    /**
     *
     * @return La llista de freqüències expresada com a HashMap.
     */
    public HashMap<String, Integer> getWordFreq(){
        HashMap<String, Integer> ret = new HashMap<>();
        for (String s : _WordFreq.keySet()) {
            if (_WordFreq.get(s) > 0) ret.put(s, _WordFreq.get(s));
        }
        return ret;
    }

    /**
     *
     * @return La freqüència dels parells de lletres que apareixen a la llista.
     */
    public HashMap<String, Integer> getPairsFreq(){
        HashMap<String, Integer> PairsFreq = new HashMap<String, Integer>();
        for (String key : _WordFreq.keySet()) {
            int value = (Integer) _WordFreq.get(key);
            for (int i = 0; i < key.length() - 1; i++) {
                String adjacentPair = key.substring(i, i + 2);
                if (PairsFreq.containsKey(adjacentPair)){
                    int count = PairsFreq.get(adjacentPair);
                    PairsFreq.put(adjacentPair, count +value);
                }
                else
                    PairsFreq.put(adjacentPair, value);
            }
        }
        return PairsFreq;
    }

    //MEMBER FUNCTIONS

    /**
     * Mofifica la freqüència de una paraula a la llista de freqüències
     * @param word Paraula que es vol modificar
     * @param times Valor que s'assignarà a la paraula
     * @throws NegativeAppearancesException En cas de que tinguem aparicions negatives.
     */
    public void modifyOrInsertWordAppearances(String word, int times) throws NegativeAppearancesException{
        if (times< 0)
            throw new NegativeAppearancesException(word);
        _WordFreq.put(word, times);
    }
}
