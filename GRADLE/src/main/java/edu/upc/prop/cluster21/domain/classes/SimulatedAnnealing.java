package edu.upc.prop.cluster21.domain.classes;

import java.util.*;

/**
 * La classe Simulated Annealing s'encarrega de trobat una solució subòptima al problema de QAP que suposa l'assignació de characters al teclat.
 * A través d'una metaheurística ens retornarà una solució que la majoria de casos es òptima o està molt aprop del òptim. Tot i això, la qualitat d'aquesta
 * no està garantida.
 */
public class SimulatedAnnealing implements Algorithm {

	/**
	 * Nombre de files
	 */
	private static final int rows = 3;
	/**
	 * Nombre de columnes
	 */
	private static final int cols = 10;
	/**
	 * Nombre total de cel·les.
	 */
	private static final int totalCells = rows*cols;
	/**
	 * Matriu de distàncies entre caselles del teclat
	 */
	private static double[][] distances;

	/**
	 * Millor solució arribada en un punt en concret
	 */
	private static Character[] bestSol;
	/**
	 * Millor puntuació a la que s'ha arribat en un punt en concret
	 */
	private static double bestEval;
	/**
	 * Nombre de caràcters
	 */
	private static int n;

	/**
	 * Freqüències de parells de paraules a partir de les quals es crea el teclat.
	 */
	HashMap<String, Integer> freq;
	/**
	 * Caràcters que conté el teclat
	 */
	HashSet<Character> symb;

	/**
	 * Instància seguint el patró singleton.
	 */
	private static SimulatedAnnealing current;
	public static SimulatedAnnealing getInstance() {
		if (current == null) {
			current = new SimulatedAnnealing();
		}
		return current;
	}

	/**
	 * La funció retorna un teclat optimitzat a partir de les dades d'entrades i l'algorisme Simulated Annealing.
	 * @param symbols Conjunt de caràcters del teclat.
	 * @param frequencies Parells de freqüències dels caràcters del teclat.
	 * @return Solució a la que s'ha arribat
	 */
	@Override
	public Character[][] generate_optimized_keyboard(HashSet<Character> symbols, HashMap<String, Integer> frequencies) {
		distances = new double[totalCells][totalCells];
		computeDistances();

		n = symbols.size();

		freq = frequencies;
		symb = symbols;

		bestSol = computeSimulatedAnnealing(10, 10000, 0.9995);
		bestEval = computeCost(bestSol);
		Character[][] solution = new Character[3][10];

		for (int k = 0; k < n; ++k) {
			int i = Utilities.keysPos[k] / 10;
			int j = Utilities.keysPos[k] % 10;

			solution[i][j] = bestSol[k];
		}
		return solution;
	}

	/**
	 * Executa el simmulated annealing.
	 * @param startingTemperature Temperatura inicial.
	 * @param numberOfIterations Nombre d'iteracions.
	 * @param coolingRate Rapidesa amb la que decau la temperatura.
	 * @return
	 */
	private Character[] computeSimulatedAnnealing(double startingTemperature, int numberOfIterations, double coolingRate) {
		double t = startingTemperature;
		Character []sol = greedySolution(symb, freq);
		double bestCost = computeCost(sol);
		for (int i = 0; i < numberOfIterations; i++) {
			if (t > 0.1) {
				int[] swapped = swapChars(sol);
				double currentCost = computeCost(sol);
				if (currentCost < bestCost) {
					bestCost = currentCost;
				}
				else if (Math.exp((bestCost - currentCost) / t) < Math.random()) {
					revertSwap(sol, swapped);
				}
			}
			else {
				continue;
			}
			t *= coolingRate;
		}
		return sol;
	}

	/**
	 * Intercanvia dues tecles a la solució provisional i retorna els indexos de les que ha intercanviat
	 * @param sol Solució actual
	 * @return Índex de les tecles que s'han intercanviat.
	 */
	private int[] swapChars(Character[] sol){
		int []swp = new int[2];
		Random rand = new Random();
		swp[0] = rand.nextInt(n);
		swp[1] = rand.nextInt(n);
		Character aux = sol[swp[0]];
		sol[swp[0]] = sol[swp[1]];
		sol[swp[1]] = aux;
		return swp;
	}

	/**
	 * Intercanvia les tecles swp a la solució sol. Es fa servir per desfer un canvi anterior.
	 * @param sol Solució actual
	 * @param swp Indexos que s'han d'intercanviar.
	 */
	private void revertSwap(Character[] sol, int[] swp){
		Character aux = sol[swp[0]];
		sol[swp[0]] = sol[swp[1]];
		sol[swp[1]] = aux;
	}

	/**
	 * Funció que calcula el cost d'una solució.
	 * @param sol Solució.
	 * @return Cost de sol
	 */
	private double computeCost(Character[] sol) {
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
	 * Funció que calcula totes les distàncies entre les tecles.
	 */
	private static void computeDistances() {
		for (int i = 0; i < totalCells; ++i) {
			for (int j = 0; j < totalCells; ++j) {
				int row1 = i / cols;
				int col1 = i % cols;
				int row2 = j / cols;
				int col2 = j % cols;

				double dist = Math.sqrt(Math.pow(row1 - row2, 2) + Math.pow(col1 - col2, 2));

				//La matriu es simetrica:
				distances[i][j] = distances[j][i] = dist;
			}
		}
	}

	/**
	 * Calcul de una solució inicial random. Situa els caràcters amb més freqüència junts.
	 * @param symbols Caràcters del teclat.
	 * @param freq Freqüències dels parells de caràcters del teclat.
	 * @return
	 */
	private Character[] greedySolution(HashSet<Character> symbols, HashMap<String, Integer> freq) {
		PriorityQueue<Utilities.Pair<Integer, String>> pq = new PriorityQueue<>(new SimulatedAnnealing.SortByFirst());
		Set<String> pairSet = freq.keySet();
		for (String s : pairSet) pq.add(new Utilities.Pair<>(freq.get(s), s));
		HashSet<Character> used = new HashSet<>();

		int i = 0;
		Character[] sol = new Character[n];
		while (!pq.isEmpty()) {
			String s = pq.poll().second;
			Character s1 = s.charAt(0);
			Character s2 = s.charAt(1);
			if (!used.contains(s1)){
				used.add(s1);
				sol[i] = s1;
				++i;
			}
			if (!used.contains(s2)){
				used.add(s2);
				sol[i] = s2;
				++i;
			}
		}
		if (i < n){
			for (Character c : symbols){
				if (!used.contains(c) && i < n){
					used.add(c);
					sol[i] = c;
					++i;
				}
			}
		}
		return sol;
	}

	class SortByFirst implements Comparator<Utilities.Pair<Integer, String>> {

		//@Override
		public int compare(Utilities.Pair<Integer, String> o1, Utilities.Pair<Integer, String> o2) {
			return o2.first.compareTo(o1.first);
		}
	}
}
