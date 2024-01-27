package edu.upc.prop.cluster21;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import edu.upc.prop.cluster21.domain.classes.FrequencyList;
import edu.upc.prop.cluster21.exceptions.NegativeAppearancesException;
import java.util.HashMap;

/**
 * Test unitari JUnit per a la classe FrequencyList.
 */

public class TestClassFrequencyList {

    @Test
    public void createFrequencyList() {
        String text = "Hola com estas hola vermell ho";
        FrequencyList fq = new FrequencyList("prova", text);
        HashMap<String, Integer> aux =  new HashMap<>();
        aux.put("ho", 3);
        aux.put("ol", 2);
        aux.put("la", 2);
        aux.put("co", 1);
        aux.put("om", 1);
        aux.put("es", 1);
        aux.put("st", 1);
        aux.put("ta", 1);
        aux.put("as", 1);
        aux.put("ve", 1);
        aux.put("er", 1);
        aux.put("rm", 1);
        aux.put("me", 1);
        aux.put("el", 1);
        aux.put("ll", 1);
        assertEquals(aux, fq.getPairsFreq());
    }


    @Test
    public void getNameTest() {
        String text = "prova name";
        FrequencyList fq = new FrequencyList("my_name_is_taken", text);
        assertEquals("my_name_is_taken", fq.getName());
    }
    @Test
    public void getFreqList() {
        String text = "prova get a";
        FrequencyList fq = new FrequencyList("prova", text);
        HashMap<String, Integer> aux =  new HashMap<>();
        aux.put("pr", 1);
        aux.put("ro", 1);
        aux.put("ov", 1);
        aux.put("va", 1);
        aux.put("ge", 1);
        aux.put("et", 1);
        assertEquals(aux, fq.getPairsFreq());
    }

    @Test
    public void modifyOrInsertWordAppearances() throws NegativeAppearancesException{
        String text = "Hola";
        FrequencyList fq = new FrequencyList("prova", text);
        fq.modifyOrInsertWordAppearances("ho", 1);
        HashMap<String, Integer> aux =  new HashMap<>();
        aux.put("ho", 2);
        aux.put("ol", 1);
        aux.put("la", 1);
        assertEquals(aux, fq.getPairsFreq());
    }
    @Test(expected = NegativeAppearancesException.class)
    public void modifyOrInsertWordAppearances2() throws NegativeAppearancesException{
        String text = "Hola";
        FrequencyList fq = new FrequencyList("prova", text);
        fq.modifyOrInsertWordAppearances("ho", 1);
        HashMap<String, Integer> aux =  new HashMap<>();
        aux.put("ho", 2);
        aux.put("ol", 1);
        aux.put("la", 1);
        assertEquals(aux, fq.getPairsFreq());
        fq.modifyOrInsertWordAppearances("ho", -3);
    }
}
