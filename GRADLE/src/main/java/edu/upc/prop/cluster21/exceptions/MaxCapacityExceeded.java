package edu.upc.prop.cluster21.exceptions;

public class MaxCapacityExceeded extends Exception{

    int capacity;
    public MaxCapacityExceeded(int capacity) {
        super();
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }
}
