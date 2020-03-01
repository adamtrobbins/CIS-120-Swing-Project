
public class Point {
    private int x;
    private int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    //Returns new vector starting from this point and ending at point p
    public Vector disp(Point p) {
        return new Vector(p.getX() - x, p.getY() - y);
    }
    //Shifts the point by displacement vector v
    public Point translate(Vector v) {
        return new Point(x + v.getXC(), y + v.getYC());
    }
    //Given a point and adjacent points on either side, shifts the point in along the angle bisector
    //of the angle formed by the three points by the given amount. Used to shift in vertices in Pgon
    public Point shiftIn(double amount, Point leftAdj, Point rightAdj) {
        Vector v1 = disp(leftAdj);
        Vector v2 = disp(rightAdj);
        
        double len = amount / Math.sin(v1.angle(v2) * 0.5);
        
        return translate(v1.add(v2).lengthen((int) len));
    }
}
