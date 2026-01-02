package mx.com.ferbo.business.n;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.dao.n.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.n.DomiciliosDAO;
import mx.com.ferbo.dao.n.TiposDomicilioDAO;

import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.TiposDomicilio;
import mx.com.ferbo.util.FacesUtils;

import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class DomiciliosBL implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(DomiciliosBL.class);
    
    @Inject
    private ClienteDomiciliosDAO clienteDomiciliosDAO;
    
    @Inject
    private DomiciliosDAO domiciliosDAO;
    
    @Inject
    private TiposDomicilioDAO tiposDomicilioDAO;
    
    public List<ClienteDomicilios> buscarDomiciliosPorCliente(Cliente clienteSelected)
    {
        return clienteDomiciliosDAO.buscaPorCliente(clienteSelected);
    }
    
    public List<Domicilios> buscarTodosDomicilios()
    {
        return domiciliosDAO.buscarTodos();
    }
    
    public List<TiposDomicilio> buscarTiposDomicilios()
    {
        return tiposDomicilioDAO.buscarTodos();
    }
    
    public List<ClienteDomicilios> filtrarListado(List<ClienteDomicilios> listAux, Cliente clienteSelected)
    {
        List<ClienteDomicilios> listClienteDomiciliosFiltered = listAux.stream()
            .filter(ps -> clienteSelected != null ? (ps.getCteCve().getCteCve().intValue() == clienteSelected.getCteCve().intValue()) : false)
            .collect(Collectors.toList());
        
        return listClienteDomiciliosFiltered;
    }
    
    public List<ClienteDomicilios> filtrarListadoDomicilio(List<ClienteDomicilios> listAux, TiposDomicilio tipoDomicilioSelected, Cliente clienteSelected)
    {
        List<ClienteDomicilios> listClienteDomiciliosFiltered = listAux.stream()
            .filter(ps -> tipoDomicilioSelected != null)
            .filter(ps -> Objects.equals(ps.getDomicilios().getDomicilioTipoCve().getDomicilioTipoCve(), tipoDomicilioSelected.getDomicilioTipoCve()))
            .filter(ps -> Objects.equals(ps.getCteCve().getCteCve(), clienteSelected.getCteCve()))
            .collect(Collectors.toList());
        return listClienteDomiciliosFiltered;
    }
    
    public List<ClienteDomicilios> filtrarListadoDomicilioFiscal(List<ClienteDomicilios> listaDomiciliosCliente)
    {
        String tipoDomicilio = "Domicilio Fiscal";
        List<ClienteDomicilios> auxListDomiciliosCliente = listaDomiciliosCliente.stream()
                .filter(ps -> ps.getDomicilios().getDomicilioTipoCve().getDomicilioTipoDesc().contains(tipoDomicilio))
            .collect(Collectors.toList());
        
        return auxListDomiciliosCliente;
    }
    
    public ClienteDomicilios nuevoClienteDomicilio(Cliente clienteSelected, Domicilios domicilioSelected) throws InventarioException 
    {
        FacesUtils.requireNonNull(clienteSelected, "Debe seleccionar un cliente");
        FacesUtils.requireNonNull(domicilioSelected, "Debe seleccionar un domicilio");
                
        ClienteDomicilios clienteDomicilios = new ClienteDomicilios();
        clienteDomicilios.setCteCve(clienteSelected);
        clienteDomicilios.setDomicilios(domicilioSelected);
        
        return clienteDomicilios;
    }
    
    public Domicilios nuevoDomicilio()
    {
        Domicilios domicilio = new Domicilios();
        domicilio.setAsentamiento(new AsentamientoHumano());
        
        AsentamientoHumanoPK pk = new AsentamientoHumanoPK();
        pk.setCiudades(new Ciudades());
        pk.getCiudades().setCiudadesPK(new CiudadesPK());
        pk.getCiudades().getCiudadesPK().setMunicipios(new Municipios());
        pk.getCiudades().getCiudadesPK().getMunicipios().setMunicipiosPK(new MunicipiosPK());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().setEstados(new Estados());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().setEstadosPK(new EstadosPK());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().setPais(new Paises());
        
        domicilio.getAsentamiento().setAsentamientoHumanoPK(pk);
        
        return domicilio;
    }
    
    public Domicilios agregarDomicilio(Domicilios domicilioSelected, TiposDomicilio tipoDomicilioSelected) throws InventarioException 
    {
        Domicilios auxDomicilio = domicilioSelected;
        
        FacesUtils.requireNonNull(tipoDomicilioSelected == null, "Debe seleccionar un tipo de domicilio");
        if(tipoDomicilioSelected.getDomicilioTipoCve() <= 0) {
            throw new InventarioException("Debe seleccionar un tipo de domicilio");
        }
        
        FacesUtils.requireNonNull(auxDomicilio.getAsentamiento(), "Debe debe haber seleccionado un asentamiento.");
        FacesUtils.requireNonNull(auxDomicilio.getDomicilioCalle(), "Debe debe haber ingresado una calle.");
        FacesUtils.requireNonNull(auxDomicilio.getDomicilioNumExt(), "Debe debe haber ingresado un numero exterior.");
        FacesUtils.requireNonNull(auxDomicilio.getDomicilioNumInt(), "Debe debe haber ingresado un numero interior.");
        FacesUtils.requireNonNull(auxDomicilio.getDomicilioTel1(), "Debe debe haber ingresado un telefono 1.");
        FacesUtils.requireNonNull(auxDomicilio.getDomicilioTel2(), "Debe debe haber ingresado un telefono 2.");
        FacesUtils.requireNonNull(auxDomicilio.getDomicilioFax(), "Debe debe haber ingresado un fax.");

        auxDomicilio.setDomicilioTipoCve(tipoDomicilioSelected);

        return auxDomicilio;
    }
    
    public ClienteDomicilios actualizarDomicilios(Cliente clienteSelected, ClienteDomicilios clienteDomicilioSelected, Domicilios domicilioSelected, TiposDomicilio tipoDomicilioSelected) throws InventarioException 
    {
        ClienteDomicilios auxClienteDomicilios = clienteDomicilioSelected;
        
        Domicilios auxDomicilios = domicilioSelected;
        auxDomicilios.setDomicilioTipoCve(tipoDomicilioSelected);

        FacesUtils.requireNonNull(auxDomicilios.getAsentamiento(), "Debe debe haber seleccionado un asentamiento.");
        FacesUtils.requireNonNull(auxDomicilios.getDomicilioCalle(), "Debe debe haber ingresado una calle.");
        FacesUtils.requireNonNull(auxDomicilios.getDomicilioNumExt(), "Debe debe haber ingresado un numero exterior.");
        FacesUtils.requireNonNull(auxDomicilios.getDomicilioNumInt(), "Debe debe haber ingresado un numero interior.");
        FacesUtils.requireNonNull(auxDomicilios.getDomicilioTel1(), "Debe debe haber ingresado un telefono 1.");
        FacesUtils.requireNonNull(auxDomicilios.getDomicilioTel2(), "Debe debe haber ingresado un telefono 2.");
        FacesUtils.requireNonNull(auxDomicilios.getDomicilioFax(), "Debe debe haber ingresado un fax.");
        
        auxClienteDomicilios.setCteCve(clienteSelected);
        auxClienteDomicilios.setDomicilios(auxDomicilios);
        
        return auxClienteDomicilios;
    }

}
