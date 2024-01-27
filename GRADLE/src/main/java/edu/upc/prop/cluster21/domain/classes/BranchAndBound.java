package edu.upc.prop.cluster21.domain.classes;

import java.util.*;

/**
 * Classe que conté tot el codi de l'algorisme exacte Branch And Bound per trobar una solució al problema QAP.
 * Es tracta d'una cerca exhaustiva on es poda en base a una cota determinada.
 */

public class BranchAndBound implements Algorithm {
	/**
	 * Nombre de files
	 */
	private static final int rows = 3;
	/**
	 * Nombre de columnes
	 */
	private static final int cols = 10;
	/**
	 * Nombre de cel·les totals
	 */
	private static final int totalCells = rows*cols;
	/**
	 * Matriu de distàncies
	 */
	private static double[][] distances;
	/**
	 * Array de caràcters que representa la millor solució actual
	 */
	private static Character[] bestSol;
	/**
	 * Millor avaluació actual
	 */
	private static double bestEval;
	/**
	 * Enter que representa el nombre de símbols a emplaçar
	 */
	private static int n;
	/**
	 * HashMap que de freqüències. Permet veure el nombre d'aparicions de cada parell de símbols.
	 */
	private static HashMap<String, Integer> freq;
	/**
	 * Instancia actual seguint el patró de disseny Singleton
	 */
	private static BranchAndBound current;

	/**
	 * Mètode que permet obtenir la instància actual seguint el patró Singleton
	 * @return Instància actual
	 */
	public static BranchAndBound getInstance() {
		if (current == null) {
			current = new BranchAndBound();
		}
		return current;
	}


	/**
	 * Mètode que, a partir d'un conjunt de símbols i les freqüències entre cada parell, retorna una matriu amb
	 * l'emplaçament òptim de forma que es minimitzi el cost associat (sumatori de distàncies per freqüències)
	 * @param symbols Símbols de l'alfabet
	 * @param frequencies Freqüències entre tot parell de símbols
	 * @return Matriu de caràcters que representa l'emplaçament òptim
	 */
	@Override
	public Character[][] generate_optimized_keyboard(HashSet<Character> symbols, HashMap<String, Integer> frequencies) {
		//Lo ideal seria que les distancies estiguin a utilities, ja que son sempre les mateixes entre tecles.
		distances = new double[totalCells][totalCells];
		computeDistances();

		n = symbols.size();
		freq = frequencies;
		bestSol = greedySolution(symbols, frequencies);
		bestEval = computeCost(bestSol);

		Character[] ini = new Character[n];
		HashSet<Character> notInSolution = new HashSet<>(symbols);
		HashSet<Character> inSolution = new HashSet<>();

		LazyBranchAndBound(ini, notInSolution, inSolution);

		Character[][] solution = new Character[3][10];

		for (int k = 0; k < n; ++k) {
			int i = Utilities.keysPos[k] / 10;
			int j = Utilities.keysPos[k] % 10;

			solution[i][j] = bestSol[k];
		}
		return solution;
	}

	/**
	 * Mètode que permet determinar el cost associat a una solució, donada la matriu final (Necessari per la funcionalitat
	 * de saber que tan millora o empitjora un teclat al aplicar-hi modificacions)
	 * @param sol Matriu que representa la solució
	 * @param frequencies Llista de freqüències entre tot parell de símbols
	 * @return Double que representa el cost associat a la solució
	 */
	public double computeCost(Character[][] sol, HashMap<String, Integer> frequencies) {
		distances = new double[totalCells][totalCells];
		computeDistances();
		double cost = 0;
		for (int i = 0; i < 30; ++i) {
			for (int j = i+1; j < 30; ++j) {
				if (sol[i / 10][i % 10] != null && sol[j / 10][j % 10] != null) {
					int freq1 = frequencies.getOrDefault(sol[i / 10][i % 10] + "" + sol[j / 10][j % 10], 0);
					//if (aux != null) freq1 = (Integer) aux;
					int freq2 = frequencies.getOrDefault(sol[j/10][j % 10] + "" + sol[i/10][i%10], 0);
					cost += distances[i][j]*(freq1 + freq2);
				}
			}
		}
		return cost / 2;
	}


	/**
	 * Mètode recursiu que implementa la versió Lazy del Branch and Bound, que deixa a bestSol la millor solució trobadda
	 * @param solution Solució actual a la recursió.
	 * @param notInSolution Símbols que no es troben a la solució actual
	 * @param inSolution Símbols que es troben a la solució actual
	 */
	private static void LazyBranchAndBound(Character[] solution, HashSet<Character> notInSolution, HashSet<Character> inSolution) {
		//Si llego a una solución
		for (Character s : notInSolution) {
			Character[] newSol = Arrays.copyOf(solution, n);
			newSol[inSolution.size()] = s;

			HashSet<Character> nsol= new HashSet<>(notInSolution);
			HashSet<Character> sol = new HashSet<>(inSolution);
			sol.add(s);
			nsol.remove(s);

			if (nsol.isEmpty()) {
				double eval = computeCost(newSol);
				if (eval < bestEval) {
					bestSol = Arrays.copyOf(newSol, n);
					bestEval = eval;
				}
			}
			else {
				double eval = GilmoreLawlerBound.getInstance().ComputeBound(newSol, freq, distances, sol, nsol);
				if (eval < bestEval) LazyBranchAndBound(newSol, nsol, sol);
			}
		}
	}


	/**
	 * Mètode que permet computar el cost d'una solució parcial
	 * @param sol Solució parcial
	 * @return Cost associat a la solució
	 */
	private static double computeCost(Character[] sol) {
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
	 * Mètode que permet inicialitzar la matriu de distàncies del teclat, utilitzant la distància euclídea.
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
	 * Mètode que implementa una inicialització d'una solució greedy per podar molt més en un inici del Branch and Bound.
	 * @param symbols Símbols de l'alfabet
	 * @param freq Freqüències entre tot parell de símbols
	 * @return Solució greedy.
	 */
	private Character[] greedySolution(HashSet<Character> symbols, HashMap<String, Integer> freq) {
		PriorityQueue<Utilities.Pair<Integer, String>> pq = new PriorityQueue<>(new Utilities.SortByFirst());
		Set<String> pairSet = freq.keySet();
		for (String s : pairSet) pq.add(new Utilities.Pair<>((Integer)freq.get(s), s));
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
}






