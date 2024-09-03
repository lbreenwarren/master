
public class Map implements Cloneable {

    private class Elem {
        String key;
        String value;
        Elem left;
        Elem right;
        public Elem(){}
        public Elem(String k, String v) {
            key = k;
            value = v;
            left = null;
            right = null;
        }
    }

    private Elem _root;
    private int _size;

    public Map() {
        _root = new Elem();
        _root.left = _root;
        _root.right = null;
        _size = 0;
    }

    public Map(Map v) {

        if (v._root == v._root.left) {
            _root = new Elem();
            _root.left = _root;
            _root.right = null;
            _size = 0;
        } else {
            _root = new Elem();
            _root.left = _root;
            _root.right = null;
            _root.left = copyTree(v._root.left);
            _size = v._size;
        }
    }

    private Elem copyTree(Elem origRoot) {
        if (origRoot == null) {
            return null;
        } else {
            Elem newRoot = new Elem();
            newRoot.key = origRoot.key;
            newRoot.value = origRoot.value;
            newRoot.left = copyTree(origRoot.left);
            newRoot.right = copyTree(origRoot.right);
            return newRoot;
        }
    }

    private boolean isLeaf(Elem node) {
        if (node.left == null && node.right == null) {
            return true;
        }
        return false;

    }


    public boolean insert(String k, String v) {
        Elem tmp = _root;
        if (_root.left == _root) {
            _root = new Elem(k, v);
        }
        while (tmp != null) {
            int compare = tmp.key.compareTo(k);
            if (compare == 0) {
                if (tmp.value == null) {
                    tmp.value = v;
                    return true;
                } else {
                    return false;
                }
            }
            if (compare < 0) {
                if (tmp.left == null) {
                    tmp.left = new Elem(k,v);
                    ++_size;
                    return true;
                } else {
                    tmp = tmp.left;
                }
            } else if (compare > 0) {
                if (tmp.right == null) {
                    tmp.right = new Elem(k,v);
                    ++_size;
                    return true;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return false;
    }

    public boolean erase(String k) {
        Elem tmp = _root;
        String key = (String)k;
        if (!containsKey(k)) {
            return false;
        }
        Elem remove = _root;
        while (tmp != null) {
            int compare = tmp.key.compareTo(key);
            if (compare == 0) {
                remove = tmp;
            }
            if (compare < 0) {              
                tmp = tmp.left;
            } else if (compare > 0) {
                tmp = tmp.right;    
            }
        }
        if (isLeaf(remove)) {
            remove.value = null;
            return true;
        } else if (remove.left == null) {
            remove.key = remove.right.key;
            remove.value = remove.right.value;
            remove.right = null;
            return true;
        } else if (remove.right == null) {
            remove.key = remove.left.key;
            remove.value = remove.left.value;
            remove.left = null;
            return true;
        } else {
            Elem newTmp = remove.right;
            while (!isLeaf(newTmp)) {
                newTmp = newTmp.left;
            }
            remove.key = newTmp.key;
            remove.value = newTmp.value;
            erase(newTmp.key);
            return true;
        }
    }

	public void clear() {
        _root.left = _root;
        _root.right = null;
    }

    public boolean isEmpty() {
        if (_root.left == _root) {
            return true;
        }
        return false;
    }

    public int size() {
       return _size;
    }

    @SuppressWarnings("unchecked")
    public String get(Object k) {
        Elem tmp = _root;
        String key = (String)k;
        while (tmp != null) {
            int compare = tmp.key.compareTo(key);
            if (compare == 0) {
                return tmp.value;
            }
            if (compare < 0) {
                if (tmp.left == null) {
                    return null;
                } else {
                    tmp = tmp.left;
                }
            } else if (compare > 0) {
                if (tmp.right == null) {
                   return null;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return null;
    }

    public String replace(String key, String val) {
        Elem tmp = _root;
        String ret = "";
        while (tmp != null) {
            int compare = tmp.key.compareTo(key);
            if (compare == 0) {
                ret = tmp.value;
                tmp.value = val;
                return ret;
            }
            if (compare < 0) {
                if (tmp.left == null) {
                    return null;
                } else {
                    tmp = tmp.left;
                }
            } else if (compare > 0) {
                if (tmp.right == null) {
                    return null;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return null;
    }

    boolean containsKey(String k) {
        if (get(k) == null) {
            return false;
        }
        return true;
    }

    String getorDefault(String key, String defaultValue) {
        String ret = get(key);
        if (ret == null) {
            insert(key, defaultValue);
            return defaultValue;
        } else {return ret;}
    }

    String insertIfAbsent(String key, String value) {
        String ret = get(key);
        if (ret == null) {
            insert(key, value);
            return null;
        } else {
            return ret;
        }
    }

    @Override
    public Object clone() {
        return new Map(this);
    }

    private void printTree(int level, Elem p, StringBuilder res) {
        int i;
        if (p != null ) {
            printTree(level+1, p.right, res);
            for(i=0;i<level;i++) {
                res.append("\t");;
            }
            res.append(p.key).append(" ").append(p.value).append("\n");
            printTree(level+1, p.left, res);
        }
    }

    @Override
    public String toString() {
        if (_root == _root.left) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        printTree(0, _root.left, res);
        return res.toString();
    }
}
