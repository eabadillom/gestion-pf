package mx.com.ferbo.model;

public class Inventario {
	String folio;
	String Producto;
	String Cantidad;
	String Unidad_Manejo;
	String Planta_Origen;
	String Posicion_Origen;
	String Caducidad;
	String codigo;
	String lote;
	String SAP;
	String Planta;
	String Camara;
	String posicion;
	
	public Inventario() {
		
	}
	public Inventario(String folio, String producto, String cantidad, String unidad_Manejo, String planta_Origen,
			String posicion_Origen, String caducidad, String codigo, String lote, String sAP, String planta,
			String camara, String posicion) {
		this.folio = folio;
		this.Producto = producto;
		this.Cantidad = cantidad;
		this.Unidad_Manejo = unidad_Manejo;
		this.Planta_Origen = planta_Origen;
		this.Posicion_Origen = posicion_Origen;
		this.Caducidad = caducidad;
		this.codigo = codigo;
		this.lote = lote;
		this.SAP = sAP;
		this.Planta = planta;
		this.Camara = camara;
		this.posicion = posicion;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getProducto() {
		return Producto;
	}
	public void setProducto(String producto) {
		Producto = producto;
	}
	public String getCantidad() {
		return Cantidad;
	}
	public void setCantidad(String cantidad) {
		Cantidad = cantidad;
	}
	public String getUnidad_Manejo() {
		return Unidad_Manejo;
	}
	public void setUnidad_Manejo(String unidad_Manejo) {
		Unidad_Manejo = unidad_Manejo;
	}
	public String getPlanta_Origen() {
		return Planta_Origen;
	}
	public void setPlanta_Origen(String planta_Origen) {
		Planta_Origen = planta_Origen;
	}
	public String getPosicion_Origen() {
		return Posicion_Origen;
	}
	public void setPosicion_Origen(String posicion_Origen) {
		Posicion_Origen = posicion_Origen;
	}
	public String getCaducidad() {
		return Caducidad;
	}
	public void setCaducidad(String caducidad) {
		Caducidad = caducidad;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getSAP() {
		return SAP;
	}
	public void setSAP(String sAP) {
		SAP = sAP;
	}
	public String getPlanta() {
		return Planta;
	}
	public void setPlanta(String planta) {
		Planta = planta;
	}
	public String getCamara() {
		return Camara;
	}
	public void setCamara(String camara) {
		Camara = camara;
	}
	public String getPosicion() {
		return posicion;
	}
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}
	@Override
	public String toString() {
		return "Inventario [folio=" + folio + ", Producto=" + Producto + ", Cantidad=" + Cantidad + ", Unidad_Manejo="
				+ Unidad_Manejo + ", Planta_Origen=" + Planta_Origen + ", Posicion_Origen=" + Posicion_Origen
				+ ", Caducidad=" + Caducidad + ", codigo=" + codigo + ", lote=" + lote + ", SAP=" + SAP + ", Planta="
				+ Planta + ", Camara=" + Camara + ", posicion=" + posicion + "]";
	}

}
