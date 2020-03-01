
public class LineSegment {
    private Point p0;
    private Point p1;
    public LineSegment(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }
    //Determines the slope of the line segement
    private double calcSlope() {
        double rise = p1.getY() - p0.getY();
        double run = p1.getX() - p0.getX();
        if (run == 0) {
            return Double.NaN;
        }
        return rise / run;
    }
    //Returns true iff a ray extending from point P directly upwards intersects this line segment
    public boolean interPointRay(Point p) {
        double x = p.getX();
        double y = p.getY();
        if (p0.getX() >= x == p1.getX() >= x) {
            return false;
        }
        double yInter = calcSlope() * (x - p0.getX()) + p0.getY();
        return yInter > y;
    }
}
