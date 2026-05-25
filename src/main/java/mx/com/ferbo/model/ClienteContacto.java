/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "cliente_contacto")
@NamedQueries({
        @NamedQuery(name = "ClienteContacto.findAll", query = "SELECT c FROM ClienteContacto c"),
        @NamedQuery(name = "ClienteContacto.findByStHabilitado", query = "SELECT c FROM ClienteContacto c WHERE c.stHabilitado = :stHabilitado"),
        @NamedQuery(name = "ClienteContacto.findByNbUsuario", query = "SELECT c FROM ClienteContacto c WHERE c.nbUsuario = :nbUsuario"),
        @NamedQuery(name = "ClienteContacto.findByNbPassword", query = "SELECT c FROM ClienteContacto c WHERE c.nbPassword = :nbPassword"),
        @NamedQuery(name = "ClienteContacto.findByStUsuario", query = "SELECT c FROM ClienteContacto c WHERE c.stUsuario = :stUsuario"),
        @NamedQuery(name = "ClienteContacto.findByFhAlta", query = "SELECT c FROM ClienteContacto c WHERE c.fhAlta = :fhAlta"),
        @NamedQuery(name = "ClienteContacto.findByFhCadPasswd", query = "SELECT c FROM ClienteContacto c WHERE c.fhCadPasswd = :fhCadPasswd"),
        @NamedQuery(name = "ClienteContacto.findByFhUltAcceso", query = "SELECT c FROM ClienteContacto c WHERE c.fhUltAcceso = :fhUltAcceso"),
        @NamedQuery(name = "ClienteContacto.findById", query = "SELECT c FROM ClienteContacto c WHERE c.id = :id"),
        @NamedQuery(name = "ClienteContacto.findAllByIdCliente", query = "SELECT DISTINCT cc FROM ClienteContacto cc "
                +
                "LEFT JOIN FETCH cc.idContacto co " +
                "LEFT JOIN FETCH co.medioCntList " +
                "WHERE cc.idCliente.cteCve = :idCliente") })
public class ClienteContacto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name = "st_habilitado")
    private boolean stHabilitado;

    @Size(max = 50)
    @Column(name = "nb_usuario")
    private String nbUsuario;

    @Size(max = 1024)
    @Column(name = "nb_password")
    private String nbPassword;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "st_usuario")
    private String stUsuario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fh_alta")
    @Temporal(TemporalType.DATE)
    private Date fhAlta;

    @Column(name = "fh_cad_passwd")
    @Temporal(TemporalType.DATE)
    private Date fhCadPasswd;

    @Column(name = "fh_ult_acceso")
    @Temporal(TemporalType.DATE)
    private Date fhUltAcceso;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "id_cliente", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente idCliente;

    @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Contacto idContacto;

    @Column(name = "st_facturacion")
    private Boolean recibeFacturacion;

    @Column(name = "st_inventario")
    private Boolean recibeInventario;
    
  
    public ClienteContacto() {
        idCliente = new Cliente();
        idContacto = new Contacto();
    }

    public ClienteContacto(Integer id) {
        this.id = id;
    }

    public ClienteContacto(Integer id, boolean stHabilitado, String stUsuario, Date fhAlta) {
        this.id = id;
        this.stHabilitado = stHabilitado;
        this.stUsuario = stUsuario;
        this.fhAlta = fhAlta;
    }

    public boolean getStHabilitado() {
        return stHabilitado;
    }

    public void setStHabilitado(boolean stHabilitado) {
        this.stHabilitado = stHabilitado;
    }

    public String getNbUsuario() {
        return nbUsuario;
    }

    public void setNbUsuario(String nbUsuario) {
        this.nbUsuario = nbUsuario;
    }

    public String getNbPassword() {
        return nbPassword;
    }

    public void setNbPassword(String nbPassword) {
        this.nbPassword = nbPassword;
    }

    public String getStUsuario() {
        return stUsuario;
    }

    public void setStUsuario(String stUsuario) {
        this.stUsuario = stUsuario;
    }

    public Date getFhAlta() {
        return fhAlta;
    }

    public void setFhAlta(Date fhAlta) {
        this.fhAlta = fhAlta;
    }

    public Date getFhCadPasswd() {
        return fhCadPasswd;
    }

    public void setFhCadPasswd(Date fhCadPasswd) {
        this.fhCadPasswd = fhCadPasswd;
    }

    public Date getFhUltAcceso() {
        return fhUltAcceso;
    }

    public void setFhUltAcceso(Date fhUltAcceso) {
        this.fhUltAcceso = fhUltAcceso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Contacto getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Contacto idContacto) {
        this.idContacto = idContacto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteContacto))
            return false;

        ClienteContacto that = (ClienteContacto) o;

        if (this.id != null && that.id != null) {
            return Objects.equals(this.id, that.id);
        } else {
            return this == that;
        }
    }

    @Override 
    public int hashCode(){
        return (id != null) ? id.hashCode() : System.identityHashCode(this);    
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ClienteContacto[ id=" + id + " ]";
    }

    public Boolean getRecibeFacturacion() {
        return recibeFacturacion;
    }

    public void setRecibeFacturacion(Boolean recibeFacturacion) {
        this.recibeFacturacion = recibeFacturacion;
    }

    public Boolean getRecibeInventario() {
        return recibeInventario;
    }

    public void setRecibeInventario(Boolean recibeInventario) {
        this.recibeInventario = recibeInventario;
    }

}
