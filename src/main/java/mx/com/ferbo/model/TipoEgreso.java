package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name ="tipo_egreso")
@NamedQueries({
	@NamedQuery(name ="TipoEgreso.findByAll", query ="SELECT t FROM TipoEgreso t"),
	@NamedQuery(name ="TipoEgreso.findById", query ="SELECT t FROM TipoEgreso t WHERE t.idTipoEgreso = :idTipoEgreso"),
	@NamedQuery(name ="TipoEgreso.findByNombre", query ="SELECT t FROM TipoEgreso t WHERE t.nombreTipoEgreso = :nombreTipoEgreso")
})


public class TipoEgreso implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id_tipo_egreso")
	@NotNull
	private Integer idTipoEgreso;

	@Column(name ="nombre_tipo_egreso")
	@NotNull
	private String nombreTipoEgreso;

	public Integer getIdTipoEgreso() {
		return idTipoEgreso;
	}

	public void setIdTipoEgreso(Integer idTipoEgreso) {
		this.idTipoEgreso = idTipoEgreso;
	}

	public String getNombreTipoEgreso() {
		return nombreTipoEgreso;
	}

	public void setNombreTipoEgreso(String nombreTipoEgreso) {
		this.nombreTipoEgreso = nombreTipoEgreso;
	}

}
