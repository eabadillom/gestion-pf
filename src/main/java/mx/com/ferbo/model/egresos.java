package mx.com.ferbo.model;

import java.io.Serializable;

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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "concepto_egreso")
@NamedQueries({
	@NamedQuery(name = "egresos.findByAll", query ="SELECT e FROM egresos e"),
	@NamedQuery(name ="egresos.finById", query = "SELECT e FROM egresos e WHERE  e.idEgreso = :id"),
	@NamedQuery(name = "egresos.findByCategoria", query = "SELECT e FROM egresos e WHERE  e.categoriaEgreso = :categoriaEgreso"),
	@NamedQuery(name = "egresos.findByNombreEgreso", query ="SELECT e FROM egresos e WHERE e.nombreEgreso = :nombreEgreso")
})
public class egresos implements Serializable{

	private static final long serialVersionUID = 5758852946569378638L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional =false)
	@Column(name = "id_egreso")
	private Integer idEgreso;
	
	@JoinColumn(name = "categoria_egreso", referencedColumnName = "id_categoria_egreso")
	@ManyToOne(optional = false)
	private CategoriaEgreso  categoriaEgreso;
	
	@NotNull
	@Column(name="nombre_egreso")
	private String nombreEgreso;
	
	public Integer getIdEgreso() {
		return idEgreso;
	}

	public void setIdEgreso(Integer idEgreso) {
		this.idEgreso = idEgreso;
	}

	public CategoriaEgreso getCategoriaEgreso() {
		return categoriaEgreso;
	}

	public void setCategoriaEgreso(CategoriaEgreso categoriaEgreso) {
		this.categoriaEgreso = categoriaEgreso;
	}

	public String getNombreEgreso() {
		return nombreEgreso;
	}

	public void setNombreEgreso(String nombreEgreso) {
		this.nombreEgreso = nombreEgreso;
	}

	
	
}
