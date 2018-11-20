package src.Model;

import java.util.Objects;

public class Point {
    private int id;
    private double x;
    private double y;

    public Point(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point() {
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
