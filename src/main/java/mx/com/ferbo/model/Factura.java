/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "factura")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findById", query = "SELECT f FROM Factura f WHERE f.id = :id"),
    @NamedQuery(name = "Factura.findByNumero", query = "SELECT f FROM Factura f WHERE f.numero = :numero"),
    @NamedQuery(name = "Factura.findByMoneda", query = "SELECT f FROM Factura f WHERE f.moneda = :moneda"),
    @NamedQuery(name = "Factura.findByRfc", query = "SELECT f FROM Factura f WHERE f.rfc = :rfc"),
    @NamedQuery(name = "Factura.findByNombreCliente", query = "SELECT f FROM Factura f WHERE f.nombreCliente = :nombreCliente"),
    @NamedQuery(name = "Factura.findByFecha", query = "SELECT f FROM Factura f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "Factura.findByObservacion", query = "SELECT f FROM Factura f WHERE f.observacion = :observacion"),
    @NamedQuery(name = "Factura.findBySubtotal", query = "SELECT f FROM Factura f WHERE f.subtotal = :subtotal"),
    @NamedQuery(name = "Factura.findByIva", query = "SELECT f FROM Factura f WHERE f.iva = :iva"),
    @NamedQuery(name = "Factura.findByTotal", query = "SELECT f FROM Factura f WHERE f.total = :total"),
    @NamedQuery(name = "Factura.findByPais", query = "SELECT f FROM Factura f WHERE f.pais = :pais"),
    @NamedQuery(name = "Factura.findByEstado", query = "SELECT f FROM Factura f WHERE f.estado = :estado"),
    @NamedQuery(name = "Factura.findByMunicipio", query = "SELECT f FROM Factura f WHERE f.municipio = :municipio"),
    @NamedQuery(name = "Factura.findByCiudad", query = "SELECT f FROM Factura f WHERE f.ciudad = :ciudad"),
    @NamedQuery(name = "Factura.findByColonia", query = "SELECT f FROM Factura f WHERE f.colonia = :colonia"),
    @NamedQuery(name = "Factura.findByCp", query = "SELECT f FROM Factura f WHERE f.cp = :cp"),
    @NamedQuery(name = "Factura.findByCalle", query = "SELECT f FROM Factura f WHERE f.calle = :calle"),
    @NamedQuery(name = "Factura.findByNumExt", query = "SELECT f FROM Factura f WHERE f.numExt = :numExt"),
    @NamedQuery(name = "Factura.findByNumInt", query = "SELECT f FROM Factura f WHERE f.numInt = :numInt"),
    @NamedQuery(name = "Factura.findByTelefono", query = "SELECT f FROM Factura f WHERE f.telefono = :telefono"),
    @NamedQuery(name = "Factura.findByFax", query = "SELECT f FROM Factura f WHERE f.fax = :fax"),
    @NamedQuery(name = "Factura.findByPorcentajeIva", query = "SELECT f FROM Factura f WHERE f.porcentajeIva = :porcentajeIva"),
    @NamedQuery(name = "Factura.findByNumeroCliente", query = "SELECT f FROM Factura f WHERE f.numeroCliente = :numeroCliente"),
    @NamedQuery(name = "Factura.findByValorDeclarado", query = "SELECT f FROM Factura f WHERE f.valorDeclarado = :valorDeclarado"),
    @NamedQuery(name = "Factura.findByInicioServicios", query = "SELECT f FROM Factura f WHERE f.inicioServicios = :inicioServicios"),
    @NamedQuery(name = "Factura.findByFinServicios", query = "SELECT f FROM Factura f WHERE f.finServicios = :finServicios"),
    @NamedQuery(name = "Factura.findByMontoLetra", query = "SELECT f FROM Factura f WHERE f.montoLetra = :montoLetra"),
    @NamedQuery(name = "Factura.findByPlazo", query = "SELECT f FROM Factura f WHERE f.plazo = :plazo"),
    @NamedQuery(name = "Factura.findByCliente", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente"),
    @NamedQuery(name = "Factura.findByClienteStatusFactura", query = "SELECT f FROM Factura f WHERE f.cliente.cteCve = :clienteCve and f.status.id = :status"),
    @NamedQuery(name = "Factura.findByRetencion", query = "SELECT f FROM Factura f WHERE f.retencion = :retencion"),
    @NamedQuery(name = "Factura.findByPeriodo", query = "SELECT f FROM Factura f WHERE f.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY f.fecha"),
    @NamedQuery(name = "Factura.findByClientePeriodo", query = "SELECT f FROM Factura f WHERE f.cliente = :cliente AND f.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY f.fecha"),
    @NamedQuery(name = "Factura.findByNomSerie", query = "SELECT f FROM Factura f WHERE f.nomSerie = :nomSerie"),
    @NamedQuery(name = "Factura.findBySerieNumero", query = "SELECT f FROM Factura f WHERE f.numero = :numero AND f.nomSerie = :serie"),
    @NamedQuery(name = "Factura.findByPlanta", query = "SELECT f FROM Factura f WHERE f.planta.plantaCve = :plantaCve")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "moneda")
    private String moneda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "rfc")
    private String rfc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "observacion")
    private String observacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iva")
    private BigDecimal iva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private BigDecimal total;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "pais")
    private String pais;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 30)
    @Column(name = "municipio")
    private String municipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 30)
    @Column(name = "ciudad")
    private String ciudad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 30)
    @Column(name = "colonia")
    private String colonia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cp")
    private String cp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "num_ext")
    private String numExt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "num_int")
    private String numInt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 10)
    @Column(name = "telefono")
    private String telefono;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 10)
    @Column(name = "fax")
    private String fax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje_iva")
    private BigDecimal porcentajeIva;
    @Size(max = 30)
    @Column(name = "numero_cliente")
    private String numeroCliente;
    @Column(name = "valor_declarado")
    private BigDecimal valorDeclarado;
    @Column(name = "inicio_servicios")
    @Temporal(TemporalType.DATE)
    private Date inicioServicios;
    @Column(name = "fin_servicios")
    @Temporal(TemporalType.DATE)
    private Date finServicios;
    @Size(max = 255)
    @Column(name = "monto_letra")
    private String montoLetra;
    @JoinColumn(name = "tipo_facturacion", referencedColumnName = "ID")
    @ManyToOne
    private TipoFacturacion tipoFacturacion;
    @JoinColumn(name = "planta", referencedColumnName = "PLANTA_CVE")
    @ManyToOne
    private Planta planta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plazo")
    private int plazo;
    @Column(name = "retencion")
    private BigDecimal retencion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "nom_serie")
    private String nomSerie;
    @JoinColumn(name = "cliente", referencedColumnName = "CTE_CVE")
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "status", referencedColumnName = "id")
    @ManyToOne
    private StatusFactura status;
    @Size(max = 5)
    @Column(name = "metodo_pago")
    private String metodoPago;
    @Size(max = 1)
    @Column(name = "tp_persona")
    private String tipoPersona;    
    @Size(max = 5)
    @Column(name = "cd_regimen")
    private String cdRegimen;
    @Size(max = 5)
    @Column(name = "cd_uso_cfdi")
    private String cdUsoCfdi;
    @Size(max = 50)
    @Column(name = "uuid")
    private String uuid;
    @Size(max = 80)
    @Column(name = "emi_nombre")
    private String emisorNombre;
    @Size(max = 14)
    @Column(name = "emi_rfc")
    private String emisorRFC;
    @Size(max = 5)
    @Column(name = "emi_cd_regimen")
    private String emisorCdRegimen;
    
//    @OneToOne(mappedBy = "factura", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    private CancelaFactura cancelaFactura;
    
    @OneToMany(mappedBy = "facturaId")
    private List<ChequeDevuelto> chequeDevueltoList;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "factura")
    private List<Pago> pagoList;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "factura")
    private List<FacturaMedioPago> facturaMedioPagoList;
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "factura")
    private List<ServicioFactura> servicioFacturaList;
    
    @OneToMany(cascade = {CascadeType.PERSIST},mappedBy = "factura")//MODIFIQUE 1 JUNIO
    private List<ConstanciaFacturaDs> constanciaFacturaDsList;
    
    @OneToMany(cascade = {CascadeType.PERSIST},mappedBy = "factura")//MODIFIQUE 1 JUNIO
    private List<ConstanciaFactura> constanciaFacturaList;
    
    @OneToMany(mappedBy = "factura")
    private List<ConstanciaFacturaCmp> constanciaFacturaCmpList;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "notaPorFacturaPK.factura")
    private List<NotaPorFactura> notaFacturaList;

    public Factura() {
    }

    public Factura(Integer id) {
        this.id = id;
    }

    
    public Factura(Integer id, String numero,String moneda, String rfc, String nombreCliente, Date fecha, String observacion, BigDecimal subtotal, BigDecimal iva, BigDecimal total, String pais, String estado,
    		String municipio, String ciudad, String colonia, String cp,String calle, String numExt,String numInt, String telefono,String fax, BigDecimal porcentajeIva, String numeroCliente, BigDecimal valorDeclarado, 
    		Date inicioServicios, Date finServicios, String montoLetra,TipoFacturacion tipoFacturacion, Planta planta, int plazo, BigDecimal retencion,String nomSerie, Cliente cliente, 
    		StatusFactura status,	String metodoPago, String tipoPersona, String cdRegimen, String cdUsoCfdi, String uuid, String emisorNombre, String emisorRFC,String emisorCdRegimen) {
		this.id = id;
		this.numero = numero;
		this.moneda = moneda;
		this.rfc = rfc;
		this.nombreCliente = nombreCliente;
		this.fecha = fecha;
		this.observacion = observacion;
		this.subtotal = subtotal;
		this.iva = iva;
		this.total = total;
		this.pais = pais;
		this.estado = estado;
		this.municipio = municipio;
		this.ciudad = ciudad;
		this.colonia = colonia;
		this.cp = cp;
		this.calle = calle;
		this.numExt = numExt;
		this.numInt = numInt;
		this.telefono = telefono;
		this.fax = fax;
		this.porcentajeIva = porcentajeIva;
		this.numeroCliente = numeroCliente;
		this.valorDeclarado = valorDeclarado;
		this.inicioServicios = inicioServicios;
		this.finServicios = finServicios;
		this.montoLetra = montoLetra;
		this.tipoFacturacion = tipoFacturacion;
		this.planta = planta;
		this.plazo = plazo;
		this.retencion = retencion;
		this.nomSerie = nomSerie;
		this.cliente = cliente;
		this.status = status;
		this.metodoPago = metodoPago;
		this.tipoPersona = tipoPersona;
		this.cdRegimen = cdRegimen;
		this.cdUsoCfdi = cdUsoCfdi;
		this.uuid = uuid;
		this.emisorNombre = emisorNombre;
		this.emisorRFC = emisorRFC;
		this.emisorCdRegimen = emisorCdRegimen;

	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public BigDecimal getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(BigDecimal valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }

    public Date getInicioServicios() {
        return inicioServicios;
    }

    public void setInicioServicios(Date inicioServicios) {
        this.inicioServicios = inicioServicios;
    }

    public Date getFinServicios() {
        return finServicios;
    }

    public void setFinServicios(Date finServicios) {
        this.finServicios = finServicios;
    }

    public String getMontoLetra() {
        return montoLetra;
    }

    public void setMontoLetra(String montoLetra) {
        this.montoLetra = montoLetra;
    }

    public TipoFacturacion getTipoFacturacion() {
        return tipoFacturacion;
    }

    public void setTipoFacturacion(TipoFacturacion tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public BigDecimal getRetencion() {
        return retencion;
    }

    public void setRetencion(BigDecimal retencion) {
        this.retencion = retencion;
    }

    public String getNomSerie() {
        return nomSerie;
    }

    public void setNomSerie(String nomSerie) {
        this.nomSerie = nomSerie;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatusFactura getStatus() {
        return status;
    }

    public void setStatus(StatusFactura status) {
        this.status = status;
    }

    public List<ChequeDevuelto> getChequeDevueltoList() {
        return chequeDevueltoList;
    }

    public void setChequeDevueltoList(List<ChequeDevuelto> chequeDevueltoList) {
        this.chequeDevueltoList = chequeDevueltoList;
    }

    public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getCdRegimen() {
		return cdRegimen;
	}

	public void setCdRegimen(String cdRegimen) {
		this.cdRegimen = cdRegimen;
	}

	public String getCdUsoCfdi() {
		return cdUsoCfdi;
	}

	public void setCdUsoCfdi(String cdUsoCfdi) {
		this.cdUsoCfdi = cdUsoCfdi;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEmisorNombre() {
		return emisorNombre;
	}

	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}

	public String getEmisorRFC() {
		return emisorRFC;
	}

	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}

	public String getEmisorCdRegimen() {
		return emisorCdRegimen;
	}

	public void setEmisorCdRegimen(String emisorCdRegimen) {
		this.emisorCdRegimen = emisorCdRegimen;
	}

	public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    public List<FacturaMedioPago> getFacturaMedioPagoList() {
        return facturaMedioPagoList;
    }

    public void setFacturaMedioPagoList(List<FacturaMedioPago> facturaMedioPagoList) {
        this.facturaMedioPagoList = facturaMedioPagoList;
    }

    public List<ServicioFactura> getServicioFacturaList() {
        return servicioFacturaList;
    }

    public void setServicioFacturaList(List<ServicioFactura> servicioFacturaList) {
        this.servicioFacturaList = servicioFacturaList;
    }

    public List<ConstanciaFacturaDs> getConstanciaFacturaDsList() {
        return constanciaFacturaDsList;
    }

    public void setConstanciaFacturaDsList(List<ConstanciaFacturaDs> constanciaFacturaDsList) {
        this.constanciaFacturaDsList = constanciaFacturaDsList;
    }

    public List<ConstanciaFactura> getConstanciaFacturaList() {
        return constanciaFacturaList;
    }

    public void setConstanciaFacturaList(List<ConstanciaFactura> constanciaFacturaList) {
        this.constanciaFacturaList = constanciaFacturaList;
    }

    public List<ConstanciaFacturaCmp> getConstanciaFacturaCmpList() {
        return constanciaFacturaCmpList;
    }

    public void setConstanciaFacturaCmpList(List<ConstanciaFacturaCmp> constanciaFacturaCmpList) {
        this.constanciaFacturaCmpList = constanciaFacturaCmpList;
    }

    public List<NotaPorFactura> getNotaFacturaList() {
		return notaFacturaList;
	}

	public void setNotaFacturaList(List<NotaPorFactura> notaFacturaList) {
		this.notaFacturaList = notaFacturaList;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Factura[ id=" + id + " ]";
    }

//	public CancelaFactura getCancelaFactura() {
//		return cancelaFactura;
//	}
//
//	public void setCancelaFactura(CancelaFactura cancelaFactura) {
//		this.cancelaFactura = cancelaFactura;
//	}
}