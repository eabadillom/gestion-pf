package mx.com.ferbo.utils;

public class ToolException extends Exception {

	private static final long serialVersionUID = -8369698797249605379L;
	
	public ToolException() {
		super();
	}
	
	public ToolException(String message) {
		super(message);
	}
	
	public ToolException(Throwable cause) {
		super(cause);
	}
	
	public ToolException(String message, Throwable cause) {
		super(message, cause);
	}

}
