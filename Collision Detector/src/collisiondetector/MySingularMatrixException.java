package collisiondetector;

/**
 * This exception is thrown when a matrix is singular, i.e. det(M) == 0.<br/>
 *
 * @author skorkmaz
 */
public class MySingularMatrixException extends Exception {

    public MySingularMatrixException(String message) {
        super(message);
    }

}
