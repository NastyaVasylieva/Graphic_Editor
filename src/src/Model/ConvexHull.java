package src.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConvexHull {
    private List<Point> pointList = new ArrayList<>();

    public ConvexHull() {
    }

    public ConvexHull(List<Point> pointList) {
        this.pointList = pointList;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void clear(){
        this.pointList.clear();
    }

    public void addPoint(Point point){
        this.pointList.add(point);
    }

    public Point getPoint(int id){
        return this.pointList.get(id);
    }

    public int getSize(){
        return this.pointList.size();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConvexHull)) return false;
        ConvexHull that = (ConvexHull) o;
        return Objects.equals(pointList, that.pointList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointList);
    }
}
