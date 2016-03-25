package collisiondetector;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests of Apache commons.math DecompositionSolver.<br/>
 *
 * @author skorkmaz
 */
public class CommonsMathDecompositionSolverTest {

    @Test
    public void testDecompositionSolver_1() {
        //Test case in DecompositionSolver documentation
        //M * X = C --> X = inv(M)*C
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{{2, 3, -2}, {-1, 7, 6}, {4, -3, -5}}, false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(new double[]{1, -2, 1}, false);
        RealVector solution = solver.solve(constants);
        //System.out.println("solution = " + solution);
        double expX = -369.863013698630e-003; //Expected values generated with Matlab
        double expY = 178.082191780822e-003;
        double expZ = -602.739726027397e-003;
        double tol = 1e-15;
        assertEquals(expX, solution.getEntry(0), tol);
        assertEquals(expY, solution.getEntry(1), tol);
        assertEquals(expZ, solution.getEntry(2), tol);
    }

    @Test
    public void testDecompositionSolver_2() {
        //Test cases generated with Matlab R2015a (uniform random, range -50..50)
        //M * X = C --> X = inv(M)*C
        double[][] mArray = {{-13.2714084868631020, -25.7396613720330300, -14.9986080999233810}, {24.4867856241672680, -37.0403024162459720, -21.2915386853823950}, {39.2267188231107440, -27.4932117872086720, 42.7488007179427850},
        {33.8405769673030220, 49.9329479025719390, -28.6339414879200900}, {-33.2439069788729140, -14.4592848732118780, -10.2160875897294690}, {0.2200615409472704, -45.2922290451166060, -16.6331819619705210},
        {46.2113795722863330, -49.4165675427629130, -26.7018466586351870}, {-6.2026822367201930, 11.0307030069875300, 43.2468675920978570}, {44.0336654420907080, 30.1075756966119600, 26.3262775333741810},
        {-17.0958804160752460, 8.4523478528555600, -9.7445607936877536}, {-27.6538023874147500, 32.9914141021522770, 36.2057298433146140}, {-18.7613657980746460, -20.9537508452731880, 11.4739550216399590},
        {17.5861618952774280, -10.0924773273208150, -39.4931228708829280}, {-25.1050529960191970, 9.9438249091412061, 32.1442194126391880}, {-2.4214373430467404, 30.0522765803850970, 34.1086330045422560},
        {20.0824730771236800, -11.0871236430773120, 7.2971367270484251}, {24.2469756987280080, -7.0697515000162525, 34.9722054218673580}, {25.7884150620785420, 45.6344657500658710, -22.3654683701776560},
        {-41.4097305032749010, -40.9833997831036500, -6.1010333194409725}, {0.0498891603950540, 40.4666479690125130, 28.1722612917166230}, {2.1589676498054899, 38.4388938509860480, -35.1534977056591560},
        {34.3999511474936100, -1.6705432265714393, 48.7487575189352920}, {-30.3795081268748000, -16.2187956128976300, -34.0952445277605490}, {-19.6148377222347570, 29.8485829521549140, -26.3120216773945260},
        {47.2305555688457020, -9.8116601991914436, 5.9477405987275489}, {14.3698049421404760, 13.1930797977457100, 43.3591915842208950}, {36.0098876854160610, 48.5236639414830360, 22.0343206000748070},
        {-30.1263249901078790, -9.7648383064051885, 49.5381776517886290}, {-10.4633726044789840, 15.8856481072931220, 15.3163281094690400}, {49.2175302495305260, 40.1348123034511560, -39.1563572144485190}};

        double[][] cArray = {{-44.8686243183204500, 9.2666755206386568, -33.7101099782267680},
        {-27.0397468031802430, 43.6120177502068320, 18.3188784699982680},
        {32.6449539702681620, 7.3463588604866104, 29.2581664120225890},
        {49.1187788329792880, -29.6301124814377880, 32.7209084946654940},
        {-14.5493755599560900, -6.9930537384166556, 7.2239218412684636},
        {12.2323615152450390, 8.8361684613538856, 46.3468465806991500},
        {11.9815927260871010, -23.9376320605579340, -5.4343788560424784},
        {20.2236632667890320, -12.4528337302452600, 47.3704902674983970},
        {-1.5961483187658203, 13.9031092681230250, 38.7636865889604110},
        {-46.3885969204643940, 11.8091239046973730, 6.7144361897995566}};

        double[][] xArray = {{1.7027813679463510, 1.6258740720121754, -1.3053965205462621},
        {-1.0435153045724448, -0.1849660391328233, -0.6114880405449349},
        {0.6359137341055030, -0.2401010514426780, 0.3223172879181158},
        {-2.1420774915187337, -0.6588974609945644, -1.8540916433505588},
        {1.5560502419481024, -1.1814847393818524, 1.3632356993607448},
        {0.9135938253366687, 0.3471153168409664, -0.3105809215390705},
        {0.1476246279481449, -0.4006570453517039, -0.2744454638797899},
        {-5.2814822296541548, 1.8224241977940518, 4.2042267902562243},
        {0.0996594735433224, 0.6895739112509534, 0.0778016912178608},
        {-2.7322661943383464, 1.2172966203709292, -2.3580809209987827}};

        int nTestCases = mArray.length / 3;
        for (int i = 0; i < nTestCases; i++) {
            int row0 = i * 3;
            int row1 = 1 + i * 3;
            int row2 = 2 + i * 3;
            RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{
                {mArray[row0][0], mArray[row0][1], mArray[row0][2]},
                {mArray[row1][0], mArray[row1][1], mArray[row1][2]},
                {mArray[row2][0], mArray[row2][1], mArray[row2][2]}}, false);
            DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
            RealVector constants = new ArrayRealVector(new double[]{cArray[i][0], cArray[i][1], cArray[i][2]}, false);
            RealVector solution = solver.solve(constants);
            //System.out.println("solution = " + solution);
            double expX = xArray[i][0];
            double expY = xArray[i][1];
            double expZ = xArray[i][2];
            double tol = 2e-15;
            assertEquals(expX, solution.getEntry(0), tol);
            assertEquals(expY, solution.getEntry(1), tol);
            assertEquals(expZ, solution.getEntry(2), tol);
        }
    }

    @Test
    public void testDecompositionSolver_3() {
        //Singular Matrix, i.e. det(M) == 0
        //M * X = C --> X = inv(M)*C
        RealMatrix coefficients = new Array2DRowRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {4, -3, -5}}, false);
        DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
        RealVector constants = new ArrayRealVector(new double[]{1, -2, 1}, false);
        try {
            RealVector solution = solver.solve(constants);
            assertTrue(false);
        } catch (SingularMatrixException e) {
            System.out.println(e);
            assertTrue(true);
        }
    }

}
