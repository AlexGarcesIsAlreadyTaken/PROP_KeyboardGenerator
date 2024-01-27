package edu.upc.prop.cluster21;

import edu.upc.prop.cluster21.domain.classes.Keyboard;
import edu.upc.prop.cluster21.exceptions.MaxCapacityExceeded;
import edu.upc.prop.cluster21.exceptions.ObjectAlreadyExistException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import org.junit.Test;

import java.rmi.AlreadyBoundException;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Test unitari JUnit per a la classe Teclat
 */
public class TestClassKeyboard {
    Keyboard k;

    @Test(expected = ObjectDoesNotExistException.class)
    public void pairFreqsAndSymbolsCoherence() throws ObjectDoesNotExistException {
        HashSet<Character> alphabet = new HashSet<>();
        alphabet.add('a');
        HashMap<String, Integer> pairFreqs = new HashMap<>();
        pairFreqs.put("ab", 1);
        new Keyboard("", pairFreqs, alphabet, 1);
    }

    @Test
    public void createEmptyKeyboard() throws ObjectDoesNotExistException{
        k = new Keyboard("", new HashMap<>(), new HashSet<>(), 1);
        Character[][] distr = new Character[3][10];
        assertArrayEquals(distr, k.getKeyboard());
    }

    private void createKeyboard() throws ObjectDoesNotExistException{
        HashSet<Character> alphabet = new HashSet<>();
        alphabet.add('a');
        alphabet.add('b');
        alphabet.add('c');
        HashMap<String, Integer> pf = new HashMap<>();
        pf.put("ab", 5);
        pf.put("ac", 2);
        pf.put("bc", 1);
        k = new Keyboard("", pf, alphabet, 1);
    }

    @Test
    public void removeKey() throws ObjectDoesNotExistException {
        createKeyboard();
        k.removeKey('a');
        Character[][] distr = new Character[3][10];
        distr[1][5] = 'b';
        distr[2][4] = 'c';
        distr[2][0] = '↑';
        distr[2][9] = '⌫';
        assertArrayEquals(distr, k.getKeyboard());
    }

    @Test
    public void removeNonExistingKey() throws ObjectDoesNotExistException {
        createKeyboard();
        ObjectDoesNotExistException e = assertThrows(ObjectDoesNotExistException.class, () -> k.removeKey( 'd'));
        assertEquals("d", e.getName());
    }

    @Test
    public void addKey() throws ObjectDoesNotExistException, ObjectAlreadyExistException, MaxCapacityExceeded {
        createKeyboard();
        k.addKey('d');
        char c = k.getKeyboard()[2][4];
        assertEquals('c', c);
    }

    @Test (expected = ObjectAlreadyExistException.class)
    public void addExistingKey() throws ObjectAlreadyExistException, MaxCapacityExceeded, ObjectDoesNotExistException {
        createKeyboard();
        k.addKey('c');
    }

    @Test
    public void swapKeys() throws ObjectDoesNotExistException {
        createKeyboard();
        k.swapKeys('b', 'c');
        Character[][] distr = new Character[3][10];
        distr[1][4] = 'a';
        distr[1][5] = 'c';
        distr[2][4] = 'b';
        distr[2][0] = '↑';
        distr[2][9] = '⌫';
        showKeyboard(distr);
        assertArrayEquals(distr, k.getKeyboard());
    }

    @Test
    public void swapNonExistingKey() throws ObjectDoesNotExistException {
        createKeyboard();
        ObjectDoesNotExistException e = assertThrows(ObjectDoesNotExistException.class, () -> k.swapKeys('a', 'd'));
        assertEquals("d", e.getName());
    }

    @Test
    public void changeKey() throws ObjectAlreadyExistException, ObjectDoesNotExistException {
        createKeyboard();
        k.changeKey(1, 1, 'e');
        Character[][] distr = new Character[3][10];
        distr[1][4] = 'a';
        distr[1][5] = 'b';
        distr[2][4] = 'c';
        distr[1][1] = 'e';
        distr[2][0] = '↑';
        distr[2][9] = '⌫';
        assertArrayEquals(distr, k.getKeyboard());
    }

    @Test (expected = ObjectAlreadyExistException.class)
    public void changeKeyForExistingKey() throws ObjectDoesNotExistException, ObjectAlreadyExistException {
        createKeyboard();
        k.changeKey(1, 1, 'c');
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void changeKeyImpossiblePosition() throws ObjectAlreadyExistException, ObjectDoesNotExistException {
        createKeyboard();
        k.changeKey(2, 0, 'e');
    }

    private void showKeyboard(Character[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("+-----");
            }
            System.out.println("+");

            for (int j = 0; j < matrix[i].length; j++) {
                char actual;
                actual = (matrix[i][j] != null) ? matrix[i][j] : ' ';
                System.out.print("|  " + actual + "  ");
            }
            System.out.println("|");
        }

        for (int j = 0; j < matrix[0].length; j++) {
            System.out.print("+-----");
        }
        System.out.println("+");
    }

}
