package src.Model;
import java.util.*;

public interface IPolygon {

    /**
     * Повертає true, якщо всі точки в <code>points</code> є колінеарними.
     * @param points Список точок.
     * @return true, якщо всі точки в <code>points</code> є колінеарними.
     * @return false, якщо всі точки в <code>points</code> не є колінеарними.
     */
    boolean areAllCollinear(List<Point> points);


    /**
     * Повертає опуклу множину, створену з списку точок <code>points</code>.
     * @param points Список точок.
     * @return Об'єкт класу ConvexHull - створену опуклу множину.
     *          Перший і останній елемент створеної опуклої множини є однаковим.
     * @throws Exception, якщо всі точки зі списку є колінеарні, або точок є менше ніж 3.
     */
    ConvexHull getConvexHull(List<Point> points) throws Exception;


    /**
     * Повертає точку, у якої координата y - найменша.
     * Якщо таких точок буде декілька, повертає одну з них.
     * @param points Список точок.
     * @return точку, у якої координата y - найменша.
     *         Якщо таких точок буде декілька, повертає одну з них.
     */
    Point getLowestPoint(List<Point> points);


    /**
     * Повертає посортовану множину точок з списку точок <code>points</code>.
     * Множина точок посортована у зростанні по куту, який вони утворюють з віссю x.
     * @param points Список точок.
     * @return посортовану множину точок з списку точок <code>points</code>.
     * @see Polygon#getLowestPoint(List)
     */
    Set<Point> getSortedPointSet(List<Point> points);


    /**
     * Повертає напрямок між 3 точками <code>a</code>, <code>b</code> та <code>c</code>.
     * Векторний добуток <tt>C</tt> між 3 векторами обчислюється:
     *
     * <tt>(b.x-a.x * c.y-a.y) - (b.y-a.y * c.x-a.x)</tt>
     *
     * Якщо  <tt>C</tt> < 0, напрямок є за годинниковою стрілкою,
     * якщо  <tt>C</tt> > 0, напрямок є проти годинникової стрілки,
     * інакше - вони колінеарні.
     * @param a початкова точка.
     * @param b друга точка.
     * @param c остання точка.
     * @return Polygon#Turn, сформований трьома послідовними точками.
     */
    Polygon.Turn getTurn(Point a, Point b, Point c);

    /**
     *Перевіряє, чи точка лежить всередині оболонки
     * @param point Точка, яку будемо перевіряти
     * @param convexHull Опукла оболонка
     * @return true, якщо точка лежить всередині опуклої оболонки,
     *          інакше - false
     */
    boolean isInside(Point point, ConvexHull convexHull);


}
