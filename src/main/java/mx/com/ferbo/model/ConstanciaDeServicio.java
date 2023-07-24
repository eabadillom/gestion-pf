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
import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "CONSTANCIA_DE_SERVICIO")
@NamedQueries({
    @NamedQuery(name = "ConstanciaDeServicio.findAll", query = "SELECT c FROM ConstanciaDeServicio c"),
    @NamedQuery(name = "ConstanciaDeServicio.findByFolio", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.folio = :folio"),
    @NamedQuery(name = "ConstanciaDeServicio.findByFecha", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ConstanciaDeServicio.findByNombreTransportista", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.nombreTransportista = :nombreTransportista"),
    @NamedQuery(name = "ConstanciaDeServicio.findByPlacasTransporte", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.placasTransporte = :placasTransporte"),
    @NamedQuery(name = "ConstanciaDeServicio.findByObservaciones", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "ConstanciaDeServicio.findByFolioCliente", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.folioCliente = :folioCliente"),
    @NamedQuery(name = "ConstanciaDeServicio.findByValorDeclarado", query = "SELECT c FROM ConstanciaDeServicio c WHERE c.valorDeclarado = :valorDeclarado")})
public class ConstanciaDeServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FOLIO")
    private Integer folio;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 100)
    @Column(name = "NOMBRE_TRANSPORTISTA")
    private String nombreTransportista;
    @Size(max = 6)
    @Column(name = "PLACAS_TRANSPORTE")
    private String placasTransporte;
    @Size(max = 200)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "FOLIO_CLIENTE")
    private String folioCliente;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALOR_DECLARADO")
    private BigDecimal valorDeclarado;
    @OneToMany(mappedBy = "folio")
    private List<PartidaServicio> partidaServicioList;
    @JoinColumn(name = "CTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cteCve;
    @JoinColumn(name = "STATUS", referencedColumnName = "edo_cve")
    @ManyToOne
    private EstadoConstancia status;
    @OneToMany(mappedBy = "folio")
    private List<ConstanciaServicioDetalle> constanciaServicioDetalleList;

    public ConstanciaDeServicio() {
    }

    public ConstanciaDeServicio(Integer folio) {
        this.folio = folio;
    }

    public ConstanciaDeServicio(Integer folio, String folioCliente) {
        this.folio = folio;
        this.folioCliente = folioCliente;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public List<PartidaServicio> getPartidaServicioList() {
        return partidaServicioList;
    }

    public void setPartidaServicioList(List<PartidaServicio> partidaServicioList) {
        this.partidaServicioList = partidaServicioList;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    public EstadoConstancia getStatus() {
        return status;
    }

    public void setStatus(EstadoConstancia status) {
        this.status = status;
    }

    public List<ConstanciaServicioDetalle> getConstanciaServicioDetalleList() {
        return constanciaServicioDetalleList;
    }

    public void setConstanciaServicioDetalleList(List<ConstanciaServicioDetalle> constanciaServicioDetalleList) {
        this.constanciaServicioDetalleList = constanciaServicioDetalleList;
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
        if (!(object instanceof ConstanciaDeServicio)) {
            return false;
        }
        ConstanciaDeServicio other = (ConstanciaDeServicio) object;
        if ((this.folio == null && other.folio != null) || (this.folio != null && !this.folio.equals(other.folio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaDeServicio[ folio=" + folio + " ]";
    }
    
}
