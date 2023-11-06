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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "CAMARA")
@NamedQueries({
    @NamedQuery(name = "Camara.findAll", query = "SELECT c FROM Camara c"),
    @NamedQuery(name = "Camara.findByCamaraCve", query = "SELECT c FROM Camara c WHERE c.camaraCve = :camaraCve"),
    @NamedQuery(name = "Camara.findByPlantaCve", query = "SELECT c FROM Camara c WHERE c.plantaCve.plantaCve = :plantaCve"),
    @NamedQuery(name = "Camara.findByCamaraDs", query = "SELECT c FROM Camara c WHERE c.camaraDs = :camaraDs"),
    @NamedQuery(name = "Camara.findByCamaraAbrev", query = "SELECT c FROM Camara c WHERE c.camaraAbrev = :camaraAbrev")})
public class Camara implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "camara")
    private List<Posicion> posicionList;
   
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CAMARA_CVE")
    private Integer camaraCve;
    @Size(max = 80)
    @Column(name = "CAMARA_DS")
    private String camaraDs;
    @Size(max = 6)
    @Column(name = "CAMARA_ABREV")
    private String camaraAbrev;
    @JoinColumn(name = "PLANTA_CVE", referencedColumnName = "PLANTA_CVE")
    @ManyToOne(fetch = FetchType.LAZY)
    private Planta plantaCve;
    @OneToMany(mappedBy = "camaraCve", fetch = FetchType.LAZY)
    private List<Partida> partidaList;
    
    
    public Camara() {
    }

	public Camara(Integer camaraCve) {
        this.camaraCve = camaraCve;
    }

    public Integer getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(Integer camaraCve) {
        this.camaraCve = camaraCve;
    }

    public String getCamaraDs() {
        return camaraDs;
    }

    public void setCamaraDs(String camaraDs) {
        this.camaraDs = camaraDs;
    }

    public String getCamaraAbrev() {
        return camaraAbrev;
    }

    public void setCamaraAbrev(String camaraAbrev) {
        this.camaraAbrev = camaraAbrev;
    }

    public Planta getPlantaCve() {
        return plantaCve;
    }

    public void setPlantaCve(Planta plantaCve) {
        this.plantaCve = plantaCve;
    }

    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {
        this.partidaList = partidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (camaraCve != null ? camaraCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Camara)) {
            return false;
        }
        Camara other = (Camara) object;
        if ((this.camaraCve == null && other.camaraCve != null) || (this.camaraCve != null && !this.camaraCve.equals(other.camaraCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Camara[ camaraCve=" + camaraCve + " ]";
    }
    
}
