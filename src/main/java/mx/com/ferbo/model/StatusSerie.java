/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "status_serie")
@NamedQueries({
    @NamedQuery(name = "StatusSerie.findAll", query = "SELECT s FROM StatusSerie s"),
//    @NamedQuery(name = "StatusSerie.find", query = "SELECT s FROM StatusSerie s WHERE s.id != 3"),
    @NamedQuery(name = "StatusSerie.findById", query = "SELECT s FROM StatusSerie s WHERE s.id = :id"),
    @NamedQuery(name = "StatusSerie.findByDescripcion", query = "SELECT s FROM StatusSerie s WHERE s.descripcion = :descripcion")})
public class StatusSerie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusSerie")
    private List<SerieFactura> serieFacturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusSerie")
    private List<SerieNota> serieNotaList;

    public StatusSerie() {
    }

    public StatusSerie(Integer id) {
        this.id = id;
    }

    public StatusSerie(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
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

    public List<SerieFactura> getSerieFacturaList() {
        return serieFacturaList;
    }

    public void setSerieFacturaList(List<SerieFactura> serieFacturaList) {
        this.serieFacturaList = serieFacturaList;
    }

    public List<SerieNota> getSerieNotaList() {
        return serieNotaList;
    }

    public void setSerieNotaList(List<SerieNota> serieNotaList) {
        this.serieNotaList = serieNotaList;
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
        if (!(object instanceof StatusSerie)) {
            return false;
        }
        StatusSerie other = (StatusSerie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.StatusSerie[ id=" + id + " ]";
    }
    
}
