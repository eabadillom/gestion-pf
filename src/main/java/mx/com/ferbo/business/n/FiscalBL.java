package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import mx.com.ferbo.dao.n.RegimenFiscalDAO;
import mx.com.ferbo.dao.n.UsoCfdiDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.RegimenFiscal;
import mx.com.ferbo.model.UsoCfdi;
import mx.com.ferbo.util.ClienteUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class FiscalBL {

    private static Logger log = LogManager.getLogger(FiscalBL.class);

    @Inject
    private RegimenFiscalDAO regimenFiscalDAO;

    @Inject
    private UsoCfdiDAO usoCfdiDAO;

    public List<UsoCfdi> obtenerCfdis() {
        log.info("Inicia el proceso de obtener los cfdis");
        return usoCfdiDAO.buscarTodos();
    }

    public List<UsoCfdi> filtrarCfdis(List<UsoCfdi> listCfdi, Cliente cliente) throws InventarioException {
        log.info("Inicia proceso de filtrado de los cfdis por el tipo cliente");
        FacesUtils.requireNonNull(cliente, "El cliente no puede estar vacio");

        List<UsoCfdi> listAux = new ArrayList<>();

        if (cliente.getTipoPersona().equals("F")) {
            log.info("Se filtra la lista por que el cliente {} es una persona física", cliente.getNombre());
            listAux = listCfdi.stream()
                    .filter(uso -> Boolean.TRUE.equals(uso.getAplicaPersonaFisica()))
                    .collect(Collectors.toList());
        } else if (cliente.getTipoPersona().equals("M")) {
            log.info("Se filtra la lista por que el cliente {} es una persona moral", cliente.getNombre());
            listAux = listCfdi.stream()
                    .filter(uso -> Boolean.TRUE.equals(uso.getAplicaPersonaMoral()))
                    .collect(Collectors.toList());
        }
        log.info("Finaliza proceso de filtrado de los cfdis por el tipo cliente");
        return listAux;
    }

    public List<UsoCfdi> obtenerUsoCfdis(Cliente cliente) throws InventarioException {
        log.info("Se obtiene el tipo de cfdis por el tipo de perona");

        FacesUtils.requireNonNull(cliente, "El cliente no puede estar vacío");

        FacesUtils.requireNonNull(cliente.getTipoPersona(), "El tipo de persona del cliente esta vacío");

        return usoCfdiDAO.buscarUsoCfdiPorTipoPersona(cliente.getTipoPersona());

    }

    public List<RegimenFiscal> obtenerRegimenesFiscales() {
        log.info("Inicia proceso de obtencion de los regimines fiscales");
        return regimenFiscalDAO.buscarTodos();
    }

    public List<RegimenFiscal> filtrarRegimenesFiscales(List<RegimenFiscal> listRegimenes, Cliente cliente)
            throws InventarioException {
        log.info("Inicia proceso de filtrado de los regimines fiscales en base al tipo de persona");
        FacesUtils.requireNonNull(cliente, "El cliente no puede estar vacio");

        List<RegimenFiscal> listAux = new ArrayList<>();

        if (cliente.getTipoPersona().equals("F")) {
            log.info("Se filtra por tipo de persona física");
            listAux = listRegimenes.stream()
                    .filter(RegimenFiscal::isPersonaFisica)
                    .collect(Collectors.toList());
        } else if (cliente.getTipoPersona().equals("M")) {
            log.info("Se filtra por tipo de persona moral");
            listAux = listRegimenes.stream()
                    .filter(RegimenFiscal::isPersonaMoral)
                    .collect(Collectors.toList());
        }

        log.info("Finaliza proceso de filtrado de los regimines fiscales en base al tipo de persona");
        return listAux;
    }

    public List<RegimenFiscal> obtenerRegimenesFiscales(Cliente cliente) throws InventarioException {
        log.info("Inicia proceso para obtener los regimines fiscales del cliente {}", cliente.getNombre());
        FacesUtils.requireNonNull(cliente, "El cliente no puede estar vacío");

        FacesUtils.requireNonNull(cliente.getTipoPersona(), "El tipo de persona del cliente esta vacío");

        return regimenFiscalDAO.buscarPorTipoPersona(cliente.getTipoPersona());
    }

    public void validarInfoFiscal(Cliente cliente) throws InventarioException {

        log.info("Inicia proceso de validación de información fiscal del cliente");

        if (cliente.getTipoPersona() == null || cliente.getTipoPersona().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente debe esta asociado a un tipo de persona");
        }

        if (cliente.getCteRfc() == null || cliente.getCteRfc().equalsIgnoreCase("")) {
            throw new InventarioException("El RFC del cliente no puede estar vacío");
        }

        if (cliente.getNumeroCte() == null || cliente.getNumeroCte().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente de debe tener asignado un número");
        }

        if (cliente.getNombre() == null || cliente.getNombre().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente debe tener un nombre");
        }

        if (cliente.getTipoPersona().equalsIgnoreCase("m")) {
            if (cliente.getRegimenCapital() == null || cliente.getRegimenCapital().equalsIgnoreCase("")) {
                throw new InventarioException("El cliente debe tener un régimen capital");
            }
        }

        if (cliente.getRegimenFiscal() == null || cliente.getRegimenFiscal().getNb_regimen() == null || cliente.getRegimenFiscal().getNb_regimen().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente debe tener un régimen fiscal");
        }

        if (cliente.getUsoCfdi() == null || cliente.getUsoCfdi().getUsoCfdi() == null || cliente.getUsoCfdi().getUsoCfdi().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente debe tener un uso de CFDI");
        }

        if (cliente.getMetodoPago() == null || cliente.getMetodoPago().getNbMetodoPago() == null || cliente.getMetodoPago().getNbMetodoPago().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente debe tener un metodo de pago");
        }

        if (cliente.getFormaPago() == null || cliente.getFormaPago().equalsIgnoreCase("")) {
            throw new InventarioException("El cliente debe tener una forma de pago");
        }
        log.info("Finaliza proceso de validación de información fiscal del cliente");
    }

    public void validarRegimenCapital(Cliente cliente) throws InventarioException {
        log.info("Inicia proceso de validación del regimen capital del cliente {}", cliente.getNombre());
        if ("M".equalsIgnoreCase(cliente.getCteRfc())
                && (cliente.getRegimenCapital() == null || cliente.getRegimenCapital().trim().equalsIgnoreCase(""))) {
            throw new InventarioException("Debe indicar un régimen capital");
        }
    }

    public void validarCodigoUnico(Cliente cliente, Cliente clienteBuscado) throws InventarioException {

        log.info("Inicia proceso de validación del código unico del cliente {}", cliente.getNombre());
        if (clienteBuscado != null) {
            if (cliente == null) {
                throw new InventarioException("El cliente a agregar está vacío");
            }

            if (cliente.equals(clienteBuscado)) {

                throw new InventarioException("El código único " + cliente.getCodUnico()
                        + " ya está registrado para el cliente " + clienteBuscado.getNombre());
            }
        }
        log.info("Finaliza proceso de validación del código unico del cliente {}", cliente.getNombre());
    }

    public void validarRFC(Cliente cliente) throws InventarioException {
        log.info("Inicia proceso de validación del RFC del cliente {}", cliente.getNombre());
        if (ClienteUtil.validarRFC(cliente.getTipoPersona(), cliente.getCteRfc()) == false) {
            throw new InventarioException("El RFC es incorrecto");
        }

        String codigoUnico = cliente.getCteRfc();
        if ("F".equalsIgnoreCase(cliente.getTipoPersona())) {
            codigoUnico = codigoUnico.substring(0, 4);
        } else if ("M".equalsIgnoreCase(cliente.getTipoPersona())) {
            codigoUnico = codigoUnico.substring(0, 3);
        }

        if (cliente.getCteCve() == null
                && (ClienteUtil.RFC_GENERICO_NACIONAL.equalsIgnoreCase(cliente.getCteRfc()) == false
                || ClienteUtil.RFC_GENERICO_EXTRANJERO
                        .equalsIgnoreCase(cliente.getCteRfc()) == false)) {
            cliente.setCodUnico(codigoUnico);
        }
        log.info("Finaliza proceso de validación del RFC del cliente {}", cliente.getNombre());
    }

}
