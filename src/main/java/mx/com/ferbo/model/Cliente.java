/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "cliente")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c ORDER BY c.nombre"),
    @NamedQuery(name = "Cliente.findByCteCve",  query = "SELECT c FROM Cliente c WHERE c.cteCve = :cteCve"),
    @NamedQuery(name = "Cliente.findByCteNombre",  query = "SELECT c FROM Cliente c WHERE c.nombre = :cteNombre"),
    @NamedQuery(name = "Cliente.findByCteRfc",  query = "SELECT c FROM Cliente c  WHERE c.cteRfc = :cteRfc"),
    @NamedQuery(name = "Cliente.findByNumeroCte",  query = "SELECT c FROM Cliente c  WHERE c.numeroCte = :numeroCte"),
    @NamedQuery(name = "Cliente.findByCteMail",  query = "SELECT c FROM Cliente c WHERE c.cteMail = :cteMail"),
    @NamedQuery(name = "Cliente.findByHabilitado",  query = "SELECT c FROM Cliente c  WHERE c.habilitado = :habilitado ORDER BY c.nombre"),
    @NamedQuery(name = "Cliente.findByCodUnico",  query = "SELECT c FROM Cliente c WHERE c.codUnico = :codUnico")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CTE_CVE")
    private Integer cteCve;
    
    @Size(max = 150)
    @Column(name = "CTE_NOMBRE")
    private String nombre;

    @Size(max=150)
    @Column(name = "nb_alias")
    private String alias;
    
    
    @Size(max = 20)
    @Column(name = "CTE_RFC")
    private String cteRfc;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "numero_cte")
    private String numeroCte;
    
    @Size(max = 255)
    @Column(name = "cte_mail")
    private String cteMail;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "habilitado")
    private boolean habilitado;
    
    @Size(max = 3)
    @Column(name = "COD_UNICO")
    private String codUnico;
    
    @Size(min = 1, max = 1)
    @Column(name = "tp_persona")
    private String tipoPersona;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "cd_regimen", referencedColumnName = "cd_regimen")
    private RegimenFiscal  regimenFiscal;
    
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "cd_uso_cfdi", referencedColumnName = "cd_uso_cfdi")
    private UsoCfdi usoCfdi;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cd_metodo_pago", referencedColumnName = "cd_metodo_pago")
    private MetodoPago metodoPago;
    
    @Column(name="cd_forma_pago")
    private String formaPago;
    
    @Size(max = 150)
    @Column(name = "nb_regimen_capital")
    private String regimenCapital;
    
    @Size(max = 50)
    @Column(name = "uuid")
    private String uuid;
    
    @OneToMany(mappedBy = "clienteCve", fetch = FetchType.LAZY)
    private List<ConstanciaServicios> constanciaServiciosList;
    
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Factura> facturaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cteCve", fetch = FetchType.LAZY)
    private List<ProductoPorCliente> productoPorClienteList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cteCve", fetch = FetchType.LAZY)
    private List<ClienteDomicilios> clienteDomiciliosList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<DetalleFacturacion> detalleFacturacionList;
    
    @OneToMany(mappedBy = "cteCve", fetch = FetchType.LAZY)
    private List<Aviso> avisoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cteCve", fetch = FetchType.LAZY)
    private List<CuotaMensualServicio> cuotaMensualServicioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<PrecioServicio> precioServicioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCliente", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ClienteContacto> clienteContactoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cteCve", fetch = FetchType.LAZY)
    private List<ConstanciaDeServicio> constanciaDeServicioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cteCve", fetch = FetchType.LAZY)
    private List<ConstanciaDeDeposito> constanciaDeDepositoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clienteCve", fetch = FetchType.LAZY)
    private List<ConstanciaSalida> constanciaSalidaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<CuotaMinima> cuotaMinimaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<ConstanciaTraspaso> constanciaTraspasoList;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "cliente", fetch = FetchType.LAZY, orphanRemoval = true)
    private CandadoSalida candadoSalida;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "serieConstanciaPK.cliente", orphanRemoval = true)
    private List<SerieConstancia> serieConstanciaList;
    
    public Cliente() {
    }
    
    public Cliente(Integer cteCve) {
        this.cteCve = cteCve;
    }
    
    public Cliente(Integer cteCve, String numeroCte, boolean habilitado) {
        this.cteCve = cteCve;
        this.numeroCte = numeroCte;
        this.habilitado = habilitado;
    }
    
    public void add(ClienteContacto clienteContacto) {
        if(this.clienteContactoList == null)
        this.clienteContactoList = new ArrayList<ClienteContacto>();
    	clienteContacto.setIdCliente(this);
    	this.clienteContactoList.add(clienteContacto);
    }
    
    public void remove(ClienteContacto clienteContacto) {
        if(this.clienteContactoList == null)
        return;
    	clienteContacto.setIdCliente(null);
    	this.clienteContactoList.remove(clienteContacto);
    }
    
    public void remove(CandadoSalida candadoSalida) {
        candadoSalida.setCliente(null);
    	this.candadoSalida = null;
    }
    
	public Integer getCteCve() {
        return cteCve;
    }
    
    public void setCteCve(Integer cteCve) {
        this.cteCve = cteCve;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String cteNombre) {
    	if(cteNombre != null)
    		cteNombre = cteNombre.trim();
        this.nombre = cteNombre;
    }
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
    	if(alias != null)
    		alias = alias.trim();
        this.alias = alias;
    }
    
    public String getCteRfc() {
        return cteRfc;
    }
    
    public void setCteRfc(String cteRfc) {
    	if(cteRfc != null)
    		cteRfc = cteRfc.trim();
        this.cteRfc = cteRfc;
    }
    
    public String getNumeroCte() {
        return numeroCte;
    }
    
    public void setNumeroCte(String numeroCte) {
    	if(numeroCte != null)
    		numeroCte = numeroCte.trim();
        this.numeroCte = numeroCte;
    }
    
    public String getCteMail() {
        return cteMail;
    }

    public void setCteMail(String cteMail) {
    	if(cteMail != null)
    		cteMail = cteMail.trim();
        this.cteMail = cteMail;
    }

    public boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getCodUnico() {
        return codUnico;
    }

    public void setCodUnico(String codUnico) {
    	if(codUnico != null)
    		codUnico = codUnico.trim();
        this.codUnico = codUnico;
    }

    public List<ConstanciaServicios> getConstanciaServiciosList() {
        return constanciaServiciosList;
    }

    public void setConstanciaServiciosList(List<ConstanciaServicios> constanciaServiciosList) {
        this.constanciaServiciosList = constanciaServiciosList;
    }

    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    public List<ProductoPorCliente> getProductoPorClienteList() {
        return productoPorClienteList;
    }

    public void setProductoPorClienteList(List<ProductoPorCliente> productoPorClienteList) {
        this.productoPorClienteList = productoPorClienteList;
    }

    public List<ClienteDomicilios> getClienteDomiciliosList() {
        return clienteDomiciliosList;
    }

    public void setClienteDomiciliosList(List<ClienteDomicilios> clienteDomiciliosList) {
        this.clienteDomiciliosList = clienteDomiciliosList;
    }

    public List<DetalleFacturacion> getDetalleFacturacionList() {
        return detalleFacturacionList;
    }

    public void setDetalleFacturacionList(List<DetalleFacturacion> detalleFacturacionList) {
        this.detalleFacturacionList = detalleFacturacionList;
    }

    public List<Aviso> getAvisoList() {
        return avisoList;
    }

    public void setAvisoList(List<Aviso> avisoList) {
        this.avisoList = avisoList;
    }

    public List<CuotaMensualServicio> getCuotaMensualServicioList() {
        return cuotaMensualServicioList;
    }

    public void setCuotaMensualServicioList(List<CuotaMensualServicio> cuotaMensualServicioList) {
        this.cuotaMensualServicioList = cuotaMensualServicioList;
    }

    public List<PrecioServicio> getPrecioServicioList() {
        return precioServicioList;
    }

    public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
        this.precioServicioList = precioServicioList;
    }

    public List<ClienteContacto> getClienteContactoList() {
        return clienteContactoList;
    }

    public void setClienteContactoList(List<ClienteContacto> clienteContactoList) {
        this.clienteContactoList = clienteContactoList;
    }

    public List<ConstanciaDeServicio> getConstanciaDeServicioList() {
        return constanciaDeServicioList;
    }

    public void setConstanciaDeServicioList(List<ConstanciaDeServicio> constanciaDeServicioList) {
        this.constanciaDeServicioList = constanciaDeServicioList;
    }

    public List<ConstanciaDeDeposito> getConstanciaDeDepositoList() {
        return constanciaDeDepositoList;
    }

    public void setConstanciaDeDepositoList(List<ConstanciaDeDeposito> constanciaDeDepositoList) {
        this.constanciaDeDepositoList = constanciaDeDepositoList;
    }

    public List<ConstanciaSalida> getConstanciaSalidaList() {
        return constanciaSalidaList;
    }

    public void setConstanciaSalidaList(List<ConstanciaSalida> constanciaSalidaList) {
        this.constanciaSalidaList = constanciaSalidaList;
    }

    public List<CuotaMinima> getCuotaMinimaList() {
        return cuotaMinimaList;
    }

    public void setCuotaMinimaList(List<CuotaMinima> cuotaMinimaList) {
        this.cuotaMinimaList = cuotaMinimaList;
    }

    public List<ConstanciaTraspaso> getConstanciaTraspasoList() {
        return constanciaTraspasoList;
    }

    public void setConstanciaTraspasoList(List<ConstanciaTraspaso> constanciaTraspasoList) {
        this.constanciaTraspasoList = constanciaTraspasoList;
    }


	@Override
    public int hashCode() {
        int hash = 0;
        hash += (cteCve != null ? cteCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.cteCve == null && other.cteCve != null) || (this.cteCve != null && !this.cteCve.equals(other.cteCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Cliente[ cteCve=" + cteCve + " ]";
    }

	public String getRegimenCapital() {
		return regimenCapital;
	}

	public void setRegimenCapital(String regimenCapital) {
		this.regimenCapital = regimenCapital;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public RegimenFiscal getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(RegimenFiscal regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public UsoCfdi getUsoCfdi() {
		return usoCfdi;
	}

	public void setUsoCfdi(UsoCfdi usoCfdi) {
		this.usoCfdi = usoCfdi;
	}

	public MetodoPago getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(MetodoPago metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public CandadoSalida getCandadoSalida() {
		return candadoSalida;
	}

	public void setCandadoSalida(CandadoSalida candadoSalida) {
		this.candadoSalida = candadoSalida;
	}
	
	public List<SerieConstancia> getSerieConstanciaList() {
		return serieConstanciaList;
	}

	public void setSerieConstanciaList(List<SerieConstancia> serieConstanciaList) {
		this.serieConstanciaList = serieConstanciaList;
	}

	public void addSerieConstancia(SerieConstancia serie) {
		if(this.serieConstanciaList == null)
			this.serieConstanciaList = new ArrayList<SerieConstancia>();
		
		if(serie.getSerieConstanciaPK() == null)
			serie.setSerieConstanciaPK(new SerieConstanciaPK());
		
		serie.getSerieConstanciaPK().setCliente(this);
		
		this.serieConstanciaList.add(serie);
	}

	
    
}
