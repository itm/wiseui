package eu.wisebed.wiseui.shared.exception;

public class WisemlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4313625292150681976L;

    private String stacktraceString;

	public WisemlException() {
		
	}
	
	public WisemlException(final Throwable throwable) {
		super(throwable);
	}
	
	public WisemlException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

    public WisemlException(final String message, final Throwable throwable, final String stacktraceString) {
        super(message, throwable);
        this.stacktraceString = stacktraceString;
    }

    public String getStacktraceString() {
        return stacktraceString;
    }
}
