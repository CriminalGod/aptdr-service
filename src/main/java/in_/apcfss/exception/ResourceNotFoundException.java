package in_.apcfss.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
