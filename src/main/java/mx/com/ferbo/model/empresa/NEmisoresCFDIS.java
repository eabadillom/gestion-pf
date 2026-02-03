package mx.com.ferbo.model.empresa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.model.RegimenFiscal;

@Entity
@Table(name = "emisor")
@NamedQueries({
        @NamedQuery(name = "NEmisoresCFDIS.findAll", query = "SELECT e FROM NEmisoresCFDIS e ORDER BY e.nombre"),
        @NamedQuery(name = "NEmisoresCFDIS.findBycdEmisor", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.id = :id"),
        @NamedQuery(name = "NEmisoresCFDIS.findBynbEmisor", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.nombre = :nombre"),
        @NamedQuery(name = "NEmisoresCFDIS.findBytpPersona", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.tipoPersona = :tipoPersona"),
        @NamedQuery(name = "NEmisoresCFDIS.findByregimenCapital", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.regimenCapital = :regimenCapital"),
        @NamedQuery(name = "NEmisoresCFDIS.findByrRFC", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.rfc = :rfc"),
        @NamedQuery(name = "NEmisoresCFDIS.findByIniOperaciones", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.inicioOperacion = :inicioOperacion"),
        @NamedQuery(name = "NEmisoresCFDIS.findByultimoCambio", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.ultimoCambio = :ultimoCambio"),
        @NamedQuery(name = "NEmisoresCFDIS.findByPadron", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.padron = :padron"),
        @NamedQuery(name = "NEmisoresCFDIS.findByuuid", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.uuid = :uuid"),
        @NamedQuery(name = "NEmisoresCFDIS.findByregimenFiscal", query = "SELECT e FROM NEmisoresCFDIS e WHERE e.regimen = :regimen") })
public class NEmisoresCFDIS implements Serializable, Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_emisor")
    private Integer id;

    @Size(min = 1, max = 150)
    @Column(name = "nb_emisor")
    private String nombre;

    @Size(min = 1, max = 1)
    @Column(name = "tp_persona")
    private String tipoPersona;

    @Size(min = 0, max = 150)
    @Column(name = "nb_regimen_capital")
    private String regimenCapital;

    @Size(min = 1, max = 20)
    @Column(name = "nb_rfc")
    private String rfc;

    @Column(name = "fh_inicio_op")
    @Temporal(TemporalType.DATE)
    private Date inicioOperacion;

    @Column(name = "fh_ult_cambio")
    @Temporal(TemporalType.DATE)
    private Date ultimoCambio;

    @Size(min = 0, max = 1)
    @Column(name = "st_padron")
    private String padron;

    @JoinColumn(name = "cd_regimen")
    @ManyToOne
    private RegimenFiscal regimen;

    @Column(name = "nu_cp")
    @Size(min = 0, max = 5)
    @Basic(optional = true)
    private String codigoPostal;

    @Column(name = "uuid")
    @Size(min = 1, max = 50)
    private String uuid = null;

    @OneToMany(mappedBy = "emisor")
    private List<Certificado> certificados;

    public NEmisoresCFDIS(){
        // Constructor sin parametros
    }

    @Override
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

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getRegimenCapital() {
        return regimenCapital;
    }

    public void setRegimenCapital(String regimenCapital) {
        this.regimenCapital = regimenCapital;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Date getInicioOperacion() {
        return inicioOperacion;
    }

    public void setInicioOperacion(Date inicioOperacion) {
        this.inicioOperacion = inicioOperacion;
    }

    public Date getUltimoCambio() {
        return ultimoCambio;
    }

    public void setUltimoCambio(Date ultimoCambio) {
        this.ultimoCambio = ultimoCambio;
    }

    public String getPadron() {
        return padron;
    }

    public void setPadron(String padron) {
        this.padron = padron;
    }

    public RegimenFiscal getRegimen() {
        return regimen;
    }

    public void setRegimen(RegimenFiscal regimen) {
        this.regimen = regimen;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Certificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<Certificado> certificados) {
        this.certificados = certificados;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NEmisoresCFDIS))
            return false;

        NEmisoresCFDIS that = (NEmisoresCFDIS) o;
        return id != null && id.equals(that.id);
    }

    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NEmisoresCFDIS [id=" + id + ", nombre=" + nombre + ", tipoPersona=" + tipoPersona + ", regimenCapital="
                + regimenCapital + ", rfc=" + rfc + ", inicioOperacion=" + inicioOperacion + ", ultimoCambio="
                + ultimoCambio + ", padron=" + padron + ", codigoPostal=" + codigoPostal + ", uuid=" + uuid + "]";
    }

}
