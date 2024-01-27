package edu.upc.prop.cluster21.domain.classes;

import java.util.HashSet;
import java.util.HashMap;

public interface Algorithm {
	Character[][] generate_optimized_keyboard(HashSet<Character> symbols, HashMap<String, Integer> frequencies);
}
