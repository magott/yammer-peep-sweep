package no.magott.yammer.peep.sweep.employment;


public class EmploymentServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmploymentServiceException(String message, Exception cause) {
		super(message, cause);
	}

	public EmploymentServiceException(String message) {
		super(message);
	}

}
