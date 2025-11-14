package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.model.CandadoSalida;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.MetodoPago;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.InventarioException;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ClienteBL {

    private static Logger log = LogManager.getLogger(ClienteBL.class);

    @Inject
    private ClienteDAO clienteDAO;

    @Inject
    private FiscalBL fiscalBL;

    @Inject
    private PlantaBL plantaBL;

    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = clienteDAO.buscarTodos();

        if (lista == null) {
            return new ArrayList<>();
        }

        return lista;
    }

    public Cliente obtenerTodoCliente(Integer id, Boolean isFullInfo) throws InventarioException {

        return clienteDAO.obtenerPorId(id, isFullInfo);

    }

    public Cliente obtenerPorCodigoUnico(String codigo) {

        return clienteDAO.buscarPorCodigoUnico(codigo);

    }

    public Cliente nuevoCliente() {
        Cliente cliente = new Cliente();
        cliente = new Cliente();
        cliente.setMetodoPago(new MetodoPago());
        cliente.setRegimenFiscal(new RegimenFiscal());
        cliente.setUsoCfdi(new UsoCfdi());
        cliente.setCandadoSalida(new CandadoSalida());
        cliente.setClienteContactoList(new ArrayList());
        cliente.setAvisoList(new ArrayList());

        return cliente;
    }

    public void asignarCandadoSalida(List<Planta> plantas, Cliente cliente) {

        CandadoSalida candadoSalida = new CandadoSalida();
        candadoSalida.setHabilitado(true);
        candadoSalida.setCliente(cliente);
        candadoSalida.setNumSalidas(1);

        cliente.setCandadoSalida(candadoSalida);

        for (Planta planta : plantas) {

            SerieConstanciaPK serieConstanciaPK_I = new SerieConstanciaPK();
            serieConstanciaPK_I.setCliente(cliente);
            serieConstanciaPK_I.setPlanta(planta);
            serieConstanciaPK_I.setTpSerie("I");
            SerieConstancia serieConstanciaI = new SerieConstancia();
            serieConstanciaI.setSerieConstanciaPK(serieConstanciaPK_I);
            serieConstanciaI.setNuSerie(1);
            cliente.addSerieConstancia(serieConstanciaI);
            planta.add(serieConstanciaI);

            SerieConstanciaPK serieConstanciaPK_O = new SerieConstanciaPK();
            serieConstanciaPK_O.setCliente(cliente);
            serieConstanciaPK_O.setPlanta(planta);
            serieConstanciaPK_O.setTpSerie("O");
            SerieConstancia serieConstanciaO = new SerieConstancia();
            serieConstanciaO.setSerieConstanciaPK(serieConstanciaPK_O);
            serieConstanciaO.setNuSerie(1);
            cliente.addSerieConstancia(serieConstanciaO);
            planta.add(serieConstanciaO);

            SerieConstanciaPK serieConstanciaPK_T = new SerieConstanciaPK();
            serieConstanciaPK_T.setCliente(cliente);
            serieConstanciaPK_T.setPlanta(planta);
            serieConstanciaPK_T.setTpSerie("T");
            SerieConstancia serieConstanciaT = new SerieConstancia();
            serieConstanciaT.setSerieConstanciaPK(serieConstanciaPK_T);
            serieConstanciaT.setNuSerie(1);
            cliente.addSerieConstancia(serieConstanciaT);
            planta.add(serieConstanciaT);

            SerieConstanciaPK serieConstanciaPK_S = new SerieConstanciaPK();
            serieConstanciaPK_S.setCliente(cliente);
            serieConstanciaPK_S.setPlanta(planta);
            serieConstanciaPK_S.setTpSerie("S");
            SerieConstancia serieConstanciaS = new SerieConstancia();
            serieConstanciaS.setSerieConstanciaPK(serieConstanciaPK_S);
            serieConstanciaS.setNuSerie(1);
            cliente.addSerieConstancia(serieConstanciaS);
            planta.add(serieConstanciaS);
        }
    }

    public String guardarOActualizar(Cliente cliente) throws InventarioException {

        String status = null;
        if (cliente.getCteCve() == null) {
            if ("m".equalsIgnoreCase(cliente.getRegimenCapital())) {
                fiscalBL.validarRegimenCapital(cliente);
            }
            Cliente clientAux = obtenerPorCodigoUnico(cliente.getCodUnico());
            fiscalBL.validarCodigoUnico(cliente, clientAux);
            List<Planta> plantas = plantaBL.obtenerPlantas(Boolean.TRUE);
            asignarCandadoSalida(plantas, cliente); 
            cliente.setHabilitado(true);
            clienteDAO.guardar(cliente);
            status = "agregado";
        } else {
            clienteDAO.actualizar(cliente);
            status = "actualizado";
        }
        return "Cliente " + status + " exitosamente";
    }

    public List<Cliente> filtrarPorEstatus(List<Cliente> original, Boolean estado) {
        if (original == null) {
            return Collections.emptyList();
        }

        return original.stream()
                .filter(cliente -> estado.equals(cliente.getHabilitado())).collect(Collectors.toList());
    }

}
