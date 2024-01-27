package edu.upc.prop.cluster21.domain.controllers;

import edu.upc.prop.cluster21.domain.classes.Alphabet;
import edu.upc.prop.cluster21.domain.classes.FrequencyList;
import edu.upc.prop.cluster21.domain.classes.User;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.persistence.TextManager;
import edu.upc.prop.cluster21.persistence.UserManager;
import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * CtrlDomain és la classe controlador general de la capa de domini, que ajunta els controladors específics.
 */

public class CtrlDomain {

	/**
	 * Instància del controlador en ús
	 */
	private static CtrlDomain current;

	/**
	 * Funció que retorna la instància del controlador amb la que treballa l'usuari
	 * @return Retorna el CtrlDomain amb què es treballarà.
	 */
	public static CtrlDomain getInstance() {
		if (current == null) current = new CtrlDomain();
		return current;
	}

	/**
	 * Funcio que ens permet registrar nous usuaris al sistema
	 * @param user
	 * @param password
	 * @throws ObjectAlreadyExistException
	 */
	public void createUser(String user, String password) throws ObjectAlreadyExistException, InputException, OutputException{
		UserManager.getInstance().CreateUser(user, password);
		User.getInstance().setUser(user);
		User.getInstance().setPassword(password);
	}

	public void logIn(String user, String password) throws IncorrectPasswordException, ObjectDoesNotExistException, InputException{
		String[] user_bd  = UserManager.getInstance().getUser(user);
		if (user_bd[1].equals(password)){
			User.getInstance().setUser(user);
			User.getInstance().setPassword(password);

			CtrlAlphabet.getInstance().loadAlphabets(user);
			CtrlKeyboard.getInstance().loadKeyboards(user);
			CtrlFreqList.getInstance().loadFreqLists(user);
		}
		else
			throw new IncorrectPasswordException();
	}

	public void logOut(){
		CtrlAlphabet.getInstance().rmAlphabets();
		CtrlKeyboard.getInstance().rmKeyboards();
		CtrlFreqList.getInstance().rmFreqLists();
	}


	/**
	 * Funció que crea un teclat a partir d'una llista de frequüències creada a partir d'un text donat i un alfabet dels
	 * existents.
	 * @param name Nom que tindrà el teclat creat
	 * @param aName Nom de l'alfabet usat per crear el teclat
	 * @param text Text a partir del qual es crea la llista de freqüències
	 * @param algorithm Algorisme utilitzat per generar el teclat
	 * @throws ObjectDoesNotExistException Es llença quan es selecciona un objecte que no existeix prèviament a la
	 * creació del teclat, com podria ser un alfabet.
	 * @throws ObjectAlreadyExistException Es llença quan el teclat o llista que es vol crear ja existeix.
	 */
	public void createKeyboardWithText(String name, String aName, String text, int algorithm) throws ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
		FrequencyList list = new FrequencyList("proba", text);
		HashMap<String, Integer> pairsFreq = list.getPairsFreq();
		Set<Character> symbols = CtrlAlphabet.getInstance().getAlphabet(aName);

		CtrlKeyboard.getInstance().createKeyboard(name, pairsFreq, new HashSet<>(symbols), algorithm);
	}

	/**
	 * Funció idem a l'anterior, però que crea el teclat directament amb una llista de freqüències que ja existeix.
	 * @param name Nom que tindrà el teclat creat
	 * @param aName Nom de l'alfabet usat per crear el teclat
	 * @param lName Nom de la llista de freq usada per crear el teclat
	 * @param algorithm Algorisme utilitzat per generar el teclat
	 * @throws ObjectDoesNotExistException Es llença quan es selecciona un objecte que no existeix prèviament a la
	 * creació del teclat, com podria ser un alfabet.
	 * @throws ObjectAlreadyExistException Es llença quan el teclat que es vol crear ja existeix.
	 */
	public void createKeyboardWithList(String name, String aName, String lName, int algorithm) throws ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
		Set<Character> symbols = CtrlAlphabet.getInstance().getAlphabet(aName);
		HashMap<String, Integer> pairsFreq = CtrlFreqList.getInstance().getPairsFreq(lName);

		CtrlKeyboard.getInstance().createKeyboard(name, pairsFreq, new HashSet<>(symbols), algorithm);
	}

	/**
	 * Funcio per llegir text d'un fitxer
	 * @param path ruta fins al fitxer
	 * @return text contingut al fitxer
	 * @throws InputException en cas d'error de lectura
	 */
	public String loadText(String path) throws InputException{
		return TextManager.getInstance().readText(path);
	}
}


