package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "metodo_pago")

@NamedQueries({
		@NamedQuery(name = "MetodoPago.findAll",query = "SELECT mp FROM MetodoPago mp " ),
		@NamedQuery(name = "MetodoPago.findByCdMetodoPago" ,query = "SELECT mp FROM MetodoPago mp WHERE mp.cdMetodoPago =  :cdMetodoPago"),
		@NamedQuery(name = "MetodoPago.findByNbMetodoPago",query = "SELECT mp FROM MetodoPago mp WHERE mp.nbMetodoPago = :nbMetodoPago")})
		//@NamedQuery(name = "MetodoPago.findByFechaInicio" ,query = "SELECT mp FROM MetodoPago mp WHERE mp.fechaInicio = :fechaInicio"),
		//@NamedQuery(name = "MetodoPago.finByFechaFinal", query = "SELECT mp FROM MetodoPago mp WHERE mp.fechaFinal = :fechaFinal")})

public class MetodoPago implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "cd_metodo_pago")
	@Size(max = 5)
	private String cdMetodoPago;
	
	@NotNull
	@Column(name = "nb_metodo_pago")
	@Size(max = 100)
	private String nbMetodoPago;
	
	@Column(name = "fh_vigencia_ini")
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date fechaInicio;
	
	@Column(name = "fh_vigencia_fin")
	@Temporal(TemporalType.DATE)
	private Date fechaFinal;
	
	public MetodoPago() {
		
	}

	public String getCdMetodoPago() {
		return cdMetodoPago;
	}

	public void setCdMetodoPago(String cdMetodoPago) {
		this.cdMetodoPago = cdMetodoPago;
	}

	public String getNbMetodoPago() {
		return nbMetodoPago;
	}

	public void setNbMetodoPago(String nbMetodoPago) {
		this.nbMetodoPago = nbMetodoPago;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	
	

}
