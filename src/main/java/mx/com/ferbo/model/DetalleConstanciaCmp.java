/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "detalle_constancia_cmp")
@NamedQueries({
    @NamedQuery(name = "DetalleConstanciaCmp.findAll", query = "SELECT d FROM DetalleConstanciaCmp d"),
    @NamedQuery(name = "DetalleConstanciaCmp.findById", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByServicioCve", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.servicioCve = :servicioCve"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByServicioDs", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.servicioDs = :servicioDs"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCantidadSer", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.cantidadSer = :cantidadSer"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCuota", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.cuota = :cuota"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCuotaDiaria", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.cuotaDiaria = :cuotaDiaria"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByDias", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.dias = :dias"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByProductoCve", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.productoCve = :productoCve"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByProductoDs", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.productoDs = :productoDs"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByPeso", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.peso = :peso"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByUnidadPeso", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.unidadPeso = :unidadPeso"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCantidad", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByUnidadManejo", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.unidadManejo = :unidadManejo"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByValor", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.valor = :valor"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByImporte", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.importe = :importe"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByIva", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.iva = :iva"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByTotal", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.total = :total"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByPartidaCve", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.partidaCve = :partidaCve"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByPlantaCve", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.plantaCve = :plantaCve"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByPlantaDs", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.plantaDs = :plantaDs"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByPlantaAbrev", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.plantaAbrev = :plantaAbrev"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCamaraCve", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.camaraCve = :camaraCve"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCamaraDs", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.camaraDs = :camaraDs"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByCamaraAbrev", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.camaraAbrev = :camaraAbrev"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByNoTarimas", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.noTarimas = :noTarimas"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByPlantaCod", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.plantaCod = :plantaCod"),
    @NamedQuery(name = "DetalleConstanciaCmp.findByServicioCod", query = "SELECT d FROM DetalleConstanciaCmp d WHERE d.servicioCod = :servicioCod")})
public class DetalleConstanciaCmp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_cve")
    private int servicioCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "servicio_ds")
    private String servicioDs;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_ser")
    private BigDecimal cantidadSer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuota")
    private BigDecimal cuota;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuota_diaria")
    private BigDecimal cuotaDiaria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dias")
    private int dias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "producto_cve")
    private int productoCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "producto_ds")
    private String productoDs;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private BigDecimal peso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "unidad_peso")
    private String unidadPeso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "unidad_manejo")
    private String unidadManejo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "importe")
    private BigDecimal importe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iva")
    private BigDecimal iva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private BigDecimal total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "partida_cve")
    private int partidaCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "planta_cve")
    private int plantaCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "planta_ds")
    private String plantaDs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "planta_abrev")
    private String plantaAbrev;
    @Basic(optional = false)
    @NotNull
    @Column(name = "camara_cve")
    private int camaraCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "camara_ds")
    private String camaraDs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "camara_abrev")
    private String camaraAbrev;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_tarimas")
    private BigDecimal noTarimas;
    @Size(max = 10)
    @Column(name = "planta_cod")
    private String plantaCod;
    @Size(max = 20)
    @Column(name = "servicio_cod")
    private String servicioCod;
    @JoinColumn(name = "constancia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ConstanciaFacturaCmp constancia;

    public DetalleConstanciaCmp() {
    }

    public DetalleConstanciaCmp(Integer id) {
        this.id = id;
    }

    public DetalleConstanciaCmp(Integer id, int servicioCve, String servicioDs, BigDecimal cantidadSer, BigDecimal cuota, BigDecimal cuotaDiaria, int dias, int productoCve, String productoDs, BigDecimal peso, String unidadPeso, BigDecimal cantidad, String unidadManejo, BigDecimal valor, BigDecimal importe, BigDecimal iva, BigDecimal total, int partidaCve, int plantaCve, String plantaDs, String plantaAbrev, int camaraCve, String camaraDs, String camaraAbrev, BigDecimal noTarimas) {
        this.id = id;
        this.servicioCve = servicioCve;
        this.servicioDs = servicioDs;
        this.cantidadSer = cantidadSer;
        this.cuota = cuota;
        this.cuotaDiaria = cuotaDiaria;
        this.dias = dias;
        this.productoCve = productoCve;
        this.productoDs = productoDs;
        this.peso = peso;
        this.unidadPeso = unidadPeso;
        this.cantidad = cantidad;
        this.unidadManejo = unidadManejo;
        this.valor = valor;
        this.importe = importe;
        this.iva = iva;
        this.total = total;
        this.partidaCve = partidaCve;
        this.plantaCve = plantaCve;
        this.plantaDs = plantaDs;
        this.plantaAbrev = plantaAbrev;
        this.camaraCve = camaraCve;
        this.camaraDs = camaraDs;
        this.camaraAbrev = camaraAbrev;
        this.noTarimas = noTarimas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getServicioCve() {
        return servicioCve;
    }

    public void setServicioCve(int servicioCve) {
        this.servicioCve = servicioCve;
    }

    public String getServicioDs() {
        return servicioDs;
    }

    public void setServicioDs(String servicioDs) {
        this.servicioDs = servicioDs;
    }

    public BigDecimal getCantidadSer() {
        return cantidadSer;
    }

    public void setCantidadSer(BigDecimal cantidadSer) {
        this.cantidadSer = cantidadSer;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getCuotaDiaria() {
        return cuotaDiaria;
    }

    public void setCuotaDiaria(BigDecimal cuotaDiaria) {
        this.cuotaDiaria = cuotaDiaria;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getProductoCve() {
        return productoCve;
    }

    public void setProductoCve(int productoCve) {
        this.productoCve = productoCve;
    }

    public String getProductoDs() {
        return productoDs;
    }

    public void setProductoDs(String productoDs) {
        this.productoDs = productoDs;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getUnidadPeso() {
        return unidadPeso;
    }

    public void setUnidadPeso(String unidadPeso) {
        this.unidadPeso = unidadPeso;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadManejo() {
        return unidadManejo;
    }

    public void setUnidadManejo(String unidadManejo) {
        this.unidadManejo = unidadManejo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getPartidaCve() {
        return partidaCve;
    }

    public void setPartidaCve(int partidaCve) {
        this.partidaCve = partidaCve;
    }

    public int getPlantaCve() {
        return plantaCve;
    }

    public void setPlantaCve(int plantaCve) {
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

    public int getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(int camaraCve) {
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

    public BigDecimal getNoTarimas() {
        return noTarimas;
    }

    public void setNoTarimas(BigDecimal noTarimas) {
        this.noTarimas = noTarimas;
    }

    public String getPlantaCod() {
        return plantaCod;
    }

    public void setPlantaCod(String plantaCod) {
        this.plantaCod = plantaCod;
    }

    public String getServicioCod() {
        return servicioCod;
    }

    public void setServicioCod(String servicioCod) {
        this.servicioCod = servicioCod;
    }

    public ConstanciaFacturaCmp getConstancia() {
        return constancia;
    }

    public void setConstancia(ConstanciaFacturaCmp constancia) {
        this.constancia = constancia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleConstanciaCmp)) {
            return false;
        }
        DetalleConstanciaCmp other = (DetalleConstanciaCmp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetalleConstanciaCmp[ id=" + id + " ]";
    }
    
}
