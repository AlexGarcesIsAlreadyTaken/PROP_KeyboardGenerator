package edu.upc.prop.cluster21.domain.classes;

import java.util.Comparator;

/**
 * Classe que conté constants i elements utilitzats a tota la resta del codi
 */
public final class Utilities {
	/*
	 * La matriu del teclat te id's de la seguent forma:
	 *	0 	1 	2 	3 	4 	5 	6 	7 	8 	9
	 *	10	11	12	13	14	15	16	17	18	19
	 *	20	21	22	23	24	25	26	27	28	29
	 *
	 * Les caselles 20 i 29 no s'usen, son shift i eliminar.
	 * A la matriu distances, a la posicio (i, j) i ha la distancia entre les caselles amb id's i i j.
	 * S'utilitzarà a l'algorisme.
	 *
	 * Al array keyPos, a la posicio 0 i ha l'id de la tecla que usariem si nomes volem una tecla al teclat,
	 * a la 1 l'id que usariem per una segona tecla si volem 2, etc. Es per saber quin subconjunt de tecles
	 * utilitzar si no tindrem les 28.
	 */

	/**
	 * Nombre de tecles d'un teclat
	 */
	public static final int KeysPerKeyboard = 28;

	/**
	 * Array que permet saber on col·locar un símbol sí tenim menys de 28. Per exemple, si tenim un únic símbol, aquest
	 * aniria a keysPos[0] = 14, aniria a la posició amb id 14 de la matriu. Així evitem que si hi ha només un símbol
	 * aquest aparegui no centrat.
	 */
	public static final int[] keysPos =
		{14, 15, 24, 25, 4, 5, 13, 16, 23, 26, 3, 6, 12, 17, 22, 27, 2, 7, 11, 18, 21, 28, 1, 8, 10, 19, 0, 9};

	/**
	 * Epsilon per poder fer comprovar si diferencies amb doubles son o no son zero (per evitar errors de precisió).
	 */
	public static final double epsilon = 0.000001d; //To avoid precision errors in hungarian algorithm

	/**
	 * Classe que representa un parell d'elements de tipus genèric.
	 * @param <T1> Tipus de l'element 1
	 * @param <T2> Tipus de l'element 2
	 */
	public static class Pair<T1, T2> {
		public T1 first;
		public T2 second;

		/**
		 * Constructora
		 * @param first Primer element
		 * @param second Segon element
		 */
		public Pair(T1 first, T2 second) {
			this.first = first;
			this.second = second;
		}
	}

	/**
	 * Classe que permet ordenar Pairs en base el primer element.
	 */
	public static class SortByFirst implements Comparator<Pair<Integer, String>> {
		public int compare(Utilities.Pair<Integer, String> o1, Utilities.Pair<Integer, String> o2) {
			return o2.first.compareTo(o1.first);
		}
	}

	/**
	 * CLasse que permet ordenar Pairs en base al segon element.
	 */
	public static class SortBySecond implements Comparator<Utilities.Pair<String, Integer>> {
		public int compare(Utilities.Pair<String, Integer> o1, Utilities.Pair<String, Integer> o2) {

			return o2.second.compareTo(o1.second);
		}
	}

}
