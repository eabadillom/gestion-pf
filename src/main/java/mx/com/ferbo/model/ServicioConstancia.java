/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "servicio_constancia")
@NamedQueries({
    @NamedQuery(name = "ServicioConstancia.findAll", query = "SELECT s FROM ServicioConstancia s"),
    @NamedQuery(name = "ServicioConstancia.findById", query = "SELECT s FROM ServicioConstancia s WHERE s.id = :id"),
    @NamedQuery(name = "ServicioConstancia.findByDescripcion", query = "SELECT s FROM ServicioConstancia s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "ServicioConstancia.findByCosto", query = "SELECT s FROM ServicioConstancia s WHERE s.costo = :costo"),
    @NamedQuery(name = "ServicioConstancia.findByTarifa", query = "SELECT s FROM ServicioConstancia s WHERE s.tarifa = :tarifa"),
    @NamedQuery(name = "ServicioConstancia.findByBaseCargo", query = "SELECT s FROM ServicioConstancia s WHERE s.baseCargo = :baseCargo"),
    @NamedQuery(name = "ServicioConstancia.findByPlantaCve", query = "SELECT s FROM ServicioConstancia s WHERE s.plantaCve = :plantaCve"),
    @NamedQuery(name = "ServicioConstancia.findByPlantaDs", query = "SELECT s FROM ServicioConstancia s WHERE s.plantaDs = :plantaDs"),
    @NamedQuery(name = "ServicioConstancia.findByPlantaAbrev", query = "SELECT s FROM ServicioConstancia s WHERE s.plantaAbrev = :plantaAbrev"),
    @NamedQuery(name = "ServicioConstancia.findByCamaraCve", query = "SELECT s FROM ServicioConstancia s WHERE s.camaraCve = :camaraCve"),
    @NamedQuery(name = "ServicioConstancia.findByCamaraDs", query = "SELECT s FROM ServicioConstancia s WHERE s.camaraDs = :camaraDs"),
    @NamedQuery(name = "ServicioConstancia.findByCamaraAbrev", query = "SELECT s FROM ServicioConstancia s WHERE s.camaraAbrev = :camaraAbrev"),
    @NamedQuery(name = "ServicioConstancia.findByUnidadMedida", query = "SELECT s FROM ServicioConstancia s WHERE s.unidadMedida = :unidadMedida"),
    @NamedQuery(name = "ServicioConstancia.findByCodigo", query = "SELECT s FROM ServicioConstancia s WHERE s.codigo = :codigo"),
    @NamedQuery(name = "ServicioConstancia.findByPlantaCod", query = "SELECT s FROM ServicioConstancia s WHERE s.plantaCod = :plantaCod")})
public class ServicioConstancia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private BigDecimal costo;
    
    @Column(name = "tarifa")
    private BigDecimal tarifa;
    
    @Column(name = "baseCargo")
    private BigDecimal baseCargo;
    
    @Column(name = "planta_cve")
    private Integer plantaCve;
    
    @Size(max = 80)
    @Column(name = "planta_ds")
    private String plantaDs;
    
    @Size(max = 6)
    @Column(name = "planta_abrev")
    private String plantaAbrev;
    
    @Column(name = "camara_cve")
    private Integer camaraCve;
    
    @Column(name = "camara_ds")
    @Size(max = 80)
    private String camaraDs;
    
    @Column(name = "camara_abrev")
    @Size(max = 80)
    private String camaraAbrev;
    
    @Size(max = 20)
    @Column(name = "unidad_medida")
    private String unidadMedida;
    
    @Size(max = 20)
    @Column(name = "codigo")
    private String codigo;
    
    @Size(max = 10)
    @Column(name = "planta_cod")
    private String plantaCod;
    
    @Size(max = 5)
    @Column(name = "cd_unidad")
    private String cdUnidad;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "constancia", referencedColumnName = "id")
    private ConstanciaFactura constancia;

    public ServicioConstancia() {
    }

    public ServicioConstancia(Integer id) {
        this.id = id;
    }

    public ServicioConstancia(Integer id, String descripcion, BigDecimal costo) {
        this.id = id;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ConstanciaFactura getConstancia() {
        return constancia;
    }

    public void setConstancia(ConstanciaFactura constancia) {
        this.constancia = constancia;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public BigDecimal getBaseCargo() {
        return baseCargo;
    }

    public void setBaseCargo(BigDecimal baseCargo) {
        this.baseCargo = baseCargo;
    }

    public Integer getPlantaCve() {
        return plantaCve;
    }

    public void setPlantaCve(Integer plantaCve) {
        this.plantaCve = plantaCve;
    }

    public String getPlantaDs() {
        return plantaDs;
    }

    public void setPlantaDs(String plantaDs) {
        this.plantaDs = plantaDs;
    }

    public String getPlantaAbrev() {
        return plantaAbrev;
    }

    public void setPlantaAbrev(String plantaAbrev) {
        this.plantaAbrev = plantaAbrev;
    }

    public Integer getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(Integer camaraCve) {
        this.camaraCve = camaraCve;
    }

    public String getCamaraDs() {
        return camaraDs;
    }

    public void setCamaraDs(String camaraDs) {
        this.camaraDs = camaraDs;
    }

    public String getCamaraAbrev() {
        return camaraAbrev;
    }

    public void setCamaraAbrev(String camaraAbrev) {
        this.camaraAbrev = camaraAbrev;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPlantaCod() {
        return plantaCod;
    }

    public void setPlantaCod(String plantaCod) {
        this.plantaCod = plantaCod;
    }

    public String getCdUnidad() {
		return cdUnidad;
	}

	public void setCdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}

	@Override
    public int hashCode() {
		if(this.id == null)
			return System.identityHashCode(this);
		return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServicioConstancia)) {
            return false;
        }
        ServicioConstancia other = (ServicioConstancia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ServicioConstancia[ id=" + id + " ]";
    }
    
}
