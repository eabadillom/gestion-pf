package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Embeddable
public class DetallePartidaPK implements Serializable, Cloneable {

	private static final long serialVersionUID = -7593443111857351832L;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "DET_PART_CVE")
    private int detPartCve;
	
	@ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "PARTIDA_CVE")
    private Partida partidaCve;

    public DetallePartidaPK() {
    }
    
    public DetallePartidaPK clone() throws CloneNotSupportedException {
    	return (DetallePartidaPK) super.clone();
    }

	public DetallePartidaPK(@NotNull int detPartCve, Partida partidaCve) {
		super();
		this.detPartCve = detPartCve;
		this.partidaCve = partidaCve;
	}
	
	public int getDetPartCve() {
		return detPartCve;
	}

	public void setDetPartCve(int detPartCve) {
		this.detPartCve = detPartCve;
	}

	public Partida getPartidaCve() {
		return partidaCve;
	}

	public void setPartidaCve(Partida partidaCve) {
		this.partidaCve = partidaCve;
	}

	@Override
	public int hashCode() {
		return Objects.hash(detPartCve, partidaCve);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DetallePartidaPK other = (DetallePartidaPK) obj;
		return detPartCve == other.detPartCve && Objects.equals(partidaCve, other.partidaCve);
	}

	@Override
	public String toString() {
		return "DetallePartidaPK [detPartCve=" + detPartCve + ", partidaCve=" + partidaCve + "]";
	}
    
}
