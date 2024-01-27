package edu.upc.prop.cluster21.exceptions;

public class ObjectDoesNotExistException extends ObjectExistenceException {

    public ObjectDoesNotExistException(TypeObject typeObject, String name) {
        super(typeObject, name);
    }

}
