package mx.com.ferbo.dao.n;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.SalidaDetalle;
import mx.com.ferbo.model.ServiciosSalida;
import mx.com.ferbo.ui.OrdenDeSalidas;
import mx.com.ferbo.util.DAOException;

/**
 *
 * @author alberto
 */
@Named
@ApplicationScoped
public class SalidaDAO extends BaseDAO<Salida, Integer> 
{
    private static Logger log = LogManager.getLogger(SalidaDAO.class);

    public SalidaDAO(){
        super(Salida.class);
    }
    
    public List<Salida> findAll() throws DAOException {
        List<Salida> listModel = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            listModel = em.createNamedQuery("Salida.findAll", Salida.class)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta de las salidas...", ex);
            throw new DAOException("Problema al obtener las salida");
        } finally {
            super.close(em);
        }
        
        return listModel;
    }
    
    public List<Salida> findByCliente(Integer idCliente, String clave, Date fechaSalida) throws DAOException {
        List<Salida> listModel = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            listModel = em.createNamedQuery("Salida.findByCliente", Salida.class)
                .setParameter("idCliente", idCliente)
                .setParameter("clave", clave)
                .setParameter("fechaSalida", fechaSalida)
                .getResultList();
        } catch (Exception ex) {
            log.error("Problema en la consulta de las salidas...", ex);
            throw new DAOException("Problema al obtener las salida");
        } finally {
            super.close(em);
        }
        
        return listModel;
    }
    
    public List<Salida> findByParametros(String clave, Date fechaSalida, Integer idCliente, Integer idPlanta) throws DAOException {
        List<Salida> modelList = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            modelList = em.createQuery(
            		  "SELECT DISTINCT s from Salida s "
            		+ "INNER JOIN s.cliente c "
            		+ "INNER JOIN s.listSalidaDetalle d "
            		+ "INNER JOIN d.partida p "
            		+ "INNER JOIN p.camaraCve cam "
            		+ "INNER JOIN cam.plantaCve plt "
            		+ "WHERE s.status.clave = :clave AND c.cteCve = :idCliente AND s.fechaSalida = :fechaSalida AND plt.plantaCve = :idPlanta "
            		+ "ORDER BY s.id ASC, s.fechaSalida ASC", this.modelClass)
            		.setParameter("clave", clave)
            		.setParameter("fechaSalida", fechaSalida)
            		.setParameter("idCliente", idCliente)
            		.setParameter("idPlanta", idPlanta)
            		.getResultList();
            		
        } catch (Exception ex) {
            log.error("Problema en la consulta de la salida por parametros...", ex);
            throw new DAOException("Problema al obtener la salida");
        } finally {
            super.close(em);
        }
        
        return modelList;
    }
    
    /**Método innecesario. La búsqueda por ID ya está implementada en la clase BaseDAO.
     * @param idSalida
     * @return
     * @throws DAOException
     */
    @Deprecated
    public Salida findById(Integer idSalida) throws DAOException {
        Salida model = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            model = em.createNamedQuery("Salida.findById", Salida.class)
                .setParameter("idStatus", idSalida)
                .getSingleResult();
        } catch (Exception ex) {
            log.error("Problema en la consulta de la salida por el folio...", ex);
            throw new DAOException("Problema al obtener la salida");
        } finally {
            super.close(em);
        }
        
        return model;
    }
    
    public Optional<Salida> buscar(String folioSalida) {
    	Optional<Salida> response = null;
        Salida model = null;
        EntityManager em = null;
        
        try {
            em = super.getEntityManager();
            model = em.createNamedQuery("Salida.findByFolioSalida", Salida.class)
                .setParameter("folioSalida", folioSalida)
                .getSingleResult();
            
            for(SalidaDetalle detalle : model.getListSalidaDetalle()) {
            	log.debug("Planta: {}", detalle.getPartida().getCamaraCve().getPlantaCve());
            	log.debug("Entrada: {}", detalle.getPartida().getFolio().getFolioCliente());
            	log.debug("Producto: {}", detalle.getPartida().getUnidadDeProductoCve().getProductoCve().getProductoCve());
            	log.debug("Unidad: {}", detalle.getPartida().getUnidadDeProductoCve().getUnidadDeManejoCve().getUnidadDeManejoCve());
            	detalle.getPartida().getDetallePartidaList().forEach(dp -> log.debug("Detalle partida: {}", dp.getDetallePartidaPK().getDetPartCve()));
            }
            
            for(ServiciosSalida servicio : model.getListServiciosSalida())
                log.debug("Servicio: {}", servicio.getIdSrvSalida());
            
            log.debug("Status: {}", model.getStatus().getClave());
            
            response = Optional.of(model);
            
        } catch (Exception ex) {
            log.error("Problema en la consulta de la salida por el folio...", ex);
            response = Optional.empty();
        } finally {
            this.close(em);
        }
        
        return response;
    }
    
    public List<OrdenDeSalidas> buscarInventarioSalida(String clave, String folioSalida, Date fecha) {
        List<OrdenDeSalidas> listaOrdenDeSalidas = null;
        EntityManager em = null;
        String sql = null;
        try {
            em = super.getEntityManager();
            
            sql = "SELECT \n" +
                    "	s.cd_salida, \n" +
                    "	s.cd_folio_salida, \n" +
                    "	pt.folio,\n" +
                    "	ss.nb_clave, \n" +
                    "	s.fh_salida, \n" +
                    "	s.tm_salida, \n" +
                    "	pt.PARTIDA_CVE, \n" +
                    "	sd.nu_cantidad, \n" +
                    "	pt.PESO_TOTAL, \n" +
                    "	dp.dtp_codigo, \n" +
                    "	dp.dtp_lote, \n" +
                    "	dp.dtp_caducidad, \n" +
                    "	dp.dtp_SAP, \n" +
                    "	dp.dtp_pedimento, \n" +
                    "	udm.UNIDAD_DE_MANEJO_CVE,\n" +
                    "	udm.UNIDAD_DE_MANEJO_DS,\n" +
                    "	prd.NUMERO_PROD , \n" +
                    "	prd.PRODUCTO_CVE, \n" +
                    "	prd.PRODUCTO_DS, \n" +
                    "	c.CAMARA_DS, \n" +
                    "	plt.PLANTA_DS\n" +
                    "FROM salida s\n" +
                    "INNER JOIN salida_detalle sd on sd.cd_salida = s.cd_salida \n" +
                    "INNER JOIN status_salida ss on ss.cd_status = s.cd_status \n" +
                    "INNER JOIN partida pt ON sd.partida_cve = pt.PARTIDA_CVE\n" +
                    "INNER JOIN camara c on pt.CAMARA_CVE = c.CAMARA_CVE\n" +
                    "INNER JOIN planta plt ON c.PLANTA_CVE = plt.PLANTA_CVE\n" +
                    "INNER JOIN (\n" +
                    "	SELECT dp.*\n" +
                    "	FROM detalle_partida dp\n" +
                    "	INNER JOIN (\n" +
                    "		select max(DET_PART_CVE) AS DET_PART_CVE, PARTIDA_CVE\n" +
                    "		from detalle_partida dp\n" +
                    "		GROUP BY PARTIDA_CVE \n" +
                    "	) maxDP ON maxDP.DET_PART_CVE = dp.DET_PART_CVE and maxDP.PARTIDA_CVE = dp.PARTIDA_CVE \n" +
                    ") dp on pt.PARTIDA_CVE = dp.PARTIDA_CVE\n" +
                    "INNER JOIN unidad_de_producto udp ON pt.UNIDAD_DE_PRODUCTO_CVE = udp.UNIDAD_DE_PRODUCTO_CVE\n" +
                    "INNER JOIN unidad_de_manejo udm ON udp.UNIDAD_DE_MANEJO_CVE = udm.UNIDAD_DE_MANEJO_CVE\n" +
                    "INNER JOIN producto prd ON udp.PRODUCTO_CVE = prd.PRODUCTO_CVE\n" +
                    "WHERE ss.nb_clave = :clave AND s.cd_folio_salida = :folioSalida AND s.fh_salida = :fecha";
            
            SimpleDateFormat formatoSimple = new SimpleDateFormat("yyyy-MM-dd");
            String fech = formatoSimple.format(fecha);
            Query query = em.createNativeQuery(sql)
                .setParameter("clave", clave)
                .setParameter("fecha", fech)
                .setParameter("folioSalida", folioSalida);
            @SuppressWarnings("unchecked")
			List<Object[]> results = query.getResultList();
            listaOrdenDeSalidas = new ArrayList<OrdenDeSalidas>();
            for (Object[] o : results) {
                OrdenDeSalidas ods = new OrdenDeSalidas();
                int idx = 0;
                ods.setId((Integer) o[idx++]);
                ods.setFolioSalida((String) o[idx++]);
                ods.setFolioOrdenSalida((Integer) o[idx++]);
                ods.setStatus((String) o[idx++]);
                ods.setFechaSalida((Date) o[idx++]);
                ods.setHoraSalia((Time) o[idx++]);
                ods.setPartidaCve((Integer) o[idx++]);
                ods.setCantidad((Integer) o[idx++]);
                ods.setPeso((BigDecimal) o[idx++]);
                ods.setCodigo((String) o[idx++]);
                ods.setLote((String) o[idx++]);
                ods.setFechaCaducidad((Date) o[idx++]);
                ods.setSAP((String) o[idx++]);
                ods.setPedimento((String) o[idx++]);
                ods.setUnidadManejoCve((Integer) o[idx++]);
                ods.setUnidadManejo((String) o[idx++]);
                ods.setCodigoProducto((String) o[idx++]);
                ods.setProductoClave((Integer) o[idx++]);
                ods.setNombreProducto((String) o[idx++]);
                ods.setNombreCamara((String) o[idx++]);
                ods.setNombrePlanta((String) o[idx++]);
                listaOrdenDeSalidas.add(ods);
            }
        } catch (Exception e) {
            log.error("Problemas para obtener la informacion requerida", e);
        } finally {
            super.close(em);
        }
        
        return listaOrdenDeSalidas;
    }
    
    public Integer totalSalidasPorCliente(String clave, Date fechaSalida, Integer idPlanta) {
        BigInteger temporal = null;
        Integer total = null;
        EntityManager em = null;
        String sql = null;
        
        try{
            em = super.getEntityManager();
            
            sql = "select count(*) as cta_salidas \n" +
                    "from ( \n" +
                    "	select s.cd_folio_salida \n" +
                    "	from salida s \n" +
                    "	inner join salida_detalle sd on sd.cd_salida = s.cd_salida \n" +
                    "	inner join status_salida ss on ss.cd_status = s.cd_status \n" +
                    "	inner join partida p on sd.partida_cve = p.partida_cve \n" +
                    "	inner join camara cam on p.camara_cve = cam.camara_cve \n" +
                    "	inner join planta plt on cam.planta_cve = plt.planta_cve \n" +
                    "	where ss.nb_clave = :clave and s.fh_salida = :fechaSalida and plt.planta_cve = :idPlanta \n" +
                    "	group by s.cd_folio_salida \n" +
                    ") t";
            
            Query query = em.createNativeQuery(sql)
                .setParameter("clave", clave)
                .setParameter("fechaSalida", fechaSalida)
                .setParameter("idPlanta", idPlanta);
            
            temporal = (BigInteger) query.getSingleResult();
            total = temporal.intValue() == 0 ? null : temporal.intValue();
        } catch (Exception e) {
            log.error("Problemas para obtener la informacion requerida", e);
        } finally {
            super.close(em);
        }
        
        return total;
    }
    
}
