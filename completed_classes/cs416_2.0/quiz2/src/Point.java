public class Point{

    private double x;
    private double y;

    public Point( double x, double y ){
        this.x = x;
        this.y = y;
    }

    public Point max(Point p) {
        double maxX = 0;
        if (p.x > this.x) {
            maxX = p.x;
        } else {
            maxX = this.x;
        }
        double maxY = 0;
        if (p.y > this.y) {
            maxY = p.y;
        } else {
            maxY = this.y;
        }
        Point maxPoint = new Point(maxX, maxY);
        return maxPoint;
    }

    public String toString(){
        return String.format("(%.2f, %.2f)", x, y);
    }
    }

