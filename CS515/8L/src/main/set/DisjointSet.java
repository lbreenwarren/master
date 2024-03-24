package set;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T extends Comparable<T>> {

	Map<T, T> parents;
	Map<T, Integer> depths;

	public DisjointSet() {
		parents = new HashMap<T, T>();
		depths = new HashMap<T, Integer>();
	}

	/**
	 * Creates a singleton from the parameter.
	 */
	public void createSet(T _t) {
        // TO IMPLEMENT
	}

	/**
	 * Finds and returns the representative of the set which contains the
	 * parameter. Implements path compression.
	 */
	public T findSet(T _t) {
		//TO IMPLEMENT
        return null;
	}

	/**
	 * Combines the sets which contain the parameters.
	 */
	public void unionSets(T _u, T _v) {
	    //TO IMPLEMENT
	}
}
