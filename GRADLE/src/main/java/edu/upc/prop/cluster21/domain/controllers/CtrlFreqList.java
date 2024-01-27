package edu.upc.prop.cluster21.domain.controllers;

import edu.upc.prop.cluster21.domain.classes.Alphabet;
import edu.upc.prop.cluster21.domain.classes.FrequencyList;
import edu.upc.prop.cluster21.domain.classes.User;
import edu.upc.prop.cluster21.domain.classes.Utilities;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.persistence.AlphabetManager;
import edu.upc.prop.cluster21.persistence.ListFreqManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * CtrlFreqList és la classe controlador específica per a les funcionalitats de les llistes
 * de freqüències a la capa de domini
 */

public class CtrlFreqList {
    /**
     * Instància actual de FreqList seguint el patró singleton.
     */
    private static CtrlFreqList current;

    /**
     * Llistes de freqüències gestionades per el sistema.
     */
    private static HashMap<String, FrequencyList> _frequencyLists;

    /**
     * Retorna la instància actual seguint el patró singleton.
     * @return La instància actual.
     */
    public static CtrlFreqList getInstance() {
        if (current == null) {
            current = new CtrlFreqList();
            _frequencyLists = new HashMap<>();
        }
        return current;
    }

    /**
     * Carrega totes les llistes de freqüències del usuari al controlador
     * @param user  usuari del qual volem carregar les llistes
     */
    public void loadFreqLists(String user) throws InputException{
        _frequencyLists.clear();
        HashMap<String, HashMap<String, Integer>> aux = ListFreqManager.getInstance().loadListFreqs(user);
        for (HashMap.Entry<String, HashMap<String, Integer>> fq : aux.entrySet()){
            _frequencyLists.put(fq.getKey(), new FrequencyList(fq.getKey(), fq.getValue()));
        }
    }

    /**
     * Esborrem totes les llistes de freqüències del controlador
     */
    public void rmFreqLists(){
        _frequencyLists.clear();
    }

    /**
     * Funció que retorna una llista de freqüència a partir d'un identificador.
     * @param name Identificador de la instància de la qual es vol la llista de freqüències.
     * @return Llista de freqüències de la instància amb indentificador name.
     * @throws ObjectDoesNotExistException En cas de que no existeixi cap instància amb identificador name.
     */
    public HashMap<String, Integer> getWordFreq(String name) throws ObjectDoesNotExistException{
        if (!_frequencyLists.containsKey(name))
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, name);
        return (_frequencyLists.get(name).getWordFreq());
    }

    public HashMap<String, Integer> getPairsFreq(String name) throws ObjectDoesNotExistException{
        if (!_frequencyLists.containsKey(name))
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, name);
        return (_frequencyLists.get(name).getPairsFreq());
    }

    /**
     * Funció que permet crear i afegir al conjunt una llista de freqüències
     * @param name Identificador que tindrà la llista.
     * @param text Text a partir del qual es deduirà la llista.
     * @throws ObjectAlreadyExistException En cas de que ja existeixi una llista amb indentificador name.
     */
    public void createFrequencyList(String name, String text) throws ObjectAlreadyExistException, OutputException {
        if (_frequencyLists.containsKey(name))
            throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, name);
        _frequencyLists.put(name, new FrequencyList(name, text));
        String userName = User.getInstance().getUser();
        ListFreqManager.getInstance().saveListFreq(userName, name, _frequencyLists.get(name).getWordFreq());
    }

    /**
     * Funció que elimina una llista de freqüències del conjunt.
     * @param name Identificador de la llista que es vol eliminar.
     */
    public void deleteFrequencyList(String name) throws ObjectDoesNotExistException {
       if (!_frequencyLists.containsKey(name)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, name);
        System.out.println("Remove " + name);
        _frequencyLists.remove(name);
        String userName = User.getInstance().getUser();
        ListFreqManager.getInstance().deleteListFreq(userName, name);
    }

    /**
     * Funció per afegir una nova paraula amb la seva freqüència a una de les llistes del conjunt de l'usuari
     * @param name Nom de la llista a la qual es vol afegir una paraula
     * @param word Paraula que es vol afegir
     * @param freq Freqüència de la paraula que es vol afegir
     * @throws ObjectDoesNotExistException Es llença quan al conjunt no existeix la llista name
     */
    public void addWordToFreqList(String name, String word, Integer freq) throws ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
        if (_frequencyLists.containsKey(name)) {
            _frequencyLists.get(name).setNewWord(word, freq);

            String userName = User.getInstance().getUser();
            ListFreqManager.getInstance().modifyListFreq(userName,name, word, freq);
        }
        else throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, _frequencyLists.get(name).getName());
    }

    /**
     * Funció per eliminar una paraula a una de les llistes del conjunt de l'usuari
     * @param name Nom de la llista que es vol modificar
     * @param word Paraula que es vol eliminar de la llista
     * @throws ObjectDoesNotExistException Es llença quan al conjunt no existeix la llista name
     */
    public void deleteWordToFreqList(String name, String word) throws ObjectDoesNotExistException, InputException, OutputException {
        if (_frequencyLists.containsKey(name)) {
            _frequencyLists.get(name).deleteWord(word);
            String userName = User.getInstance().getUser();
            ListFreqManager.getInstance().modifyListFreq(userName,name, word, 0);
        }
        else throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, _frequencyLists.get(name).getName());
    }

    /**
     * Funcio per obtenir els identificadors de totes les llistes de freqüències.
     * @return Llista d'identificadors de totes les llistes.
     */
    public ArrayList<String> getFrequencyListsNames() {
        ArrayList<String> ret = new ArrayList<>();
        for (HashMap.Entry<String, FrequencyList> fq : _frequencyLists.entrySet()) {
            ret.add(fq.getKey());
        }
        return ret;
    }

    /**
     * Funció per modificar la freqüència d'una paraula en la llista de freqüències.
     * @param name Identificador de la llista que es vol modificar.
     * @param word Paraula que es vol modificar.
     * @param times Nombre d'aparicions pel que es vol modificar.
     * @throws NegativeAppearancesException En cas de que resulti en aparicions negatives.
     * @throws ObjectDoesNotExistException En cas de que l'identificador no representi cap llista.
     */
    public void modifyAppearances(String name, String word, int times) throws NegativeAppearancesException, ObjectDoesNotExistException, InputException, OutputException {
        if (!_frequencyLists.containsKey(name))
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, name);
        _frequencyLists.get(name).modifyOrInsertWordAppearances(word,times);

        String userName = User.getInstance().getUser();
        ListFreqManager.getInstance().modifyListFreq(userName,name, word, times);
    }

    public void importFreqList(String filePath) throws InputException, OutputException {

        Utilities.Pair<String, HashMap<String, Integer>> imported = ListFreqManager.getInstance().importFreqList(filePath);

        //Ara guardem l'alfabet al conjunt d'alfabets de l'usuari.
        FrequencyList newFreq = new FrequencyList(imported.first, imported.second);
        _frequencyLists.put(imported.first, newFreq);

        String userName = User.getInstance().getUser();
        ListFreqManager.getInstance().saveListFreq(userName, imported.first, imported.second);
    }

    public void exportFreqList(String name, String filePath) throws ObjectDoesNotExistException, OutputException {
        if (!_frequencyLists.containsKey(name)) throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.FREQUENCYLIST, name);
        ListFreqManager.getInstance().exportFreqList(name, _frequencyLists.get(name).getWordFreq(), filePath);
    }
}
