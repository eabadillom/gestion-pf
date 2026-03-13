package mx.com.ferbo.business.salidas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.business.n.PartidaBL;
import mx.com.ferbo.business.n.ProductoBL;
import mx.com.ferbo.business.n.UnidadManejoBL;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.ui.SalidaUI;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class OrdenSalidasBL 
{
    private static Logger log = LogManager.getLogger(OrdenSalidasBL.class);
    
    @Inject
    private PartidaBL partidaBL;
    
    @Inject
    private ProductoBL productoBL;

    @Inject
    private UnidadManejoBL unidadDeManejoBL;
    
    public List<SalidaUI> procesarOrdenesSalida(List<OrdenDeSalidas> listOrdenDeSalidas) throws InventarioException
    {
        List<SalidaUI> listSalidasUI = new ArrayList();
        
        for (OrdenDeSalidas orden : listOrdenDeSalidas) {
            Partida partida = partidaBL.obtenerPartidaPorSalida(orden);
            BigDecimal pesoCalculado = calcularPeso(partida, orden);
            SalidaUI salidaUI = construirSalidaUI(orden, partida, pesoCalculado);
            listSalidasUI.add(salidaUI);
        }
        
        return listSalidasUI;
    }
    
    private BigDecimal calcularPeso(Partida partida, OrdenDeSalidas orden) 
    {
        BigDecimal cantidadInicial = new BigDecimal(partida.getCantidadTotal());
        BigDecimal pesoInicial = partida.getPesoTotal();
        BigDecimal pesoPorUnidad = pesoInicial.divide(cantidadInicial, 3, BigDecimal.ROUND_HALF_UP);
        BigDecimal cantidadOrden = new BigDecimal(orden.getCantidad());

        return pesoPorUnidad.multiply(cantidadOrden);
    }
    
    private SalidaUI construirSalidaUI(OrdenDeSalidas orden, Partida partida, BigDecimal pesoCalculado) 
    {
        SalidaUI preUI = new SalidaUI(
                orden.getFolioSalida(),
                orden.getStatus(),
                orden.getFechaSalida(),
                orden.getHoraSalida(),
                orden.getPartidaCve(),
                orden.getCantidad(),
                orden.getPeso(),
                orden.getCodigo(),
                orden.getLote(),
                orden.getFechaCaducidad(),
                orden.getSAP(),
                orden.getPedimento(),
                orden.getTemperatura(),
                orden.getUnidadManejo(),
                orden.getCodigoProducto(),
                orden.getNombreProducto(),
                partida.getCamaraCve().getPlantaCve().getPlantaAbrev(),
                partida.getCamaraCve().getCamaraAbrev(),
                orden.getFolioOrdenSalida(),
                orden.getProductoClave(),
                orden.getUnidadManejoCve()
        );

        preUI.setSalidaSelected(false);
        preUI.setPeso(pesoCalculado);
        preUI.setFolioEntrada(partida.getFolio().getFolioCliente());

        return preUI;
    }
    
    public List<PartidaServicio> addPartidasServicios(List<SalidaUI> listSalidaUI, ConstanciaDeServicio cds) throws InventarioException {
        List<PartidaServicio> listPartidaServicio = new ArrayList<>();
        
        for (SalidaUI orden : listSalidaUI) {
            Producto pr = productoBL.buscarProductoPorId(orden.getProductoClave());
            UnidadDeManejo udm = unidadDeManejoBL.obtenerUDMPorId(orden.getUnidadManejoCve());
            Integer cantidad = orden.getCantidad();
            BigDecimal Cantidad = new BigDecimal(cantidad);
            BigDecimal pso = orden.getPeso();
            BigDecimal psoPorProducto = pso.divide(Cantidad, 3, BigDecimal.ROUND_HALF_UP);
            BigDecimal cantidadOrdenSalida = new BigDecimal(orden.getCantidad());

            PartidaServicio ps = new PartidaServicio();
            ps.setCantidadDeCobro(psoPorProducto.multiply(cantidadOrdenSalida));
            ps.setCantidadTotal(orden.getCantidad());
            ps.setFolio(cds);
            ps.setProductoCve(pr);
            ps.setUnidadDeCobro(udm);
            ps.setUnidadDeManejoCve(udm);
            listPartidaServicio.add(ps);
        }
        
        return listPartidaServicio;
    }
    
    public List<ConstanciaSalidaServicios> addConstanciaSalidaServicios(List<ServiciosSalida> listaServiciosSalida, ConstanciaSalida constancia) {
        List<ConstanciaSalidaServicios> listConstSalServicios = new ArrayList();
        ConstanciaSalidaServicios css = null;
        
        if(listaServiciosSalida == null || listaServiciosSalida.isEmpty())
            return listConstSalServicios;
        
        for (ServiciosSalida ss : listaServiciosSalida) {
            ConstanciaSalidaServiciosPK constanciaSalidaServiciosPK = new ConstanciaSalidaServiciosPK();
            constanciaSalidaServiciosPK.setConstanciaSalidaCve(constancia);
            constanciaSalidaServiciosPK.setServicioCve(ss.getServicio());
            css = new ConstanciaSalidaServicios();
            css.setConstanciaSalidaServiciosPK(constanciaSalidaServiciosPK);
            css.setIdConstancia(constancia);
            css.setServicioCve(ss.getServicio());
            css.setNumCantidad(BigDecimal.valueOf(ss.getCantidad()));
            listConstSalServicios.add(css);
        }
        log.debug(listConstSalServicios);
        
        return listConstSalServicios;
    }
    
    public List<ConstanciaServicioDetalle> addConstSrvDet(List<ServiciosSalida> listaServiciosSalida, ConstanciaDeServicio cds) {
        List<ConstanciaServicioDetalle> listaConstanciaSrv = new ArrayList<>();
        
        if(listaServiciosSalida == null || listaServiciosSalida.isEmpty())
            return listaConstanciaSrv;
        
        for (ServiciosSalida servSalida : listaServiciosSalida) {
            ConstanciaServicioDetalle consdetalle = new ConstanciaServicioDetalle();
            consdetalle.setFolio(cds);
            consdetalle.setServicioCantidad(BigDecimal.valueOf(servSalida.getCantidad()));
            consdetalle.setServicioCve(servSalida.getServicio());
            listaConstanciaSrv.add(consdetalle);
        }
        log.debug(listaConstanciaSrv);
        
        return listaConstanciaSrv;
    }
    
}
