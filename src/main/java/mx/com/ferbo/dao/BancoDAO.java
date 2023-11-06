package mx.com.ferbo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.util.EntityManagerUtil;

public class BancoDAO extends IBaseDAO<Bancos, Integer> {
	private static Logger log = LogManager.getLogger(BancoDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<Bancos> findall() {
		
		EntityManager entity = null;
		List<Bancos> b = null;
		
		try {
			entity = EntityManagerUtil.getEntityManager();
			Query sql = entity.createNamedQuery("Bancos.findAll", Bancos.class);
			b = sql.getResultList();
		} catch (Exception e) {
			log.error("Problema al encontrar bancos",e);
		}finally {
			EntityManagerUtil.close(entity);
		}
		
		return b;
	}	 
	@Override
	public Bancos buscarPorId(Integer id) {
		Bancos bc = null;
		EntityManager em = null;
		try {
			em = EntityManagerUtil.getEntityManager();
			bc = em.find(Bancos.class, id);
			
		}catch(Exception e) {
			log.error("Error al buscar banco...",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return bc;
	}

	@Override
	public List<Bancos> buscarTodos() {
		
		EntityManager em = null;		
		List<Bancos> listado = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			listado = em.createNamedQuery("Bancos.findAll", Bancos.class).getResultList();
		} catch (Exception e) {
			log.error("Problema para buscar Todos los bancos", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		
		return listado;
	}

	@Override
	public List<Bancos> buscarPorCriterios(Bancos e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String actualizar(Bancos bancos) {
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.merge(bancos);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("ERROR actualizando Banco" + e.getMessage());
			return "ERROR";
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String guardar(Bancos bancos) {
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(bancos);
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al guardar banco",e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminar(Bancos bancos) {
		
		EntityManager em = null;
		
		try {
			em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			em.remove(em.merge(bancos));
			em.getTransaction().commit();
		} catch (Exception e) {
			log.error("Problema al eliminar banco", e);
		}finally {
			EntityManagerUtil.close(em);
		}
		return null;
	}

	@Override
	public String eliminarListado(List<Bancos> listado) {
		// TODO Auto-generated method stub
		return null;
	}

//	PRIVATE STATIC LOGGER LOG = LOGGER.GETLOGGER(BANCODAO.CLASS);
//
//	PRIVATE STATIC FINAL STRING SELECT_BANCOS = "SELECT CLAVE, NOMBRE FROM BANCOS;";
//	PRIVATE STATIC FINAL STRING SELECT_CLAVE_BANCO = "SELECT ID FROM BANCOS WHERE CLAVE = ?;";
//	PRIVATE STATIC FINAL STRING SELECT_NOMBRE_BANCO = "SELECT ID FROM BANCOS WHERE NOMBRE = ?;";
//	PRIVATE STATIC FINAL STRING SELECT_GRL_BANCO = "SELECT ID FROM BANCOS WHERE CLAVE = ? AND NOMBRE = ?;";
//	PRIVATE STATIC FINAL STRING INSERT_BANCO = "INSERT INTO BANCOS (CLAVE, NOMBRE) VALUES (?, ?);";
//	PRIVATE STATIC FINAL STRING UPDATE_CLAVE_BANCO = "UPDATE BANCOS SET CLAVE = ? WHERE ID = ?;";
//	PRIVATE STATIC FINAL STRING UPDATE_NOMBRE_BANCO = "UPDATE BANCOS SET NOMBRE = ? WHERE ID = ?;";
//	PRIVATE STATIC FINAL STRING UPDATE_GRL_BANCO = "UPDATE BANCOS SET CLAVE = ?, NOMBRE = ? WHERE ID = ?;";
//
//	@OVERRIDE
//	PUBLIC LIST<BANCOS> GETBANCO() {
//		CONNECTION CONNECTION = NULL;
//		PREPAREDSTATEMENT PREPAREDSTATEMENT = NULL;
//		RESULTSET RESULTSET = NULL;
//		LIST<BANCOS> LISTABANCOS = NULL;
//		BANCOS BANCOS = NULL;
//		TRY {
////			CONNECTION = GETCONNECTION();
//			PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(SELECT_BANCOS);
//			RESULTSET = PREPAREDSTATEMENT.EXECUTEQUERY();
//			LISTABANCOS = NEW ARRAYLIST<>();
//			WHILE (RESULTSET.NEXT()) {
//				BANCOS = NEW BANCOS();
//				BANCOS.SETCLAVE(RESULTSET.GETSTRING("CLAVE"));
//				BANCOS.SETNOMBRE(RESULTSET.GETSTRING("NOMBRE"));
//				LISTABANCOS.ADD(BANCOS);
//			}
//		} CATCH (SQLEXCEPTION EX) {
//			EX.PRINTSTACKTRACE(SYSTEM.OUT);
//		} FINALLY {
////			TRY {
////				CLOSE(RESULTSET);
////				CLOSE(PREPAREDSTATEMENT);
////				CLOSE(CONNECTION);
////			} CATCH (SQLEXCEPTION E) {
////				// TODO AUTO-GENERATED CATCH BLOCK
////				E.PRINTSTACKTRACE();
////			}
//		}
//		RETURN LISTABANCOS;
//	}
//
//	@OVERRIDE
//	PUBLIC VOID INSERTBANCO(BANCOS BANCO) {
//		CONNECTION CONNECTION = NULL;
//		PREPAREDSTATEMENT PREPAREDSTATEMENT = NULL;
//		TRY {
////			CONNECTION = GETCONNECTION();
//			PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(INSERT_BANCO, PREPAREDSTATEMENT.RETURN_GENERATED_KEYS);
//			PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETCLAVE());
//			PREPAREDSTATEMENT.SETSTRING(2, BANCO.GETNOMBRE());
//			PREPAREDSTATEMENT.EXECUTEUPDATE();
//
//			CONNECTION.COMMIT();
//		} CATCH (SQLEXCEPTION E) {
//			// TODO AUTO-GENERATED CATCH BLOCK
//			E.PRINTSTACKTRACE();
//		} FINALLY {
////			TRY {
////				CLOSE(PREPAREDSTATEMENT);
////				CLOSE(CONNECTION);
////			} CATCH (SQLEXCEPTION E) {
////				// TODO AUTO-GENERATED CATCH BLOCK
////				E.PRINTSTACKTRACE();
////			}
//		}
//	}
//
//	@SUPPRESSWARNINGS("RESOURCE")
//	@OVERRIDE
//	PUBLIC VOID UPDATEBANCO(BANCOS BANCO) {
//		BANCOS BANCO_TMP = NULL;
//		CONNECTION CONNECTION = NULL;
//		PREPAREDSTATEMENT PREPAREDSTATEMENT = NULL;
//		RESULTSET RESULTSET = NULL;
//		TRY {
////			CONNECTION = GETCONNECTION();
//			IF ((BANCO.GETCLAVE() != NULL || !BANCO.GETCLAVE().ISEMPTY() || BANCO.GETCLAVE().LENGTH() != 0)
//					&& (BANCO.GETNOMBRE() != NULL || !BANCO.GETNOMBRE().ISEMPTY() || BANCO.GETNOMBRE().LENGTH() != 0)) {
//				PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(SELECT_GRL_BANCO);
//				PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETCLAVE());
//				PREPAREDSTATEMENT.SETSTRING(2, BANCO.GETNOMBRE());
//				RESULTSET = PREPAREDSTATEMENT.EXECUTEQUERY();
//				BANCO_TMP = NEW BANCOS();
//				WHILE (RESULTSET.NEXT()) {
//					BANCO_TMP.SETID(RESULTSET.GETINT("ID"));
//				}
//			}ELSE IF ((BANCO.GETCLAVE() != NULL || !BANCO.GETCLAVE().ISEMPTY() || BANCO.GETCLAVE().LENGTH() != 0)
//					&& (BANCO.GETNOMBRE() == NULL || BANCO.GETNOMBRE().ISEMPTY() || BANCO.GETNOMBRE().LENGTH() == 0)) {
//				PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(SELECT_CLAVE_BANCO);
//				PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETCLAVE());
//				RESULTSET = PREPAREDSTATEMENT.EXECUTEQUERY();
//				BANCO_TMP = NEW BANCOS();
//				WHILE (RESULTSET.NEXT()) {
//					BANCO_TMP.SETID(RESULTSET.GETINT("ID"));
//				}
//			} ELSE IF ((BANCO.GETCLAVE() != NULL || !BANCO.GETCLAVE().ISEMPTY() || BANCO.GETCLAVE().LENGTH() != 0)
//					&& (BANCO.GETNOMBRE() == NULL || BANCO.GETNOMBRE().ISEMPTY() || BANCO.GETNOMBRE().LENGTH() == 0)) {
//				PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(SELECT_NOMBRE_BANCO);
//				PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETNOMBRE());
//				RESULTSET = PREPAREDSTATEMENT.EXECUTEQUERY();
//				BANCO_TMP = NEW BANCOS();
//				WHILE (RESULTSET.NEXT()) {
//					BANCO_TMP.SETID(RESULTSET.GETINT("ID"));
//				}
//			}
//			
//			IF ((BANCO.GETCLAVE() != NULL || !BANCO.GETCLAVE().ISEMPTY() || BANCO.GETCLAVE().LENGTH() != 0)
//					&& (BANCO.GETNOMBRE() != NULL || !BANCO.GETNOMBRE().ISEMPTY() || BANCO.GETNOMBRE().LENGTH() != 0)) {
//				PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(UPDATE_GRL_BANCO);
//				PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETCLAVE());
//				PREPAREDSTATEMENT.SETSTRING(2, BANCO.GETNOMBRE());
//				PREPAREDSTATEMENT.SETINT(3, BANCO_TMP.GETID());
//			} ELSE IF ((BANCO.GETCLAVE() != NULL || !BANCO.GETCLAVE().ISEMPTY() || BANCO.GETCLAVE().LENGTH() != 0)
//					&& (BANCO.GETNOMBRE() == NULL || BANCO.GETNOMBRE().ISEMPTY() || BANCO.GETNOMBRE().LENGTH() == 0)) {
//				PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(UPDATE_CLAVE_BANCO);
//				PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETCLAVE());
//				PREPAREDSTATEMENT.SETINT(2, BANCO_TMP.GETID());
//			} ELSE IF ((BANCO.GETCLAVE() == NULL || BANCO.GETCLAVE().ISEMPTY() || BANCO.GETCLAVE().LENGTH() == 0)
//					&& (BANCO.GETNOMBRE() != NULL || !BANCO.GETNOMBRE().ISEMPTY() || BANCO.GETNOMBRE().LENGTH() != 0)) {
//				PREPAREDSTATEMENT = CONNECTION.PREPARESTATEMENT(UPDATE_NOMBRE_BANCO);
//				PREPAREDSTATEMENT.SETSTRING(1, BANCO.GETNOMBRE());
//				PREPAREDSTATEMENT.SETINT(2, BANCO_TMP.GETID());
//			}
//
//			PREPAREDSTATEMENT.EXECUTEUPDATE();
//
//			CONNECTION.COMMIT();
//		} CATCH (SQLEXCEPTION E) {
//			// TODO AUTO-GENERATED CATCH BLOCK
//			E.PRINTSTACKTRACE();
//		} FINALLY {
////			TRY {
////				CLOSE(PREPAREDSTATEMENT);
////				CLOSE(CONNECTION);
////			} CATCH (SQLEXCEPTION E) {
////				// TODO AUTO-GENERATED CATCH BLOCK
////				E.PRINTSTACKTRACE();
////			}
//		}
//	}

	
}
