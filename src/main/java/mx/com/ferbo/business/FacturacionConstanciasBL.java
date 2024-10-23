package mx.com.ferbo.business;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Tarima;

public class FacturacionConstanciasBL {
	
	private static Logger log = LogManager.getLogger(FacturacionConstanciasBL.class);
	
	public BigDecimal getCantidad(List<Partida> partidas, String tipoFacturacion) {

		BigDecimal cantidad = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
		
		if(tipoFacturacion.equals("T")) {
			this.contarTarimas(partidas);
		} else if(tipoFacturacion.equals("K")) {
			this.contarPeso(partidas);
		}
		log.info("Cantidad: {} {}", cantidad, tipoFacturacion);

		for (Partida p : partidas) {

			if (tipoFacturacion.equals("T")) {
				cantidad = cantidad.add(p.getNoTarimas());
			} else {
				cantidad = cantidad.add(p.getPesoTotal());
				log.debug("Peso total: {}" + p.getPesoTotal());
				log.debug("Cantidad: " + cantidad);
			}
		}
		
		log.info("Cantidad: {} {}", cantidad, tipoFacturacion);

		return cantidad;
	}
	
	private BigDecimal contarTarimas(List<Partida> partidas) {
		BigDecimal cantidad = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
		BigDecimal fraccionTarimas = null;
		List<Tarima> tarimas = null;
		
		//Primero se verifica si hay partidas asociadas a una tarima y se realiza el conteo de tarimas.
		log.info("Contando partidas por tarima...");
		
		tarimas = partidas.stream()
				.filter(p -> p.getTarima() != null)
				.map(Partida::getTarima)
				.distinct()
				.collect(Collectors.toList())
				;
		
		tarimas.forEach(tarima -> log.info("Tarima: {}", tarima));
		
		cantidad = cantidad.add(new BigDecimal(tarimas.size()).setScale(3, BigDecimal.ROUND_HALF_UP));
		
		log.info("Contando tarimas a partir del atrubuto no_tarima.");
		cantidad = partidas.stream()
				.filter(p -> p.getNoTarimas().compareTo(BigDecimal.ONE.setScale(3, BigDecimal.ROUND_HALF_UP)) >= 0 )
				.map(item -> item.getNoTarimas())
				.reduce(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
				;
		
		fraccionTarimas = partidas.stream()
				.filter(p -> p.getNoTarimas().compareTo(BigDecimal.ONE.setScale(3, BigDecimal.ROUND_HALF_UP)) < 0 )
				.map(item -> item.getNoTarimas())
				.reduce(BigDecimal.ZERO.setScale(3, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
				;
		
		fraccionTarimas = fraccionTarimas.setScale(0, BigDecimal.ROUND_CEILING);
		cantidad = cantidad.add(fraccionTarimas);
		
		return cantidad;
	}
	
	private BigDecimal contarPeso(List<Partida> partidas) {
		BigDecimal cantidad = new BigDecimal(0).setScale(3, BigDecimal.ROUND_HALF_UP);
		
		cantidad = partidas.stream()
				.map(item -> item.getPesoTotal())
				.reduce(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal::add)
				;
		
		return cantidad;
	}
	
	
	

}
