package edu.upc.prop.cluster21.domain.classes;

/**
 * Classe que representa a la versió actual de l'usuari
 */
public class User {
    
    private static User current;

    public static User getInstance() {
        if (current == null) current = new User();
        return current;
    }

    private String _user;
    private String _password;

    public User() {}

    public String getUser(){
        return _user;
    }

    public String getPassword(){
        return _password;
    }

    public void setUser(String user){
        _user = user;
    }

    public void setPassword(String password){
        _password = password;
    }

}
