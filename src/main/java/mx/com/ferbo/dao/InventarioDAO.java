package mx.com.ferbo.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import mx.com.ferbo.commons.dao.IBaseDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.DetalleConstanciaSalida;
import mx.com.ferbo.model.DetallePartida;
import mx.com.ferbo.model.Inventario;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.util.EntityManagerUtil;

public class InventarioDAO extends IBaseDAO<ConstanciaDeDeposito, Integer> {
	@SuppressWarnings("unchecked")
	public List<ConstanciaDeDeposito> findall() {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> cdd = null;
		Query sql = entity.createNamedQuery("ConstanciaDeDeposito.findAll", ConstanciaDeDeposito.class);
		cdd = sql.getResultList();
		return cdd;
	}

	@Override
	public ConstanciaDeDeposito buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarTodos() {
		//TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConstanciaDeDeposito> buscarPorCriterios(ConstanciaDeDeposito e) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Inventario> buscarPorCliente(Cliente cliente) {
		EntityManager entity = EntityManagerUtil.getEntityManager();
		List<ConstanciaDeDeposito> constancia = new ArrayList<>();
		List<Inventario> listaInventario = new ArrayList<>();
		constancia = entity.createNamedQuery("ConstanciaDeDeposito.findByCteCve", ConstanciaDeDeposito.class)
				.setParameter("cteCve", cliente.getCteCve()).getResultList();
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