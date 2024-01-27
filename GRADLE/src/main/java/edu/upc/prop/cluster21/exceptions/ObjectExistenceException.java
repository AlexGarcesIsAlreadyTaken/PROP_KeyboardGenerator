package edu.upc.prop.cluster21.exceptions;

public abstract class ObjectExistenceException extends Exception {
    public enum TypeObject {
        KEYBOARD,
        CHARACTER,
        FREQUENCYLIST,
        ALPHABET,
        WORD,
        USER,
        LISTFREQ,
    };
    String name;
    /*
    Define la clase del objeto que ha hecho saltarlo
        1: Alphabet
        3: Character
        2: Frequency List
        3: Keyboard
     */
    TypeObject typeObject;
    public ObjectExistenceException(TypeObject typeObject, String name) {
        super();
        this.typeObject = typeObject;
        this.name = name;
    }

    public String getName() {return this.name;}

    public TypeObject getTypeObject() {return this.typeObject;}

}
