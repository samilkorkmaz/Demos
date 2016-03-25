package collisiondetector;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

/**
 * Detect collision (intersection) between lines and planes.<br/>
 * It uses the Apache commons.math library to solve matrix equations.
 *
 * @author skorkmaz
 */
public class CollisionDetector {

    private static final double DEFAULT_DOUBLE_TOLERANCE = 1e-15;

    /**
     * Calculate intersection of line and triangular plane.<br/>
     * Reference: https://en.wikipedia.org/wiki/Line%25E2%2580%2593plane_intersection?oldid=697480228
     */
    public static Point3D calcLineTriangleIntersect(Point3D[] lineSegment, Point3D[] triangularPlane) throws MySingularMatrixException {
        if (triangularPlane.length != 3) {
            throw new IllegalArgumentException(String.format("Not a triangular plane! Number of points (%d) should be 3!", triangularPlane.length));
        }
        Point3D intersectionPoint = new Point3D(Double.NaN, Double.NaN, Double.NaN);
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{
            {lineSegment[0].getX() - lineSegment[1].getX(), triangularPlane[1].getX() - triangularPlane[0].getX(), triangularPlane[2].getX() - triangularPlane[0].getX()},
            {lineSegment[0].getY() - lineSegment[1].getY(), triangularPlane[1].getY() - triangularPlane[0].getY(), triangularPlane[2].getY() - triangularPlane[0].getY()},
            {lineSegment[0].getZ() - lineSegment[1].getZ(), triangularPlane[1].getZ() - triangularPlane[0].getZ(), triangularPlane[2].getZ() - triangularPlane[0].getZ()}},
                false);
        try {
            DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
            RealVector constants = new ArrayRealVector(new double[]{
                lineSegment[0].getX() - triangularPlane[0].getX(),
                lineSegment[0].getY() - triangularPlane[0].getY(),
                lineSegment[0].getZ() - triangularPlane[0].getZ()}, false);
            RealVector solution = solver.solve(constants);
            double t = solution.getEntry(0);
            double u = solution.getEntry(1);
            double v = solution.getEntry(2);

            //Note below the use of gte instead of ">=" and lte instead of "<=". gte and lte methods do those checks with a tolerance value to handle values like 
            //1.0000000000000002 correctly (which is "1" for practical purposes).
            if ((gte(t, 0) && lte(t, 1)) /*intersection point is on lineSegment*/
                    && (gte(u, 0) && lte(u, 1)) && (gte(v, 0) && lte(v, 1) && lte(u + v, 1)) /*intersection point is on trianglePlane*/) {
                intersectionPoint.setX(lineSegment[0].getX() + (lineSegment[1].getX() - lineSegment[0].getX()) * t);
                intersectionPoint.setY(lineSegment[0].getY() + (lineSegment[1].getY() - lineSegment[0].getY()) * t);
                intersectionPoint.setZ(lineSegment[0].getZ() + (lineSegment[1].getZ() - lineSegment[0].getZ()) * t);
            } else {
                intersectionPoint.setX(Double.NaN);
                intersectionPoint.setY(Double.NaN);
                intersectionPoint.setZ(Double.NaN);
            }
        } catch (SingularMatrixException e) { //happens when line and plane are parallel to each other
            throw new MySingularMatrixException("Singular matrix (det(M) == 0), cannot perform inv(M)!\nM = " + coefficients + "\nSource exception: " + e);
        }
        return intersectionPoint;
    }

    /**
     * Calculate intersection of line and rectangular plane.<br/>
     * The rectangular plane is divided into two triangular planes and intersection is calculated for each of them.
     */
    public static Point3D calcLineRectIntersect(Point3D[] lineSegment, Point3D[] rectPlane) throws MySingularMatrixException {
        if (rectPlane.length != 4) {
            throw new IllegalArgumentException(String.format("Not a rectangular plane! Number of points (%d) should be 4!", rectPlane.length));
        }
        Point3D[] triangularPlane1 = {
            new Point3D(rectPlane[0].getX(), rectPlane[0].getY(), rectPlane[0].getZ()),
            new Point3D(rectPlane[1].getX(), rectPlane[1].getY(), rectPlane[1].getZ()),
            new Point3D(rectPlane[2].getX(), rectPlane[2].getY(), rectPlane[2].getZ())};

        //Check intersection with first triangular plane:
        Point3D intersectionPoint = calcLineTriangleIntersect(lineSegment, triangularPlane1);
        //If there is no intersection with first triangular plane, check intersection with second triangular plane:
        if (Double.isNaN(intersectionPoint.getX())) {
            Point3D[] triangularPlane2 = {
                new Point3D(rectPlane[2].getX(), rectPlane[2].getY(), rectPlane[2].getZ()),
                new Point3D(rectPlane[3].getX(), rectPlane[3].getY(), rectPlane[3].getZ()),
                new Point3D(rectPlane[0].getX(), rectPlane[0].getY(), rectPlane[0].getZ())};
            intersectionPoint = calcLineTriangleIntersect(lineSegment, triangularPlane2);
        }

        return intersectionPoint;
    }

    /**
     * Greater than or equal to within default tolerance.
     */
    public static boolean gte(double val1, double val2) {
        return gte(val1, val2, DEFAULT_DOUBLE_TOLERANCE);
    }

    /**
     * Greater than or equal to within specified tolerance.
     */
    public static boolean gte(double val1, double val2, double tolerance) {
        return val1 >= val2 - tolerance;
    }

    /**
     * Less than or equal to within default tolerance.
     */
    public static boolean lte(double val1, double val2) {
        return lte(val1, val2, DEFAULT_DOUBLE_TOLERANCE);
    }

    /**
     * Less than or equal to within specified tolerance.
     */
    public static boolean lte(double val1, double val2, double tolerance) {
        return val1 <= val2 + tolerance;
    }

}
