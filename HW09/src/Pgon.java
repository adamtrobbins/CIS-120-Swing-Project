import java.awt.*;

public class Pgon {
    private int n;
    private Point[] points;
    private int[][] coords;
    public Pgon(Point[] points) {
        this.points = points;
        n = points.length;
        coords = new int[2][n];
        for (int i = 0; i < n; i++) {
            coords[0][i] = points[i].getX();
            coords[1][i] = points[i].getY();
        }
    }
    //Displays polygon
    public void draw(Graphics gc) {
        gc.fillPolygon(coords[0], coords[1], n);
    }
    //Returns a new polygon with each vertex shifted in by the given amount
    public Pgon shiftIn(double amount) {
        Point[] out = new Point[n];
        for (int i = 0; i < n; i++) {
            int bef = Math.floorMod(i - 1, n);
            int aft = Math.floorMod(i + 1, n);
            out[i] = points[i].shiftIn(amount, points[bef], points[aft]); 
        }
        return new Pgon(out);
    }
    //Returns the geometric center of a polygon
    public Point center() {
        double xsum = 0;
        double ysum = 0;
        for (Point p : points) {
            xsum += p.getX();
            ysum += p.getY();
        }
        return new Point((int) (xsum / n), (int) (ysum / n));
    }
    //Returns true iff the given point matches one of the ones in points
    public boolean pointMatches(Point pc) {
        for (Point p : points) {
            if (p == pc) {
                return true;
            }
        }
        return false;
    }
    //Returns true iff this polygon and the given polygon share an edge (not just a point) 
    public boolean sharesEdge(Pgon polygon) {
        if (this == polygon) {
            return false;
        }
        int count = 0;
        for (Point p : points) {
            if (polygon.pointMatches(p)) {
                count++;
            }
        }
        return count >= 2;
    }
    //Returns true iff p is inside of the polygon. Uses the fact that iff a point is inside a
    //polygon, a ray extending from that point will necessarily intersect the polygon an odd number
    // of times. If even, p must be outside. 
    public boolean isWithin(Point p) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            int next = Math.floorMod(i + 1, n);
            LineSegment ls = new LineSegment(points[i], points[next]);
            if (ls.interPointRay(p)) {
                count++;
            }
        }
        return count % 2 == 1;
    }
}


