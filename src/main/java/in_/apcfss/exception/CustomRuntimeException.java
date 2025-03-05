package in_.apcfss.exception;

public class CustomRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public CustomRuntimeException(String msg) {
    super(msg);
  }

  public CustomRuntimeException() {
    super();
  }

  public CustomRuntimeException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

}
