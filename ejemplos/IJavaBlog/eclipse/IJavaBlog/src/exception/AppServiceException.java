package exception;

public class AppServiceException extends AppException {
	private static final long serialVersionUID = 3283403408784107787L;

	public AppServiceException() {
	}

	public AppServiceException(Throwable tw) {
		super(tw);
	}
}