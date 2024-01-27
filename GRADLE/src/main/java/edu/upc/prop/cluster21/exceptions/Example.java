package edu.upc.prop.cluster21.exceptions;

public class Example extends Exception {

	public Example() {
		super("Error d'exemple. Torna a introduir la dada sisplau: ");
	}

	//Es podrien fer altres amb parametres
	
	public Example(String s) {
		super("L'item amb id " + s + " no existeix");
	}
}

//Si volem capturar els errors en algun punt (els errors es van propagant cap amunt):
//
// si a() invoca a b() i b() invoca a c(). Si c() llança una excepció, es mira si c()
// la captura (try / catch), si c() no la captura es mira a b(), etc. Habitualment 
// capturarem els errors a la capa de presentació. Si no es captura l'excepció en cap
// lloc s'abortarà el programa amb un missatge de l'excepció que ho ha provocat.
//
// Si volem capturar l'error :
// 		try {
// 			computeSomething(); //Si en algun punt la funció llança Example, salta al catch de Example
//		} 
//		catch (Example e) {
//    	System.out.println("ERROR: " + e.getMessage());
//		}
//		
//	getMessage seria L'item amb id x no existex. :)


