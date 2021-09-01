package ch.taskmanager.model;



/**
 * Exception class to handle exceptions
 * from TaskManager dealing with Process operations like Add, Kill etc
 */
public class ProcessManagerException extends RuntimeException {


    private static final long serialVersionUID = 2021989461536218850L;

    private String errorCode;

    public ProcessManagerException() {

    }

    public ProcessManagerException(String message) {
        super(message);
    }

    public ProcessManagerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProcessManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessManagerException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
