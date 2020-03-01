
public class Vector {
    private int x;
    private int y;
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Vector(double xD, double yD) {
        this.x = (int) xD;
        this.y = (int) yD;
    }
    //Gets x component
    public int getXC() {
        return x;
    }
    //Gets y component
    public int getYC() {
        return y;
    }
    //Sums two vectors
    public Vector add(Vector v) {
        return new Vector(x + v.getXC(), y + v.getYC());
    }
    //Returns a new vector with the same direction but desired length
    public Vector lengthen(int length) {
        return scale(length / magnitude());
    }
    //Returns a scaled version of the vector
    public Vector scale(double scaleFactor) {
        return new Vector(x * scaleFactor, y * scaleFactor);
    }
    //Computes the dot product of two vectors
    public double dot(Vector v) {
        return x * v.getXC() + y * v.getYC();
    }
    //Computes the angle between two vectors
    public double angle(Vector v) {
        return Math.acos(this.dot(v) / (this.magnitude() * v.magnitude()));
    }
    //Returns a unit vector with the same direction as this vector
    public Vector noramlize() {
        return lengthen(1);
    }
    //Returns the magnitude of the vector
    public double magnitude() {
        return Math.pow(x * x + y * y, 0.5);
    }
}
