package edu.upc.prop.cluster21.domain.classes;

import java.util.Arrays;

/**
 * HungarianAlgorithm és la classe que s'encarrega de resoldre instàncies
 * del <em>Linear Assignment Probkem</em>. Resulta ser important, ja que pel
 * càlcul de la cota de <em>Gilmore-Lawler</em> que s'utilitza a l'algorisme <em>Branch and Bound</em>,
 * en un punt fa falta intentar trobar l'assignació òptima per les instal·lacions no
 * col·locades i el cost d'aquesta. L'entrada del HungarianAlgorithm és una matriu
 * de dimensió (N-m)x(N-m) (on N és el total d'instal·lacions i m es el nombre d'instal·lacions
 * ja col·locades) on l'elememt (i, k) intenta acotar el cost addicional que suposaria
 * col·locar l'i-èssima instal·lació no col·locada a la k-èssima ubicació. Aquests algorisme és
 * ideal per resoldre el problema, ja que es tracta d'un algorisme d'optimització combinatòria
 * conegut que que resol el <em>Linear Assignment Problem</em> en temps polinòmic. Per la implementació
 * de l'algorisme s'han seguit els passos descrits a
 * <a href="https://www.cs.upc.edu/prop/data/uploads/prop_q1_2023_2024.pdf">Información sobre el QAP</a>.
 *
 * @author Ismael El Basli
 */

public class HungarianAlgorithm {

    /*
     Javadoc se usa principalmente para generar documentación de la interfaz pública de una
     clase (métodos públicos, constructores, campos públicos, etc.). Los detalles internos,
     como los atributos privados, no suelen documentarse en Javadoc, pero es una buena práctica
     proporcionar comentarios descriptivos en el código para que otros desarrolladores puedan
     entender el propósito y el uso de esos atributos en el contexto de la implementación de la clase.
     (AIXÒ HO MENCIONAREM A LA DOCUMENTACIÓ!)
     */

    /**
     * matrix es tracta de la matriu que manipula l'algorisme per arribar a l'assignació òptima.
     */
    private  double[][] matrix;

    /**
     * originalMatrix es la matriu original que es passa a l'algorisme, s'utilitza al final per computar
     * el cost de l'assignació òptima, ja que matrix és molt diferent a la matriu original un cop finalitzat
     * l'algorisme.
     */
    private  double[][] originalMatrix;

    /**
     * rowWithLine es un array de booleans amb tants elements com files té la matriu passada a
     * l'algorisme. En un punt, el HungarianAlgorithm necessita cobrir els zeros d'una matriu
     * amb el mínim nombre de línies, i aquest array ens indica que si a l'element i-èssim hi
     * ha el valor true, la fila i s'utilitza com a línia cobridora, i no s'utilitza en cas contrari.
     */
    private boolean[] rowWithLine;

    /**
     * colWithLine es un array de booleans amb tants elements com columnes té la matriu passada a
     * l'algorisme. En un punt, el HungarianAlgorithm necessita cobrir els zeros d'una matriu
     * amb el mínim nombre de línies, i aquest array ens indica que si a l'element i-èssim hi
     * ha el valor true, la columna i s'utilitza com a línia cobridora, i no s'utilitza en cas contrari.
     */
    private boolean[] colWithLine;

    /**
     * linesUsed es un enter que inica el mínim nombre de línies usades per cobrir tots els
     * zeros d'una matriu. És important, ja que a l'algorisme s'acaba d'iterar quan es necessiten
     * n línies per cobrir la matriu, sigui la matriu de dimensión nxn.
     */
    private int linesUsed;

    /**
     * n es el nombre de files de la matriu, que és equivalent al nombre de columnes,
     * ja que la matriu es quadrada.
     */
    private int n;

    /**
     * solutionFound és un booleà que indica si j'ha s'ha trobat o no una assignació òptima
     * correcte. És important per deixar de fer crides recursives a l'algorisme de backtracking.
     */
    private boolean solutionFound;

    /**
     * bestAssignment es un array de enteros que nos permite saber la mejor assignación encontrada
     * usando backtracking (se entiende como mejor
     * )
     * bestAssignment és un array de enters que ens permet saber la millor assignació trobada
     * utilitzant backTracking. A la posició i-èssima hi ha la columna d'aquella fila on es marca
     * un zero, -1 en cas que no es marqui res.
     */
    private int[] bestAssignment;


    /**
     * Creadora. Inicialitza els valors dels atributs privats de la classe necessaris per resoldre
     * el problema.
     * @param m Matriu de dimensió (N-m)x(N-m) (on N és el total d'instal·lacions i m es el nombre d'instal·lacions
     * ja col·locades) on l'elememt (i, k) intenta acotar el cost addicional que suposaria
     * col·locar l'i-èssima instal·lació no col·locada a la k-èssima ubicació.
     */
    public HungarianAlgorithm(double[][] m)  {
        //if (m.length != m[0].length) throw new MatrixIsNotSquare();
        n = m.length;
        matrix = new double[n][n];
        originalMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = m[i][j];
                originalMatrix[i][j] = m[i][j];
            }
        }
        bestAssignment = new int[n];
        solutionFound = false;
    }

    /**
     * Funció que calcula el cost de l'assignació òptima, utilitzant el Hungarian Algorithm.
     * @return Valor double que representa el cost de l'assignació òptima.
     */
    public double computeOptimalAssignmentCost() {
        preprocessing();

        coverWithMinLines();
        while (linesUsed < n) {
            updateMatrix();
            coverWithMinLines();
        }

        int[] initialAssignment = new int[n]; //en la fila i se usara la columna initialAssignment[i], -1 si no usa nada
        Arrays.fill(initialAssignment, -1);
        Arrays.fill(bestAssignment, -1);

        FindFirstSolution(initialAssignment, 0, 0);

        double total = 0;
        for(int row = 0; row < n; row++) {
            //Aqui seguro que todos los elementos de bestassignment son != -1
            total += originalMatrix[row][bestAssignment[row]];
        }
        return total;
    }

    /**
     * Mètode que s'encarrega del recobriment amb línies mínimes
     */
    private void coverWithMinLines() {
        rowWithLine = new boolean[n];
        colWithLine = new boolean[n];

        int[] initialAssignment = new int[n]; //en la fila i se usara la columna initialAssignment[i], -1 si no usa nada
        Arrays.fill(initialAssignment, -1);
        Arrays.fill(bestAssignment, -1);

        //1. Primero deberiamos programar un algoritmo que de la asignacion mas completa posible usando backtracking.
        backtrackingMaxCompleteAssignment(initialAssignment, 0, 0);

        //2. Marcamos todas las filas sin assignacion
        for (int i = 0; i < n; ++i) {
            if (bestAssignment[i] == -1) rowWithLine[i] = true;
        }

        //Se repite hasta que esten todos los zeros marcados
        do {
            //3.1. Marcamos todas las columnas que tienen algun 0 en una fila marcada
            for (int col = 0; col < n; ++col) {
                boolean mark = false;
                for (int row = 0; row < n; ++row) {
                    if (Math.abs(matrix[row][col]) < Utilities.epsilon && rowWithLine[row]) {
                        mark = true;
                    }
                }
                colWithLine[col] = mark;
            }
            //3.2. Marcamos todas las filas que tienen su asignacion en una columna marcada
            for (int i = 0; i < n; ++i) {
                if (bestAssignment[i] != -1 && colWithLine[bestAssignment[i]]) rowWithLine[i] = true;
            }
        } while (!allZerosMarked());

        //4. Las columnas marcadas y las filas no marcadas son las lineas minimas a usar
        //actualizo los arrays de lineas, las columnas marcadas las dejo, pero las filas
        //marcadas ahora no se marcan y las no marcadas si.

        for (int i = 0; i < n; ++i) rowWithLine[i] = !rowWithLine[i];

        //Y actualizo lines useeed!!!!
        linesUsed = 0;
        for (int i = 0; i < n; ++i) {
            if (rowWithLine[i]) ++linesUsed;
            if (colWithLine[i]) ++linesUsed;
        }
    }

    /**
     * Mètode que deixa a bestAssignment la millor assignació que es pot realitzar (més zeros marcats), utilitzant backtracking
     * @param assignment Assignació actual
     * @param idx Índex actual
     * @param count Nombre de zeros marcats
     */
    private void backtrackingMaxCompleteAssignment(int[] assignment, int idx, int count) {
        if (idx == n) {
            if (count > countMarkedZeros(bestAssignment)) {
                System.arraycopy(assignment, 0, bestAssignment, 0, n);
            }
        }
        else {
            for (int i = 0; i < n; ++i) {
                if (isValid(assignment, idx, i)) {
                    assignment[idx] = i;
                    backtrackingMaxCompleteAssignment(assignment, idx+1, count+1);
                    assignment[idx] = -1;
                }
            }
            //Falta el cas de no marcar cap 0 a la fila actual
            backtrackingMaxCompleteAssignment(assignment, idx+1, count);
        }
    }

    /**
     * Mètode que troba una solució qualsevol quan sabem segur que aquesta existeix, utilitzant backtracking
     * @param assignment Assignació actual
     * @param idx Índex actual
     * @param count Nombre de zeros marcats
     */
    private void FindFirstSolution(int[] assignment, int idx, int count) {
        if (idx == n && !solutionFound) {
            if (count > countMarkedZeros(bestAssignment)) {
                System.arraycopy(assignment, 0, bestAssignment, 0, n);
                if (count == n) solutionFound = true; //La primera solucion ya nos vale
            }
        }
        else if (!solutionFound){
            for (int i = 0; i < n; ++i) {
                if (isValid(assignment, idx, i)) {
                    assignment[idx] = i;
                    backtrackingMaxCompleteAssignment(assignment, idx+1, count+1);
                    assignment[idx] = -1;
                }
            }
            //Falta el cas de no marcar cap 0 a la fila actual
            backtrackingMaxCompleteAssignment(assignment, idx+1, count);
        }
    }

    /**
     * Retorna si una assignació és o no vàlida en marcar una posició (i, j)
     * @param assignment Assignació actual
     * @param row Fila
     * @param col Columna
     * @return True si és válid marcar la posició (row, col) a l'assignació assignment, false en cas contrari
     */
    private boolean isValid(int[] assignment, int row, int col) {
        for (int i = 0; i < n; i++) {
            if (assignment[i] == col) {
                return false; // Ya se marcó un cero en esta columna
            }
        }
        return Math.abs(matrix[row][col]) < Utilities.epsilon;
    }

    /**
     * Conta el nombre de zeros marcats en una assignació
     * @param assignment Assignació
     * @return Nombre de zeros marcats
     */
    private int countMarkedZeros(int[] assignment) {
        int count = 0;
        for (int i = 0; i < n; ++i) {
            if (assignment[i] != -1) ++count;
        }
        return count;
    }

    /**
     * Mètode que actualitza la matriu en base a com ho descriu el Hungarian algorithm.
     */
    private void updateMatrix() {
            //Encontramos el mínimo no cubierto
            double minNotCovered = findMinNotCovered();

            //Añadimos a cada numero cubierto el minimo numero no cubierto
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (rowWithLine[i]) matrix[i][j] += minNotCovered;
                    if (colWithLine[j]) matrix[i][j] += minNotCovered;
                }
            }

            //Encontramos el minimo de la matriz
            double min = findMinMatrix();

            //Substraemos el minimo elemento de la matriz de cada elemento de la matriz
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    matrix[i][j] -= min;
                }
            }

    }

    /**
     * Troba l'element de valor mínim de la matriu matrix
     * @return El valor mínim de la matriu matrix
     */
    private double findMinMatrix() {
        double minMatrixValue = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] < minMatrixValue) {
                    minMatrixValue = matrix[i][j];
                }
            }
        }
        return minMatrixValue;
    }

    /**
     * Troba el valor mínim de matrix no cobert per una línia
     * @return El valor mínim de matrix no cobert per una línia
     */
    private double findMinNotCovered() {
        double minNotCovered = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (!rowWithLine[i]) {
                for (int j = 0; j < n; j++) {
                    if (!colWithLine[j] && matrix[i][j] < minNotCovered) {
                        minNotCovered = matrix[i][j];
                    }
                }
            }
        }
        return minNotCovered;
    }

    /**
     * Preprocessat del Hungarian Algorithm
     */
    private void preprocessing() {
        //Sabemos que siempre tenemos matrizes cuadradas.

        double min_value;
        //Se substrae de cada fila el minimo valor de esa fila
        for (int i = 0; i < n; ++i) {
            //Buscamos el valor minimo
            min_value = Double.MAX_VALUE;
            for (int j = 0; j < n; ++j) {
                if (matrix[i][j] < min_value) min_value = matrix[i][j];
            }
            //Se substrae
            for (int j = 0; j < n; ++j) matrix[i][j] -= min_value;
        }

        //Se substrae de cada columna el minimo valor de esa columna
        for (int i = 0; i < n; ++i) {
            //Buscamos el valor minimo
            min_value = Double.MAX_VALUE;
            for (int j = 0; j < n; ++j) {
                if (matrix[j][i] < min_value) min_value = matrix[j][i];
            }
            //Se substrae
            for (int j = 0; j < n; ++j) matrix[j][i] -= min_value;
        }
    }

    /**
     * Mètode que permet saber si tots els zeros estan marcats
     * @return True si tots els zeros estan marcats, false en cas contrari.
     */
    private boolean allZerosMarked() {
        boolean correct = true;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (Math.abs(matrix[i][j]) < Utilities.epsilon && !colWithLine[j] && rowWithLine[i])
                    correct = false;
            }
        }
        return correct;
    }
}
