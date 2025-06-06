package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

 
@Entity
@Table(name = "tarima")
@NamedQueries({
	@NamedQuery(name = "Tarima.findByFolio", query = 
			  "SELECT t FROM Tarima t "
			+ "JOIN t.partidas p "
			+ "JOIN p.folio cdd "
			+ "WHERE cdd.folio = :folio")
})
public class Tarima implements Serializable {
	
	private static final long serialVersionUID = 7674317677823854599L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "cd_tarima")
	private Integer id;
	
	@Column(name = "nb_tarima")
	@Basic(optional = false)
	@Size(max = 50)
	private String nombre;
        
        @Column(name = "st_excedente")
        @Basic(optional = false)
        private boolean excedente;

	@OneToMany(mappedBy = "tarima")
	private List<Partida> partidas;
	
	public Tarima() {
	}
	
	public Tarima(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	public Tarima(String nombre) {
		this.nombre = nombre;
	}

        public boolean isExcedente() {
            return excedente;
        }

        public void setExcedente(boolean excedente) {
            this.excedente = excedente;
        }

	@Override
	public int hashCode() {
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
		Tarima other = (Tarima) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Tarima [id=" + id + ", nombre=" + nombre + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
	}
}
