package collisiondetector;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests of CollisionDetector.
 *
 * @author skorkmaz
 */
public class CollisionDetectorTest {

    private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-15;

    @Test
    public void testCalcLineTriangleIntersect_1() throws MySingularMatrixException {
        Point3D[] lineSegment = {new Point3D(-2, 1, 2), new Point3D(1, 0, 0)};
        Point3D[] trianglePlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0)};
        Point3D intersect = CollisionDetector.calcLineTriangleIntersect(lineSegment, trianglePlane);
        System.out.println("intersect = " + intersect);
        double expX = 1;
        double expY = 0;
        double expZ = 0;
        assertEquals(expX, intersect.getX(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expY, intersect.getY(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expZ, intersect.getZ(), DEFAULT_DOUBLE_TOLERANCE);
    }

    @Test
    public void testCalcLineTriangleIntersect_2() throws MySingularMatrixException {
        //No intersection.
        Point3D[] lineSegment = {new Point3D(-2, 1, 2), new Point3D(1.01, 0, 0)};
        Point3D[] trianglePlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0)};
        Point3D intersect = CollisionDetector.calcLineTriangleIntersect(lineSegment, trianglePlane);
        System.out.println("intersect = " + intersect);
        double expX = Double.NaN;
        double expY = Double.NaN;
        double expZ = Double.NaN;
        assertEquals(expX, intersect.getX(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expY, intersect.getY(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expZ, intersect.getZ(), DEFAULT_DOUBLE_TOLERANCE);
    }

    @Test
    public void testCalcLineTriangleIntersect_3() throws MySingularMatrixException {
        //No intersection.
        Point3D[] lineSegment = {new Point3D(-2, 1, 2), new Point3D(0.5, 0.75, 0)};
        Point3D[] trianglePlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0)};
        Point3D intersect = CollisionDetector.calcLineTriangleIntersect(lineSegment, trianglePlane);
        System.out.println("intersect = " + intersect);
        double expX = Double.NaN;
        double expY = Double.NaN;
        double expZ = Double.NaN;
        assertEquals(expX, intersect.getX(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expY, intersect.getY(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expZ, intersect.getZ(), DEFAULT_DOUBLE_TOLERANCE);
    }

    @Test
    public void testCalcLineTriangleIntersect_4() throws MySingularMatrixException {
        //Line and triangle plane parallel, resulting in singular matrix exception.
        Point3D[] lineSegment = {new Point3D(0, 0, 1), new Point3D(1, 0, 1)};
        Point3D[] trianglePlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0)};
        try {
            Point3D intersect = CollisionDetector.calcLineTriangleIntersect(lineSegment, trianglePlane);
            assertTrue(false);
        } catch (MySingularMatrixException e) {
            System.out.println(e);
            assertTrue(true);
        }
    }

    @Test
    public void testCalcLineRectIntersect_1() throws MySingularMatrixException {
        Point3D[] lineSegment = {new Point3D(-2, 1, 2), new Point3D(1, 0, 0)};
        Point3D[] rectPlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0), new Point3D(0, 1, 0)};
        Point3D intersect = CollisionDetector.calcLineRectIntersect(lineSegment, rectPlane);
        System.out.println("intersect = " + intersect);
        double expX = 1;
        double expY = 0;
        double expZ = 0;
        assertEquals(expX, intersect.getX(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expY, intersect.getY(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expZ, intersect.getZ(), DEFAULT_DOUBLE_TOLERANCE);
    }

    @Test
    public void testCalcLineRectIntersect_2() throws MySingularMatrixException {
        //No intersection.
        Point3D[] lineSegment = {new Point3D(-2, 1, 2), new Point3D(1.01, 0, 0)};
        Point3D[] rectPlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0), new Point3D(0, 1, 0)};
        Point3D intersect = CollisionDetector.calcLineRectIntersect(lineSegment, rectPlane);
        System.out.println("intersect = " + intersect);
        double expX = Double.NaN;
        double expY = Double.NaN;
        double expZ = Double.NaN;
        assertEquals(expX, intersect.getX(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expY, intersect.getY(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expZ, intersect.getZ(), DEFAULT_DOUBLE_TOLERANCE);
    }

    @Test
    public void testCalcLineRectIntersect_4() throws MySingularMatrixException {
        Point3D[] lineSegment = {new Point3D(-2, 1, 2), new Point3D(0.5, 0.75, 0)};
        Point3D[] rectPlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0), new Point3D(0, 1, 0)};
        Point3D intersect = CollisionDetector.calcLineRectIntersect(lineSegment, rectPlane);
        System.out.println("intersect = " + intersect);
        double expX = 0.5;
        double expY = 0.75;
        double expZ = 0;
        assertEquals(expX, intersect.getX(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expY, intersect.getY(), DEFAULT_DOUBLE_TOLERANCE);
        assertEquals(expZ, intersect.getZ(), DEFAULT_DOUBLE_TOLERANCE);
    }

    @Test
    public void testCalcLineRectIntersect_5() throws MySingularMatrixException {
        //Line and rect plane parallel, resulting in singular matrix exception.
        Point3D[] lineSegment = {new Point3D(0, 0, 1), new Point3D(1, 0, 1)};
        Point3D[] rectPlane = {new Point3D(0, 0, 0), new Point3D(1, 0, 0), new Point3D(1, 1, 0), new Point3D(0, 1, 0)};
        try {
            Point3D intersect = CollisionDetector.calcLineRectIntersect(lineSegment, rectPlane);
            assertTrue(false);
        } catch (MySingularMatrixException e) {
            System.out.println(e);
            assertTrue(true);
        }
    }

}
