package set;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T extends Comparable<T>> {

	Map<T, T> parents;
	Map<T, Integer> depths;
	int _num_sets;

	public DisjointSet() {
		parents = new HashMap<T, T>();
		depths = new HashMap<T, Integer>();
	}

	/**
	 * Creates a singleton from the parameter.
	 */
	public void createSet(T _t) {
        parents.put(_t, _t);
		depths.put(_t, 0);
		++_num_sets;
	}

	/**
	 * Finds and returns the representative of the set which contains the
	 * parameter. Implements path compression.
	 */
	public T findSet(T _t) {
		T tmp = _t;
		while(parents.get(tmp) != tmp) {
			T tmp2 = tmp;
			tmp = parents.get(tmp);
			parents.replace(tmp2, tmp);
		}
        return tmp;
	}

	/**
	 * Combines the sets which contain the parameters.
	 */
	public void unionSets(T _u, T _v) {
	    T u_id = findSet(_u);
		T v_id = findSet(_v);
		if (u_id == v_id) {
			return;
		} else {
			if (depths.get(u_id) < depths.get(v_id)){
				parents.replace(u_id, v_id);
			}
			else if (depths.get(u_id) > depths.get(v_id)) {
				parents.replace(v_id, u_id);
			} else {
				parents.replace(u_id, v_id);
				depths.replace(v_id, depths.get(v_id) + 1);
			}
		}
		--_num_sets;
	}

	public int size() {
		return _num_sets;
	}
}
