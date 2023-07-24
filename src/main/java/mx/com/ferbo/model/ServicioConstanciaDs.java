/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "SERVICIO_CONSTANCIA_DS")
@NamedQueries({
    @NamedQuery(name = "ServicioConstanciaDs.findAll", query = "SELECT s FROM ServicioConstanciaDs s"),
    @NamedQuery(name = "ServicioConstanciaDs.findById", query = "SELECT s FROM ServicioConstanciaDs s WHERE s.id = :id"),
    @NamedQuery(name = "ServicioConstanciaDs.findByDescripcion", query = "SELECT s FROM ServicioConstanciaDs s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "ServicioConstanciaDs.findByCosto", query = "SELECT s FROM ServicioConstanciaDs s WHERE s.costo = :costo"),
    @NamedQuery(name = "ServicioConstanciaDs.findByTarifa", query = "SELECT s FROM ServicioConstanciaDs s WHERE s.tarifa = :tarifa"),
    @NamedQuery(name = "ServicioConstanciaDs.findByCodigo", query = "SELECT s FROM ServicioConstanciaDs s WHERE s.codigo = :codigo"),
    @NamedQuery(name = "ServicioConstanciaDs.findByUdCobro", query = "SELECT s FROM ServicioConstanciaDs s WHERE s.udCobro = :udCobro")})
public class ServicioConstanciaDs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "COSTO")
    private BigDecimal costo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TARIFA")
    private BigDecimal tarifa;
    @Size(max = 20)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 10)
    @Column(name = "UD_COBRO")
    private String udCobro;
    @JoinColumn(name = "CONSTANCIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConstanciaFacturaDs constancia;

    public ServicioConstanciaDs() {
    }

    public ServicioConstanciaDs(Integer id) {
        this.id = id;
    }

    public ServicioConstanciaDs(Integer id, String descripcion, BigDecimal costo, BigDecimal tarifa) {
        this.id = id;
        this.descripcion = descripcion;
        this.costo = costo;
        this.tarifa = tarifa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUdCobro() {
        return udCobro;
    }

    public void setUdCobro(String udCobro) {
        this.udCobro = udCobro;
    }

    public ConstanciaFacturaDs getConstancia() {
        return constancia;
    }

    public void setConstancia(ConstanciaFacturaDs constancia) {
        this.constancia = constancia;
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
        if (!(object instanceof ServicioConstanciaDs)) {
            return false;
        }
        ServicioConstanciaDs other = (ServicioConstanciaDs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ServicioConstanciaDs[ id=" + id + " ]";
    }
    
}
