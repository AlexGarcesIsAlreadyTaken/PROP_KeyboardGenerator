package edu.upc.prop.cluster21.presentation.views;


import edu.upc.prop.cluster21.domain.controllers.*;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.CtrlPresentation;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.*;

/** Es tracta de la vista per terminal de la primera entrega. Permetrà interacció amb l'usuari,
 * per a que aquest pugui provar totes les funcionalitats
 * @author Ismael El Basli
 */
public class TerminalView {
  
  private Scanner reader;

  /** Crea una TerminalView, definint un scanner per les lectures.
   *
   */
  public TerminalView () {
    reader = new Scanner(System.in);
  }

  /** Inicialitza el menu.
   *
   */
  public void run() {
    System.out.println("\n" +
            "\n" +
            " _____  _____ _   _  ___________  ___ ______ ___________  ______ _____   _____ _____ _____  _       ___ _____ _____ \n" +
            "|  __ \\|  ___| \\ | ||  ___| ___ \\/ _ \\|  _  \\  _  | ___ \\ |  _  \\  ___| |_   _|  ___/  __ \\| |     / _ \\_   _/  ___|\n" +
            "| |  \\/| |__ |  \\| || |__ | |_/ / /_\\ \\ | | | | | | |_/ / | | | | |__     | | | |__ | /  \\/| |    / /_\\ \\| | \\ `--. \n" +
            "| | __ |  __|| . ` ||  __||    /|  _  | | | | | | |    /  | | | |  __|    | | |  __|| |    | |    |  _  || |  `--. \\\n" +
            "| |_\\ \\| |___| |\\  || |___| |\\ \\| | | | |/ /\\ \\_/ / |\\ \\  | |/ /| |___    | | | |___| \\__/\\| |____| | | || | /\\__/ /\n" +
            " \\____/\\____/\\_| \\_/\\____/\\_| \\_\\_| |_/___/  \\___/\\_| \\_| |___/ \\____/    \\_/ \\____/ \\____/\\_____/\\_| |_/\\_/ \\____/ \n" +
            "                                                                                                                    \n");
    runMenu();
  }

  //---------------------------------------------------------
  // --------------------- MENÚ PRINCIPAL -------------------
  // --------------------------------------------------------
  private void runMenu() {
    System.out.println("MENÚ D'OPCIONS:");
    System.out.println("1. GESTIONAR ALFABETS");
    System.out.println("2. GESTIONAR LLISTES DE FREQÜÈNCIES");
    System.out.println("3. GESTIONAR TECLATS");
    System.out.println("4. JOCS DE PROVA PREDEFINITS");
    System.out.println("5. SORTIR");
    System.out.println();
    System.out.print("Sel·lecciona una opció: {1, 2, 3, 4, 5}: ");
    int option = readNumber();
    reader.nextLine();

    switch(option) {
      case 1:
        alphabetManagementMenu();
        break;
      case 2:
        frequencyListManagementMenu();
        break;
      case 3:
        keyboardManagementMenu();
        break;
      case 4:
        manageJocsProva();
        break;
      case 5:
        System.exit(0);
        break;
      default:
        System.out.println("El nombre introduït no és correcte.");
        System.out.println("S'ha d'introduïr un nombre en el rang [1,4].");
        System.out.println();
        runMenu();
        break;
    }
  }

  //---------------------------------------------------------
  // ------------------ GESTIÓ D'ALFABETS -------------------
  // --------------------------------------------------------
  private void alphabetManagementMenu() {
    System.out.println();
    System.out.println("MENÚ DE GESTIÓ DELS ALFABETS:");
    System.out.println("1. Visualitzar un dels meus alfabets");
    System.out.println("2. Crear un nou alfabet");
    System.out.println("3. Modificar un dels meus alfabets");
    System.out.println("4. Eliminar un dels meus alfabets");
    System.out.println("5. Tornar al menú principal");
    System.out.println();
    System.out.print("Sel·lecciona una opció: {1, 2, 3, 4, 5}: ");

    int option = readNumber();
    reader.nextLine();
    switch(option) {
      case 1:
        viewAlphabet();
        break;
      case 2:
        createAlphabet();
        break;
      case 3:
        modifyAlphabet();
        break;
      case 4:
        deleteAlphabet();
        break;
      case 5:
        runMenu();
        break;
      default:
        System.out.println("El nombre introduït no és correcte.");
        System.out.println("S'ha d'introduïr un nombre en el rang [1,5].");
        System.out.println();
        alphabetManagementMenu();
        break;
    }
    alphabetManagementMenu();
  }

  private void viewAlphabet() {
    ArrayList<String> names = CtrlPresentation.getInstance().getAlphabetsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap alfabet guardat.");
    }
    else {
      System.out.println("Els noms dels alfabets guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.print("Introdueix el nom de l'alfabet a visualitzar: ");
      String name = readLine();
      showAlphabet(name);
    }
  }

  private void createAlphabet() {
    System.out.print("Introdueix el nom que tindrà l'alfabet: ");
    String name = reader.nextLine();
    System.out.println("Introdueix els símbols de l'alfabet (de qualsevol forma, junts o amb espais): ");
    String text = reader.nextLine();
    try {
        try {
            CtrlPresentation.getInstance().createAlphabet(name, text);
        } catch (OutputException e) {
           //meter cajita error
        }
        System.out.println("L'alfabet s'ha creat amb èxit.");
    } catch (ObjectAlreadyExistException | MaxCapacityExceeded e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
  private void modifyAlphabet() {
    ArrayList<String> names = CtrlPresentation.getInstance().getAlphabetsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap alfabet guardat.");
    }
    else {
      System.out.println("Els noms dels alfabets guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.print("Introdueix el nom de l'alfabet a modificar: ");
      String name = readLine();
      System.out.println("L'alfabet " + name + " és el següent:");
      showAlphabet(name);

      System.out.print("Vols afegir un símbol o eliminar-ne un? {1. Afegir 2. Eliminar}: ");
      int option = readNumber();
      reader.nextLine();

      if (option == 1) {
        try {
          System.out.print("Introdueix el símbol a afegir: ");
          String s = readLine();
          Character letter = s.charAt(0);
          CtrlPresentation.getInstance().addLetter(name, letter);
          System.out.println("La modificació s'ha realitzat amb èxit.");
        } catch (ObjectAlreadyExistException | MaxCapacityExceeded | ObjectDoesNotExistException e) {
          System.out.println("ERROR: " + e.getMessage());
        }catch (InputException | OutputException ex){
          System.out.println("Error");
        }
      }
      else {
        try {
          System.out.print("Introdueix el símbol a eliminarr: ");
          String s = readLine();
          Character letter = s.charAt(0);
          CtrlPresentation.getInstance().deleteLetter(name, letter);
          System.out.println("La modificació s'ha realitzat amb èxit.");
        } catch (ObjectDoesNotExistException e) {
          System.out.println("ERROR: " + e.getMessage());
        } catch (MinimumCapacityReached e) {
          System.out.println("ERROR: " + e.getMessage());
        }catch (InputException | OutputException ex){
          System.out.println("Error");
        }
      }
    }
  }

  private void deleteAlphabet() {
    ArrayList<String> names = CtrlPresentation.getInstance().getAlphabetsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap alfabet guardat.");
    }
    else {
      System.out.println("Els noms dels alfabets guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.print("Introdueix el nom de l'alfabet a eliminar: ");
      String name = readLine();

      try {
        CtrlPresentation.getInstance().deleteAlphabet(name);
        System.out.println("L'alfabet s'ha eliminat amb èxit.");
      }
      catch (InputException | OutputException ex){
        System.out.println("Error");
      }
    }
  }

  private void showAlphabet(String name) {
    try {
      Set<Character> symbols = CtrlPresentation.getInstance().getAlphabet(name);
      for (Character c : symbols) System.out.print(c + " ");
      System.out.println();
    } catch(ObjectDoesNotExistException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }


  //---------------------------------------------------------
  // ------------ GESTIÓ DE LLISTES DE FREQÜÈNCIES-----------
  // --------------------------------------------------------
  private void frequencyListManagementMenu() {
    System.out.println();
    System.out.println("MENÚ DE GESTIÓ DE LES LLISTES DE FREQÜÈNCIES:");
    System.out.println("1. Visualtizar una de les meves llistes de freqüències");
    System.out.println("2. Crear una nova llista de freqüències");
    System.out.println("3. Modificar una de les meves llistes de freqüències");
    System.out.println("4. Eliminar una de les meves lliste de freqüències");
    System.out.println("5. Tornar al menú principal");
    System.out.println();
    System.out.println("Sel·lecciona una opció: {1, 2, 3, 4, 5}");

    int option = readNumber();
    reader.nextLine();

    switch(option) {
      case 1:
        viewFrequencyList();
        break;
      case 2:
        createFrequencyList();
        break;
      case 3:
        modifyFrequencyList();
        break;
      case 4:
        deleteFrequencyList();
        break;
      case 5:
        runMenu();
        break;
      default:
        System.out.println("El nombre introduït no és correcte.");
        System.out.println("S'ha d'introduïr un nombre en el rang [1,5].");
        System.out.println();
        frequencyListManagementMenu();
        break;
    }
    frequencyListManagementMenu();
  }

  private void viewFrequencyList() {
    ArrayList<String> names = CtrlPresentation.getInstance().getFrequencyListsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap llista de freqüències guardada.");
    }
    else {
      System.out.println("Els noms de les llistes de freqüències guardades són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.print("Introdueix el nom de la llista de freqüències a visualitzar: ");
      String name = readLine();
      showFrequencyList(name);
    }
  }

  private void createFrequencyList() {
    System.out.print("Introdueix el nom que tindrà la llista de freqüències: ");
    String name = reader.nextLine();
    System.out.println("Introdueix el text: ");
    String text = reader.nextLine();
    try {
      CtrlPresentation.getInstance().createFrequencyList(name, text);
      System.out.println("La llista de freqüències s'ha creat amb èxit.");
    } catch (ObjectAlreadyExistException | OutputException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  private void modifyFrequencyList() {
    ArrayList<String> names = CtrlPresentation.getInstance().getFrequencyListsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap llista de freqüències guardada.");
    }
    else {
      System.out.println("Els noms de les llistes de freqüències guardades són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.println("Introdueix el nom de la llista de freqüències a modificar: ");
      String name = readLine();

      System.out.println("La llista de freqüències " + name + " és la següent:");
      showFrequencyList(name);

      System.out.println("Introdueix la paraula a la que li vols canviar la freqüència:");
      String word = readLine();
      System.out.println("Introdueix la nova freqüència:");
      int extra = readNumber();
      reader.nextLine();

      try {
        CtrlPresentation.getInstance().modifyFrequencyList(name, word, extra);
        System.out.println("La modificació s'ha realitzat amb èxit.");
      } catch (NegativeAppearancesException | ObjectDoesNotExistException e) {
        System.out.println("ERROR: " + e.getMessage());
      } catch (OutputException | InputException e){
        System.out.println("ERROR: problemes guardant els canvis");
      }
    }
  }

  private void deleteFrequencyList() {
    ArrayList<String> names = CtrlPresentation.getInstance().getFrequencyListsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap llista de freqüències guardada.");
    }
    else {
      System.out.println("Els noms de les llistes de freqüències guardades són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.println("Introdueix el nom de la llista de freqüències a eliminar: ");
      String name = readLine();

      try {
        CtrlPresentation.getInstance().deleteFrequencyList(name);
        System.out.println("La llista de freqüències s'ha eliminat amb èxit.");
      } catch (ObjectDoesNotExistException e) {
        System.out.println("ERROR: La llista de freqüències " + e.getName() + " no existía abans de voler borrar-la.");
      }
    }
  }

  private void showFrequencyList(String name) {
    try {
      HashMap<String, Integer> freq = CtrlPresentation.getInstance().getWordFreq(name);
      for (Map.Entry<String, Integer> entry : freq.entrySet()) {
        System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " aparicions.");
      }
    } catch(ObjectDoesNotExistException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  //---------------------------------------------------------
  // ------------------ GESTIÓ DE TECLATS -------------------
  // --------------------------------------------------------
  private void keyboardManagementMenu() {
    System.out.println();
    System.out.println("MENÚ DE GESTIÓ DELS TECLATS");
    System.out.println("1. Visualitzar un dels meus teclats");
    System.out.println("2. Crear un nou teclat");
    System.out.println("3. Modificar un dels meus teclats");
    System.out.println("4. Eliminar un dels meus teclats");
    System.out.println("5. Tornar al menú principal");
    System.out.println();
    System.out.println("Sel·lecciona una opció: {1, 2, 3, 4, 5}");

    int option = readNumber();
    reader.nextLine();
    switch(option) {
      case 1:
        viewKeyboard();
        break;
      case 2:
        createKeyboard();
        break;
      case 3:
        modifyKeyboard();
        break;
      case 4:
        deleteKeyboard();
        break;
      case 5:
        runMenu();
        break;
      default:
        System.out.println("El nombre introduït no és correcte.");
        System.out.println("S'ha d'introduïr un nombre en el rang [1,5].");
        System.out.println();
        keyboardManagementMenu();
        break;
    }
      keyboardManagementMenu();
  }


  private void viewKeyboard() {
    ArrayList<String> names = CtrlPresentation.getInstance().getKeyboardsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap teclat guardat.");
    }
    else {
      System.out.println("Els noms dels teclats guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.println("Introdueix el nom del teclat a visualitzar: ");
      String name = readLine();
      try {
        Character[][] layout = CtrlPresentation.getInstance().getKeyboardDistribution(name);
        showKeyboard(layout);
      } catch (ObjectDoesNotExistException e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  private void createKeyboard() {
    ArrayList<String> names = CtrlPresentation.getInstance().getAlphabetsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap alfabet guardat. Per crear un teclat fa falta un alfabet.");
    }
    else {

      System.out.print("Indica el nom que tindrà el nou teclat: ");
      String name = readLine();
      System.out.println("Els noms dels alfabets guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.print("Indica el nom de l'alfabet que utilitzaràs per crear el teclat: ");
      String aName = readLine();
      System.out.print("Vols crear-lo a partir d'un text o a partir d'una de les teves llistes de freqüència? {1. Text 2. Llista}: ");
      int option = readNumber();
      reader.nextLine();

      if (option == 1) {
        System.out.println("Introdueix el text:");
        String text = readLine();
        System.out.println("AVÍS: L'algorisme B&B (Branch & Bound) és exacte, pel que partir d'alfabets amb 12 símbols comença a tardar.");
        System.out.println("Recomanem per alfabets amb molts símbols utilitzar SA (Simulated Annealing).");
        System.out.print("Quin algorisme vols utilitzar: Branch and Bound o Simulated Annealing? {1. B&B 2. SA}: ");
        int algo = readNumber();
        reader.nextLine();
        try {
          CtrlPresentation.getInstance().createKeyboardWithText(name, aName, text, algo);
          System.out.println("El teclat s'ha creat amb èxit");
        } catch (ObjectDoesNotExistException e) {
          System.out.println("ERROR: La lletra " + e.getName() + " és al text però no a l'alfabet");
        } catch (ObjectAlreadyExistException e) {
            System.out.println("ERROR: Ja existeix el teclat " + e.getName() + ".");
        }
        catch (InputException | OutputException ex){
          System.out.println("Error");
        }
      }
      else {
        ArrayList<String> n = CtrlPresentation.getInstance().getFrequencyListsNames();
        System.out.println("Els noms de les llistes de freqüències guardades són:");
        for (String str : n) {
          System.out.println("- " + str);
        }
        System.out.println();
        System.out.print("Introdueix el nom de la llista de freqüències a utilitzar: ");
        String lName = readLine();
        System.out.print("Quin algorisme vols utilitzar: Branch and Bound o Simulated Annealing? {1. B&B 2. SA}: ");
        int algo = readNumber();
        reader.nextLine();
        try {
          CtrlPresentation.getInstance().createKeyboardWithList(name, aName, lName, algo);
          System.out.println("El teclat s'ha creat amb èxit");
        } catch (ObjectDoesNotExistException e) {
          System.out.println("ERROR: La lletra " + e.getName() + "és a la llista de freqüencies però no a l'alfabet");
        } catch (ObjectAlreadyExistException e) {
          System.out.println("ERROR: Ja existeix el teclat " + e.getName() + ".");
        }catch (InputException | OutputException ex){
          System.out.println("Error");
        }
      }
    }
  }

  private void modifyKeyboard() {
    ArrayList<String> names = CtrlPresentation.getInstance().getKeyboardsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap teclat guardat.");
    }
    else {
      System.out.println("Els noms dels teclats guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.println("Introdueix el nom del teclat a modificar: ");
      String name = readLine();
      System.out.println();

      System.out.println("Sel·lecciona una opció:");
      System.out.println("1. Afegir un símbol al teclat");
      System.out.println("2. Eliminar un símbol del teclat");
      System.out.println("3. Intercanviar la posició de dos símbols");
      System.out.println("4. Col·locar un símbol en una tecla determinada");

      int option = readNumber();
      reader.nextLine(); //Per netejar el buffer

      String s;
      Character symbol;
      switch (option) {
        case 1:
          System.out.print("Introdueix el símbol a afegir: ");
          s = readLine();
          symbol = s.charAt(0);
          try {
            CtrlPresentation.getInstance().addKey(name, symbol);
            System.out.println("Modificació realitzada amb èxit.");
          } catch (MaxCapacityExceeded e) {
            System.out.println("ERROR: El teclat " + name + "és ple, no es poden afegir més teclats");
          } catch (ObjectAlreadyExistException e) {
              System.out.println("Error: El teclat " + name + "ja te la tecla " + e.getName() + ".");
          } catch (ObjectDoesNotExistException e) {
            System.out.println("Error: El teclat " + e.getName() + "no existeix");
          } catch (InputException | OutputException ex){
            System.out.println("Error");
          }
            break;
        case 2:
          System.out.print("Introdueix el símbol a eliminar: ");
          s = readLine();
          symbol = s.charAt(0);
          try {
            CtrlPresentation.getInstance().deleteKey(name, symbol);
            System.out.println("Modificació realitzada amb èxit.");
          } catch (ObjectDoesNotExistException e) {
            if (e.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
              System.out.println("ERROR: La lletra " + e.getName() + "no és al teclat.");
            else System.out.println("ERROR: No existeix cap teclat amb nom " + e.getName() + ".");
          } catch (InputException | OutputException ex){
            System.out.println("Error");
          }
          break;
        case 3:
          System.out.print("Introdueix el primer símbol ");
          s = readLine();
          symbol = s.charAt(0);

          System.out.print("Introdueix el segon símbol ");
          s = readLine();
          Character symbol2 = s.charAt(0);

          try {
            CtrlPresentation.getInstance().swapKeys(name, symbol, symbol2);
            System.out.println("Modificació realitzada amb èxit.");
          } catch (ObjectDoesNotExistException e) {
            if (e.getTypeObject() == ObjectExistenceException.TypeObject.CHARACTER)
              System.out.println("ERROR: La lletra " + e.getName() + "no és al teclat.");
            else System.out.println("ERROR: No existeix cap teclat amb nom " + e.getName() + ".");
          } catch (InputException | OutputException ex){
            System.out.println("Error");
          }
          break;
      }
    }
  }

  private void deleteKeyboard() {
    ArrayList<String> names = CtrlPresentation.getInstance().getKeyboardsNames();
    if (names.isEmpty()) {
      System.out.println("No hi ha cap teclat guardat.");
    }
    else {
      System.out.println("Els noms dels teclats guardats són:");
      for (String str : names) {
        System.out.println("- " + str);
      }
      System.out.println();
      System.out.println("Introdueix el nom del teclat a eliminar: ");
      String name = readLine();

     try {
       CtrlPresentation.getInstance().deleteKeyboard(name);
     } catch (ObjectDoesNotExistException e) {
       System.out.println("ERROR: No existeix el teclat " + e.getName() + ".");
     }catch (InputException | OutputException ex){
       System.out.println("Error");
     }
    }
  }

  private void showKeyboard(Character[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        System.out.print("+-----");
      }
      System.out.println("+");

      for (int j = 0; j < matrix[i].length; j++) {
        char actual;
        actual = matrix[i][j] != null ? matrix[i][j] : ' ';
        System.out.print("|  " + actual + "  ");
      }
      System.out.println("|");
    }

    for (int j = 0; j < matrix[0].length; j++) {
      System.out.print("+-----");
    }
    System.out.println("+");
}

  //Funcions per llegir de l'entrada
  private int readNumber() {
    int number;
    number = reader.nextInt();
    return number;
  }

  private String readLine() {
    return reader.nextLine().trim();
  }


  private void manageJocsProva(){
    System.out.println();
    System.out.println("MENÚ DE GESTIÓ DELS JOCS DE PROVA:");
    System.out.println();

    File dir = new File("jocsProva");
    String ls[] = dir.list();

    for (int i = 0; i < ls.length; ++i)
      System.out.println((i+1) + ". Joc de prova: " + ls[i]);
    System.out.print("Insereix el número del joc de proves que vols executar: ");

    int option = readNumber() -1;
    reader.nextLine();
    runProva(ls[option]);
    runMenu();
  }
  private void runProva(String file){
      File f = new File("jocsProva/" + file);
      int algo;
      try {
        Scanner freader = new Scanner(f);
        String alg = freader.nextLine().trim();
        if (alg.equals("B&B"))
          algo = 1;
        else if (alg.equals("SA"))
          algo = 2;
        else{
          System.out.println("Error reading algorithm");
          return ;
        }
        String alf = freader.nextLine().trim();
          try {
              CtrlPresentation.getInstance().createAlphabet(file, alf);
          } catch (OutputException e) {
              //meter cajita error
          }

          String fqlist = freader.nextLine().trim();
        CtrlPresentation.getInstance().createFrequencyList(file, fqlist);

        CtrlPresentation.getInstance().createKeyboardWithList(file, file, file, algo);

        Character[][] layout = CtrlPresentation.getInstance().getKeyboardDistribution(file);
        showKeyboard(layout);

        CtrlPresentation.getInstance().deleteAlphabet(file);
        CtrlPresentation.getInstance().deleteFrequencyList(file);
        CtrlPresentation.getInstance().deleteKeyboard(file);

      }
      catch (FileNotFoundException | ObjectAlreadyExistException | ObjectDoesNotExistException| MaxCapacityExceeded | OutputException e) {
        System.out.println("Error in test");
      }catch (InputException ex){
        System.out.println("Error");
      }
    }
}
