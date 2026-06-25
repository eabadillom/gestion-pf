package mx.com.ferbo.ui;

import java.math.BigDecimal;

import mx.com.ferbo.model.SalidaDetalle;

public class SalidaDetalleUI extends SalidaDetalle{

	private static final long serialVersionUID = -2716299863764563927L;
	
	private Boolean selected;
	private BigDecimal temperatura;

	public Boolean getSelected() {
		return selected;
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public BigDecimal getTemperatura() {
		return temperatura;
	}
	
	public void setTemperatura(BigDecimal temperatura) {
		this.temperatura = temperatura;
	}

}
