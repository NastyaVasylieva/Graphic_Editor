package src.Model;

import java.util.*;

public class Polygon implements IPolygon {

    /**
     * Перелічення можливого напрямку між 3 точками
     */
    public enum Turn {CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR}

    @Override
    public boolean areAllCollinear(List<Point> points) {
        if (points.size() < 2) {
            return true;
        }

        final Point a = points.get(0);
        final Point b = points.get(1);

        for (int i = 2; i < points.size(); i++) {

            Point c = points.get(i);

            if (getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }


    @Override
    public ConvexHull getConvexHull(List<Point> points) throws Exception{
        if (points.size() < 3) {
            throw new Exception("Потрібно більше 3 точок!");
        }

        List<Point> sorted = new ArrayList<Point>(getSortedPointSet(points));

        if (areAllCollinear(sorted)) {
            throw new Exception("Точки не можуть лежати на одній прямій!");
        }

        Stack<Point> stack = new Stack<Point>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            Point head = sorted.get(i);
            Point middle = stack.pop();
            Point tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch (turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }

        //Закриваємо оболонку
        stack.push(sorted.get(0));

        return new ConvexHull(new ArrayList<Point>(stack));
    }

    @Override
    public Point getLowestPoint(List<Point> points) {
        Point lowest = points.get(0);

        for (int i = 1; i < points.size(); i++) {

            Point temp = points.get(i);

            if (temp.getY() < lowest.getY() || (temp.getY() == lowest.getY() && temp.getX() < lowest.getX())) {
                lowest = temp;
            }

        }
        return lowest;
    }

    @Override
    public Set<Point> getSortedPointSet(List<Point> points) {
        final Point lowest = getLowestPoint(points);

        TreeSet<Point> set = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {

                if(a == b || a.equals(b)) {
                    return 0;
                }

                double thetaA = Math.atan2((long)a.getY() - lowest.getY(), (long)a.getX() - lowest.getX());
                double thetaB = Math.atan2((long)b.getY() - lowest.getY(), (long)b.getX() - lowest.getX());

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' point, let the point closest to it come first

                    double distanceA = Math.sqrt((((long)lowest.getX() - a.getX()) * ((long)lowest.getX() - a.getX())) +
                            (((long)lowest.getY() - a.getY()) * ((long)lowest.getY() - a.getY())));
                    double distanceB = Math.sqrt((((long)lowest.getX() - b.getX()) * ((long)lowest.getX() - b.getX())) +
                            (((long)lowest.getY() - b.getY()) * ((long)lowest.getY() - b.getY())));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }

    @Override
    public  Turn getTurn(Point a, Point b, Point c) {
        double crossProduct = ((b.getX() - a.getX()) * (c.getY() - a.getY())) -
                ((b.getY() - a.getY()) * (c.getX() - a.getX()));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }

    @Override
    public boolean isInside(Point point, ConvexHull convexHull) {
        int pos = 0, neg = 0;

        for (int i = 0; i < convexHull.getSize(); i++)
        {
            //Якщо точка вже є в заданому полігоні
            if (convexHull.getPoint(i) == point)
                return true;

            //Формуємо сегмент між ітою точкою
            double x1 = convexHull.getPoint(i).getX();
            double y1 = convexHull.getPoint(i).getY();

            //Та з і+1ою, або якщо ітий елемент останній, то з першою
            int i2 = i < convexHull.getSize()- 1 ? i + 1 : 0;

            double x2 = convexHull.getPoint(i2).getX();
            double y2 = convexHull.getPoint(i2).getY();

            double x = point.getX();
            double y = point.getY();

            //Векторний добуток
            double d = (x - x1)*(y2 - y1) - (y - y1)*(x2 - x1);

            if (d > 0) pos++;
            if (d < 0) neg++;

            //Якщо знак змінився => точка зовні
            if (pos > 0 && neg > 0)
                return false;
        }
        //Якщо немає зміни у напрямку, то на одній стороні всіх сегментів,
        //а отже всередині
        return true;
    }

}