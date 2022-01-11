package il.ac.hit.costmanager;

/**
 * CostManagerException is the exception thrown in the model
 * CostManagerException extends from Exception
 */
public class CostManagerException extends Exception{

    /**
     * CostManagerException constructor which receives a message to pass to the extended class Exception
     * @param message the message
     */
    public CostManagerException(String message) {
        super(message);
    }

    /**
     * CostManagerException constructor which receives a message to pass to the extended class Exception and the cause
     * @param message the message
     * @param cause the cause
     */
    public CostManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
