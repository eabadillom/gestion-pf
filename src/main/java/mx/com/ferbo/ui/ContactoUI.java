package mx.com.ferbo.ui;

import mx.com.ferbo.model.Contacto;

public class ContactoUI extends Contacto {

	private static final long serialVersionUID = -7845445501936133157L;
	
	private String cadena;
	
	public ContactoUI(Contacto contacto) {
		this.setId(contacto.getId());
		this.setNombre(contacto.getNombre());
		this.setApellido1(contacto.getApellido1());
		this.setApellido2(contacto.getApellido2());
	}

	public String getCadena() {
		return cadena;
	}

	public void setCadena(String cadena) {
		this.cadena = cadena;
	}

}
