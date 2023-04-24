public class Point {
    private double x;
    private double y;

    public Point( double x, double y ){
        this.x = x;
        this.y = y;
    }

    public Point max(Point p){
        double x1;
        double y1;
        x1 = Math.max(p.x, this.x);
        y1 = Math.max(p.y, this.y);
        return new Point(x1, y1);
    }

    public String toString(){
        return String.format("(%.2f, %.2f)", x, y);
    }

}
