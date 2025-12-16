package mx.com.ferbo.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.n.EgresosDAO;
import mx.com.ferbo.dao.n.ImporteEgresosDAO;
import mx.com.ferbo.dao.n.TipoEgresoDAO;
import mx.com.ferbo.model.Egresos;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.TipoEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@RequestScoped
public class EgresosBL {

    private static final Logger log = LogManager.getLogger(EgresosBL.class);

    @Inject
    private ImporteEgresosDAO importeEgresosDAO;

    @Inject
    private EgresosDAO egresosDAO;

    @Inject
    private TipoEgresoDAO tipoEgresoDAO;

    public List<TipoEgreso> obtenerTiposEgreso() throws InventarioException {
        try {
            return tipoEgresoDAO.findAll();
        } catch (DAOException ex) {
            log.warn("Error al buscar todos los tipos de egreso. {}", ex);
            throw new InventarioException("Hubo un problema al buscar todos los tipos de egreso.");
        }
    }

    public List<Egresos> obtenerEgresos() throws InventarioException {
        try {
            return egresosDAO.findAll();
        } catch (DAOException ex) {
            log.warn("Error al buscar todos los egresos. {}", ex);
            throw new InventarioException("Hubo un problema al buscar todos los egresos.");
        }
    }

    public List<ImporteEgreso> obtenerImportesEgreso() throws InventarioException {
        try {
            return importeEgresosDAO.findAll();
        } catch (DAOException ex) {
            log.warn("Error al buscar todos los importes de egresos. {}", ex);
            throw new InventarioException("Hubo un problema al buscar todos los importes de egresos.");
        }
    }

    public List<ImporteEgreso> buscarPorEmisorYPeriodo(EmisoresCFDIS emisorCFDIS, Date fechaInicio, Date fechaFin) {
        try {
            Date[] rango = DateUtil.obtenerRango(new Date());
            fechaInicio = rango[0];
            fechaFin = rango[1];
            return importeEgresosDAO.buscarPorEmisorYFechas(emisorCFDIS != null ? emisorCFDIS.getNb_emisor() : null,
                    fechaInicio, fechaFin);
        } catch (DAOException ex) {
            log.warn("Error al buscar los importes de ingreso por emisor del mes actual.");
            return new ArrayList<ImporteEgreso>();
        }
    }

    public void calcularTotal(Parametro parametro, ImporteEgreso importe, BigDecimal ieps) {
        BigDecimal ivaPorc = new BigDecimal(parametro.getValor() != null ? parametro.getValor() : "0")
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        BigDecimal iepsPorc = (ieps != null ? ieps : BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        BigDecimal subTotal = importe.getSubTotal();

        BigDecimal porcentajeIVA = subTotal.multiply(ivaPorc).setScale(2, RoundingMode.HALF_UP);
        BigDecimal porcentajeIEPS = subTotal.multiply(iepsPorc).setScale(2, RoundingMode.HALF_UP);

        importe.setIva(porcentajeIVA);
        importe.setIeps(porcentajeIEPS);
        importe.setImporte(subTotal.add(porcentajeIVA).add(porcentajeIEPS));
    }

    public void guardarEgreso(Egresos egreso, String concepto) throws InventarioException {
        try {
            egreso.setNombreEgreso(concepto);
            egresosDAO.guardar(egreso);
        } catch (Exception ex) {
            log.warn("Error al momento de guardar el egreso. {}", ex);
            throw new InventarioException("Hubo un problema al guardar el egreso.");
        }
    }

    public void actualizarImporte(ImporteEgreso importe, BigDecimal ieps) throws InventarioException {
        try {
            importe.setIeps(ieps);
            importeEgresosDAO.guardar(importe);
        } catch (Exception ex) {
            log.warn("Error al momento de actualizar el importe del egreso. {}", ex);
            throw new InventarioException("Hubo un problema al actualizar el importe del egreso");
        }

    }

    @SuppressWarnings("unused")
    public StreamedContent exportarReporte(String formato, ImporteEgreso importe, Date fechaInicio, Date fechaFin)
            throws JRException, IOException, SQLException, InventarioException {

        String jasperPath = "/jasper/ReporteEgresos.jrxml";
        String images = "/images/logo.jpeg";
        File reportFile = null;
        File imgfile = null;
        JasperReportUtil jasperReportUtil = new JasperReportUtil();
        Map<String, Object> parameters = new HashMap<>();
        Connection connection = null;

        StreamedContent archivo = null;

        try {
            // Obtener las rutas de los recursos
            URL resource = getClass().getResource(jasperPath);
            URL resourceimg = getClass().getResource(images);
            String file = resource.getFile();
            String img = resourceimg.getFile();
            reportFile = new File(file);
            imgfile = new File(img);

            // Obtener el emisor
            String emi = null;
            Integer cd_emi = null;

            if (cd_emi != null) {
                emi = importe.getCdEmisor().getNb_emisor();
            }

            // Establecer los parámetros del reporte
            connection = EntityManagerUtil.getConnection();
            parameters.put("REPORT_CONNECTION", connection);
            parameters.put("emisor", emi);
            parameters.put("fechaini", fechaInicio);
            parameters.put("fechafin", fechaFin);
            parameters.put("image", imgfile.getPath());

            // Procesar el formato
            formato = formato.toLowerCase().trim();
            String filename = "ReporteEgresos_" + new Date().getTime() + "." + formato;

            byte[] bytes = null;

            // Generar el reporte según el formato
            switch (formato) {
                case "pdf":
                    bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
                    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                        InputStream input = byteArrayInputStream;
                        archivo = DefaultStreamedContent.builder()
                                .contentType("application/pdf")
                                .name(filename)
                                .stream(() -> input)
                                .build();
                    }
                    break;

                case "xlsx":
                    bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
                    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                        InputStream input = byteArrayInputStream;
                        archivo = DefaultStreamedContent.builder()
                                .contentType("application/vnd.ms-excel")
                                .name(filename)
                                .stream(() -> input)
                                .build();
                    }
                    break;

                default:
                    throw new InventarioException("El tipo de formato " + formato + " aún no tiene un procedimiento.");
            }

            return archivo;

        } catch (Exception ex) {
            throw new InventarioException("Hubo un problema al generar el reporte: " + ex.getMessage(), ex);
        } finally {
            // Cerrar la conexión
            if (connection != null) {
                conexion.close((Connection) connection);
            }
        }
    }

}