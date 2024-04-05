package mx.com.ferbo.model;

import java.io.Serializable;

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

@Entity
@Table(name = "categoria_egreso")
@NamedQueries({ @NamedQuery(name = "CategoriaEgreso.findByAll", query = "SELECT ce FROM CategoriaEgreso ce"),
		@NamedQuery(name = "CategoriaEgreso.findById", query = "SELECT ce FROM CategoriaEgreso ce WHERE ce.idCategoria = :idCategoria"),
		@NamedQuery(name = "CategoriaEgreso.findByTipoCategoria", query = "SELECT ce FROM CategoriaEgreso ce WHERE ce.tipoCategoria = :tipoCategoria"),
		@NamedQuery(name = "CategoriaEgreso.findByNombreCategoria", query = "SELECT ce FROM CategoriaEgreso ce WHERE ce.nombreCategoria = :nombreCategoria") })

public class CategoriaEgreso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic
	@Column(name = "id_categoria_egreso")
	@NotNull
	private Integer idCategoria;

	@Column(name = "tipo_categoria")
	@NotNull
	private String tipoCategoria;

	@Column(name = "nombre_categoria")
	@NotNull
	private String nombreCategoria;

	public Integer getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(Integer idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(String tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

}
