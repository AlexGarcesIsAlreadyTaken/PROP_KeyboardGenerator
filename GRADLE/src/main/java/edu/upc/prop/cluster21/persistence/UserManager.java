package edu.upc.prop.cluster21.persistence;
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

/**
 * Classe que s'encarrega de la gestió d'usuaris en memòria
 */
public class UserManager {
    private String dir = "data/users.json";

    /**
     * La instància de la classe actual
     */
    private static UserManager current;

    /**
     * Funció per obtenir la instància actual seguint el patró singleton
     * @return La instància actual
     */
    public static UserManager getInstance() {
        if (current == null) {
            current = new UserManager();
        }
        return current;
    }

    /**
     * Crea un usuari a l'aplicació
     * @param name Nom del usuari que volem crear
     * @param password Contrasenya del nou usuari
     * @throws ObjectAlreadyExistException En cas de que el nom ja estigui en ús
     * @throws InputException En cas d'error de lectura al sistema de fitxers
     * @throws OutputException En cas d'error escriptura al sistema de fitxers
     */
    public void CreateUser(String name, String password) throws ObjectAlreadyExistException, InputException, OutputException{

        File jsonFile = new File(dir);
        JSONObject jsonRoot;

        if (jsonFile.exists()) jsonRoot = loadJsonFromFile(jsonFile);
        else jsonRoot = new JSONObject();

        for (Object key : jsonRoot.keySet()){
            String aux = (String)key;
            if (aux.equals(name))
                throw new ObjectAlreadyExistException(ObjectExistenceException.TypeObject.USER, name);
        }

        jsonRoot.put(name, password);

        writeJsonToFile(jsonRoot, jsonFile);
    }

    /**
     * Retorna un usuari i contrasenya a partir de l'usuari
     * @param name L'usuari que volem obtenir
     * @return Una array on la primera posició és l'usuari i la segona la contrasenya
     * @throws ObjectDoesNotExistException En cas de que l'usuari no existeixi
     * @throws InputException En cas d'error de lectura al sistema de fitxers
     */
    public String[] getUser(String name) throws ObjectDoesNotExistException, InputException{
        String []ret = new String[2];

        JSONObject jsonRoot = loadJsonFromFile(new File(dir));
        for (Object key : jsonRoot.keySet()) {
            String aux = (String) key;
            System.out.println(aux + " " + name);
            if (aux.equals(name)) {
                ret[0] = new String(name);
                ret[1] = new String((String) jsonRoot.get(name));
                return ret;
            }
        }
        throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.USER, name);
    }

    /**
     * Modifica la contrasenya d'un usuari
     * @param name El nom de l'usuari que volem modificar
     * @param newpassword La nova contrasenya
     * @throws ObjectDoesNotExistException En cas de que l'usuari no existeixi
     * @throws InputException En cas d'error de lectura
     * @throws OutputException En cas d'error d'escriptura
     */
    public void modifyUser(String name, String newpassword) throws ObjectDoesNotExistException, InputException, OutputException{
        JSONObject jsonRoot = loadJsonFromFile(new File(dir));

        boolean found = false;
        for (Object key : jsonRoot.keySet()){
            String aux = (String)key;
            if (aux.equals(name))
                found = true;
        }
        if (found == false)
            throw new ObjectDoesNotExistException(ObjectExistenceException.TypeObject.USER, name);

        jsonRoot.put(name, newpassword);
        writeJsonToFile(jsonRoot, new File(dir));
    }

    /**
     * Llegeix un fitxer com a JSONObject
     * @param jsonFile El fitxer que volem llegir
     * @return El JSONObject resultant
     * @throws InputException En cas d'error de lectura
     */
    private JSONObject loadJsonFromFile(File jsonFile) throws InputException {
        JSONObject jsonObject = null;
        try (FileReader reader = new FileReader(jsonFile)) {
            jsonObject = (JSONObject) org.json.simple.JSONValue.parse(reader);
        } catch (IOException e) {
            throw new InputException();
        }
        return jsonObject != null ? jsonObject : new JSONObject();
    }

    /**
     * Escriu un fitxer com a JSONObject
     * @param jsonObject El JSONObject que volem escriure
     * @param jsonFile El fitxer on el volem escriure
     * @throws OutputException En cas d'error d'escriptura
     */
    private void writeJsonToFile(JSONObject jsonObject, File jsonFile) throws OutputException {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            jsonObject.writeJSONString(writer);
        } catch (IOException e) {
            throw new OutputException();
        }
    }
}
