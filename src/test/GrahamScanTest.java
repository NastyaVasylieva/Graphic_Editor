package test;


import org.junit.Test;
import src.Model.ConvexHull;
import src.Model.Point;
import src.Model.Polygon;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GrahamScanTest {

    Polygon polygon = new Polygon();
    @Test
    public void areAllCollinearTest() {

        Point a = new Point(2, 4);
        Point b = new Point(6, 6);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(5, 5);

        assertThat(polygon.areAllCollinear(Arrays.asList(c)), is(true));
        assertThat(polygon.areAllCollinear(Arrays.asList(c, e)), is(true));
        assertThat(polygon.areAllCollinear(Arrays.asList(c, e, f)), is(true));
        assertThat(polygon.areAllCollinear(Arrays.asList(c, b, e, e, e, f, c)), is(true));
        assertThat(polygon.areAllCollinear(Arrays.asList(a, b, d)), is(false));
    }

    @Test
    public void getConvexHullTest() throws Exception {

        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);

        ConvexHull convexHull = polygon.getConvexHull(Arrays.asList(a, b, c, d, e, f, g, h, i));

        assertThat(convexHull.getSize(), is(5));

        assertThat(convexHull.getPoint(0), is(f));
        assertThat(convexHull.getPoint(1), is(i));
        assertThat(convexHull.getPoint(2), is(d));
        assertThat(convexHull.getPoint(3), is(a));
        assertThat(convexHull.getPoint(4), is(f));

        Point j = new Point(-2, 3);
        Point k = new Point(6, -2);
        Point m = new Point(6, 6);

        convexHull = polygon.getConvexHull(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, m));

        assertThat(convexHull.getSize(), is(5));

        assertThat(convexHull.getPoint(0), is(k));
        assertThat(convexHull.getPoint(1), is(m));
        assertThat(convexHull.getPoint(2), is(d));
        assertThat(convexHull.getPoint(3), is(j));
        assertThat(convexHull.getPoint(4), is(k));

        j = new Point(-2, 7);
        k = new Point(8, -2);
        m = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        convexHull = polygon.getConvexHull(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k, m));

        assertThat(convexHull.getSize(), is(4));

        assertThat(convexHull.getPoint(0), is(k));
        assertThat(convexHull.getPoint(1), is(m));
        assertThat(convexHull.getPoint(2), is(j));
        assertThat(convexHull.getPoint(3), is(k));
    }

    @Test(expected = Exception.class)
    public void getConvexHullTestFail() throws Exception {

        Point a = new Point(2, 4);
        Point b = new Point(3, 3);
        Point c = new Point(4, 2);

        polygon.getConvexHull(Arrays.asList(a, b, c));
    }

    @Test
    public void getLowestPointTest() {

        Point a = new Point(2, 4);
        Point b = new Point(3, 5);
        Point c = new Point(3, 3);
        Point d = new Point(4, 6);
        Point e = new Point(4, 4);
        Point f = new Point(4, 2);
        Point g = new Point(5, 5);
        Point h = new Point(5, 3);
        Point i = new Point(6, 4);

        Point lowest = polygon.getLowestPoint(Arrays.asList(a, b, c, d, e, f, g, h, i));

        assertThat(lowest, is(f));

        Point j = new Point(-1, -1);
        Point k = new Point(6, -1);

        lowest = polygon.getLowestPoint(Arrays.asList(a, b, c, d, e, f, g, h, i, j, k));

        assertThat(lowest, is(j));
    }


    @Test
    public void getSortedPointSetTest() {

        Point a = new Point(2, 2);
        Point b = new Point(1, 1);
        Point c = new Point(0, 0);
        Point d = new Point(2, 0);
        Point e = new Point(1, 0);
        Point f = new Point(0, 1);
        Point g = new Point(0, 2);
        Point h = new Point(2, 2); // duplicate
        Point i = new Point(2, 2); // duplicate

        List<Point> points = Arrays.asList(a, b, c, d, e, f, g, h, i);

        Set<Point> set = polygon.getSortedPointSet(points);
        Point[] array = set.toArray(new Point[set.size()]);

        assertThat(set.size(), is(7));

        assertThat(array[0], is(c));
        assertThat(array[1], is(e));
        assertThat(array[2], is(d));
        assertThat(array[3], is(b));
        assertThat(array[4], is(a));
        assertThat(array[5], is(f));
        assertThat(array[6], is(g));
    }

    @Test
    public void getTurnTest() {


        Point a = new Point(1, 1);
        Point b = new Point(4, 4);
        Point c = new Point(8, 8);
        Point d = new Point(7, 9);
        Point e = new Point(9, 7);
        Point f = new Point(0, -1);
        Point g = new Point(-1, 0);
        Point h = new Point(-2, 1);

        assertThat(polygon.getTurn(a, b, c), is(Polygon.Turn.COLLINEAR));
        assertThat(polygon.getTurn(a, c, b), is(Polygon.Turn.COLLINEAR));
        assertThat(polygon.getTurn(b, a, c), is(Polygon.Turn.COLLINEAR));
        assertThat(polygon.getTurn(c, b, a), is(Polygon.Turn.COLLINEAR));
        assertThat(polygon.getTurn(e, d, c), is(Polygon.Turn.COLLINEAR));
        assertThat(polygon.getTurn(h, f, g), is(Polygon.Turn.COLLINEAR));

        assertThat(polygon.getTurn(a, b, e), is(Polygon.Turn.CLOCKWISE));
        assertThat(polygon.getTurn(a, b, f), is(Polygon.Turn.CLOCKWISE));
        assertThat(polygon.getTurn(a, c, e), is(Polygon.Turn.CLOCKWISE));
        assertThat(polygon.getTurn(a, c, f), is(Polygon.Turn.CLOCKWISE));
        assertThat(polygon.getTurn(c, b, g), is(Polygon.Turn.CLOCKWISE));
        assertThat(polygon.getTurn(d, b, f), is(Polygon.Turn.CLOCKWISE));

        assertThat(polygon.getTurn(a, b, d), is(Polygon.Turn.COUNTER_CLOCKWISE));
        assertThat(polygon.getTurn(a, e, d), is(Polygon.Turn.COUNTER_CLOCKWISE));
        assertThat(polygon.getTurn(e, c, f), is(Polygon.Turn.COUNTER_CLOCKWISE));
        assertThat(polygon.getTurn(b, d, a), is(Polygon.Turn.COUNTER_CLOCKWISE));
        assertThat(polygon.getTurn(a, g, f), is(Polygon.Turn.COUNTER_CLOCKWISE));
        assertThat(polygon.getTurn(f, b, a), is(Polygon.Turn.COUNTER_CLOCKWISE));
    }

}
