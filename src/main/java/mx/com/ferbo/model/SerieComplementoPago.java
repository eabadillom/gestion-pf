package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "serie_complemento_pago")
@NamedQueries({
    @NamedQuery(name = "SerieComplementoPago.findAll", query = "SELECT scp FROM SerieComplementoPago scp"),
    @NamedQuery(name = "SerieComplementoPago.findById", query = "SELECT scp FROM SerieComplementoPago scp WHERE scp.id = :id"),
    @NamedQuery(name = "SerieComplementoPago.findByEmisor", query = "SELECT scp FROM SerieComplementoPago scp WHERE scp.emisor = :emisor")
})
public class SerieComplementoPago implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_serie_comp_pago")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "nb_serie")
    private String serie;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nu_numero")
    private String numero;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cd_emisor")
    private Integer emisor;

    public SerieComplementoPago() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getEmisor() {
        return emisor;
    }

    public void setEmisor(Integer emisor) {
        this.emisor = emisor;
    }

    @Override
    public int hashCode() {
        if(this.id == null)
            return System.identityHashCode(this);
        return Objects.hash(this.id);
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
        final SerieComplementoPago other = (SerieComplementoPago) obj;
        if(this.id == null || other.id == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
       
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "SerieComplementoPago[" + "id=" + id + ", serie=" + serie + ", numero=" + numero + ", emisor=" + emisor + ']';
    }
    
}
