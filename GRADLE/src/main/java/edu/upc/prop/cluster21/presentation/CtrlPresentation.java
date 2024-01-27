package edu.upc.prop.cluster21.presentation;

import java.util.*;

import edu.upc.prop.cluster21.domain.controllers.CtrlAlphabet;
import edu.upc.prop.cluster21.domain.controllers.CtrlDomain;
import edu.upc.prop.cluster21.domain.controllers.CtrlFreqList;
import edu.upc.prop.cluster21.domain.controllers.CtrlKeyboard;
import edu.upc.prop.cluster21.exceptions.*;
import edu.upc.prop.cluster21.presentation.views.*;

public class CtrlPresentation {
        private static CtrlPresentation current;
        private TerminalView t;
        private LoginView loginView;
        private ManagerView managerView;
        private AlphabetView alphabetView;
        private FreqListView freqListView;
        private KeyboardView keyboardView;
	      //private CtrlDomain c;

	
	      public static CtrlPresentation getInstance() {
                if (current == null) current = new CtrlPresentation();
                return current;
        }

    public void startTerminalView() {
                t = new TerminalView(); 
                t.run();
        }

        // ---------------- ALFABETS --------------
        public ArrayList<String>  getAlphabetsNames() {
            return CtrlAlphabet.getInstance().getAlphabetsNames();
        }

        public Set<Character> getAlphabet(String name) throws ObjectDoesNotExistException {
            return CtrlAlphabet.getInstance().getAlphabet(name);
        }

        public void createAlphabet(String name, String text) throws ObjectAlreadyExistException, MaxCapacityExceeded, OutputException {
            CtrlAlphabet.getInstance().createAlphabet(name, text);
        }

        public void addLetter(String name, Character letter) throws ObjectAlreadyExistException, MaxCapacityExceeded, ObjectDoesNotExistException, InputException, OutputException {
              CtrlAlphabet.getInstance().AddLetter(name, letter);
        }

        public void deleteLetter(String name, Character letter) throws ObjectDoesNotExistException, MinimumCapacityReached, InputException, OutputException {
            CtrlAlphabet.getInstance().DeleteLetter(name, letter);
        }

        public void deleteAlphabet(String name) throws InputException, OutputException {
              CtrlAlphabet.getInstance().deleteAlphabet(name);
        }


        // ---------------- FREQÜÈNCIES --------------
        public ArrayList<String>  getFrequencyListsNames() {
              return CtrlFreqList.getInstance().getFrequencyListsNames();
        }

        public HashMap<String, Integer> getWordFreq(String name) throws ObjectDoesNotExistException {
              return CtrlFreqList.getInstance().getWordFreq(name);
        }

        public void createFrequencyList(String name, String text) throws ObjectAlreadyExistException, OutputException {
              CtrlFreqList.getInstance().createFrequencyList(name, text);
        }

        public void deleteFrequencyList(String name) throws ObjectDoesNotExistException {
              CtrlFreqList.getInstance().deleteFrequencyList(name);
        }

        public void addWordToList(String name, String word, Integer freq) throws ObjectAlreadyExistException, ObjectDoesNotExistException, InputException, OutputException {
              CtrlFreqList.getInstance().addWordToFreqList(name, word, freq);
        }

        public void deleteWordToList(String name, String word) throws ObjectDoesNotExistException, InputException, OutputException {
              CtrlFreqList.getInstance().deleteWordToFreqList(name, word);
        }

        public void modifyFrequencyList(String name, String word, int times) throws NegativeAppearancesException, ObjectDoesNotExistException, InputException, OutputException {
              CtrlFreqList.getInstance().modifyAppearances(name, word, times);
        }


        // ---------------- TECLATS --------------
        public ArrayList<String> getKeyboardsNames() {
              return CtrlKeyboard.getInstance().getKeyboardsNames();
        }

        public Character[][] getKeyboardDistribution(String name) throws ObjectDoesNotExistException {
              return CtrlKeyboard.getInstance().getKeyboardDistribution(name);
        }

        public void createKeyboardWithText(String name, String aName, String text, int algorithm) throws ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
            CtrlDomain.getInstance().createKeyboardWithText(name, aName, text, algorithm);
        }

        public void createKeyboardWithList(String name, String aName, String lName, int algorithm) throws ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
            CtrlDomain.getInstance().createKeyboardWithList(name, aName, lName, algorithm);
        }

        public void deleteKeyboard(String name) throws ObjectDoesNotExistException, InputException, OutputException {
            CtrlKeyboard.getInstance().removeKeyboard(name);
        }

        public void addKey(String kName, Character key) throws ObjectDoesNotExistException, ObjectAlreadyExistException, MaxCapacityExceeded, InputException, OutputException {
            CtrlKeyboard.getInstance().addKey(kName, key);
        }

        public void deleteKey(String kName, Character key) throws ObjectDoesNotExistException, InputException, OutputException {
            CtrlKeyboard.getInstance().deleteKey(kName, key);
        }

        public void swapKeys(String kName, Character key1, Character key2) throws ObjectDoesNotExistException, InputException, OutputException {
              CtrlKeyboard.getInstance().swapKeys(kName, key1, key2);
        }

        public void changeKey(String kName, int i, int j, Character key) throws ArrayIndexOutOfBoundsException, ObjectDoesNotExistException, ObjectAlreadyExistException, InputException, OutputException {
            CtrlKeyboard.getInstance().changeKey(kName, i, j, key);
        }

        // ----------------SWING----------------
        public void paintLoginView() {
            if (loginView == null) loginView = new LoginView();
            else loginView.restart();
            loginView.setVisible(true);
        }

        public void paintManagerView(String user) {
            if (managerView == null) managerView = new ManagerView(user);
            else managerView.restart(user);
            managerView.setVisible(true);
        }

        public void login(String user, String password) throws IncorrectPasswordException, ObjectDoesNotExistException, InputException {
            CtrlDomain.getInstance().logIn(user, password);
        }

        public void createAccount(String user, String password) throws ObjectAlreadyExistException, InputException, OutputException{
            CtrlDomain.getInstance().createUser(user, password);
        }

        public void visualizeAlphabet(String name) throws ObjectDoesNotExistException {
            managerView.setEnabled(false);
            alphabetView = new AlphabetView(name, CtrlAlphabet.getInstance().getAlphabet(name));
            alphabetView.makeVisible();
        }

        public void closeAlphabetVisualizer() {
              if (alphabetView != null) alphabetView.setVisible(false);
              managerView.setEnabled(true);
        }

        public void logout() {
            CtrlDomain.getInstance().logOut();
        }

    public void visualizeKeyboard(String name) throws ObjectDoesNotExistException {
              managerView.setEnabled(false);
              keyboardView = new KeyboardView(name, CtrlKeyboard.getInstance().getKeyboardDistribution(name));
              keyboardView.setVisible(true);
    }

    public void closeKeyboardVisualizer() {
              if (keyboardView != null) keyboardView.setVisible(false);
              managerView.setEnabled(true);
    }

    public void visualizeFreqList(String name) throws ObjectDoesNotExistException {
              managerView.setEnabled(false);
              freqListView = new FreqListView(name, CtrlFreqList.getInstance().getWordFreq(name));
              freqListView.makeVisible();
    }

    public void closeFreqListVisualizer() {
              if (freqListView != null) freqListView.setVisible(false);
              managerView.setEnabled(true);
    }

    public String loadText(String absolutePath) throws InputException {
              return CtrlDomain.getInstance().loadText(absolutePath);
    }

    public void importAlphabet(String filePath) throws InputException, OutputException {
              CtrlAlphabet.getInstance().importAlphabet(filePath);
    }

    public void importFrequencyList(String filePath) throws InputException, OutputException{
              CtrlFreqList.getInstance().importFreqList(filePath);
    }

    public void importKeyboard(String filePath) throws InputException, OutputException {
              CtrlKeyboard.getInstance().importKeyboard(filePath);
    }

    public void exportAlphabet(String name, String filePath) throws OutputException {
              CtrlAlphabet.getInstance().exportAlphabet(name, filePath);
    }

    public void exportFrequencyList(String name, String filePath) throws OutputException, ObjectDoesNotExistException {
              CtrlFreqList.getInstance().exportFreqList(name, filePath);
    }

    public void exportKeyboard(String name, String filePath) throws OutputException {
              CtrlKeyboard.getInstance().exportKeyboard(name, filePath);
    }


    public  Double getKeyboardImprovement(String name) {
            return CtrlKeyboard.getInstance().getKeyboardImprovement(name);
    }
}
