package src.Controller;

import src.Model.Point;
import java.util.*;

public interface IGrahamMethod{

    /**
     * Знаходить елемент стеку, який наступний від найвищого.
     * @param pointStack Стек точок
     * @return Повертає знайдений об'єкт класу точка.
     */
    Point findNextToTop(Stack<Point> pointStack);


    /**
     * Обчислює квадрат відстані між двома точками.
     * @param point1
     * @param point2
     * @return Знайдене значення квадрату відстані.
     */
    double distanceSqr(Point point1, Point point2);


    /**
     * Знаходить напрямок руху за трьома заданими точками.
     * @param point1
     * @param point2
     * @param point3
     * @return Повертає ціле число: 0 - якщо напрямок колінеарний,
     * 1 - за годинниковою стрілкою, 2 - проти годинникової стрілки.
     */
    int findOrientation(Point point1, Point point2, Point point3);


    /**
     * Будує опуклу оболноку по
     * @param pointList
     * @return Стек значень точок, які належать знайденій опуклій оболонці
     */
    Stack generateConvexHull(List<Point> pointList);

}
