package edu.upc.prop.cluster21.persistence;

import edu.upc.prop.cluster21.domain.classes.Utilities;
import edu.upc.prop.cluster21.exceptions.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Classe que s'encarrega de la gestió de llistes de freqüències al disc
 */
public class ListFreqManager {

    /**
     * Instància seguint el patró singleton
     */
    private static ListFreqManager current;

    /**
     * Getter de la única instància seguint el patró singleton
     * @return La instància actual
     */
    public static ListFreqManager getInstance() {
        if (current == null) current = new ListFreqManager();
        return current;
    }

    /**
     * Salva una llista de freqüències a memòria
     * @param userName L'usuari que vol guardar la llista
     * @param listFreqName El nom de la llista
     * @param freqs Les freqüències de la llista
     * @throws OutputException En cas d'error d'escriptura
     */
    public void saveListFreq(String userName, String listFreqName, HashMap<String, Integer> freqs) throws OutputException{
        createDirectoryIfNotExists("data/" + userName + "/LlistFreqs");


        File jsonFile = new File(getDir(userName, listFreqName));
        JSONObject jsonRoot;
        if (jsonFile.exists()) jsonFile.delete();

        jsonRoot = new JSONObject(freqs);
        writeJsonToFile(jsonRoot, jsonFile);
    }

    /**
     * Carrega totes les llistes de freqüències que té emmagatzemades un usuari.
     * @param user L'usuari del qual volem les llistes
     * @return Un hashmap amb totes les llistes de l'usuari
     * @throws InputException En cas d'error llegint dels fitxers.
     */
    public HashMap<String, HashMap<String, Integer>> loadListFreqs(String user) throws InputException{

        HashMap<String, HashMap<String, Integer>> ret = new HashMap<>();

        String dir = "data/" + user + "/LlistFreqs";
        File folder = new File (dir);
        try {
            for (File fl : folder.listFiles()) {
                ret.put(fl.getName().replaceFirst("[.][^.]+$", ""), (HashMap<String, Integer>) loadJsonFromFile(fl));
                System.out.println(fl.getName());
            }

        }
        catch (NullPointerException e){
            return new HashMap<>();
        }
        return ret;
    }

    /**
     * Modifica una de les llistes de freqüències que tenim emmagatzemades.
     * @param userName L'usuari del qual pertany la llista
     * @param listFreqName El nom de la llista
     * @param word La paraula que volem modificar
     * @param times La nova freqüència de la paraula
     * @throws ObjectDoesNotExistException En cas de que l'usuari no tingui cap llista amb aquest nom
     * @throws InputException En cas d'error llegint dels fitxers
     * @throws OutputException En cas d'errror al actualitzar els fitxers
     */
    public void modifyListFreq(String userName, String listFreqName, String word, int times) throws ObjectDoesNotExistException , InputException,OutputException{

        File f = new File("data/" + userName + "/LlistFreqs/" + listFreqName + ".json");
        if (!f.exists()) {
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.LISTFREQ, listFreqName);
        }
        JSONObject jsonRoot = loadJsonFromFile(f);

        jsonRoot.put(word, times);
        writeJsonToFile(jsonRoot, new File("data/" + userName + "/LlistFreqs/" + listFreqName + ".json"));
    }

    /**
     * Elimina una llista de freqüències de la memòria.
     * @param userName L'usuari al qual pertany la llista
     * @param listFreqName El nom de la llista
     */
    public void deleteListFreq(String userName, String listFreqName){
        File f = new File("data/" + userName + "/LlistFreqs/" + listFreqName + ".json");
        if (f.exists()) f.delete();
    }

    /**
     * Importa una llista de freqüències a una direcció
     * @param filePath Path d'on hem de treure la llista
     * @return La llista que acabem d'importar
     * @throws InputException En cas d'algun error al llegir del fitxer.
     */
    public Utilities.Pair<String, HashMap<String, Integer>> importFreqList(String filePath) throws InputException {
        try {
            File jsonFile = new File(filePath);
            JSONObject jsonRoot = loadJsonFromFile(jsonFile);
            String name = (String) jsonFile.getName().replaceFirst("[.][^.]+$", "");;

            return new Utilities.Pair<>(name, jsonRoot);
        } catch (Exception e) {
            throw new InputException();
        }
    }

    /**
     * Exporta una llista de freqüències a una direcció en concret.
     * @param name El nom de la llista
     * @param freqs Les freqüències de la llista
     * @param filePath Lloc on volem guardar-la
     * @throws OutputException En cas d'error d'escriptura
     */
    public void exportFreqList(String name, HashMap<String, Integer> freqs, String filePath) throws OutputException {
        try {
            String path = filePath + "/" + name + ".json";

            File jsonFile = new File(path);
            JSONObject jsonRoot = new JSONObject(freqs);

            writeJsonToFile(jsonRoot, jsonFile);
        } catch (Exception e) {
            throw new OutputException();
        }
    }

    /**
     * Crea un directori en cas de que no existeixi
     * @param directoryPath Directori que hem de crear si no existeix
     */
    private void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                return ;
            }
        }
    }

    /**
     * Llegeix un fitxer interpretant-lo com un JSONObject (HashMap)
     * @param jsonFile Fitxer que volem llegir
     * @return El JSONObject que conté el fitxer
     * @throws InputException En cas d'error de lectura
     */
    private JSONObject loadJsonFromFile(File jsonFile) throws InputException{
        JSONObject jsonObject = null;
        try (FileReader reader = new FileReader(jsonFile)) {
            jsonObject = (JSONObject) org.json.simple.JSONValue.parse(reader);
        } catch (IOException e) {
            throw new InputException();
        }
        return jsonObject != null ? jsonObject : new JSONObject();
    }

    /**
     * Escriu un JSONObject (HashMap) a un fitxer
     * @param jsonObject El objecte a escriure
     * @param jsonFile El fitxer on el volem escriure
     * @throws OutputException En cas d'error d'escriptura
     */
    private void writeJsonToFile(JSONObject jsonObject, File jsonFile) throws OutputException{
        try (FileWriter writer = new FileWriter(jsonFile)) {
            jsonObject.writeJSONString(writer);
        } catch (IOException e) {
            throw new OutputException();
        }
    }

    /**
     * Facilita l'obtenció del directori on salvar la llista de freqüències.
     * @param name El nom del usuari
     * @param ListFreqName El nom de la llista
     * @return El directori on s'ha de guardar
     */
    private String getDir(String name, String ListFreqName){
        return (new String("data/" + name + "/LlistFreqs/" + ListFreqName + ".json"));
    }
}
