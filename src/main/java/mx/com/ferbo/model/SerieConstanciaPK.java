package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class SerieConstanciaPK implements Serializable {

    private static final long serialVersionUID = -4873673664504600355L;

	@JoinColumn(name="id_cliente")
	@ManyToOne
    private Cliente cliente;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tp_serie")
    private String tpSerie;
    
    @ManyToOne
    @JoinColumn(name = "id_planta")
    private Planta planta;

    public SerieConstanciaPK() {
    }

    public SerieConstanciaPK(Cliente cliente, String tpSerie, Planta planta) {
        this.cliente = cliente;
        this.tpSerie = tpSerie;
        this.planta = planta;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Planta getPlanta() {
		return planta;
	}

	public void setPlanta(Planta planta) {
		this.planta = planta;
	}

    public String getTpSerie() {
        return tpSerie;
    }

    public void setTpSerie(String tpSerie) {
        this.tpSerie = tpSerie;
    }

	@Override
	public int hashCode() {
		return Objects.hash(cliente, planta, tpSerie);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerieConstanciaPK other = (SerieConstanciaPK) obj;
		return Objects.equals(cliente, other.cliente) && Objects.equals(planta, other.planta)
				&& Objects.equals(tpSerie, other.tpSerie);
	}

	@Override
	public String toString() {
		return "SerieConstanciaPK [cliente=" + cliente + ", tpSerie=" + tpSerie + ", planta=" + planta + "]";
	}
}
