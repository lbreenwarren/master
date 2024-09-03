public class TextSegment implements Comparable<TextSegment>{
    private String _text;
    private int _order;

    TextSegment(int order, String text) {
        _text = text;
        _order = order;
    }

    @Override
    public String toString() { return _text; }

    @Override
    public int compareTo(TextSegment other) {
        if (this._order < other._order) {
            return -1;
        } else if (this._order > other._order) {
            return 1;
        } else {
            return 0;
        }
    }
}
