import java.util.ArrayList;
import java.util.Collections;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.NoSuchElementException;



public class UnorderedMap<KEY, T> {
    final int INIT_SIZE = 101;

    class Entry{
		KEY key;
		T data;
		Entry next;

        Entry() {
            next = null;
        }

		Entry(KEY newKey, T newData, Entry newNext) {
            key = newKey;
            data = newData;
            next = newNext;
        }
	};

	private ArrayList<Entry> table; // each table index has a linked list of Entries.
	private int tablesize;  // table size
	private int entrysize;  // total number of entries

    // Internal method to test if a positive number is prime (not efficient)
    private boolean isPrime( int n ){
        if( n == 2 || n == 3 )
            return true;
        if( n == 1 || n % 2 == 0 )
            return false;
        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;
        return true;
    }

    // Internal method to return a prime number at least as large as n.
    // Assumes n > 0.
    private int nextPrime( int n ){
        if( n % 2 == 0 )
            n++;
        for( ; !isPrime( n ); n += 2 )
            ;
        return n;
    }

    // Constructs an empty unordered_map object, containing no elements and with a entry size of zero
    // Notice the hash table with a default size 101 is constructed but empty.
	public UnorderedMap() {
        tablesize = INIT_SIZE;
        entrysize = 0;
        table = new ArrayList<Entry>();
        for (int i = 0; i <= tablesize; ++i) {
            table.add(i, null);
        }
    }

	// Copy constructor The object is initialized to have the same contents and properties as the UnorderedMap object rhs.
	UnorderedMap(UnorderedMap<KEY, T> rhs) {
        tablesize = rhs.tablesize;
        
        entrysize = rhs.entrysize;
        table = new ArrayList<Entry>();
        for (int i = 0; i < rhs.tablesize; ++i) {
            Entry othertmp = rhs.getEntryFromIndex(i);
            table.add(i, othertmp);
        }
    }

    private Entry getEntryFromIndex(int i) {
        if (i >= tablesize) {
            throw new NoSuchElementException();
        } else {
            return table.get(i);
        }
    }
	// A simple forward iterator. The order of iterator is not specified.
	class Iterator{
		public Iterator(){}
		public Iterator( Entry cur) {
            _cur = cur;
        }

        public boolean hasNext() {
            return (_cur != null);
        }

		public Entry next() {
            Entry ret = _cur;

            //if _cur has a next, return it
            if (_cur.next != null) {
                _cur = _cur.next;
                return ret;
            }
            
            //find index of _cur
            int _curIndex = Math.floorMod(_cur.key.hashCode(), tablesize);
            int i = _curIndex + 1;

            //while the next index is not off the array and doesnt have an entry, go to next
            while (i < tablesize && getEntryFromIndex(i) == null) {
                ++i;
            }
            
            //if we got to the end of the table, were done
            if (i >= tablesize) {
                _cur = null;
                return ret;
            }

            // found the next index with an entry
            _cur = getEntryFromIndex(i);
            return ret;
        }

        @Override
        @SuppressWarnings("unchecked")
		public boolean equals(Object ob) {
            Iterator tmp = (Iterator)ob;
            if (_cur == null) {
                if (tmp._cur == null) {
                    return true;
                }
                else {
                    return false;
                }
            }
            return _cur.equals(tmp._cur);
        }

		private Entry _cur;
	};

	// Returns an iterator at the first element in the UnorderedMap container
    // Note that an UnorderedMap object makes no guarantees on which specific element is considered its first element.
	// But, in any case, the range that goes from its begin to its end covers all the elements in the container.
	Iterator iterator() {
        Entry tmp = table.get(0);
        int i = 0;
        while (tmp == null && i < tablesize) {
            ++i;
            tmp = table.get(i);
        }
        if (tmp == null || i == tablesize) {
            return new Iterator(null);
        }
        return new Iterator(getEntryFromIndex(i));
    }

    // applies the action on each entry in the Hash Table
    public void forEach(BiConsumer<? super KEY, ? super T> action) {
        Iterator it = iterator();
        while(it.hasNext()) {
            Entry cur = it.next();
            action.accept(cur.key, cur.data);
        }
    }

    // applies the function on each entry in the Hash Table, updated the Entry's
    // data
    public void replaceAll(BiFunction<? super KEY, ? super T, ? extends T> function) {
        Iterator it = iterator();
        while(it.hasNext()) {
            Entry cur = it.next();
            cur.data = function.apply(cur.key, cur.data);
        }
    }


	// Inserts new elements in the unordered_map.
    // Each element is inserted only if its key is not equivalent to the key of any other element already in the container
	// (keys in an UnorderedMap are unique). function returns true if new element is inserted, false otherwise.
	boolean put(KEY key, T val) {
        // if key exists return false
        if (getEntry(key) != null) {
            return false;
        }

        //key doesnt exist, create new entry and get hash value of key
        Entry newEntry = new Entry(key, val, null);
        int hash = Math.floorMod(key.hashCode(), tablesize);

        //find index
        Entry curEntry = table.get(hash);

        //if index empty, insert new entry
        if (curEntry == null) {
            table.set(hash, newEntry);
            ++entrysize;
        //else walk list until next entry is null and insert at next
        } else {
            while(curEntry.next != null) {
                curEntry = curEntry.next;
            }
            curEntry.next = newEntry;
            ++entrysize;
        }

        //if load factor > 2, rehash
        if (entrysize/tablesize >= 2) {
            rehash();
        }
        return true;
    }

	// Removes from the UnorderedMap container a single element
	// returns true if the element is erased, false otherwise
	boolean erase (KEY key) {
        // if key doesnt exist, return false
        if (getEntry(key) == null) {
            return false;
        }
        
        //find index of key
        int hash = Math.floorMod(key.hashCode(), tablesize);
        Entry tmp = table.get(hash);
        
        //if first entry in list, replace entry in array with its next
        if (tmp.key.equals(key)) {
            table.set(hash, tmp.next);
            --entrysize;
            return true;
        }

        //else go next until next is the entry were looking for
        while(tmp.next != null && !tmp.next.key.equals(key)) {
            tmp = tmp.next;
        }

        //just in case if we hit null, return false
        if (tmp.next == null) {
            return false;
        }

        //set tmp's next to the next of the entry to remove
        tmp.next = tmp.next.next;
        --entrysize;
        return true;

    }


    // Searches the container for an element with the given key and returns the associated Entry if found,
	// otherwise it returns null.
	private Entry getEntry(KEY key) {
        int hash = Math.floorMod(key.hashCode(), tablesize);
        Entry tmp = table.get(hash);
        if (tmp == null) {
            return null;
        } else {
            while(tmp != null) {
                if (tmp.key == key) {
                    return tmp;
                }
                tmp = tmp.next;
            } 
        }
        return null;
    }

	// Searches the container for an element with the given key and returns the associated value if found,
	// otherwise it returns null.
	T get(KEY key) {
        // get index of key 
        int hash = Math.floorMod(key.hashCode(), tablesize);
        Entry tmp = table.get(hash);

        //move to next if not null and key doesnt match
        while(tmp != null && !tmp.key.equals(key)) {
            tmp = tmp.next;
        }

        if(tmp == null) {
            return null;
        }
        return tmp.data;
        
    }

    // Searches the container for an element with the given key and returns true if found,
	// otherwise it returns null.
	boolean containsKey(KEY key) {
        return get(key) != null;
    }

    // Replaces the value for the specified key only if already mapped to a value. Returns the
    // prior value in that case. If it was not already mapped, returns false.
	T replace(KEY key, T val) {
        Entry tmp = getEntry(key);
        if (tmp == null) {
            return null;
        }
        T ret = tmp.data;
        tmp.data = val;
        return ret;

    }

	// Returns the number of elements in the UnorderedMap container.
	int size(){ return entrysize; }

	// Returns the table size; included for verifying rehash operation.
	int tsize(){ return tablesize; }

    // double the hashtable when the load factor reaches 2.
	private void rehash() {
        int oldsize = tablesize;
        int newsize = nextPrime(tablesize * 2);
        //store current map in a copy
        UnorderedMap<KEY, T> copy = new UnorderedMap<KEY, T>(this);
        
        //empty current map
        forEach((KEY key, T val) -> {
            erase(key);
        });
        
        tablesize = newsize;

        //init new indices to null
        for (int i = oldsize; i < tablesize; ++i) {
            table.add(null);
        }
        
        //re hash values from copy back into map
        copy.forEach((KEY key, T val) -> {
            this.put(key, val);
        });
    }
}
