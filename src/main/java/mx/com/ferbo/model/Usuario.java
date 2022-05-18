/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByUsuario", query = "SELECT u FROM Usuario u WHERE u.usuario = :usuario"),
    @NamedQuery(name = "Usuario.findByPassword", query = "SELECT u FROM Usuario u WHERE u.password = :password"),
    @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuario.findByDescripcion", query = "SELECT u FROM Usuario u WHERE u.descripcion = :descripcion"),
    @NamedQuery(name = "Usuario.findByPerfil", query = "SELECT u FROM Usuario u WHERE u.perfil = :perfil"),
    @NamedQuery(name = "Usuario.findByApellido1", query = "SELECT u FROM Usuario u WHERE u.apellido1 = :apellido1"),
    @NamedQuery(name = "Usuario.findByApellido2", query = "SELECT u FROM Usuario u WHERE u.apellido2 = :apellido2"),
    @NamedQuery(name = "Usuario.findByMail", query = "SELECT u FROM Usuario u WHERE u.mail = :mail"),
    @NamedQuery(name = "Usuario.findByIdPlanta", query = "SELECT u FROM Usuario u WHERE u.idPlanta = :idPlanta"),
    @NamedQuery(name = "Usuario.findByStNtfSrvExt", query = "SELECT u FROM Usuario u WHERE u.stNtfSrvExt = :stNtfSrvExt"),
    @NamedQuery(name = "Usuario.findByStUsuario", query = "SELECT u FROM Usuario u WHERE u.stUsuario = :stUsuario")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "password")
    private String password;
    @Size(max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil")
    private int perfil;
    @Size(max = 50)
    @Column(name = "apellido_1")
    private String apellido1;
    @Size(max = 50)
    @Column(name = "apellido_2")
    private String apellido2;
    @Size(max = 100)
    @Column(name = "mail")
    private String mail;
    @Column(name = "id_planta")
    private Integer idPlanta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "st_ntf_srv_ext")
    private boolean stNtfSrvExt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "st_usuario")
    private String stUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<LogSeguridad> logSeguridadList;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String usuario, String password, int perfil, boolean stNtfSrvExt, String stUsuario) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.perfil = perfil;
        this.stNtfSrvExt = stNtfSrvExt;
        this.stUsuario = stUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(Integer idPlanta) {
        this.idPlanta = idPlanta;
    }

    public boolean getStNtfSrvExt() {
        return stNtfSrvExt;
    }

    public void setStNtfSrvExt(boolean stNtfSrvExt) {
        this.stNtfSrvExt = stNtfSrvExt;
    }

    public String getStUsuario() {
        return stUsuario;
    }

    public void setStUsuario(String stUsuario) {
        this.stUsuario = stUsuario;
    }

    public List<LogSeguridad> getLogSeguridadList() {
        return logSeguridadList;
    }

    public void setLogSeguridadList(List<LogSeguridad> logSeguridadList) {
        this.logSeguridadList = logSeguridadList;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Usuario[ id=" + id + " ]";
    }
    
}
