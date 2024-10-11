package mx.com.ferbo.dao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaSalida;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.StatusConstanciaSalida;
import mx.com.ferbo.util.EntityManagerUtil;

public class InventarioDAO extends IBaseDAO<ConstanciaDeDeposito, Integer> {
	private static Logger log = LogManager.getLogger(InventarioDAO.class);
	
	public BigDecimal getCantidad(Integer idCliente, Date fecha) {
		BigDecimal cantidad = null;
		EntityManager em = null;
		Query query = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			query = em.createNativeQuery("select\n"
//					+ "	cte_cve,\n"
					+ "	sum(cantidad) as cantidad\n"
//					+ "	sum(peso) as peso\n"
					+ "from (\n"
					+ "	select\n"
					+ "		cdd.CTE_CVE,\n"
					+ "		(p.CANTIDAD_TOTAL - coalesce(sal.cantidad, 0)) as cantidad,\n"
					+ "		(p.PESO_TOTAL - coalesce(sal.peso, 0)) as peso\n"
					+ "	from constancia_de_deposito cdd\n"
					+ "	inner join partida p on cdd.FOLIO = p.FOLIO\n"
					+ "	left outer join (\n"
					+ "		select\n"
					+ "			dcs.PARTIDA_CVE,\n"
					+ "			SUM(dcs.CANTIDAD) as CANTIDAD,\n"
					+ "			SUM(dcs.PESO) as PESO\n"
					+ "		from constancia_salida cs \n"
					+ "		inner join detalle_constancia_salida dcs on cs.ID = dcs.CONSTANCIA_CVE \n"
					+ "		where (cs.status = 1)\n"
					+ "		group by dcs.PARTIDA_CVE\n"
					+ "	) sal on p.PARTIDA_CVE = sal.PARTIDA_CVE\n"
					+ "	where (cdd.status = 1) and (cdd.CTE_CVE = :idCliente or :idCliente is null)\n"
					+ "	and cdd.FECHA_INGRESO <= :fechaCorte\n"
					+ ") inv\n"
					+ "where inv.cantidad > 0\n"
					+ "group by cte_cve")
					.setParameter("idCliente", idCliente)
					.setParameter("fechaCorte", fecha)
					;
			
			cantidad = (BigDecimal) query.getSingleResult();
			
		} catch(NoResultException ex) {
			log.warn("Saldo no encontrado para el cliente id: {}", idCliente);
		} catch(Exception ex) {
			log.error("Problema para obtener el saldo...", ex);
		} finally {
			EntityManagerUtil.close(em);
		}
		
		return cantidad;
			
	}
	
	@SuppressWarnings("unchecked")
	public List<ConstanciaDeDeposito> findall() {
		List<ConstanciaDeDeposito> list = null;
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("ConstanciaDeDeposito.findAll", ConstanciaDeDeposito.class);
			list = sql.getResultList();
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de constancias de depósito...",ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Inventario> buscar(Cliente cliente, Planta planta) {
		List<Inventario> inventarioList = null;
		List<ConstanciaDeDeposito> constancias = null;
		List<Partida> partidaList = null;
		Inventario inventario = null;
		List<DetalleConstanciaSalida> detalleConstanciaSalidaList = null;
		List<DetallePartida> detallePartidaList = null;
		
		String sql = null;
		EntityManager entity = null;
		try {
			sql = "SELECT DISTINCT FOLIO, CTE_CVE, FECHA_INGRESO, NOMBRE_TRANSPORTISTA, PLACAS_TRANSPORTE, OBSERVACIONES, folio_cliente, valor_declarado, status, aviso_cve, temperatura\n"
					+ "FROM (\n"
					+ "	SELECT\n"
					+ "		cdd.FOLIO, cdd.CTE_CVE, cdd.FECHA_INGRESO, cdd.NOMBRE_TRANSPORTISTA, cdd.PLACAS_TRANSPORTE, cdd.OBSERVACIONES, cdd.folio_cliente, cdd.valor_declarado, cdd.status, cdd.aviso_cve, cdd.temperatura,\n"
					+ "		p.PARTIDA_CVE,\n"
					+ "		p.CANTIDAD_TOTAL,\n"
					+ "		(p.CANTIDAD_TOTAL - COALESCE(sal.CANTIDAD, 0)) AS cantidad,\n"
					+ "		p.PESO_TOTAL,\n"
					+ "		(p.PESO_TOTAL - COALESCE(sal.PESO, 0)) AS peso\n"
					+ "	FROM constancia_de_deposito cdd\n"
					+ "	INNER JOIN partida p ON cdd.FOLIO = p.FOLIO\n"
					+ "	INNER JOIN camara cam ON p.CAMARA_CVE = cam.CAMARA_CVE \n"
					+ "	INNER JOIN planta plt ON cam.PLANTA_CVE = plt.PLANTA_CVE \n"
					+ "	LEFT OUTER JOIN (\n"
					+ "		SELECT PARTIDA_CVE, sum(CANTIDAD) AS CANTIDAD, SUM(peso) AS peso\n"
					+ "		FROM detalle_constancia_salida dcs\n"
					+ "		inner join constancia_salida cs on dcs.CONSTANCIA_CVE = cs.id\n"
					+ "		where cs.STATUS = 1\n"
					+ "		GROUP BY PARTIDA_CVE\n"
					+ "	) sal ON p.PARTIDA_CVE = sal.PARTIDA_CVE\n"
					+ "	WHERE cdd.CTE_CVE = :idCliente\n"
					+ " AND cdd.status = 1\n"
					+ "	AND plt.PLANTA_CVE = :idPlanta\n"
					+ ") I WHERE I.cantidad > 0"
					;
			
			entity = EntityManagerUtil.getEntityManager();
			
			
			constancias = entity.createNativeQuery(sql, ConstanciaDeDeposito.class)
					.setParameter("idCliente", cliente.getCteCve())
					.setParameter("idPlanta", planta.getPlantaCve())
					.getResultList()
					;
			
			inventarioList = new ArrayList<Inventario>();
			for (ConstanciaDeDeposito c : constancias) {
				partidaList = c.getPartidaList();
				for (Partida p : partidaList) {
					log.debug("Partida.plantaCve: {}, Planta abrev: {}", p.getCamaraCve().getPlantaCve().getPlantaCve(), p.getCamaraCve().getPlantaCve().getPlantaAbrev());
					if(p.getCamaraCve().getPlantaCve().getPlantaCve() != planta.getPlantaCve())
						continue;
					Integer cantidadInicial = p.getCantidadTotal();
					BigDecimal pesoInicial = p.getPesoTotal(); 
					detalleConstanciaSalidaList = p.getDetalleConstanciaSalidaList();
					detallePartidaList = p.getDetallePartidaList();
					Integer cantidadSalidas = 0;
					BigDecimal pesoSalidas = new BigDecimal(0).setScale(3,RoundingMode.HALF_UP);
					
					for (DetalleConstanciaSalida dcs : detalleConstanciaSalidaList) { //Por cada partida, obtenemos su detalle de salidas.
						ConstanciaSalida constanciaSalida = dcs.getConstanciaCve();
						StatusConstanciaSalida statusSalida = constanciaSalida.getStatus();
						if(statusSalida.getId().compareTo(1) != 0) {
							log.info("Constancia salida {} cancelada.", constanciaSalida.getNumero());
							continue;
						}
						pesoSalidas     = pesoSalidas.add(dcs.getPeso()); 
						cantidadSalidas = cantidadSalidas + dcs.getCantidad();
					}
					
					Integer cantidadRestante = cantidadInicial - cantidadSalidas;
					BigDecimal pesoRestante = pesoInicial.subtract(pesoSalidas);
					DetallePartida dp = detallePartidaList.get(detallePartidaList.size()- 1);
					
					if(cantidadRestante <= 0)
						continue;
					
					inventario = new Inventario(); 
					inventario.setFolioCliente(c.getFolioCliente());
					inventario.setFechaIngreso(c.getFechaIngreso());
					log.debug("ProductoCve: {}", p.getUnidadDeProductoCve().getProductoCve());
					inventario.setProducto(p.getUnidadDeProductoCve().getProductoCve());
					inventario.setCantidad(cantidadRestante);
					log.debug("UnidadDeManejoCve: {}", p.getUnidadDeProductoCve().getUnidadDeManejoCve());
					inventario.setUnidadManejo(p.getUnidadDeProductoCve().getUnidadDeManejoCve());
					inventario.setPeso(pesoRestante);
					inventario.setNumeroTarimas(p.getNoTarimas());
					inventario.setPartidaCve(p.getPartidaCve());
					log.debug("PlantaCve: {}", p.getCamaraCve().getPlantaCve());
					inventario.setPlanta(p.getCamaraCve().getPlantaCve());
					inventario.setCamara(p.getCamaraCve());
					inventario.setPosicion(null);
					inventario.setCaducidad(dp.getDtpCaducidad());
					inventario.setSap(dp.getDtpSAP());
					inventario.setCodigo(dp.getDtpCodigo());
					inventario.setFolio(c.getFolio());
					inventario.setLote(dp.getDtpLote());
					inventario.setMp(dp.getDtpMP());
					inventario.setPedimento(dp.getDtpPedimento());
					inventario.setPo(dp.getDtpPO());
					inventario.setCliente(c.getCteCve());
					inventarioList.add(inventario);
					log.debug("Inventario agregado: folio: {}, folioCliente: {}, partidaCve: {}", inventario.getFolio(), inventario.getFolioCliente(), inventario.getPartidaCve());
				}
			}
			
		} catch(Exception ex) {
			log.error("Problema para obtener el inventario del cliente...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return inventarioList;
	}
	
	public List<Inventario> buscarPorCliente(Cliente cliente) {
		List<Inventario> listaInventario = new ArrayList<>();
		List<ConstanciaDeDeposito> constancia = new ArrayList<>();
		EntityManager entity = null;
		try {
			entity = EntityManagerUtil.getEntityManager();
			constancia = entity.createNamedQuery("ConstanciaDeDeposito.findByCteCve", ConstanciaDeDeposito.class)
					.setParameter("cteCve", cliente.getCteCve()).getResultList();
			
			for (ConstanciaDeDeposito c : constancia) {
				List<Partida> partidaList = c.getPartidaList();
				for (Partida p : partidaList) {
					Inventario inventario = new Inventario(); 
					inventario.setFolioCliente(c.getFolioCliente());
					p.getUnidadDeProductoCve();
					inventario.setProducto(p.getUnidadDeProductoCve().getProductoCve());
					inventario.setUnidadManejo(p.getUnidadDeProductoCve().getUnidadDeManejoCve());
					inventario.setPartidaCve(p.getPartidaCve());
					inventario.setPlanta(p.getCamaraCve().getPlantaCve());
					inventario.setCamara(p.getCamaraCve());
					inventario.setPosicion(null);
					Integer cantidadInicial = p.getCantidadTotal(); //Obtenemos la cantidad inicial de la partida
					BigDecimal pesoInicial = p.getPesoTotal(); 
					inventario.setCantidad(cantidadInicial);
					inventario.setPeso(pesoInicial);
					List<DetalleConstanciaSalida> detalleConstanciaSalidaList = p.getDetalleConstanciaSalidaList();
					List<DetallePartida> detallePartidaList = p.getDetallePartidaList();
					p.getCamaraCve().getPlantaCve();
					
					
					Integer suma=0;
					BigDecimal sumaPeso = new BigDecimal(0).setScale(3,RoundingMode.HALF_UP);
					
					for (DetalleConstanciaSalida dcs : detalleConstanciaSalidaList) { //Por cada partida, obtenemos su detalle de salidas.  
						sumaPeso = sumaPeso.add(dcs.getPeso()); 
						suma = suma + dcs.getCantidad();
					}
					Integer cantidadRestante = cantidadInicial - suma;
					BigDecimal pesoRestante = pesoInicial.subtract(sumaPeso);
					inventario.setCantidad(cantidadRestante);
					inventario.setPeso(pesoRestante);
					
					for (DetallePartida dp : detallePartidaList) {
						inventario.setCaducidad(dp.getDtpCaducidad());
						inventario.setSap(dp.getDtpSAP());
						inventario.setCodigo(dp.getDtpCodigo());
						inventario.setFolio(null);
						inventario.setLote(dp.getDtpLote());
						inventario.setMp(dp.getDtpMP());
						inventario.setPedimento(dp.getDtpPedimento());
						inventario.setPo(dp.getDtpPO());
						
						break;
					}
					inventario.setCliente(c.getCteCve());
					listaInventario.add(inventario);
				}
			}
		} catch(Exception ex) {
			log.error("Problema para obtener el listado de inventario...", ex);
		} finally {
			EntityManagerUtil.close(entity);
		}
		
		return listaInventario;
	}
	
	@Deprecated
	public List<Inventario> buscarPorCliente(Cliente cliente,Planta planta) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> constancia = new ArrayList<>();
		List<Inventario> listaInventario = new ArrayList<>();
		constancia = entity.createNamedQuery("ConstanciaDeDeposito.findByCteCveAndPlanta", ConstanciaDeDeposito.class)
				.setParameter("cteCve", cliente.getCteCve()).setParameter("plantaCve", planta.getPlantaCve()).getResultList();
		//System.out.println(constancia);//imprimo para verificar
		for (ConstanciaDeDeposito c : constancia) {
			Inventario inventario = new Inventario(); // Inicializamos Inventario
			List<Partida> partidaList = c.getPartidaList();

			for (Partida p : partidaList) {
				//Inventario inventario = new Inventario(); // Inicializamos Inventario
				//inventario.setConstanciaDeDeposito(c);//agregue constanciadedeposito
				inventario.setFolioCliente(c.getFolioCliente());
				p.getUnidadDeProductoCve();
				inventario.setProducto(p.getUnidadDeProductoCve().getProductoCve());
				inventario.setUnidadManejo(p.getUnidadDeProductoCve().getUnidadDeManejoCve());
				inventario.setPartidaCve(p.getPartidaCve());
				inventario.setPlanta(p.getCamaraCve().getPlantaCve());
				inventario.setCamara(p.getCamaraCve());
				inventario.setPosicion(null);
				Integer cantidadInicial = p.getCantidadTotal(); // Obtenemos la cantidad inicial de la partida
				BigDecimal pesoInicial = p.getPesoTotal();
				inventario.setCantidad(cantidadInicial);
				inventario.setPeso(pesoInicial);
				List<DetalleConstanciaSalida> detalleConstanciaSalidaList = p.getDetalleConstanciaSalidaList();
				List<DetallePartida> detallePartidaList = p.getDetallePartidaList();
				p.getCamaraCve().getPlantaCve();

				Integer suma = 0;
				BigDecimal sumaPeso = new BigDecimal(0).setScale(3, RoundingMode.HALF_UP);

				for (DetalleConstanciaSalida dcs : detalleConstanciaSalidaList) { // Por cada partida, obtenemos su
																					// detalle de salidas.
					sumaPeso = sumaPeso.add(dcs.getPeso());
					suma = suma + dcs.getCantidad();
				}
				Integer cantidadRestante = cantidadInicial - suma;
				BigDecimal pesoRestante = pesoInicial.subtract(sumaPeso);
				inventario.setCantidad(cantidadRestante);
				inventario.setPeso(pesoRestante);
				for (DetallePartida dp : detallePartidaList) {
					inventario.setCaducidad(dp.getDtpCaducidad());
					inventario.setSap(dp.getDtpSAP());
					inventario.setCodigo(dp.getDtpCodigo());
					inventario.setFolio(null);
					inventario.setLote(dp.getDtpLote());
					inventario.setMp(dp.getDtpMP());
					inventario.setPedimento(dp.getDtpPedimento());
					inventario.setPo(dp.getDtpPO());
					break;
				}
				//listaInventario.add(inventario);
			}
			inventario.setConstanciaDeDeposito(c);
			listaInventario.add(inventario);
		}
		entity.close();
		return listaInventario;
	}

	@Override
	public String guardar(ConstanciaDeDeposito e) {
		return null;
	}

	@Override
	public String eliminar(ConstanciaDeDeposito e) {
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeDeposito> listado) {
		return null;
	}
	@Override
	public String actualizar(ConstanciaDeDeposito e) {
		return null;
	}

	@Override
	public ConstanciaDeDeposito buscarPorId(Integer id) {
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarTodos() {
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
		return null;
	}
}