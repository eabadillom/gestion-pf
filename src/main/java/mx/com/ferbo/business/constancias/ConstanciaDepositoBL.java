package mx.com.ferbo.business.constancias;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ConstanciaDepositoDAO;
import mx.com.ferbo.dao.n.ConstanciaSalidaDAO;
import mx.com.ferbo.dao.n.EstadoConstanciaDAO;
import mx.com.ferbo.dao.n.FacturaDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.EstadoConstancia;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ConstanciaDepositoBL {

    private static Logger log = LogManager.getLogger(ConstanciaDepositoBL.class);

    @Inject
    private FacturaDAO facturaDAO;

    @Inject
    private ConstanciaSalidaDAO constanciaSalidaDAO;

    @Inject
    private ConstanciaDepositoDAO constanciaDepositoDAO;

    public void cancelarConstanciaDeposito(ConstanciaDeDeposito constanciaDeDeposito) throws InventarioException{
        
        FacesUtils.requireNonNullWithReturn(constanciaDeDeposito, "La contancia no puede ser vacía");

        if (constanciaDeDeposito.getStatus().getEdoCve() == 2){
            throw new InventarioException("La constancia de deposito ya está cancelada");
        }

        Integer folio = constanciaDeDeposito.getFolio();

        log.info("Inicia verificación de salidas relacionadas con la constancia de deposito");
        List<ConstanciaSalida> salidas = constanciaSalidaDAO.obtenerPorFolioDeposito(folio);

        if (salidas != null && !salidas.isEmpty()){
            for (ConstanciaSalida salida : salidas){
                if (salida.getStatus().getId() != 2){
                    throw new InventarioException("No se puede cancelar la constancia de deposito por tener una salida activa");
                }
            }
        }
        log.info("Finaliza verificación de salidas relacionadas con la constancia de deposito");

        log.info("Inicia verificación de facturas relacionadas con la constancia de deposito");
        List<Factura> facturas = facturaDAO.obtenerPorFolioDeposito(folio);

        if (facturas != null && !facturas.isEmpty()){
            for(Factura factura : facturas){
                if (factura.getStatus().getId() != 2){
                    throw new InventarioException("No se puede cancelar la constancia de deposito por tener una factura activa");
                }
            }
        }
        log.info("Finaliza verificación de facturas relacionadas con la constancia de deposito");

        log.info("Inicia cambio de estado a cancelación de la constancia de deposito");
        EstadoConstanciaDAO estadoConstanciaDAO = new EstadoConstanciaDAO();
        EstadoConstancia estado = estadoConstanciaDAO.buscarPorId(2).orElseThrow(()-> new InventarioException("Estado de constanstancia cancelada, no encontrado"));
        
        constanciaDeDeposito.setStatus(estado);
        constanciaDeDeposito.setObservaciones("CONSTANCIA CANCELADA EL DIA " + DateUtil.getString(constanciaDeDeposito.getFechaIngreso(), DateUtil.FORMATO_DD_MM_YYYY));
        log.info("Finaliza cambio de estado a cancelación de la constancia de deposito");
        
        log.info("Inicia actulización de la constancia de posito");
        constanciaDepositoDAO.actualizar(constanciaDeDeposito);
        log.info("Finaliza actulización de la constancia de posito");
    }
    
}
