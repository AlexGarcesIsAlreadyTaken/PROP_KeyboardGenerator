package edu.upc.prop.cluster21.domain.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Comparator;

/**
 * Classe que implementa el càlcul de la cota de Gilmore-Lawler.
 */
public class GilmoreLawlerBound {
    /**
     * Instància actual de la classe seguint el patró Singleton
     */
    private static GilmoreLawlerBound current;

    /**
     * Mètode que permet obtenir la instancia actual de la classe seguint el patró Singleton.
     * @return
     */
    public static GilmoreLawlerBound getInstance() {
        if (current == null) current = new GilmoreLawlerBound();
        return current;
    }

    /**
     * Calcula la cota de Gilmore-Lawler
     * @param sol Solució actual
     * @param freq Frequències dels parells de lletres sobre el que es vol calcular la cota
     * @param distances Distàncies entre les posicions del teclat
     * @param used Tecles que s'estan usat fins ara en la solució
     * @param unused Tecles que fan falta per usar
     * @return El valor numéric que pren la cota de Gilmore-Lawler donada l'entrada.
     */
    public double ComputeBound(Character[] sol, HashMap<String, Integer> freq, double[][] distances, HashSet<Character> used, HashSet<Character> unused) {

        //Primer término
        double CostUsed = ComputeCostUsed(sol, freq, distances, used.size()); //Ya va bien

        ArrayList<Character> unusedArray = new ArrayList<>(unused);

        //Segundo término
        double[][] C1 = ComputeCostUsedUnused(sol, freq, distances, unusedArray, used.size()); //Ya va bien

        //Tercer término
        double[][] C2 = ComputeCostUnused(freq, distances, unusedArray, used.size());

        //Matriz que combina los términos 2 y 3
        double[][] C1C2 = new double[unused.size()][unused.size()];
        for (int i = 0; i < unused.size(); ++i){
            for (int j = 0; j < unused.size(); ++j)
                C1C2[i][j] = C1[i][j] + C2[i][j];
        }
        HungarianAlgorithm h = new HungarianAlgorithm(C1C2);
        return CostUsed + h.computeOptimalAssignmentCost();
    }

    /**
     * Calcula el primer terme de la cota, que es pot calcular de forma exacte i fa referència al cost entre els símbols
     * ja col·locats
     * @param sol Solució actual
     * @param freq Frequències dels parells de lletres
     * @param distances Distàncies entre posicions de tecles
     * @param n Número de lletres ja col·locades.
     * @return Valor numéric del cost de les lletres ja assignades (es exacte)
     */
    private double ComputeCostUsed(Character[] sol, HashMap<String, Integer> freq, double[][] distances, int n) {
        double cost = 0;

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                int f = freq.getOrDefault(sol[i] + "" + sol[j], 0) + freq.getOrDefault(sol[j] + "" + sol[i], 0);
                cost += distances[Utilities.keysPos[i]][Utilities.keysPos[j]]*f;
            }
        }
        return cost / 2;
    }

    /**
     * Calcula el segon terme de la cota, que aproxima el cost entre les instal·lacions ja col·locades i les que queden
     * per col·locar. Per estimar el valor es construeix una matriu on l'element (i, j) intenta acotar el cost adicional
     * que suposaria colocar el i-èssim símbol no col·locat a la j-èssima ubicació.
     * @param sol Solució actual
     * @param freq Freqüències entre tot parell de símbols
     * @param distances Matriu de distàncies
     * @param unused Símbols no utilitzats
     * @param n Nombre de símbols utilitzats
     * @return La matriu que acota el cost
     */
    private double[][] ComputeCostUsedUnused(Character[] sol, HashMap<String, Integer> freq, double[][] distances, ArrayList<Character> unused, int n) {
        double[][] C1 = new double[unused.size()][unused.size()];

        //Hay unudsd.size() ubicaciones a emplazar, la ubicacion k = 0 es realmente la que esta al lado del ultimo simbolo
        //de la solucion parcial.

        for (int i = 0; i < unused.size(); ++i) {
            for (int k = 0; k < unused.size(); ++k) {
                C1[i][k] = 0;
                for (int j = 0; j < n; ++j) {
                    int f = freq.getOrDefault(sol[j] + "" + unused.get(i), 0) + freq.getOrDefault(unused.get(i) + "" + sol[j], 0);
                    C1[i][k] += distances[Utilities.keysPos[j]][Utilities.keysPos[n+k]]*f;
                }
            }
        }
        return C1;
    }

    /**
     * Calcula el tercer terme de la cota, que aproxima el cost entre les instal·lacions que queden
     * per col·locar. Per estimar el valor es construeix una matriu on l'element (i, j) intenta acotar el cost adicional
     * que suposaria colocar el i-èssim símbol no col·locat a la j-èssima ubicació.
     * @param freq Freqüències entre tot parell de símbols
     * @param distances Matriu de distàncies
     * @param unused Símbols no utilitzats
     * @param n Nombre de símbols utilitzats
     * @return
     */
    private double[][] ComputeCostUnused(HashMap<String, Integer> freq, double[][] distances, ArrayList<Character> unused, int n){
        double[][] C2 = new double[unused.size()][unused.size()];
        for (int i = 0; i < unused.size(); ++i)
        {
            for (int k = 0; k < unused.size(); ++k)
            {
                ArrayList<Double> T = new ArrayList<>(); //Tráficos
                ArrayList<Double> D = new ArrayList<>();  //Distancias

                for (Character s : unused) {
                    int f = freq.getOrDefault(unused.get(i) + "" + s, 0) + freq.getOrDefault(s + "" + unused.get(i), 0);
                    T.add((double)f);
                }
                for (int j = 0; j < unused.size(); ++j) D.add(distances[Utilities.keysPos[k+n]][Utilities.keysPos[j+n]]);

                //Ordenar T de forma creciente
                T.sort(Comparator.naturalOrder());

                //Ordenar D de forma decreciente
                D.sort(Comparator.reverseOrder());

                C2[i][k] = 0;
                for (int j = 0; j < unused.size(); ++j){
                    C2[i][k] += T.get(j) * D.get(j);
                }
            }
        }
        return C2;
    }
}
