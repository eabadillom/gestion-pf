package mx.com.ferbo.util;

import java.util.regex.Pattern;

public class ClienteUtil {
	
	public static final String RFC_GENERICO_NACIONAL = "XAXX010101000";
	public static final String RFC_GENERICO_EXTRANJERO = "XEXX010101000";
	
	public static boolean validarRFC(String tipoPersona, String rfc) {
		boolean isValid = false;
		
		if(RFC_GENERICO_NACIONAL.equalsIgnoreCase(rfc)) {
			isValid = true;
		} else if (RFC_GENERICO_EXTRANJERO.equalsIgnoreCase(rfc)) {
			isValid = true;
		} else if("M".equals(tipoPersona)) {
			isValid = validarRFCMoral(rfc);
		} else if ("F".equals(tipoPersona)) {
			isValid = validarRFCFisica(rfc);
		} 
		return isValid;
	}
	
	private static boolean validarRFCFisica (String rfc) {
		boolean isValid = false;
		isValid = Pattern.matches("[A-Z][A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9][A-Z0-9][A-Z0-9][A-Z0-9]", rfc);
		return isValid;
	}
	
	private static boolean validarRFCMoral(String rfc) {
		boolean isValid = false;
		isValid = Pattern.matches("[A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9][0-9][0-9][A-Z0-9][A-Z0-9][A-Z0-9]", rfc);
		return isValid;
	}
	

}
