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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "constancia_de_deposito")
@NamedQueries({
    @NamedQuery(name = "ConstanciaDeDeposito.findAll", query = "SELECT c FROM ConstanciaDeDeposito c"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByFolio", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.folio = :folio"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByCteCve", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.cteCve.cteCve = :cteCve"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByCteCveAndPlanta", query = "SELECT DISTINCT (c) FROM ConstanciaDeDeposito c "
    		+ "INNER JOIN c.partidaList p "
    		+ "WHERE c.cteCve.cteCve = :cteCve and p.camaraCve.plantaCve.plantaCve = :plantaCve"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByFechaIngreso", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByNombreTransportista", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.nombreTransportista = :nombreTransportista"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByPlacasTransporte", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.placasTransporte = :placasTransporte"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByObservaciones", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByFolioCliente", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.folioCliente = :folioCliente"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByValorDeclarado", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.valorDeclarado = :valorDeclarado"),
    @NamedQuery(name = "ConstanciaDeDeposito.findByFolioClientePeriodo", query = "SELECT c FROM ConstanciaDeDeposito c WHERE (c.fechaIngreso BETWEEN :fechaInicio AND :fechaFin) AND ((c.folioCliente = :folioCliente OR :folioCliente IS NULL) OR (c.cteCve.cteCve = :idCliente OR :idCliente IS NULL)\t) "),
    @NamedQuery(name = "ConstanciaDeDeposito.findByTemperatura", query = "SELECT c FROM ConstanciaDeDeposito c WHERE c.temperatura = :temperatura")})
public class ConstanciaDeDeposito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FOLIO")
    private Integer folio;
    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Size(max = 100)
    @Column(name = "NOMBRE_TRANSPORTISTA")
    private String nombreTransportista;
    @Size(max = 10)
    @Column(name = "PLACAS_TRANSPORTE")
    private String placasTransporte;
    @Size(max = 200)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "folio_cliente")
    private String folioCliente;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_declarado")
    private BigDecimal valorDeclarado;
    @Size(max = 50)
    @Column(name = "temperatura")
    private String temperatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folio")
    private List<Partida> partidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folio")
    private List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList;
    @JoinColumn(name = "CTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cteCve;
    @JoinColumn(name = "aviso_cve", referencedColumnName = "aviso_cve")
    @ManyToOne
    private Aviso avisoCve;
    @JoinColumn(name = "status", referencedColumnName = "edo_cve")
    @ManyToOne
    private EstadoConstancia status;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "folio")
    private List<ConstanciaFactura> constanciaFacturaList;

    public ConstanciaDeDeposito() {
    }

    public ConstanciaDeDeposito(Integer folio) {
        this.folio = folio;
    }

    public ConstanciaDeDeposito(Integer folio, String folioCliente) {
        this.folio = folio;
        this.folioCliente = folioCliente;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getPlacasTransporte() {
        return placasTransporte;
    }

    public void setPlacasTransporte(String placasTransporte) {
        this.placasTransporte = placasTransporte;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }

    public BigDecimal getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(BigDecimal valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {//modificar para mappedby 
        this.partidaList = partidaList;
        for(Partida p:partidaList) {
        	p.setFolio(this);
        }
    }

    public List<ConstanciaDepositoDetalle> getConstanciaDepositoDetalleList() {
        return constanciaDepositoDetalleList;
    }

    public void setConstanciaDepositoDetalleList(List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList) {
        this.constanciaDepositoDetalleList = constanciaDepositoDetalleList;
        for(ConstanciaDepositoDetalle c: constanciaDepositoDetalleList) {
        	c.setFolio(this);
        }
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    public Aviso getAvisoCve() {
        return avisoCve;
    }

    public void setAvisoCve(Aviso avisoCve) {
        this.avisoCve = avisoCve;
    }

    public EstadoConstancia getStatus() {
        return status;
    }

    public void setStatus(EstadoConstancia status) {
        this.status = status;
    }
    
    public List<ConstanciaFactura> getConstanciaFacturaList() {
		return constanciaFacturaList;
	}

	public void setConstanciaFacturaList(List<ConstanciaFactura> constanciaFacturaList) {
		this.constanciaFacturaList = constanciaFacturaList;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (folio != null ? folio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConstanciaDeDeposito)) {
            return false;
        }
        ConstanciaDeDeposito other = (ConstanciaDeDeposito) object;
        if ((this.folio == null && other.folio != null) || (this.folio != null && !this.folio.equals(other.folio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaDeDeposito[ folio=" + folio + " ]";
    }
    
}
