package mx.com.ferbo.dao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.EntityManagerUtil;

public class InventarioDAO extends IBaseDAO<ConstanciaDeDeposito, Integer>{	
	
	@Override
	public ConstanciaDeDeposito buscarPorId(Integer id) {
	// TODO Auto-generated method stub
	return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarTodos() {
	// TODO Auto-generated method stub
	return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
	// TODO Auto-generated method stub
	return null;
	}

	public List<ConstanciaDeDeposito> buscarPorCliente(ConstanciaDeDeposito cteCve) {
	EntityManager entity = EntityManagerUtil.getEntityManager();
	List<ConstanciaDeDeposito> constancia = new ArrayList<>();
	constancia = entity.createNamedQuery("ConstanciaDeDeposito.findByCteCve", ConstanciaDeDeposito.class)
	.setParameter("ctecve", cteCve.getCteCve())
	.getResultList();

	for(ConstanciaDeDeposito c: constancia) {
	List<Partida> partidaList = c.getPartidaList();

	for(Partida p : partidaList) {
	Integer x = p.getCantidadTotal();


	List<DetalleConstanciaSalida> detalleConstanciaSalidaList = p.getDetalleConstanciaSalidaList();
	for(DetalleConstanciaSalida dcs : detalleConstanciaSalidaList) {
	Integer cantidad = dcs.getCantidad();

	Integer resultado = x - cantidad;

	}
	}

	}
	return constancia;
	}

	@Override
	public String actualizar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String guardar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminar(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String eliminarListado(List<ConstanciaDeDeposito> listado) {
		// TODO Auto-generated method stub
		return null;
	}

}
