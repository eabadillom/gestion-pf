package mx.com.ferbo.ui;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

public class PreSalidaUI extends OrdenDeSalidas{

	private static final long serialVersionUID = 1L;
	
	private boolean salidaSelected = false;
	private String folioEntrada = null;
	
	
	public PreSalidaUI(String folioSalida, String status, Date fechaSalida, Time horaSalida, Integer partidaCve, Integer cantidad, 
			BigDecimal peso,  String codigo, String lote, Date fechaCaducidad, String sap, String pedimento, String temperatura, 
			String unidadManejo,  String codigoProducto, String nombreProducto, String nombrePlanta, 
			String nombreCamara,  Integer folioOrdenSalida, Integer productoCve, Integer unidadManejoCve) {
				super(folioSalida, status, fechaSalida, horaSalida, partidaCve,
				cantidad, peso, codigo, lote, fechaCaducidad, sap,
				pedimento, temperatura, unidadManejo, codigoProducto, nombreProducto,
				nombrePlanta, nombreCamara, folioOrdenSalida, productoCve, unidadManejoCve);
				this.salidaSelected = false;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isSalidaSelected() {
		return salidaSelected;
	}

	public void setSalidaSelected(boolean salidaSelected) {
		this.salidaSelected = salidaSelected;
	}
	public String getFolioEntrada() {
		return folioEntrada;
	}
	public void setFolioEntrada(String folioEntrada) {
		this.folioEntrada = folioEntrada;
	}
}
