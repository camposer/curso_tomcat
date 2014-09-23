package exception;

public abstract class AppException extends RuntimeException {
	private static final long serialVersionUID = -1885522722125906906L;

	public AppException() {
		
	}
	
	public AppException(Throwable tw) {
		super(tw);
	}
}