package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "complemento_pago")
@NamedQueries({
    @NamedQuery(name = "ComplementoPago.findAll", query = "SELECT cp FROM ComplementoPago cp"),
    @NamedQuery(name = "ComplementoPago.findById", query = "SELECT cp FROM ComplementoPago cp WHERE cp.id = :id"),
    @NamedQuery(name = "ComplementoPago.findByRegistro", query = "SELECT cp FROM ComplementoPago cp WHERE cp.registro BETWEEN :inicio AND :fin"),
    @NamedQuery(name = "ComplementoPago.findByTimbrado", query = "SELECT cp FROM ComplementoPago cp WHERE cp.timbrado BETWEEN :inicio AND :fin"),
    @NamedQuery(name = "ComplementoPago.findByUUID", query = "SELECT cp FROM ComplementoPago cp WHERE cp.uuid = :uuid")
})
public class ComplementoPago implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_comp_pago")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fh_registro")
    @Temporal(TemporalType.DATE)
    private Date registro;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fh_timbrado")
    @Temporal(TemporalType.DATE)
    private Date timbrado;
    
    @Size(max = 5)
    @Column(name = "nb_serie")
    private String serie;
    
    @Size(max = 30)
    @Column(name = "nu_numero")
    private String numero;
    
    @Size(max = 25)
    @Column(name = "cd_pac")
    private String pac;
    
    @Column(name = "cd_uuid")
    @Basic(optional = false)
    @Size(max = 36)
    private String uuid;
    
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "complementoPago")
    private List<Pago> listPagos;

    public ComplementoPago() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public Date getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(Date timbrado) {
        this.timbrado = timbrado;
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

    public String getPac() {
        return pac;
    }

    public void setPac(String pac) {
        this.pac = pac;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Pago> getListPagos() {
        return listPagos;
    }

    public void setListPagos(List<Pago> listPagos) {
        this.listPagos = listPagos;
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
        final ComplementoPago other = (ComplementoPago) obj;
        if(this.id == null || other.id == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
       
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ComplementoPago[" + "id=" + id + ", registro=" + registro + ", timbrado=" + timbrado + ", serie=" + serie + ", numero=" + numero + ", pac=" + pac + ", uuid=" + uuid + ']';
    }
    
}
