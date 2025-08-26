package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;

@Entity
@Table(name = "partidas_afectadas")
@NamedQueries({
    @NamedQuery(name = "PartidasAfectadas.findAll", query = "SELECT pa FROM PartidasAfectadas pa"),
    @NamedQuery(name = "PartidasAfectadas.findByID", query = "SELECT pa FROM PartidasAfectadas pa WHERE pa.id= :id"),
    @NamedQuery(name = "PartidasAfectadas.findByTraspaso", query = "SELECT pa FROM PartidasAfectadas pa WHERE pa.traspaso = :traspaso"),
    @NamedQuery(name = "PartidasAfectadas.findByPartida", query = "SELECT pa FROM PartidasAfectadas pa WHERE pa.partida = :partida"),
    @NamedQuery(name = "PartidasAfectadas.findByPartidaTraspaso", query = "SELECT pa FROM PartidasAfectadas pa WHERE pa.partidatraspaso = :partidatraspaso"
)})
public class PartidasAfectadas implements Serializable{
    private static final long serialVersionUID = 1L;    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = true)
    @Null
    @Column(name = "traspaso")
    private String traspaso;


    @ManyToOne(optional = false)
    @JoinColumn(name = "partida", referencedColumnName = "PARTIDA_CVE")
    private Partida partida;

    @OneToOne (optional = false)
    @JoinColumn(name = "PARTIDA_TRASPASO", referencedColumnName = "id")
    private TraspasoPartida partidatraspaso;

    public PartidasAfectadas() {

    }

    public PartidasAfectadas(String traspaso,Partida partida, TraspasoPartida partidatraspaso) {
        this.traspaso = traspaso;
        this.partida = partida;
        this.partidatraspaso = partidatraspaso;
    }


    public String getTraspaso() {
        return traspaso;
    }

    public void setTraspaso(String traspaso) {
        this.traspaso = traspaso;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public TraspasoPartida getPartidatraspaso() {
        return partidatraspaso;
    }

    public void setPartidatraspaso(TraspasoPartida partidatraspaso) {
        this.partidatraspaso = partidatraspaso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PartidasAfectadas [id=" + id + ", traspaso=" + traspaso + ", partida=" + partida + ", partidatraspaso="
                + partidatraspaso + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, partida, partidatraspaso, traspaso);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PartidasAfectadas other = (PartidasAfectadas) obj;
        return Objects.equals(id, other.id) && Objects.equals(partida, other.partida)
                && Objects.equals(partidatraspaso, other.partidatraspaso) && Objects.equals(traspaso, other.traspaso);
    }

}
