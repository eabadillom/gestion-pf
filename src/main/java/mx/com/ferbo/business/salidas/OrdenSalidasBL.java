package mx.com.ferbo.business.salidas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.business.almacen.StatusConstanciaSalidaBL;
import mx.com.ferbo.model.ConstanciaDeServicio;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.ConstanciaSalidaServicios;
import mx.com.ferbo.model.ConstanciaSalidaServiciosPK;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.SalidaDetalle;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.model.UnidadDeManejo;
import mx.com.ferbo.ui.SalidaDetalleUI;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class OrdenSalidasBL 
{
    private static Logger log = LogManager.getLogger(OrdenSalidasBL.class);
    @Inject private StatusConstanciaSalidaBL statusBO;
    
    public Salida create() {
    	Salida salida = new Salida();
    	salida.setListSalidaDetalle(new ArrayList<SalidaDetalle>());
    	salida.setListServiciosSalida(new ArrayList<ServiciosSalida>());
    	return salida;
    }
    
    public List<PartidaServicio> addPartidasServicios(List<SalidaDetalleUI> detalles, ConstanciaDeServicio cds) throws InventarioException {
        List<PartidaServicio> listPartidaServicio = new ArrayList<>();
        
        for (SalidaDetalleUI detalle : detalles) {
            Producto pr = detalle.getPartida().getUnidadDeProductoCve().getProductoCve();
            UnidadDeManejo udm = detalle.getPartida().getUnidadDeProductoCve().getUnidadDeManejoCve();
            Integer cantidad = detalle.getCantidad();
            BigDecimal Cantidad = new BigDecimal(cantidad);
            BigDecimal pso = detalle.getPesoAprox();
            BigDecimal psoPorProducto = pso.divide(Cantidad, 3, BigDecimal.ROUND_HALF_UP);
            BigDecimal cantidadOrdenSalida = new BigDecimal(detalle.getCantidad());

            PartidaServicio ps = new PartidaServicio();
            ps.setCantidadDeCobro(psoPorProducto.multiply(cantidadOrdenSalida));
            ps.setCantidadTotal(detalle.getCantidad());
            ps.setFolio(cds);
            ps.setProductoCve(pr);
            ps.setUnidadDeCobro(udm);
            ps.setUnidadDeManejoCve(udm);
            listPartidaServicio.add(ps);
        }
        
        return listPartidaServicio;
    }
    
    public List<ConstanciaSalidaServicios> addConstanciaSalidaServicios(List<ServiciosSalida> listaServiciosSalida, ConstanciaSalida constancia) {
        List<ConstanciaSalidaServicios> listConstSalServicios = new ArrayList<ConstanciaSalidaServicios>();
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
    
    public ConstanciaSalida toConstanciaSalida(Salida salida)
	throws InventarioException {
    	ConstanciaSalida constancia = null;
    	StatusConstanciaSalida statusConstancia = statusBO.nueva();
    	Date fecha = new Date();
    	DateUtil.setTime(fecha, 0, 0, 0, 0);
    	constancia = new ConstanciaSalida.Builder()
		.fecha(fecha)
		.placasTransporte(salida.getPlacasTransporte())
		.nombreTransportista(salida.getNombreTransportista())
		.cliente(salida.getCliente())
		.nombreCliente(salida.getCliente().getNombre())
		.status(statusConstancia)
		.observaciones(String.format("Orden salida: %s - %s", salida.getFolioSalida(), salida.getObservaciones()))
		.build();
    	
    	//TODO Implementar logica de List<DetalleConstanciaSalida>
    	
    	//TODO Implementar logica de List<ConstanciaSalidaServicio>
    	
    	return constancia;
    }
    
}











