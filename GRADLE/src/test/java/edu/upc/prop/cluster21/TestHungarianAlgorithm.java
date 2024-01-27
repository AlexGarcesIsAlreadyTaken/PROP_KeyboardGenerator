package edu.upc.prop.cluster21;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import edu.upc.prop.cluster21.domain.classes.HungarianAlgorithm;
import edu.upc.prop.cluster21.exceptions.*;

/**
 * Test unitari JUnit per la classe HungarianAlgorithm. Per comprovar-ne el
 * correcte funcionament, s'ha provat de resoldre instàncies a
 * <a href="https://www.hungarianalgorithm.com/solve.php">Solver Hungarian Algorithm</a>, que retorna
 * sempre la solució òptima. La nostra versió hauria de retornar els mateixos valors.
 */

public class TestHungarianAlgorithm {
    @Test
    public void emptyMatrixReturnsZero() {
        double[][] values = {};
        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(0.0, cost, 0.0001);
    }

    @Test
    public void fullZerosMatrixReturnsZero() {
        double[][] values = {
            { 0.0, 0.0, 0.0, 0.0},
            { 0.0, 0.0, 0.0, 0.0},
            { 0.0, 0.0, 0.0, 0.0},
            { 0.0, 0.0, 0.0, 0.0}};
        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(0.0, cost, 0.0001);
    }

    //---------- WITHOUT DECIMALS ------------
    @Test
    public void returnsOptimalCost1() {
        double[][] values = {
            { 82, 83, 69, 92},
            { 77, 37, 49, 92},
            { 11, 69, 5, 86},
            { 8, 9, 98, 23}};
        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(140.0, cost, 0.0001);
    }

    @Test
    public void returnsOptimalCost2() {
        double[][] values = {
                { 20, 25, 22, 28},
                { 15, 18, 23, 17},
                { 19, 17, 21, 24},
                { 25, 23, 24, 24}};
        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(78.0, cost, 0.0001);
    }

    @Test
    public void returnsOptimalCost3() {
        double[][] values = {
                { 68, 38, 9, 60, 46, 58, 83, 87, 84, 20 },
                { 53, 4, 16, 4, 44, 72, 34, 97, 69, 5 },
                { 21, 2, 45, 55, 34, 15, 2, 13, 12, 51 },
                { 8, 93, 22, 66, 25, 9, 59, 71, 12, 95 },
                { 42, 35, 33, 23, 3, 8, 8, 50, 23, 95 },
                { 74, 37, 15, 21, 36, 49, 80, 55, 79, 53 },
                { 21, 97, 55, 12, 25, 67, 10, 65, 2, 49 },
                { 8, 48, 1, 92, 8, 76, 41, 32, 87, 36 },
                { 32, 73, 71, 47, 94, 92, 16, 97, 5, 4 },
                { 58, 37, 54, 52, 84, 16, 34, 5, 72, 26 } };

        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(67.0, cost, 0.0001);
    }

    //---------- WITH DECIMALS ------------

    @Test
    public void returnsOptimalCost4() {
        double[][] values = {
                { 45.9, 3.3, 13.90, 78.9},
                { 4.0, 47.7, 56.91, 80.0},
                { 78.81, 53.8, 43.16, 21.0},
                { 67.29, 3.12, 77.3, 12.91}};
        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(42.02, cost, 0.0001);
    }

    @Test
    public void returnsOptimalCost5() {
        double[][] values = {
                { 33.33, 16.4, 0.0, 78.9},
                { 4.0, 47.7, 0.0, 80.0},
                { 78.81, 53.8, 0.0, 21.0},
                { 0.0, 0.0, 0.0, 0.0}};
        HungarianAlgorithm h = new HungarianAlgorithm(values);
        double cost = h.computeOptimalAssignmentCost();
        assertEquals(20.4, cost, 0.0001);
    }

}

