/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ferbo.controller.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.business.clientes.AvisosClienteBL;
import mx.com.ferbo.dao.AvisoDAO;
import mx.com.ferbo.dao.CategoriaDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.ClienteDomiciliosDAO;
import mx.com.ferbo.dao.CuotaMinimaDAO;
import mx.com.ferbo.dao.DomiciliosDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.dao.ServicioDAO;
import mx.com.ferbo.dao.UdCobroDAO;
import mx.com.ferbo.dao.UnidadDeManejoDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ClienteDomicilios;
import mx.com.ferbo.model.CuotaMinima;
import mx.com.ferbo.model.Domicilios;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.UdCobro;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author julio
 */
@Named("avisosComercial")
@ViewScoped
public class AvisosComercialBean implements Serializable {

    private static final long serialVersionUID = -626048119540963939L;
    private static Logger log = LogManager.getLogger(AvisosComercialBean.class);

    //Objetos para clientes
    private List<Cliente> lstClientes;
    private Cliente clienteSelected;
    private ClienteDAO clienteDAO;

    // Objetos para avisos
    private List<Aviso> lstAvisos;
    private Aviso avisoSelected;
    private Aviso aviso;
    private AvisoDAO avisoDAO;

    // Objetos para Domicilios
    private Domicilios domicilios;
    private DomiciliosDAO domiciliosDAO;

    //Objetos para Cliente Domicilios
    private ClienteDomicilios clienteDomicilioSelected;
    private ClienteDomiciliosDAO clienteDomiciliosDAO;

    // Objetos para cuota mínima
    private CuotaMinima cuotaMinimaSelected;
    private CuotaMinimaDAO cuotaMinimaDAO;
    private boolean hasCuotaMinima;
    private List<CuotaMinima> lstCuotaMinima;

    //Objetos para Servicios
    private Servicio servicioSelected;
    private ServicioDAO servicioDAO;
    //private List<Servicio> lstServicios;
    private List<Servicio> lstServiciosAviso;

    // Objetos para Servicios por cliente
    private PrecioServicio precioServicioSelected;
    private PrecioServicioDAO precioServicioDAO;
    private List<PrecioServicio> lstPrecioServicio;
    private List<PrecioServicio> lstPrecioServicioSelected;
    private List<PrecioServicio> lstPrecioServicioAviso;

    // Objetos para Plantas
    private List<Planta> lstPlanta;
    private PlantaDAO plantaDAO;

    // Objetos para Categoria
    private Integer categoriaSelected;
    private CategoriaDAO categoriaDAO;
    private List<Categoria> lstCategoria;

    // Objetos para Unidad de Cobro
    private UdCobro udCobroSelected;
    private UdCobroDAO udCobroDAO;

    // Objetos para unidad de Manejo
    private UnidadDeManejo unidadDeManejoSelected;
    private UnidadDeManejoDAO unidadDeManejoDAO;

    // Objetos auxiliares
    private String renderAvisosTable;
    private String avisoTipoFacturacion;
    private Integer plantaCveSelected;

    private FacesContext faceContext;
    private HttpServletRequest request;

    public AvisosComercialBean() {
        clienteDAO = new ClienteDAO();
        domiciliosDAO = new DomiciliosDAO();
        servicioDAO = new ServicioDAO();
        clienteDomiciliosDAO = new ClienteDomiciliosDAO();
        avisoDAO = new AvisoDAO();
        lstClientes = new ArrayList<>();
        lstAvisos = new ArrayList<>();
        cuotaMinimaSelected = new CuotaMinima();
        hasCuotaMinima = false;
        cuotaMinimaDAO = new CuotaMinimaDAO();
        lstCuotaMinima = new ArrayList<>();
        this.setRenderAvisosTable("false");
        categoriaDAO = new CategoriaDAO();
        plantaDAO = new PlantaDAO();
        precioServicioDAO = new PrecioServicioDAO();
        lstPrecioServicio = new ArrayList<>();

        udCobroDAO = new UdCobroDAO();
        unidadDeManejoDAO = new UnidadDeManejoDAO();
        lstPrecioServicioSelected = new ArrayList<>();

    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        // Ya no cargar nada aquí
        faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        lstCategoria = categoriaDAO.buscarTodos();
        categoriaSelected = 1;
        lstPlanta = plantaDAO.findall();
    }

    public void onTabAvisosComercial() {

        faceContext = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        lstClientes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        lstCategoria = categoriaDAO.buscarTodos();
        categoriaSelected = 1;
        lstPlanta = plantaDAO.findall();

    }

    public void filtraAvisos() {

        if (clienteSelected != null) {
            lstAvisos = AvisosClienteBL.obtenerAvisosPorCliente(clienteSelected.getCteCve());
            this.setRenderAvisosTable("true");
            PrimeFaces.current().ajax().update("form:dt-avisos");
        }
    }

    public void buscaPrecioServicioAviso(Aviso a) {
        // Cargar aviso con detalles
        Aviso avisoCompleto = AvisosClienteBL.cargarAvisoCompleto(a.getAvisoCve());

        this.aviso = avisoCompleto;
        this.avisoSelected = avisoCompleto;
        this.plantaCveSelected = avisoCompleto.getPlantaCve().getPlantaCve();

        // Buscar servicios disponibles sin aviso asignado
        lstPrecioServicio = AvisosClienteBL.buscarServiciosDisponibles(
                clienteSelected.getCteCve(),
                avisoCompleto.getAvisoCve()
        );

        // Buscar servicios asignados al aviso
        PrecioServicio criterio = new PrecioServicio();
        criterio.setCliente(clienteSelected);
        criterio.setAvisoCve(avisoCompleto);

        lstPrecioServicioAviso = AvisosClienteBL.buscarServiciosPorCriterio(criterio);

        PrimeFaces.current().ajax().update("soPlantaAct");
    }

    public void estableceCuotaMinima() {
        cuotaMinimaSelected = AvisosClienteBL.establecerCuotaMinima(
                cuotaMinimaSelected,
                clienteSelected,
                hasCuotaMinima
        );

        if (!hasCuotaMinima) {
            hasCuotaMinima = false;
        }
    }

    public boolean buscaCuotaMinima() {
        Optional<CuotaMinima> resultado = AvisosClienteBL.buscarCuotaMinima(cuotaMinimaSelected, clienteSelected);

        if (resultado.isPresent()) {
            cuotaMinimaSelected = resultado.get();
            this.setHasCuotaMinima(true);
            return true;
        }

        return false;
    }

    public void cambiaCuotaMinima() {
        if (!hasCuotaMinima) {
            estableceCuotaMinima(); // ya usa el método refactorizado
        }
        PrimeFaces.current().ajax().update("form:dt-avisos", "form:monto-minimo");
    }

    public void nuevoAviso() {
        this.categoriaSelected = 1;
        this.avisoSelected = AvisosClienteBL.crearNuevoAviso(clienteSelected, this.categoriaSelected);
    }

    public void guardaAviso() {
        AvisosClienteBL.guardarAviso(avisoSelected, categoriaSelected, plantaCveSelected);
        filtraAvisos(); // se queda aquí porque es parte del flujo de UI

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Agregado"));
        PrimeFaces.current().ajax().update("form:messages", "dt-avisos");
    }

    public void actualizaAviso() {
        AvisosClienteBL.actualizarAviso(avisoSelected, lstPrecioServicioAviso, plantaCveSelected);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicio Actualizado"));
        PrimeFaces.current().executeScript("PF('addAvisoDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-avisos");

        resetSelectedItems();
    }

    public void eliminaAviso() {
        String resultado = AvisosClienteBL.eliminarAviso(avisoSelected);
        FacesMessage message;

        if ("OK".equals(resultado)) {
            lstAvisos.remove(avisoSelected);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Avisos", "El aviso se eliminó correctamente");
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Avisos", "El aviso no puede ser eliminado...");
        }

        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update("form:dt-avisos", "form:messages");
    }

    public void eliminaServicio(PrecioServicio ps) {
        FacesMessage message;
        FacesMessage.Severity severity;
        String mensaje;

        try {
            lstPrecioServicioAviso = AvisosClienteBL.eliminarServicioDeAviso(ps, avisoSelected, clienteSelected);
            severity = FacesMessage.SEVERITY_INFO;
            mensaje = "Servicio eliminado correctamente.";
        } catch (InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.";
            severity = FacesMessage.SEVERITY_ERROR;
        }

        message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update(":form:messages", "dt-servicioAviso");
    }

    public void remueveAviso() {
        AvisosClienteBL.eliminarListadoPrecioServicios(lstPrecioServicioSelected);
        PrimeFaces.current().ajax().update("form:dt-avisos", "form:panel-actAviso");
    }

    public void agregaServicio() {
        FacesMessage message;
        FacesMessage.Severity severity;
        String mensaje;

        try {
            AvisosClienteBL.agregarServiciosAAviso(
                    lstPrecioServicioSelected,
                    lstPrecioServicioAviso,
                    avisoSelected,
                    clienteSelected
            );

            this.buscaPrecioServicioAviso(avisoSelected);

            severity = FacesMessage.SEVERITY_INFO;
            mensaje = "Servicio agregado correctamente.";
        } catch (InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Problema para guardar servicio(s) ...", ex);
            mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.";
            severity = FacesMessage.SEVERITY_ERROR;
        }

        message = new FacesMessage(severity, "Catálogo de clientes", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().update(
                "form:dt-avisos",
                "form:panel-actAviso",
                "form:dt-servicioAviso",
                "form:dt-servicioSinAviso",
                "messages"
        );
    }

    public void resetSelectedItems() {
        this.plantaCveSelected = null;
        this.categoriaSelected = null;
    }

    public List<Cliente> getLstClientes() {
        return lstClientes;
    }

    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
    }

    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    public void setClienteDAO(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public List<Aviso> getLstAvisos() {
        return lstAvisos;
    }

    public void setLstAvisos(List<Aviso> lstAvisos) {
        this.lstAvisos = lstAvisos;
    }

    public Aviso getAvisoSelected() {
        return avisoSelected;
    }

    public void setAvisoSelected(Aviso avisoSelected) {
        this.avisoSelected = avisoSelected;
    }

    public Aviso getAviso() {
        return aviso;
    }

    public void setAviso(Aviso aviso) {
        this.aviso = aviso;
    }

    public AvisoDAO getAvisoDAO() {
        return avisoDAO;
    }

    public void setAvisoDAO(AvisoDAO avisoDAO) {
        this.avisoDAO = avisoDAO;
    }

    public Domicilios getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(Domicilios domicilios) {
        this.domicilios = domicilios;
    }

    public DomiciliosDAO getDomiciliosDAO() {
        return domiciliosDAO;
    }

    public void setDomiciliosDAO(DomiciliosDAO domiciliosDAO) {
        this.domiciliosDAO = domiciliosDAO;
    }

    public ClienteDomicilios getClienteDomicilioSelected() {
        return clienteDomicilioSelected;
    }

    public void setClienteDomicilioSelected(ClienteDomicilios clienteDomicilioSelected) {
        this.clienteDomicilioSelected = clienteDomicilioSelected;
    }

    public ClienteDomiciliosDAO getClienteDomiciliosDAO() {
        return clienteDomiciliosDAO;
    }

    public void setClienteDomiciliosDAO(ClienteDomiciliosDAO clienteDomiciliosDAO) {
        this.clienteDomiciliosDAO = clienteDomiciliosDAO;
    }

    public CuotaMinima getCuotaMinimaSelected() {
        return cuotaMinimaSelected;
    }

    public void setCuotaMinimaSelected(CuotaMinima cuotaMinimaSelected) {
        this.cuotaMinimaSelected = cuotaMinimaSelected;
    }

    public CuotaMinimaDAO getCuotaMinimaDAO() {
        return cuotaMinimaDAO;
    }

    public void setCuotaMinimaDAO(CuotaMinimaDAO cuotaMinimaDAO) {
        this.cuotaMinimaDAO = cuotaMinimaDAO;
    }

    public boolean isHasCuotaMinima() {
        return hasCuotaMinima;
    }

    public void setHasCuotaMinima(boolean hasCuotaMinima) {
        this.hasCuotaMinima = hasCuotaMinima;
    }

    public List<CuotaMinima> getLstCuotaMinima() {
        return lstCuotaMinima;
    }

    public void setLstCuotaMinima(List<CuotaMinima> lstCuotaMinima) {
        this.lstCuotaMinima = lstCuotaMinima;
    }

    public Servicio getServicioSelected() {
        return servicioSelected;
    }

    public void setServicioSelected(Servicio servicioSelected) {
        this.servicioSelected = servicioSelected;
    }

    public ServicioDAO getServicioDAO() {
        return servicioDAO;
    }

    public void setServicioDAO(ServicioDAO servicioDAO) {
        this.servicioDAO = servicioDAO;
    }

    public List<Servicio> getLstServiciosAviso() {
        return lstServiciosAviso;
    }

    public void setLstServiciosAviso(List<Servicio> lstServiciosAviso) {
        this.lstServiciosAviso = lstServiciosAviso;
    }

    public PrecioServicio getPrecioServicioSelected() {
        return precioServicioSelected;
    }

    public void setPrecioServicioSelected(PrecioServicio precioServicioSelected) {
        this.precioServicioSelected = precioServicioSelected;
    }

    public PrecioServicioDAO getPrecioServicioDAO() {
        return precioServicioDAO;
    }

    public void setPrecioServicioDAO(PrecioServicioDAO precioServicioDAO) {
        this.precioServicioDAO = precioServicioDAO;
    }

    public List<PrecioServicio> getLstPrecioServicio() {
        return lstPrecioServicio;
    }

    public void setLstPrecioServicio(List<PrecioServicio> lstPrecioServicio) {
        this.lstPrecioServicio = lstPrecioServicio;
    }

    public List<PrecioServicio> getLstPrecioServicioSelected() {
        return lstPrecioServicioSelected;
    }

    public void setLstPrecioServicioSelected(List<PrecioServicio> lstPrecioServicioSelected) {
        this.lstPrecioServicioSelected = lstPrecioServicioSelected;
    }

    public List<PrecioServicio> getLstPrecioServicioAviso() {
        return lstPrecioServicioAviso;
    }

    public void setLstPrecioServicioAviso(List<PrecioServicio> lstPrecioServicioAviso) {
        this.lstPrecioServicioAviso = lstPrecioServicioAviso;
    }

    public List<Planta> getLstPlanta() {
        return lstPlanta;
    }

    public void setLstPlanta(List<Planta> lstPlanta) {
        this.lstPlanta = lstPlanta;
    }

    public PlantaDAO getPlantaDAO() {
        return plantaDAO;
    }

    public void setPlantaDAO(PlantaDAO plantaDAO) {
        this.plantaDAO = plantaDAO;
    }

    public Integer getCategoriaSelected() {
        return categoriaSelected;
    }

    public void setCategoriaSelected(Integer categoriaSelected) {
        this.categoriaSelected = categoriaSelected;
    }

    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public List<Categoria> getLstCategoria() {
        return lstCategoria;
    }

    public void setLstCategoria(List<Categoria> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }

    public UdCobro getUdCobroSelected() {
        return udCobroSelected;
    }

    public void setUdCobroSelected(UdCobro udCobroSelected) {
        this.udCobroSelected = udCobroSelected;
    }

    public UdCobroDAO getUdCobroDAO() {
        return udCobroDAO;
    }

    public void setUdCobroDAO(UdCobroDAO udCobroDAO) {
        this.udCobroDAO = udCobroDAO;
    }

    public UnidadDeManejo getUnidadDeManejoSelected() {
        return unidadDeManejoSelected;
    }

    public void setUnidadDeManejoSelected(UnidadDeManejo unidadDeManejoSelected) {
        this.unidadDeManejoSelected = unidadDeManejoSelected;
    }

    public UnidadDeManejoDAO getUnidadDeManejoDAO() {
        return unidadDeManejoDAO;
    }

    public void setUnidadDeManejoDAO(UnidadDeManejoDAO unidadDeManejoDAO) {
        this.unidadDeManejoDAO = unidadDeManejoDAO;
    }

    public String getRenderAvisosTable() {
        return renderAvisosTable;
    }

    public void setRenderAvisosTable(String renderAvisosTable) {
        this.renderAvisosTable = renderAvisosTable;
    }

    public String getAvisoTipoFacturacion() {
        return avisoTipoFacturacion;
    }

    public void setAvisoTipoFacturacion(String avisoTipoFacturacion) {
        this.avisoTipoFacturacion = avisoTipoFacturacion;
    }

    public Integer getPlantaCveSelected() {
        return plantaCveSelected;
    }

    public void setPlantaCveSelected(Integer plantaCveSelected) {
        this.plantaCveSelected = plantaCveSelected;
    }

    public FacesContext getFaceContext() {
        return faceContext;
    }

    public void setFaceContext(FacesContext faceContext) {
        this.faceContext = faceContext;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}
