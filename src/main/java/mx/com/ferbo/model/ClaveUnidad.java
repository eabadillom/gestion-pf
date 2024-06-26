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

@Entity //refiere a que la clase sera mapeada a una tabla 
@Table(name = "clave_unidad")//Nombre de la tabla a mapear 
@NamedQueries({
	@NamedQuery(name = "ClaveUnidad.findAll", query = "SELECT c FROM ClaveUnidad c "),
	@NamedQuery(name = "ClaveUnidad.findByCdUnidad", query = "SELECT c FROM ClaveUnidad c WHERE c.cdUnidad = :cdUnidad"),
	@NamedQuery(name = "ClaveUnidad.findByNbUnidad", query = "SELECT c FROM ClaveUnidad c WHERE c.nbUnidad = :nbUnidad"),
	@NamedQuery(name = "ClaveUnidad.findbyNbDescripcion", query = "SELECT c FROM ClaveUnidad c WHERE c.nbDescripcion = :nbDescripcion"),
	@NamedQuery(name = "ClaveUnidad.findByNbNota", query = "SELECT c FROM ClaveUnidad c WHERE c.nbNota = :nbNota"),
	@NamedQuery(name = "ClaveUnidad.findByFechaInicio",query = "SELECT c FROM ClaveUnidad c WHERE c.fechInicio = :fechInicio"),
	@NamedQuery(name = "ClaveUnidad.findByFechaFinal", query = "SELECT c FROM ClaveUnidad c WHERE c.fechFinal = :fechFinal"),
	@NamedQuery(name = "ClaveUnidad.findByNbSimbolo", query = "SELECT c FROM ClaveUnidad c WHERE c.nbSimbolo = :nbSimbolo"),
	@NamedQuery(name = "ClaveUnidad.likeClaveNombre", query = "SELECT c FROM ClaveUnidad c WHERE c.cdUnidad like :clave OR c.nbUnidad like :nombre")
})

public class ClaveUnidad implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id 
	@Basic(optional = false)
	@NotNull
	@Size(max = 5)
	@Column(name = "cd_unidad")
	private String cdUnidad;
	
	@Basic(optional = false)
	@Size(max = 200)
	@Column(name = "nb_unidad")
	private String nbUnidad;
	
	@Size(max = 1000)
	@Column(name = "nb_descripcion")
	private String nbDescripcion;
	
	@Size(max = 500)
	@Column(name = "nb_nota")
	private String nbNota;
	
	@Basic(optional = false)
	@Temporal(TemporalType.DATE)//Devuelve fecha y descarta la hora
	@Column(name = "fh_vigencia_ini")
	private Date fechInicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fh_vigencia_fin")
	private Date fechFinal;
	
	@Size(max = 50)
	@Column(name = "nb_simbolo")
	private String nbSimbolo;
	
	
	public String getcdUnidad() {
		return cdUnidad;
	}
	public void setcdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}
	public String getNbUnidad() {
		return nbUnidad;
	}
	public void setNbUnidad(String nbUnidad) {
		this.nbUnidad = nbUnidad;
	}
	public String getNbDescripcion() {
		return nbDescripcion;
	}
	public void setNbDescripcion(String nbDescripcion) {
		this.nbDescripcion = nbDescripcion;
	}
	public String getNbNota() {
		return nbNota;
	}
	public void setNbNota(String nbNOta) {
		this.nbNota = nbNOta;
	}
	public Date getFechInicio() {
		return fechInicio;
	}
	public void setFechInicio(Date fechInicio) {
		this.fechInicio = fechInicio;
	}
	public Date getFechFinal() {
		return fechFinal;
	}
	public void setFechFinal(Date fechFinal) {
		this.fechFinal = fechFinal;
	}
	public String getNbSimbolo() {
		return nbSimbolo;
	}
	public void setNbSimbolo(String nbSimbolo) {
		this.nbSimbolo = nbSimbolo;
	}
	
	

}
