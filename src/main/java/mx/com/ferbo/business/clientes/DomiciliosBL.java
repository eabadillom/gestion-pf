package mx.com.ferbo.business.clientes;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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

import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
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
    
    public Domicilios guardaDomicilio(Domicilios domicilioSelected, TiposDomicilio tipoDomicilioSelected) throws InventarioException 
    {
        Domicilios auxDomicilio = domicilioSelected;
        
        if(tipoDomicilioSelected == null || tipoDomicilioSelected.getDomicilioTipoCve() <= 0) {
            throw new InventarioException("Debe seleccionar un tipo de domicilio");
        }

        if(auxDomicilio.getAsentamiento() == null){
            throw new InventarioException("Debe debe haber seleccionado un asentamiento.");
        }

        if(auxDomicilio.getDomicilioCalle() == null){
            throw new InventarioException("Debe debe haber ingresado una calle.");
        }

        if(auxDomicilio.getDomicilioNumExt() == null){
            throw new InventarioException("Debe debe haber ingresado un numero exterior.");
        }

        if(auxDomicilio.getDomicilioNumInt() == null){
            throw new InventarioException("Debe debe haber ingresado un numero interior.");
        }

        if(auxDomicilio.getDomicilioTel1() == null){
            throw new InventarioException("Debe debe haber ingresado un telefono 1.");
        }

        if(auxDomicilio.getDomicilioTel2() == null){
            throw new InventarioException("Debe debe haber ingresado un telefono 2.");
        }

        if(auxDomicilio.getDomicilioFax() == null){
            throw new InventarioException("Debe debe haber ingresado un fax.");
        }

        auxDomicilio.setDomicilioTipoCve(tipoDomicilioSelected);

        //domiciliosDAO.guardar(domicilioSelected);
        log.info("Se guardo corretamente el domicilio del cliente");
        
        return auxDomicilio;
    }
    
    public ClienteDomicilios actualizarDomicilios(Cliente clienteSelected, ClienteDomicilios clienteDomicilioSelected, Domicilios domicilioSelected, TiposDomicilio tipoDomicilioSelected) throws InventarioException 
    {
        ClienteDomicilios auxClienteDomicilios = clienteDomicilioSelected;
        
        Domicilios auxDomicilios = domicilioSelected;
        auxDomicilios.setDomicilioTipoCve(tipoDomicilioSelected);

        if(auxDomicilios.getAsentamiento() == null){
            throw new InventarioException("Debe debe haber seleccionado un asentamiento.");
        }

        if(auxDomicilios.getDomicilioCalle() == null){
            throw new InventarioException("Debe debe haber ingresado una calle.");
        }

        if(auxDomicilios.getDomicilioNumExt() == null){
            throw new InventarioException("Debe debe haber ingresado un numero exterior.");
        }

        if(auxDomicilios.getDomicilioNumInt() == null){
            throw new InventarioException("Debe debe haber ingresado un numero interior.");
        }

        if(auxDomicilios.getDomicilioTel1() == null){
            throw new InventarioException("Debe debe haber ingresado un telefono 1.");
        }

        if(auxDomicilios.getDomicilioTel2() == null){
            throw new InventarioException("Debe debe haber ingresado un telefono 2.");
        }

        if(auxDomicilios.getDomicilioFax() == null){
            throw new InventarioException("Debe debe haber ingresado un fax.");
        }

        //domiciliosDAO.actualizar(auxDomicilios);
        auxClienteDomicilios.setCteCve(clienteSelected);
        auxClienteDomicilios.setDomicilios(auxDomicilios);
        clienteDomiciliosDAO.actualizar(auxClienteDomicilios);
        log.info("Se actualizo correctamente el domicilio del cliente");
        
        return auxClienteDomicilios;
    }
    
    public List<ClienteDomicilios> guardarClienteDomicilio(List<ClienteDomicilios> listClienteDomicilios, ClienteDomicilios clienteDomicilioSelected) throws InventarioException 
    {
        List<ClienteDomicilios> auxListClienteDomicilios = listClienteDomicilios;
        
        clienteDomiciliosDAO.guardar(clienteDomicilioSelected);
        auxListClienteDomicilios.add(clienteDomicilioSelected);
        log.info("Domicilio agregado correctamente");
        
        return auxListClienteDomicilios;
    }
    
    public List<ClienteDomicilios> eliminarClienteDomicilio(List<ClienteDomicilios> listClienteDomicilios, ClienteDomicilios clienteDomicilioSelected, Cliente clienteSelected) throws InventarioException 
    {
        List<ClienteDomicilios> auxListClienteDomicilios = listClienteDomicilios;
        
        ClienteDomicilios auxClienteDomicilios = clienteDomicilioSelected;

        if(auxClienteDomicilios.getDomicilios() == null){
            throw new InventarioException("Error al querer eliminar el domicilio, contacte al administrador de sistemas");
        }

        auxClienteDomicilios.setCteCve(clienteSelected);

        clienteDomiciliosDAO.eliminar(auxClienteDomicilios);

        //domiciliosDAO.eliminar(auxClienteDomicilios.getDomicilios());
        auxListClienteDomicilios.remove(auxClienteDomicilios);
        log.info("Se elimino correctamente el domicilio del cliente");
        
        return auxListClienteDomicilios;
    }

}
