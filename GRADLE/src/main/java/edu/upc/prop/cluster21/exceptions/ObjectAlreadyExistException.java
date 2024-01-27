package edu.upc.prop.cluster21.exceptions;


public class ObjectAlreadyExistException extends ObjectExistenceException{
    public ObjectAlreadyExistException(TypeObject typeObject, String name) {
        super(typeObject, name);
    }

}
