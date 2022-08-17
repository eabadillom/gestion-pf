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
@Table(name = "CONSTANCIA_SALIDA")
@NamedQueries({
    @NamedQuery(name = "ConstanciaSalida.findAll", query = "SELECT c FROM ConstanciaSalida c"),
    @NamedQuery(name = "ConstanciaSalida.findById", query = "SELECT c FROM ConstanciaSalida c WHERE c.id = :id"),
    @NamedQuery(name = "ConstanciaSalida.findByFecha", query = "SELECT c FROM ConstanciaSalida c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "ConstanciaSalida.findByNumero", query = "SELECT c FROM ConstanciaSalida c WHERE c.numero = :numero"),
    @NamedQuery(name = "ConstanciaSalida.findByNombreCte", query = "SELECT c FROM ConstanciaSalida c WHERE c.nombreCte = :nombreCte"),
    @NamedQuery(name = "ConstanciaSalida.findByObservaciones", query = "SELECT c FROM ConstanciaSalida c WHERE c.observaciones = :observaciones")})
public class ConstanciaSalida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NUMERO")
    private String numero;
    @Size(max = 150)
    @Column(name = "NOMBRE_CTE")
    private String nombreCte;
    @JoinColumn(name = "STATUS", referencedColumnName = "ID")
    @ManyToOne
    private StatusConstanciaSalida status;
    @Size(max = 75)
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constanciaCve")
    private List<DetalleConstanciaSalida> detalleConstanciaSalidaList;
    @JoinColumn(name = "CLIENTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente clienteCve;

    public ConstanciaSalida() {
    }

    public ConstanciaSalida(Integer id) {
        this.id = id;
    }

    public ConstanciaSalida(Integer id, Date fecha, String numero) {
        this.id = id;
        this.fecha = fecha;
        this.numero = numero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreCte() {
        return nombreCte;
    }

    public void setNombreCte(String nombreCte) {
        this.nombreCte = nombreCte;
    }

    public StatusConstanciaSalida getStatus() {
        return status;
    }

    public void setStatus(StatusConstanciaSalida status) {
        this.status = status;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<DetalleConstanciaSalida> getDetalleConstanciaSalidaList() {
        return detalleConstanciaSalidaList;
    }

    public void setDetalleConstanciaSalidaList(List<DetalleConstanciaSalida> detalleConstanciaSalidaList) {
        this.detalleConstanciaSalidaList = detalleConstanciaSalidaList;
    }

    public Cliente getClienteCve() {
        return clienteCve;
    }

    public void setClienteCve(Cliente clienteCve) {
        this.clienteCve = clienteCve;
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
        if (!(object instanceof ConstanciaSalida)) {
            return false;
        }
        ConstanciaSalida other = (ConstanciaSalida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaSalida[ id=" + id + " ]";
    }
    
}
