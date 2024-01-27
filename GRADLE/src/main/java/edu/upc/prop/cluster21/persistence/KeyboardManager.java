package edu.upc.prop.cluster21.persistence;

import edu.upc.prop.cluster21.domain.classes.Utilities;
import edu.upc.prop.cluster21.exceptions.InputException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import edu.upc.prop.cluster21.exceptions.ObjectExistenceException;
import edu.upc.prop.cluster21.exceptions.OutputException;
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

/**
 * Classe que s'encarrega de la gestió de persistència dels teclats de l'usuari.
 */
public class KeyboardManager {

    /**
     * Instància singleton
     */
    private static KeyboardManager current;

    /**
     * Funció que permet obtenir la instància seguint el patró singleton
     * @return La instància
     */
    public static KeyboardManager getInstance() {
        if (current == null) current = new KeyboardManager();
        return current;
    }

    /**
     * Guarda un teclat en memòria
     * @param userName Nom del usuari al que li volem guardar el teclat
     * @param keyboardName Nom del teclat que volem guardar
     * @param distribution Distribució que volem guardar
     * @param fq Llista de freqüències del teclat que volem guardar
     */
    public void saveKeyboard(String userName, String keyboardName, Character[][] distribution, HashMap<String, Integer> fq) throws InputException, OutputException{
        String baseDir = "data/" +  userName;
        String filePath = baseDir + "/keyboards.json";

        //Si no existeix el directori, es crea:
        createDirectoryIfNotExists(baseDir);

        //Si no existeix el ficher .json es crea,
        //altrament es carrega el contingut per actualitzar-lo
        File jsonFile = new File(filePath);
        JSONObject jsonRoot;
        if (jsonFile.exists()) jsonRoot = loadJsonFromFile(jsonFile);
        else jsonRoot = new JSONObject();

        //Es crea un JSONArray per la distribució del teclat
        JSONArray keyboardArray = new JSONArray();

        for (int i = 0; i < distribution.length; i++) {
            for (int j = 0; j < distribution[i].length; j++) {
                Character symbol = distribution[i][j];
                if (symbol != null) {
                    JSONArray tripletArray = new JSONArray();
                    tripletArray.add(symbol.toString());
                    tripletArray.add(i);
                    tripletArray.add(j);
                    keyboardArray.add(tripletArray);
                }
            }
        }

        //S'afegeix el nou teclat al jsonRoot
        jsonRoot.put(keyboardName, keyboardArray);


        //S'escriu el contingut actualitzat al jsonFile
        writeJsonToFile(jsonRoot, jsonFile);


        //guardar parells de frequencies apart per poder calcular el cost del teclat posteriorment
        if (fq != null) {
            createDirectoryIfNotExists("data/" + userName + "/.fq/");

            File jsonFile2 = new File("data/" + userName + "/.fq/" + keyboardName + ".json");
            JSONObject jsonRoot2;
            if (jsonFile.exists()) jsonFile2.delete();

            jsonRoot2 = new JSONObject(fq);
            writeJsonToFile(jsonRoot2, jsonFile2);
        }
    }

    /**
     * Retorna tots els teclats d'un usuari
     * @param user Usuari del que volem els teclats
     * @return Conjunt de teclats de l'usuari
     */
    public HashMap<String, Character[][]> loadKeyboards(String user) throws InputException{
        String baseDir = "data/" + user;
        String filePath = baseDir + "/keyboards.json";
        HashMap<String, Character[][]> result = new HashMap<>();

        File jsonFile = new File(filePath);

        if (jsonFile.exists()) {
            JSONObject jsonRoot = loadJsonFromFile(jsonFile);

            for (Object key : jsonRoot.keySet()) {
                String keyboardName = (String) key;
                JSONArray keyboardArray = (JSONArray) jsonRoot.get(keyboardName);
                Character[][] distribution = new Character[3][10];

                for (Object tripletObj : keyboardArray) {
                    JSONArray tripletArray = (JSONArray) tripletObj;
                    String symbol = (String) tripletArray.get(0);
                    int i = ((Long) tripletArray.get(1)).intValue();
                    int j = ((Long) tripletArray.get(2)).intValue();
                    distribution[i][j] = symbol.charAt(0);
                }

                result.put(keyboardName, distribution);
            }
        }
        return result;
    }

    /**
     * Retorna la llista de freqüències amb la que un teclat s'ha creat
     * @param user Usuari del que volem la llista
     * @param name Nom del teclat
     * @return Llista de freqüències amb la que s'ha creat el teclat
     * @throws ObjectDoesNotExistException En cas de que el teclat no existeixi o no tingui llista
     */
    public HashMap<String, Integer> loadKeyboardFreq(String user, String name) throws ObjectDoesNotExistException, InputException{
        String baseDir = "data/" + user;
        String filePath = baseDir + "/.fq/" + name +".json";

        File jsonFile = new File(filePath);
        if (!jsonFile.exists())
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.LISTFREQ, name);
        JSONObject jsonRoot = loadJsonFromFile(jsonFile);
        return jsonRoot;
    }

    /**
     * Borra la llista d'un teclat en concret
     * @param user Nom del usuari al que li volem borrar la llista
     * @param name Nom de la llista que volem borrar
     */
    public void deleteKeyboardFreq(String user, String name){
        String baseDir = "data/" + user;
        String filePath = baseDir + "/.fq/" + name +".json";

        File jsonFile = new File(filePath);
        jsonFile.delete();
    }

    /**
     * Modifica un teclat de la memoria
     * @param userName Usuari que te el teclat a modificar
     * @param keyboardName Nom del teclat
     * @param distribution Modificació que li volem aplicar
     */
    public void modifyKeyboard(String userName, String keyboardName, Character[][] distribution) throws InputException, OutputException{
        String filePath = "data/" + userName + "/keyboards.json";
        JSONObject jsonRoot = loadJsonFromFile(new File(filePath));

        JSONArray keyboardArray = new JSONArray();

        for (int i = 0; i < distribution.length; i++) {
            for (int j = 0; j < distribution[i].length; j++) {
                Character symbol = distribution[i][j];
                if (symbol != null) {
                    JSONArray tripletArray = new JSONArray();
                    tripletArray.add(symbol.toString());
                    tripletArray.add(i);
                    tripletArray.add(j);
                    keyboardArray.add(tripletArray);
                }
            }
        }

        //S'afegeix el nou teclat actualitzat al jsonRoot (put ja sobreescriu el contingut si la clau ja existeix)
        jsonRoot.put(keyboardName, keyboardArray);

        //S'escriu el contingut actualitzat al jsonFile
        writeJsonToFile(jsonRoot, new File(filePath));
    }

    /**
     * Esborra un teclat
     * @param userName Nom de l'usuari al que es borra el teclat
     * @param keyboardName Nom del teclat que s'ha de borrar
     * @throws InputException En cas d'error de lectura
     * @throws OutputException En cas d'error d'escriptura
     */
    public void deleteKeyboard(String userName, String keyboardName) throws InputException, OutputException{
        String filePath = "data/" + userName + "/keyboards.json";
        JSONObject jsonRoot = loadJsonFromFile(new File(filePath));
        jsonRoot.remove(keyboardName);
        writeJsonToFile(jsonRoot, new File(filePath));

        //Si el fitxer esta buit l'eliminem
        if (jsonRoot.isEmpty()) {
            File jsonFile = new File(filePath);
            if (jsonFile.exists()) jsonFile.delete();
        }
    }

    /**
     * Importa un teclat des del sistema de fitxers
     * @param filePath Ruta fins al fitxer
     * @return El teclat que estava guardat al fitxer
     * @throws InputException En cas d'error al llegir el fitxer
     */
    public Utilities.Pair<String, Character[][]> importKeyboard(String filePath) throws InputException {
        try {
            File jsonFile = new File(filePath);
            JSONObject jsonRoot = loadJsonFromFile(jsonFile);
            String keyboardName = (String) jsonRoot.keySet().iterator().next();

            JSONArray keyboardArray = (JSONArray) jsonRoot.get(keyboardName);
            Character[][] distribution = new Character[3][10];

            for (Object tripletObj : keyboardArray) {
                JSONArray tripletArray = (JSONArray) tripletObj;
                String symbol = (String) tripletArray.get(0);
                int i = ((Long) tripletArray.get(1)).intValue();
                int j = ((Long) tripletArray.get(2)).intValue();
                distribution[i][j] = symbol.charAt(0);
            }

            return new Utilities.Pair<>(keyboardName, distribution);

        } catch (Exception e) {
            throw new InputException();
        }
    }

    /**
     * Exporta un teclat a una destinació en concret
     * @param keyboardName Nom del teclat que volem exportar
     * @param distribution Diestribució que s'exporta
     * @param filePath Destinació al sistema de fitxers
     * @throws OutputException En cas d'error d'escriptura
     */
    public void exportKeyboard(String keyboardName, Character[][] distribution, String filePath) throws OutputException {
        try {
            String path = filePath + "/" + keyboardName + ".json";

            File jsonFile = new File(path);
            JSONObject jsonRoot = new JSONObject();

            JSONArray keyboardArray = new JSONArray();
            for (int i = 0; i < distribution.length; i++) {
                for (int j = 0; j < distribution[i].length; j++) {
                    Character symbol = distribution[i][j];
                    if (symbol != null) {
                        JSONArray tripletArray = new JSONArray();
                        tripletArray.add(symbol.toString());
                        tripletArray.add(i);
                        tripletArray.add(j);
                        keyboardArray.add(tripletArray);
                    }
                }
            }
            jsonRoot.put(keyboardName, keyboardArray);
            writeJsonToFile(jsonRoot, jsonFile);

        } catch (Exception e) {
            throw new OutputException();
        }
    }


    /**
     * Crea un directori en cas que no existeixi
     *
     * @param directoryPath Path del directori
     */
    private void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                //res
            }
        }
    }

    /**
     * Mètode que permet obtenir l'objecte JSON associat al contingut d'un fitxer .json
     *
     * @param jsonFile Fitxer JSON
     * @return Objecte JSON que correspon al contingut del jsonFile en cas que existeixi,
     * un objecte JSON buit en cas contrari
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
     * Mètode que escriu un fitxer .json amb el contingut d'un Objecte JSON
     *
     * @param jsonObject Objecte JSON
     * @param jsonFile   Fitxer JSON
     */
    private void writeJsonToFile(JSONObject jsonObject, File jsonFile) throws OutputException{
        try (FileWriter writer = new FileWriter(jsonFile)) {
            // Escriure el nou JSON actualitzat directament a l'arxiu
            jsonObject.writeJSONString(writer);
        } catch (IOException e) {
            throw new OutputException();
        }
    }


    //------------------- TEST PROVISIONAL ------------------
    /*public static void main(String[] args) {
        // Ejemplo de uso para saveKeyboard
        Character[][] distribution = {
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'},
                {'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'},
                {'U', 'V', 'W', 'X', 'Y', 'Z', null, null, null, null}
        };

        //KeyboardManager.getInstance().saveKeyboard("isma", "qwerty", distribution);
        //KeyboardManager.getInstance().saveKeyboard("isma", "qwerty2", distribution);

        Character[][] distribution2 = {
                {'A', '*', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'},
                {'K', 'L', 'M', null, 'O', 'P', 'Q', 'R', 'S', 'T'},
                {null, 'V', 'W', 'X', 'Y', 'Z', null, null, null, null}
        };

        KeyboardManager.getInstance().modifyKeyboard("isma", "qwerty", distribution2);

        KeyboardManager.getInstance().deleteKeyboard("isma", "qwerty2");

        HashMap<String, Character[][]> loadedKeyboards = KeyboardManager.getInstance().loadKeyboards("isma");

        for (String keyboardName : loadedKeyboards.keySet()) {
            System.out.println("Keyboard: " + keyboardName);
            Character[][] matrix = loadedKeyboards.get(keyboardName);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }

    }*/
}
