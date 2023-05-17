package mx.com.ferbo.util;

import java.util.regex.Pattern;

public class ClienteUtil {
	
	public static boolean validarRFC(String tipoPersona, String rfc) {
		boolean isValid = false;
		if("M".equals(tipoPersona)) {
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
