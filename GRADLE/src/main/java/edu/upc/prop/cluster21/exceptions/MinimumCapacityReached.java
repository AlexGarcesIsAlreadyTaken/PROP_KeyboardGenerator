package edu.upc.prop.cluster21.exceptions;

public class MinimumCapacityReached extends ObjectExistenceException {
    public MinimumCapacityReached(TypeObject typeObject, String name) {
        super(typeObject, name);
    }
}
