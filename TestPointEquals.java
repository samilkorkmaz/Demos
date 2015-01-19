package puzzle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The equals() method overriden in Point class does not check object reference but x, y values. This can create problems when removing points from a list because the
 * ArrayList.remove() method removes the first object in list that "equals" the input object. In the case of a Point object, that means equal x, y coordinates. For
 * example, when you have two Point objects with same x, y in list, there is no way to remove the second object from list by using remove(Object). What you can do is to
 * create a new class that extends Point and overrides equals() so that it checks object reference.
 *
 * @author Samil Korkmaz
 * @date January 2015
 * @copyright Public Domain
 */
public class TestPointEquals {
    
    public static class MyPoint extends Point {

        public MyPoint(int x, int y) {
            super(x, y);
        }
        
        @Override
        public boolean equals(Object o) {
            return this == o; //use object reference comparison instead of x, y values.
        }
    }
    
    public static void demoWithPoint() {
        System.out.println("Demo with Point class:");
        System.out.println("----------------------");
        List<Point> pointList = new ArrayList<>();
        Point point1 = new Point(3, 5);
        Point point2 = new Point(3, 5);
        pointList.add(point1);
        pointList.add(point2);

        System.out.println("point1 == point2 : " + (point1 == point2)); //returns false
        System.out.println("point1.equals(point2) : " + point1.equals(point2)); //returns true since x, y coordinates are the same
        System.out.println("index of point2 = " + pointList.indexOf(point2)); //returns 0 because indexOf() uses Point.equals() to return the index of the first occurrence of point2 in pointList.

        Point selectedPoint = pointList.get(1); //choose second point in list
        pointList.remove(selectedPoint); //remove() uses Point.equals() to  remove the first occurrence of selectedPoint from pointList which in this case means that the first point in list will be removed. Second point will remain
        pointList.add(pointList.size(), selectedPoint); //add second point to the end of list which now means that we have two copies of the second point in list.

        selectedPoint.setLocation(10, 20);
        for (int i = 0; i < pointList.size(); i++) {
            System.out.format("p(%d).x = %d, p(%d).y = %d\n", i, pointList.get(i).x, i, pointList.get(i).y); //both list items will have same x, y coordinates because the list contains two copies of the same point.
        }
    }
    
    public static void demoWithMyPoint() {
        System.out.println("\nDemo with MyPoint class:");
        System.out.println("----------------------");
        List<MyPoint> pointList = new ArrayList<>();
        MyPoint point1 = new MyPoint(3, 5);
        MyPoint point2 = new MyPoint(3, 5);
        pointList.add(point1);
        pointList.add(point2);

        System.out.println("point1 == point2 : " + (point1 == point2)); //returns false
        System.out.println("point1.equals(point2) : " + point1.equals(point2)); //returns false since we have overriden the equals method to check object reference.
        System.out.println("index of point2 = " + pointList.indexOf(point2)); //returns 1

        MyPoint selectedPoint = pointList.get(1); //choose second point in list
        pointList.remove(selectedPoint); //remove() uses MyPoint.equals() to remove the object wich has the same reference as selectedPoint from pointList. This will leave the first point in list.
        pointList.add(pointList.size(), selectedPoint); //add second point to the end of list.

        selectedPoint.setLocation(10, 20);
        for (int i = 0; i < pointList.size(); i++) {
            System.out.format("p(%d).x = %d, p(%d).y = %d\n", i, pointList.get(i).x, i, pointList.get(i).y);
        }
    }

    public static void main(String[] args) {
        demoWithPoint();
        demoWithMyPoint();
    }
}
