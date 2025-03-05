package in_.apcfss.exception;

public class InputValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InputValidationException(String msg) {
        super(msg);
    }

    public InputValidationException() {
        super();
    }

    public InputValidationException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
