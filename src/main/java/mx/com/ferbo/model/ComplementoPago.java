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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "complemento_pago")
@NamedQuery(name = "ComplementoPago.findByRegistro", query = "SELECT cp FROM ComplementoPago cp WHERE cp.registro BETWEEN :inicio AND :fin")
@NamedQuery(name = "ComplementoPago.findByTimbrado", query = "SELECT cp FROM ComplementoPago cp WHERE cp.timbrado BETWEEN :inicio AND :fin")
@NamedQuery(name = "ComplementoPago.findByFolioSerie", query = "SELECT cp FROM ComplementoPago cp WHERE cp.numero = :numero AND cp.serie = :serie")
public class ComplementoPago implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_comp_pago")
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_emisor", nullable = false)
    private EmisoresCFDIS emisor;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_receptor", nullable = false)
    private Cliente receptor;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fh_registro")
    @Temporal(TemporalType.DATE)
    private Date registro;
    
    @Size(max = 5)
    @Column(name = "nb_serie")
    private String serie;
    
    @Size(max = 30)
    @Column(name = "nu_numero")
    private String numero;
    
    @Column(name = "cd_uso_cfdi")
    @Size(max = 5)
    @Basic(optional = false)
    private String usoCFDI;
    
    @Column(name = "cd_lugar_exp")
    @Size(max = 5)
    private String lugarExpedicion;
    
    @Column(name = "nb_emi_nombre")
    @Size(max = 80)
    @Basic(optional = false)
    private String emisorNombre;
    
    @Column(name = "cd_emi_rfc")
    @Size(min = 13, max = 14)
    @Basic(optional = false)
    private String emisorRFC;
    
    @Column(name = "cd_emi_reg_fiscal")
    @Size(max = 5)
    @Basic(optional = false)
    private String emisorRegimenFiscal;
    
    @Column(name = "nb_rec_nombre")
    @Size(max = 80)
    @Basic(optional = false)
    private String receptorNombre;
    
    @Column(name = "cd_rec_rfc")
    @Size(min = 13, max = 14)
    @Basic(optional = false)
    private String receptorRFC;
    
    @Column(name = "cd_rec_cp")
    @Size(min = 5, max = 5)
    private String receptorCodigoPostal;
    
    @Column(name = "cd_rec_reg_fiscal")
    @Size(max = 5)
    @Basic(optional = false)
    private String receptorRegimenFiscal;
    
    @Column(name = "fh_timbrado")
    @Temporal(TemporalType.DATE)
    private Date timbrado;
    
    @Size(max = 25)
    @Column(name = "cd_pac")
    private String idPac;
    
    @Column(name = "cd_uuid")
    @Size(max = 36)
    private String uuid;
    
    @Column(name = "nb_certificado_sat")
    @Size(max = 20)
    private String certificadoSAT;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "complementoPago")
    private List<Pago> listPagos;
    
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
    	return "ComplementoPago[" + "id=" + id + ", registro=" + registro + ", timbrado=" + timbrado + ", serie=" + serie + ", numero=" + numero + ", pac=" + idPac + ", uuid=" + uuid + ", certificadoSAT=" + certificadoSAT + ']';
    }

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

    public String getIdPac() {
        return idPac;
    }

    public void setIdPac(String idPac) {
        this.idPac = idPac;
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
    
    public String getCertificadoSAT() {
    	return certificadoSAT;
    }
    
    public void setCertificadoSAT(String certificadoSAT) {
    	this.certificadoSAT = certificadoSAT;
    }

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}

	public Cliente getReceptor() {
		return receptor;
	}

	public void setReceptor(Cliente receptor) {
		this.receptor = receptor;
	}

	public String getLugarExpedicion() {
		return lugarExpedicion;
	}

	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}

	public String getEmisorNombre() {
		return emisorNombre;
	}

	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}

	public String getEmisorRFC() {
		return emisorRFC;
	}

	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}

	public String getEmisorRegimenFiscal() {
		return emisorRegimenFiscal;
	}

	public void setEmisorRegimenFiscal(String emisorRegimenFiscal) {
		this.emisorRegimenFiscal = emisorRegimenFiscal;
	}

	public String getReceptorNombre() {
		return receptorNombre;
	}

	public void setReceptorNombre(String receptorNombre) {
		this.receptorNombre = receptorNombre;
	}

	public String getReceptorRFC() {
		return receptorRFC;
	}

	public void setReceptorRFC(String receptorRFC) {
		this.receptorRFC = receptorRFC;
	}

	public String getReceptorRegimenFiscal() {
		return receptorRegimenFiscal;
	}

	public void setReceptorRegimenFiscal(String receptorRegimenFiscal) {
		this.receptorRegimenFiscal = receptorRegimenFiscal;
	}

	public String getUsoCFDI() {
		return usoCFDI;
	}

	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}

	public String getReceptorCodigoPostal() {
		return receptorCodigoPostal;
	}

	public void setReceptorCodigoPostal(String receptorCodigoPostal) {
		this.receptorCodigoPostal = receptorCodigoPostal;
	}
    
}
