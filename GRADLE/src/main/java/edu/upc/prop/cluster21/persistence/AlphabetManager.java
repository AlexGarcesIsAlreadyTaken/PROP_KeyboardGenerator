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
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Classe que s'encarrega de la gestió de persistència dels alfabets de l'usuari.
 */
public class AlphabetManager {

	//singleton design pattern
	private static AlphabetManager current;

	public static AlphabetManager getInstance() {
		if (current == null) current = new AlphabetManager();
		return current;
	}

	/**
	 * Aquest mètode assumeix que no hi altres alfabets guardats amb el mateix nom, ja que
	 * el controldar d'alfabets és qui controla aquesta situació. En cas que l'alfabet sigui
	 * nou, es guarda al fitxer JSON que conté la informació dels alfabets de l'usuari. En cas
	 * que sigui el primer, es crea el fitxer alphabets.json si és necessari.
	 *
	 * @param userName Nom de l'usuari
	 * @param alphabetName Nom de l'alfabet que es vol guardar
	 * @param symbols HashSet de símbols de l'alfabet que es vol guardar
	 * @throws OutputException hi ha hagut un error escribint al fitxer on s'havia de guardar l'alfabet
	 */
	public void saveAlphabet(String userName, String alphabetName, HashSet<Character> symbols) throws OutputException {
		try {
			String baseDir = "data/" + userName;
			String filePath = baseDir + "/alphabets.json";

			//Si no existeix el directori, es crea:
			createDirectoryIfNotExists(baseDir);

			//Si no existeix el ficher .json es crea,
			//altrament es carrega el contingut per actualitzar-lo
			File jsonFile = new File(filePath);
			JSONObject jsonRoot;
			if (jsonFile.exists()) jsonRoot = loadJsonFromFile(jsonFile);
			else jsonRoot = new JSONObject();

			//Ara l'actualitzem:
			JSONArray symbolsArray = new JSONArray();
			for (Character s : symbols) symbolsArray.add(s.toString());
			jsonRoot.put(alphabetName, symbolsArray);

			writeJsonToFile(jsonRoot, jsonFile);
		} catch (Exception e) {
			throw new OutputException();
		}
	}

	/**
	 * Aquest mètode permet al controlador d'alfabets obtenir la informació de tots
	 * els alfabets de l'usuari
	 *
	 * @param user Nom de l'usuari
	 * @return Un HashMap, amb claus els noms dels alfabets i valors un HashSet amb els símbols
	 * de l'alfabet
	 * @throws InputException Hi ha hagut un error llegint del fitxer
	 */
	public HashMap<String, HashSet<Character>> loadAlphabets(String user) throws InputException {
		try {
			String baseDir = "data/" + user;
			String filePath = baseDir + "/alphabets.json";
			HashMap<String, HashSet<Character>> result = new HashMap<>();

			File jsonFile = new File(filePath);
			if (jsonFile.exists()) {
				JSONObject jsonRoot = loadJsonFromFile(jsonFile);
				for (Object key : jsonRoot.keySet()) {
					String alphabetName = (String) key;
					JSONArray symbolsArray = (JSONArray) jsonRoot.get(alphabetName);
					HashSet<Character> symbolsSet = new HashSet<>();

					for (Object symbol : symbolsArray) {
						symbolsSet.add(((String) symbol).charAt(0));
					}
					result.put(alphabetName, symbolsSet);
				}
			}
			return result;
		} catch (Exception e) {
			throw new InputException();
		}
	}

	/**
	 * Aquest mètode assumeix que l'alfabet alphabetName ja s'ha guardat previament. El que fa
	 * és actualitzar el símbols associats amb els nous resultants d'una modificació.
	 *
	 * @param userName Nom de l'usuari
	 * @param alphabetName Nom de l'alfabet
	 * @param symbols HashSet amb els símbols que ara té l'alfabet que s'ha modificat
	 */
	public void modifyAlphabet(String userName, String alphabetName, HashSet<Character> symbols) throws InputException, OutputException {
		String filePath = "data/" + userName + "/alphabets.json";
		JSONObject jsonRoot = loadJsonFromFile(new File(filePath));

		JSONArray symbolsArray = new JSONArray();
		for (Character s : symbols) symbolsArray.add(s.toString());
		jsonRoot.put(alphabetName, symbolsArray);
		writeJsonToFile(jsonRoot, new File(filePath));
	}

	/**
	 * Elimina l'alfabet alphabetName dels alfabets guardats de l'usuari userName.
	 *
	 * @param userName Nom de l'usuari
	 * @param alphabetName Nom de l'alfabet
	 */
	public void deleteAlphabet(String userName, String alphabetName) throws InputException, OutputException{
		String filePath = "data/" + userName + "/alphabets.json";
		JSONObject jsonRoot = loadJsonFromFile(new File(filePath));
		jsonRoot.remove(alphabetName);
		writeJsonToFile(jsonRoot, new File(filePath));

		//Si el fitxer esta buit l'eliminem
		if (jsonRoot.isEmpty()) {
			File jsonFile = new File(filePath);
			if (jsonFile.exists()) jsonFile.delete();
		}
	}

	/**
	 * Mètode que permet importar un alfabet donat el seu path
	 *
	 * @param filePath Path del fitxer
	 * @return Retorna una parella amb el nom de l'alfabet i els seus símbols
	 * @throws InputException Excepció si hi ha problemes en el moment de llegir el fitxer
	 */
	public Utilities.Pair<String, HashSet<Character>> importAlphabet(String filePath) throws InputException {
		try {
			File jsonFile = new File(filePath);
			JSONObject jsonRoot = loadJsonFromFile(jsonFile);
			String alphabetName = (String) jsonRoot.keySet().iterator().next();

			JSONArray symbolsArray = (JSONArray) jsonRoot.get(alphabetName);
			HashSet<Character> symbolsSet = new HashSet<>();
			for (Object symbol : symbolsArray) symbolsSet.add(((String) symbol).charAt(0));

			return new Utilities.Pair<>(alphabetName, symbolsSet);
		} catch (Exception e) {
			throw new InputException();
		}
	}

	/**
	 * Mètode que permet exportar un alfabet qualsevol en un path concret
	 *
	 * @param alphabetName Nom de l'alfabet
	 * @param symbols HashSet amb els símbols de l'alfabet
	 * @param filePath Path on es vol exportar l'alfabet
	 * @throws OutputException Excepció si hi ha problemes en el moment d'escriure un fitxer
	 */
	public void exportAlphabet(String alphabetName, HashSet<Character> symbols, String filePath) throws OutputException {
		try {
			String path = filePath + "/" + alphabetName + ".json";

			File jsonFile = new File(path);
			JSONObject jsonRoot = new JSONObject();

			JSONArray symbolsArray = new JSONArray();
			for (Character s : symbols) symbolsArray.add(s.toString());
			jsonRoot.put(alphabetName, symbolsArray);

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
	 * @param jsonFile	Fitxer JSON
	 */
	private void writeJsonToFile(JSONObject jsonObject, File jsonFile) throws OutputException {
		try (FileWriter writer = new FileWriter(jsonFile)) {
			// Escriure el nou JSON actualitzat directament a l'arxiu
			jsonObject.writeJSONString(writer);
		} catch (IOException e) {
			throw new OutputException();
		}
	}

	/*
	//---------------- TESTS PROVISIONALS ----------------------
	public static void main(String[] args) {
		HashSet<Character> symbols = new HashSet<>();
		symbols.add('A');
		symbols.add('B');
		symbols.add('C');
		AlphabetManager.getInstance().saveAlphabet("isma", "alphabet1", symbols);
		AlphabetManager.getInstance().saveAlphabet("isma", "alphabet2", symbols);
		symbols.add('z');
		AlphabetManager.getInstance().saveAlphabet("isma", "kaka", symbols);
		symbols.add('?');
		AlphabetManager.getInstance().saveAlphabet("isma", "xdxdxd", symbols);
		HashSet<Character> symbols2 = new HashSet<>();
		symbols2.add('K');
		AlphabetManager.getInstance().modifyAlphabet("isma", "kaka", symbols2);
		AlphabetManager.getInstance().deleteAlphabet("isma", "kaka");
		AlphabetManager.getInstance().deleteAlphabet("isma", "xdxdxd");
		AlphabetManager.getInstance().saveAlphabet("isma", "holabones", symbols);

		// Loading alphabets
		HashMap<String, HashSet<Character>> loadedAlphabets = AlphabetManager.getInstance().loadAlphabets("isma");
		for (String alphabetName : loadedAlphabets.keySet()) {
			System.out.println(alphabetName + ": " + loadedAlphabets.get(alphabetName));
		}
	}
	*/
}


