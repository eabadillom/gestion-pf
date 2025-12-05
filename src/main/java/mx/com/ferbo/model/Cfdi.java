package mx.com.ferbo.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Table(name = "cfdi")
public class Cfdi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cfdi_id")
	private Integer id;
	
	
	@OneToOne
	@JoinColumn(name = "id_factura", referencedColumnName = "id")
	private Factura factura;
	
	@Column(name = "cfdi_uuid")
	@Basic(optional = false)
	@Size(max = 36)
	private String uuid;
	
	@Column(name = "cfdi_fecha")
	@Basic(optional = false)
	private Date fecha;
	
	@Column(name = "cfdi_cert_sat")
	@Basic(optional = false)
	@Size(max = 20)
	private String certificadoSAT;

	@Override
	public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cfdi other = (Cfdi) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Cfdi [id=" + id + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCertificadoSAT() {
		return certificadoSAT;
	}

	public void setCertificadoSAT(String certificadoSAT) {
		this.certificadoSAT = certificadoSAT;
	}
}