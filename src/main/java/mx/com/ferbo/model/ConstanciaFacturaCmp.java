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
@Table(name = "constancia_factura_cmp")
@NamedQueries({
    @NamedQuery(name = "ConstanciaFacturaCmp.findAll", query = "SELECT c FROM ConstanciaFacturaCmp c"),
    @NamedQuery(name = "ConstanciaFacturaCmp.findById", query = "SELECT c FROM ConstanciaFacturaCmp c WHERE c.id = :id"),
    @NamedQuery(name = "ConstanciaFacturaCmp.findByFolio", query = "SELECT c FROM ConstanciaFacturaCmp c WHERE c.folio = :folio"),
    @NamedQuery(name = "ConstanciaFacturaCmp.findByFolioCliente", query = "SELECT c FROM ConstanciaFacturaCmp c WHERE c.folioCliente = :folioCliente"),
    @NamedQuery(name = "ConstanciaFacturaCmp.findByVigenciaInicio", query = "SELECT c FROM ConstanciaFacturaCmp c WHERE c.vigenciaInicio = :vigenciaInicio"),
    @NamedQuery(name = "ConstanciaFacturaCmp.findByVigenciaFin", query = "SELECT c FROM ConstanciaFacturaCmp c WHERE c.vigenciaFin = :vigenciaFin")})
public class ConstanciaFacturaCmp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "folio")
    private int folio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "folio_cliente")
    private String folioCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia_inicio")
    @Temporal(TemporalType.DATE)
    private Date vigenciaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia_fin")
    @Temporal(TemporalType.DATE)
    private Date vigenciaFin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constancia")
    private List<DetalleConstanciaCmp> detalleConstanciaCmpList;
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Factura factura;

    public ConstanciaFacturaCmp() {
    }

    public ConstanciaFacturaCmp(Integer id) {
        this.id = id;
    }

    public ConstanciaFacturaCmp(Integer id, int folio, String folioCliente, Date vigenciaInicio, Date vigenciaFin) {
        this.id = id;
        this.folio = folio;
        this.folioCliente = folioCliente;
        this.vigenciaInicio = vigenciaInicio;
        this.vigenciaFin = vigenciaFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }

    public Date getVigenciaInicio() {
        return vigenciaInicio;
    }

    public void setVigenciaInicio(Date vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }

    public Date getVigenciaFin() {
        return vigenciaFin;
    }

    public void setVigenciaFin(Date vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }

    public List<DetalleConstanciaCmp> getDetalleConstanciaCmpList() {
        return detalleConstanciaCmpList;
    }

    public void setDetalleConstanciaCmpList(List<DetalleConstanciaCmp> detalleConstanciaCmpList) {
        this.detalleConstanciaCmpList = detalleConstanciaCmpList;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
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
        if (!(object instanceof ConstanciaFacturaCmp)) {
            return false;
        }
        ConstanciaFacturaCmp other = (ConstanciaFacturaCmp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaFacturaCmp[ id=" + id + " ]";
    }
    
}
