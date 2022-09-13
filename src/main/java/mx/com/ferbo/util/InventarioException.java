package mx.com.ferbo.util;

public class InventarioException extends Exception {

	private static final long serialVersionUID = 6243100841230323823L;
	
	public InventarioException() {
		super();
	}
	
	public InventarioException(String message) {
		super(message);
	}
	
	public InventarioException(Throwable cause) {
		super(cause);
	}
	
	public InventarioException(String message, Throwable cause) {
		super(message, cause);
	}
}
