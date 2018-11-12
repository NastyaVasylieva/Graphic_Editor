package src.Controller;

import src.Model.Point;

import java.util.*;

public class GrahamMethod implements IGrahamMethod {


    Point point;

    @Override
    public Point findNextToTop(Stack<Point> pointStack) {
        Point tempPoint = pointStack.peek();
        pointStack.pop();
        Point foundPoint = pointStack.peek();
        pointStack.push(tempPoint);
        return foundPoint;
    }

    @Override
    public double distanceSqr(Point point1, Point point2) {
        return (Math.pow(point1.getX() - point2.getX(),2) +
                (Math.pow(point1.getY() - point2.getY(),2)));
    }

    @Override
    public int findOrientation(Point point1, Point point2, Point point3) {
        double val = (point2.getY() - point1.getY()) * (point3.getX() - point2.getX()) -
                (point2.getX() - point1.getX()) * (point3.getY() - point2.getY());

        if (val == 0) return 0;
        return (val > 0)? 1: 2;
    }

    @Override
    public Stack generateConvexHull(List<Point> pointList) {

        double y_min = pointList.get(0).getY();
        int minIndex = 0;

        for(int i = 0; i < pointList.size(); i++){
            double y = pointList.get(i).getY();
            if ((y < y_min) || (y_min == y &&
                    pointList.get(i).getX() < pointList.get(minIndex).getX()))
                y_min = pointList.get(i).getY();
                minIndex = i;
        }

        Collections.swap(pointList, 0, minIndex);

        point = pointList.get(0);

        Comparator<Point> comp = new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                int o = findOrientation(point, o1, o2);
                if (o == 0)
                    return (distanceSqr(point, o2) >= distanceSqr(point, o1))? -1 : 1;
                return (o == 2)? -1: 1;
            }
        };

        Collections.sort(pointList, comp);


        int newSize=1;
        for(int i = 0; i < pointList.size(); i++){
            // Keep removing i while angle of i and i+1 is same
            // with respect to p0
            while (i < pointList.size()-1 && findOrientation(point, pointList.get(i),
                    pointList.get(i+1)) == 0)
                i++;

            pointList.set(newSize, pointList.get(i));
            newSize++;  // Update size of modified array
        }

        // If modified array of points has less than 3 points,
        // convex hull is not possible

       // if (newSize < 3) throw new Exception

        // Create an empty stack and push first three points
        // to it.
        Stack<Point> pointStack = new Stack<Point>();
        pointStack.push(pointList.get(0));
        pointStack.push(pointList.get(1));
        pointStack.push(pointList.get(2));

        // Process remaining n-3 points
        for (int i = 3; i < newSize; i++)
        {
            // Keep removing top while the angle formed by
            // points next-to-top, top, and points[i] makes
            // a non-left turn
            while (findOrientation(findNextToTop(pointStack), pointStack.peek(), pointList.get(i)) != 2)
                pointStack.pop();
            pointStack.push(pointList.get(i));
        }

        return pointStack;
    }
}
