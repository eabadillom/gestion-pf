/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contacto")
public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contacto")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_nombre")
    private String nombre;
    
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_apellido_1")
    private String apellido1;
    
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_apellido_2")
    private String apellido2;
    
    @Basic(optional = false)
    @Column(name = "st_habilitado")
    private Boolean statusContacto;
    
    @Basic(optional = true)
    @Size(min = 8, max = 50)
    @Column(name = "nb_usuario")
    private String usuario;
    
    @Basic(optional = true)
    @Size(min = 8, max = 1024)
    @Column(name = "nb_password")
    private String password;
    
    @Basic(optional = true)
    @Size(min = 1, max = 1)
    @Column(name = "st_usuario")
    private String statusUsuario;
    
    @Basic(optional = true)
    @Column(name = "fh_alta")
    private LocalDate fechaAlta;
    
    @Basic(optional = true)
    @Column(name = "fh_cad_passwd")
    private LocalDate caducidadPassword;
    
    @Basic(optional = true)
    @Column(name = "fh_ult_acceso")
    private LocalDate ultimoAcceso;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contacto", orphanRemoval = true)
    private List<ClienteContacto> clienteContactoList;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "contacto", orphanRemoval = true)
    private List<MedioContacto> mediosContacto;
    
    @Override 
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof Contacto)) return false;
        Contacto that = (Contacto) o;

        if (this.id != null && that.id != null) {
            return Objects.equals(this.id, that.id);
        } else {
            return this == that;
        }
    }
     
    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Contacto[ idContacto=" + id + " ]";
    }
    
    public Contacto() {
    }

    public Contacto(Integer id) {
        this.id = id;
    }
    
    public Contacto(Integer id, String nombre, String apellido1, String apellido2) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }
    
    public void add(ClienteContacto clienteContacto) {
    	if(this.clienteContactoList == null)
    		this.clienteContactoList = new ArrayList<>();
    	clienteContacto.setContacto(this);
    	this.clienteContactoList.add(clienteContacto);
    }
    
    public void remove(ClienteContacto clienteContacto) {
    	if(this.clienteContactoList == null)
    		return;
    	clienteContacto.setCliente(null);
    	this.clienteContactoList.remove(clienteContacto);
    }
    
    public void add(MedioContacto medioContacto) {
    	if(this.mediosContacto == null)
    		this.mediosContacto = new ArrayList<MedioContacto>();
    	medioContacto.setContacto(this);
    	this.mediosContacto.add(medioContacto);
    }
    
    public void remove(MedioContacto medioContacto) {
    	if(this.mediosContacto == null)
    		return;
    	medioContacto.setContacto(null);
    	this.mediosContacto.remove(medioContacto);
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
    	this.nombre = nombre == null ? null : nombre.trim();
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1 == null ? null : apellido1.trim();
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2 == null ? null : apellido2.trim();
    }

    public List<ClienteContacto> getClienteContactoList() {
        return clienteContactoList;
    }

    public void setClienteContactoList(List<ClienteContacto> clienteContactoList) {
        this.clienteContactoList = clienteContactoList;
    }
    
    public List<MedioContacto> getMediosContacto() {
        return mediosContacto;
    }

    public void setMediosContacto(List<MedioContacto> mediosContacto) {
        this.mediosContacto = mediosContacto;
    }

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario == null ? null : usuario.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getStatusUsuario() {
		return statusUsuario;
	}

	public void setStatusUsuario(String status) {
		this.statusUsuario = status;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public LocalDate getCaducidadPassword() {
		return caducidadPassword;
	}

	public void setCaducidadPassword(LocalDate caducidadPassword) {
		this.caducidadPassword = caducidadPassword;
	}

	public LocalDate getUltimoAcceso() {
		return ultimoAcceso;
	}

	public void setUltimoAcceso(LocalDate ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public Boolean getStatusContacto() {
		return statusContacto;
	}

	public void setStatusContacto(Boolean statusContacto) {
		this.statusContacto = statusContacto;
	}
    
}
