/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "categoria")
@NamedQueries({
    @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c"),
    @NamedQuery(name = "Categoria.findByCategoriaCve", query = "SELECT c FROM Categoria c WHERE c.categoriaCve = :categoriaCve"),
    @NamedQuery(name = "Categoria.findByCategoriaDs", query = "SELECT c FROM Categoria c WHERE c.categoriaDs = :categoriaDs")})
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "categoria_cve")
    private Integer categoriaCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "categoria_ds")
    private String categoriaDs;
    @OneToMany(mappedBy = "categoriaCve")
    private List<Aviso> avisoList;

    public Categoria() {
    }

    public Categoria(Integer categoriaCve) {
        this.categoriaCve = categoriaCve;
    }

    public Categoria(Integer categoriaCve, String categoriaDs) {
        this.categoriaCve = categoriaCve;
        this.categoriaDs = categoriaDs;
    }

    public Integer getCategoriaCve() {
        return categoriaCve;
    }

    public void setCategoriaCve(Integer categoriaCve) {
        this.categoriaCve = categoriaCve;
    }

    public String getCategoriaDs() {
        return categoriaDs;
    }

    public void setCategoriaDs(String categoriaDs) {
        this.categoriaDs = categoriaDs;
    }

    public List<Aviso> getAvisoList() {
        return avisoList;
    }

    public void setAvisoList(List<Aviso> avisoList) {
        this.avisoList = avisoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoriaCve != null ? categoriaCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        if ((this.categoriaCve == null && other.categoriaCve != null) || (this.categoriaCve != null && !this.categoriaCve.equals(other.categoriaCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Categoria[ categoriaCve=" + categoriaCve + " ]";
    }
    
}
