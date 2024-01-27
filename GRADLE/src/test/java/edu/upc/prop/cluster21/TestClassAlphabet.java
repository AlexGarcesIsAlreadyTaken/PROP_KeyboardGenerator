package edu.upc.prop.cluster21;

import static org.junit.Assert.assertEquals;

import edu.upc.prop.cluster21.exceptions.MaxCapacityExceeded;
import edu.upc.prop.cluster21.exceptions.MinimumCapacityReached;
import edu.upc.prop.cluster21.exceptions.ObjectAlreadyExistException;
import edu.upc.prop.cluster21.exceptions.ObjectDoesNotExistException;
import org.junit.Test;
import edu.upc.prop.cluster21.domain.classes.Alphabet;
import java.util.HashSet;

/**
 * Test unitari JUnit per a la classe Alphabet.
 */

public class TestClassAlphabet {

    //----- TEST CORRECT CASES ------
    @Test
    public void createAlphabetizeText() throws MaxCapacityExceeded {
        String name = "Latin";
        String text = "zumo.";
        Alphabet a = new Alphabet(name, text);
        HashSet<Character> aux =  new HashSet<>();
        aux.add('z');aux.add('u');aux.add('m');aux.add('o');
        assertEquals(aux, a.getSymbols());
    }

    @Test
    public void addLetterCorrectly () throws MaxCapacityExceeded, ObjectAlreadyExistException {
        String name = "latin";
        String text = "zumo";
        Alphabet a = new Alphabet(name, text);
        HashSet<Character> aux =  new HashSet<>();
        aux.add('z');aux.add('u');aux.add('m');aux.add('o');aux.add('s');
        Character letter = 's';
        a.addLetter(letter);
        assertEquals(aux, a.getSymbols());
    }

    @Test
    public void deleteLetterCorrectly () throws MaxCapacityExceeded, ObjectDoesNotExistException, MinimumCapacityReached {
        String name = "latin";
        String text = "zumo";
        Alphabet a = new Alphabet(name, text);
        HashSet<Character> aux =  new HashSet<>();
        aux.add('z');aux.add('u');aux.add('m');
        Character letter = 'o';
        a.deleteLetter(letter);
        assertEquals(aux, a.getSymbols());
    }



    //------ TEST EXCEPTIONS --------
    @Test(expected = ObjectAlreadyExistException.class)
    public void addLetterObjectExists () throws MaxCapacityExceeded, ObjectAlreadyExistException {
        String name = "latin";
        String text = "zumo";
        Alphabet a = new Alphabet(name, text);
        Character letter = 'o';
        a.addLetter(letter);
    }
    @Test(expected = MaxCapacityExceeded.class)
    public void addLetterCapacityExceeded () throws MaxCapacityExceeded, ObjectAlreadyExistException {
        String name = "latin";
        String text = "1234567890qwertyuiopasdfghjklñzxcvbnm<>ª!·$%&/()=?¿'¡`+*";
        Alphabet a = new Alphabet(name, text);
        Character letter = '-';
        a.addLetter(letter);
    }

    @Test(expected = ObjectDoesNotExistException.class)
    public void deleteLetterNotExist () throws MaxCapacityExceeded, ObjectDoesNotExistException, MinimumCapacityReached {
        String name = "latin";
        String text = "zum";
        Alphabet a = new Alphabet(name, text);
        Character letter = 'o';
        a.deleteLetter(letter);
    }

    @Test(expected = MaxCapacityExceeded.class)
    public void createAlphabetExcedingCapacity() throws MaxCapacityExceeded {
        String name = "Latin";
        String text = "1234567890qwertyuiopasdfghjklñzxcvbnm<>ª!·$%&/()=?¿'¡`+*´´ç.";
        Alphabet a = new Alphabet(name, text);
    }
}
