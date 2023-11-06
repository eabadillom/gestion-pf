/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
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

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "CONSTANCIA_TRASPASO")
@NamedQueries({
    @NamedQuery(name = "ConstanciaTraspaso.findAll", query = "SELECT c FROM ConstanciaTraspaso c"),
    @NamedQuery(name = "ConstanciaTraspaso.findById", query = "SELECT c FROM ConstanciaTraspaso c WHERE c.id = :id"),
    @NamedQuery(name = "ConstanciaTraspaso.findByNumero", query = "SELECT c FROM ConstanciaTraspaso c WHERE c.numero = :numero"),
    @NamedQuery(name = "ConstanciaTraspaso.findByFecha", query = "SELECT c FROM ConstanciaTraspaso c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ConstanciaTraspaso.findByObservacion", query = "SELECT c FROM ConstanciaTraspaso c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "ConstanciaTraspaso.findByNombreCliente", query = "SELECT c FROM ConstanciaTraspaso c WHERE c.nombreCliente = :nombreCliente"),
    @NamedQuery(name = "ConstanciaTraspaso.findByPeriodoClienteNumero", query = "SELECT c FROM ConstanciaTraspaso c WHERE (c.fecha BETWEEN :fechaInicio AND :fechaFin) AND (c.cliente.cteCve = :idCliente OR :idCliente IS NULL) AND (c.numero = :folioCliente OR :folioCliente IS NULL )"),
    @NamedQuery(name = "ConstanciaTraspaso.findByFechaCadena", query = "SELECT c FROM ConstanciaTraspaso c WHERE c.fechaCadena = :fechaCadena")})
public class ConstanciaTraspaso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 500)
    @Column(name = "observacion")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombreCliente")
    private String nombreCliente;
    @Size(max = 25)
    @Column(name = "fecha_cadena")
    private String fechaCadena;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "traspaso")
    private List<TraspasoServicio> traspasoServicioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "traspaso")
    private List<TraspasoPartida> traspasoPartidaList;
    @JoinColumn(name = "cliente", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cliente;

    public ConstanciaTraspaso() {
    }

    public ConstanciaTraspaso(Integer id) {
        this.id = id;
    }

    public ConstanciaTraspaso(Integer id, String numero, Date fecha, String observacion, String nombreCliente) {
        this.id = id;
        this.numero = numero;
        this.fecha = fecha;
        this.observacion = observacion;
        this.nombreCliente = nombreCliente;
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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getFechaCadena() {
        return fechaCadena;
    }

    public void setFechaCadena(String fechaCadena) {
        this.fechaCadena = fechaCadena;
    }

    public List<TraspasoServicio> getTraspasoServicioList() {
        return traspasoServicioList;
    }

    public void setTraspasoServicioList(List<TraspasoServicio> traspasoServicioList) {
        this.traspasoServicioList = traspasoServicioList;
    }

    public List<TraspasoPartida> getTraspasoPartidaList() {
        return traspasoPartidaList;
    }

    public void setTraspasoPartidaList(List<TraspasoPartida> traspasoPartidaList) {
        this.traspasoPartidaList = traspasoPartidaList;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        if (!(object instanceof ConstanciaTraspaso)) {
            return false;
        }
        ConstanciaTraspaso other = (ConstanciaTraspaso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaTraspaso[ id=" + id + " ]";
    }
    
}
