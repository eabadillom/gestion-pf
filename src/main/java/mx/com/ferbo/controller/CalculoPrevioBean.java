package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import mx.com.ferbo.dao.PrecioServicioDAO;
import mx.com.ferbo.model.Aviso;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ConstanciaDeDeposito;
import mx.com.ferbo.model.ConstanciaDepositoDetalle;
import mx.com.ferbo.model.ConstanciaFactura;
import mx.com.ferbo.model.ConstanciaFacturaDs;
import mx.com.ferbo.model.ConstanciaServicioDetalle;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.PartidaServicio;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.PrecioServicio;
import mx.com.ferbo.model.ProductoConstanciaDs;
import mx.com.ferbo.model.Servicio;
import mx.com.ferbo.model.ServicioConstancia;
import mx.com.ferbo.model.ServicioConstanciaDs;
import mx.com.ferbo.model.TipoCobro;
import mx.com.ferbo.util.FormatUtil;

@Named
@ViewScoped
public class CalculoPrevioBean implements Serializable {

	private static final long serialVersionUID = -1785488265380235016L;

	@Inject
	private FacturacionConstanciasBean facturacionBean;

	private FacesContext context;
	private HttpServletRequest request;

	private List<ConstanciaFactura> listaEntradas;
	private List<ConstanciaFactura> listaVigencias;
	private List<ConstanciaFacturaDs> listaServicios;
	private List<ConstanciaFacturaDs> listaConstanciaFacturaDs;

	private PrecioServicioDAO precioServicioDAO;

	private Cliente clienteSelect;
	private Planta plantaSelect;
	private Factura factura;
	private ServicioConstancia selectServicioE;
	private ServicioConstancia selectServicioV;
	private ServicioConstanciaDs selectServicioDs;
	private Parametro iva;

	private Date fechaEmision;
	private BigDecimal cantidad;
	private BigDecimal subTotalEntrada;
	private BigDecimal subTotalVigencias;
	private BigDecimal subTotalServicios;
	private BigDecimal subTotalGeneral;
	private BigDecimal ivaTotal;
	private BigDecimal total;
	private String montoLetra;

	public CalculoPrevioBean() {

		listaConstanciaFacturaDs = new ArrayList<>();
		listaEntradas = new ArrayList<>();
		listaServicios = new ArrayList<>();
		listaVigencias = new ArrayList<>();

		clienteSelect = new Cliente();
		plantaSelect = new Planta();
		factura = new Factura();
		selectServicioE = new ServicioConstancia();
		selectServicioV = new ServicioConstancia();
		selectServicioDs = new ServicioConstanciaDs();

		precioServicioDAO = new PrecioServicioDAO();

	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		subTotalServicios = new BigDecimal(0);
		subTotalEntrada = new BigDecimal(0);
		subTotalVigencias = new BigDecimal(0);
		subTotalGeneral = new BigDecimal(0);
		ivaTotal = new BigDecimal(0);
		total = new BigDecimal(0);

		try {
			context = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) context.getExternalContext().getRequest();
			listaEntradas = (List<ConstanciaFactura>) request.getSession(false).getAttribute("entradas");
			listaVigencias = (List<ConstanciaFactura>) request.getSession(false).getAttribute("vigencias");
			listaServicios = (List<ConstanciaFacturaDs>) request.getSession(false).getAttribute("servicios");
			clienteSelect = (Cliente) request.getSession(false).getAttribute("cliente");
			plantaSelect = (Planta) request.getSession(false).getAttribute("plantaSelect");
			factura = (Factura) request.getSession(false).getAttribute("factura");
			fechaEmision = (Date) request.getSession(false).getAttribute("fechaEmision");
			iva = (Parametro) request.getSession(false).getAttribute("iva"); 

			if (listaEntradas.isEmpty()) {
				listaEntradas = new ArrayList<>();
			}

			if (listaVigencias.isEmpty()) {
				listaVigencias = new ArrayList<>();
			}

			if (listaServicios.isEmpty()) {
				listaServicios = new ArrayList<>();
			}

			procesarServicios();
			procesarEntradas();
			procesarVigencias();
			sumaGeneral();

		} catch (Exception e) {

			System.out.println("ERROR Aqui" + e.getLocalizedMessage());
			System.out.println("ERROR Aqui" + e.getCause());
		}

	}

	public FacturacionConstanciasBean getFacturacionBean() {
		return facturacionBean;
	}

	public void setFacturacionBean(FacturacionConstanciasBean facturacionBean) {
		this.facturacionBean = facturacionBean;
	}

	public List<ConstanciaFactura> getListaEntradas() {
		return listaEntradas;
	}

	public void setListaEntradas(List<ConstanciaFactura> listaEntradas) {
		this.listaEntradas = listaEntradas;
	}

	public List<ConstanciaFactura> getListaVigencias() {
		return listaVigencias;
	}

	public void setListaVigencias(List<ConstanciaFactura> listaVigencias) {
		this.listaVigencias = listaVigencias;
	}

	public List<ConstanciaFacturaDs> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(List<ConstanciaFacturaDs> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<ConstanciaFacturaDs> getListaConstanciaFacturaDs() {
		return listaConstanciaFacturaDs;
	}

	public void setListaConstanciaFacturaDs(List<ConstanciaFacturaDs> listaConstanciaFacturaDs) {
		this.listaConstanciaFacturaDs = listaConstanciaFacturaDs;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public ServicioConstanciaDs getSelectServicioDs() {
		return selectServicioDs;
	}

	public void setSelectServicioDs(ServicioConstanciaDs selectServicioDs) {
		this.selectServicioDs = selectServicioDs;
	}

	public ServicioConstancia getSelectServicioV() {
		return selectServicioV;
	}

	public void setSelectServicioV(ServicioConstancia selectServicioV) {
		this.selectServicioV = selectServicioV;
	}

	public ServicioConstancia getSelectServicioE() {
		return selectServicioE;
	}

	public void setSelectServicioE(ServicioConstancia selectServicioE) {
		this.selectServicioE = selectServicioE;
	}

	public BigDecimal getSubTotalEntrada() {
		return subTotalEntrada;
	}

	public void setSubTotalEntrada(BigDecimal subTotalEntrada) {
		this.subTotalEntrada = subTotalEntrada;
	}

	public BigDecimal getSubTotalVigencias() {
		return subTotalVigencias;
	}

	public void setSubTotalVigencias(BigDecimal subTotalVigencias) {
		this.subTotalVigencias = subTotalVigencias;
	}
	
	public BigDecimal getSubTotalServicios() {
		return subTotalServicios;
	}

	public void setSubTotalServicios(BigDecimal subTotalServicios) {
		this.subTotalServicios = subTotalServicios;
	}
	
	public BigDecimal getSubTotalGeneral() {
		return subTotalGeneral;
	}

	public void setSubTotalGeneral(BigDecimal subTotalGeneral) {
		this.subTotalGeneral = subTotalGeneral;
	}

	public BigDecimal getIvaTotal() {
		return ivaTotal;
	}

	public void setIvaTotal(BigDecimal ivaTotal) {
		this.ivaTotal = ivaTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getMontoLetra() {
		return montoLetra;
	}

	public void setMontoLetra(String montoLetra) {
		this.montoLetra = montoLetra;
	}

	public void procesarServicios() {

		BigDecimal importe = new BigDecimal(0);
		BigDecimal sumTmp;
		Integer idS = 0,idP = 0;
		
		for (ConstanciaFacturaDs cfd : listaServicios) {

			List<ConstanciaServicioDetalle> listaConstanciaSD = cfd.getConstanciaDeServicio()
					.getConstanciaServicioDetalleList();
			List<PartidaServicio> listaPartidaServicio = cfd.getConstanciaDeServicio().getPartidaServicioList();

			cfd.setServicioConstanciaDsList(new ArrayList<>());
			cfd.setProductoConstanciaDsList(new ArrayList<>());

			for (ConstanciaServicioDetalle csd : listaConstanciaSD) {

				cantidad = new BigDecimal(0);

				List<PrecioServicio> listaPrecioS = csd.getServicioCve().getPrecioServicioList();
				PrecioServicio precioServicio = getPrecioServicio(clienteSelect.getCteCve(), listaPrecioS);

				ServicioConstanciaDs servicioConstanciaDs = new ServicioConstanciaDs();
				
				servicioConstanciaDs.setId(idS);
				servicioConstanciaDs.setDescripcion(csd.getServicioCve().getServicioDs());
				servicioConstanciaDs.setConstancia(cfd);

				if (precioServicio.getPrecio() != null) {// modificado por error null al llamar al metodo
															// getPrecioServicio
					cantidad = precioServicio.getPrecio().setScale(2);// variable agregada
					importe = csd.getServicioCantidad().multiply(cantidad);

					servicioConstanciaDs.setCosto(importe.setScale(2));
					servicioConstanciaDs.setTarifa(precioServicio.getPrecio().setScale(2));
				}
				servicioConstanciaDs.setCodigo(csd.getServicioCve().getServicioCod());
				servicioConstanciaDs.setUdCobro("SRV");
				servicioConstanciaDs.setCdUnidad(csd.getServicioCve().getCdUnidad());
				servicioConstanciaDs.setCantidad(csd.getServicioCantidad());

				cfd.getServicioConstanciaDsList().add(servicioConstanciaDs);
				idS++;
			}

			for (PartidaServicio ps : listaPartidaServicio) {

				ProductoConstanciaDs productoConstanciaDs = new ProductoConstanciaDs();

				productoConstanciaDs.setId(idP);
				productoConstanciaDs.setDescripcion(ps.getProductoCve().getProductoDs());
				productoConstanciaDs.setConstancia(cfd);
				productoConstanciaDs.setCatidadCobro(ps.getCantidadDeCobro());
				productoConstanciaDs.setUnidadCobro(ps.getUnidadDeCobro().getUnidadDeManejoDs());
				productoConstanciaDs.setCantidadManejo(new BigDecimal(ps.getCantidadTotal()).setScale(2));
				productoConstanciaDs.setUnidadManejo(ps.getUnidadDeManejoCve().getUnidadDeManejoDs());

				cfd.getProductoConstanciaDsList().add(productoConstanciaDs);
				idP++;
			}
			
			sumTmp = subTotalServicios(cfd.getServicioConstanciaDsList());
			subTotalServicios = subTotalServicios.add(sumTmp);
		
		}

	}

	private PrecioServicio getPrecioServicio(Integer idCliente, List<PrecioServicio> listaPrecioS) {

		List<PrecioServicio> listaPrecioSTemp = null;
		PrecioServicio precioServicio = new PrecioServicio();

		listaPrecioSTemp = listaPrecioS.stream().filter(ps -> ps.getCliente().getCteCve().intValue() == idCliente)
				.collect(Collectors.toList());

		if (!(listaPrecioSTemp.isEmpty())) { // modificado por error de retornar objeto precioServicio en null
			Optional<PrecioServicio> precioServicioMax = listaPrecioSTemp.stream()
					.max((i, j) -> i.getAvisoCve().getAvisoCve().compareTo(j.getAvisoCve().getAvisoCve()));
			precioServicio = precioServicioMax.get();// colocar condicional, si y solo si precioServicioMax != null
		}

		return precioServicio;
	}

	// ENTRADAS (CONSTANCIA DE DEPOSITO)

	public void procesarEntradas() {

		Integer id = 0;
		BigDecimal sumTmp;
		
		for (ConstanciaFactura cf : listaEntradas) {

			ConstanciaDeDeposito cdd = cf.getFolio();
			// listaServiciosConstancias = new ArrayList<>();
			Aviso aviso = cdd.getAvisoCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			cf.setServicioConstanciaList(new ArrayList<>());

			for (ConstanciaDepositoDetalle cs : cdd.getConstanciaDepositoDetalleList()) {

				// System.out.println(cs.getServicioCve().getCobro().getId());
				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();

				BigDecimal importe = new BigDecimal(0);
				BigDecimal cantidad = null;

				PrecioServicio precioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
						clienteSelect.getCteCve(), servicio.getServicioCve());

				switch (tipoCobro.getId()) {

				case 1:
				case 2:

					cantidad = cs.getServicioCantidad();
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2));
					sc.setUnidadMedida("SRV");
					System.out.println("El tipo cobro es 1 o 2 y su importe es: " + importe);
					break;

				case 3:
				case 4:

					cantidad = getCantidadPartidas(cdd.getPartidaList(), tipoFacturacion);

					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2));
					System.out.println("El tipo de cobro es 3 o 4 y su importe es entradas: " + importe);

					if (aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					} else {
						sc.setUnidadMedida("KG");
					}

					break;

				default:

					System.out.println("No existe el tipo de cobro");
					break;
				}

				sc.setId(id);
				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio().setScale(2));
				sc.setBaseCargo(cantidad.setScale(2));
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());

				Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());

				sc.setCodigo(null);

				/*
				 * listaServiciosConstancias.add(sc);
				 * cf.setServicioConstanciaList(listaServiciosConstancias);
				 */

				cf.getServicioConstanciaList().add(sc); // datos a mostrar row ex--

				// listaServiciosEntradas.add(sc);
				id++;

			}
			
			sumTmp = subTotalEntradaVigencias(cf.getServicioConstanciaList()).setScale(2);
			subTotalEntrada = subTotalEntrada.add(sumTmp);
	
		}
	}

	public BigDecimal getCantidadPartidas(List<Partida> listaPartidas, String tipoFacturacion) {

		BigDecimal cantidad = new BigDecimal(0);

		for (Partida p : listaPartidas) {

			if (tipoFacturacion.equals("T")) {
				cantidad = cantidad.add(p.getNoTarimas());
				// cantidad = p.getNoTarimas();
			} else {
				cantidad = cantidad.add(p.getPesoTotal());
				// cantidad = p.getPesoTotal();
			}

		}

		return cantidad;
	}

	// VIGENCIAS (CONSTANCIA DE DEPOSITO)

	public void procesarVigencias() {

		Integer id = 0;
		BigDecimal importe = new BigDecimal(0);
		BigDecimal sumTmp;
		// List<ServicioConstancia> listaServiciosConstancias = null;

		for (ConstanciaFactura cf : listaVigencias) {

			ConstanciaDeDeposito cdd = cf.getFolio();
			// listaServiciosConstancias = new ArrayList<>();
			Aviso aviso = cdd.getAvisoCve();
			String tipoFacturacion = aviso.getAvisoTpFacturacion();
			cf.setServicioConstanciaList(new ArrayList<>());

			for (ConstanciaDepositoDetalle cs : cdd.getConstanciaDepositoDetalleList()) {

				// System.out.println(cs.getServicioCve().getCobro().getId());
				Servicio servicio = cs.getServicioCve();
				TipoCobro tipoCobro = servicio.getCobro();
				ServicioConstancia sc = new ServicioConstancia();

				BigDecimal cantidad = null;

				PrecioServicio precioServicio = precioServicioDAO.busquedaServicio(cdd.getAvisoCve().getAvisoCve(),
						clienteSelect.getCteCve(), servicio.getServicioCve());

				switch (tipoCobro.getId()) {

				case 1:
				case 2:
				case 3:
					break;
				case 4:

					cantidad = getCantidadPartidas(cdd.getPartidaList(), tipoFacturacion);
					importe = cantidad.multiply(precioServicio.getPrecio());
					sc.setCosto(importe.setScale(2));
					System.out.println("El tipo de cobro es 3 o 4 y su importe es: " + importe);

					if (aviso.getAvisoTpFacturacion().equals("T")) {
						sc.setUnidadMedida("TRM");
					} else {
						sc.setUnidadMedida("KG");
					}

					break;

				default:

					System.out.println("No existe el tipo de cobro");
					break;
				}

				sc.setConstancia(cf);
				sc.setTarifa(precioServicio.getPrecio().setScale(2));
				if(cantidad!=null) {
					sc.setBaseCargo(cantidad.setScale(2));
				}
				sc.setDescripcion(cs.getServicioCve().getServicioDs());
				sc.setPlantaCve(plantaSelect.getPlantaCve());
				sc.setPlantaDs(plantaSelect.getPlantaDs());
				sc.setPlantaAbrev(plantaSelect.getPlantaAbrev());
				sc.setPlantaCod(plantaSelect.getPlantaCod());

				Camara camara = cdd.getPartidaList().get(0).getCamaraCve();
				sc.setCamaraCve(camara.getCamaraCve());
				sc.setCamaraDs(camara.getCamaraDs());
				sc.setCamaraAbrev(camara.getCamaraAbrev());

				sc.setCodigo(null);

				if (tipoCobro.getId() == 4) {
					cf.getServicioConstanciaList().add(sc);
					id++;
				}
				/*
				 * listaServiciosConstancias.add(sc);
				 * cf.setServicioConstanciaList(listaServiciosConstancias);//datos a mostrar row
				 * ex--
				 */

			}
			
			sumTmp = subTotalEntradaVigencias(cf.getServicioConstanciaList()).setScale(2);
			subTotalVigencias = subTotalVigencias.add(sumTmp);
			
		}
	}

	
	public void eliminarServicioEntrada() {

		for (ConstanciaFactura cf : listaEntradas) {
			
			if(cf.getFolio().getFolioCliente().equals(selectServicioE.getConstancia().getFolio().getFolioCliente())) {
			
				for (ServicioConstancia sc : cf.getServicioConstanciaList()) {
	
					if (sc.getDescripcion().equals(selectServicioE.getDescripcion())) {
						cf.getServicioConstanciaList().remove(selectServicioE);
						break;
					}
					
				}
			}
			
		}
		
		recalculoEntradas();
		
		PrimeFaces.current().ajax().update("form:dt-selectedEntradas:dt-serviciosEntrada");

	}
	
	
	public void eliminarServicioVigencia() {
		
		for (ConstanciaFactura cf : listaVigencias) {
			
			if(cf.getFolio().getFolioCliente().equals(selectServicioV.getConstancia().getFolio().getFolioCliente())) {
				for (ServicioConstancia sc : cf.getServicioConstanciaList()) {
						if (sc.getDescripcion().equals(selectServicioV.getDescripcion())) {
							cf.getServicioConstanciaList().remove(selectServicioV);
							break;
						}
					}
			}
			
		}
		
		recalculoVigencias();
		
		PrimeFaces.current().ajax().update("form:dt-selectVigencias:dt-serviciosVigencia");
		
	}
	
	public void eliminarServicioDs() {
		
		BigDecimal sum;
		
		for(ConstanciaFacturaDs cfd: listaServicios) {
			
			if(cfd.getFolioCliente().equals(selectServicioDs.getConstancia().getFolioCliente())) {
			
				for(ServicioConstanciaDs sc: cfd.getServicioConstanciaDsList()) {
					
					if(sc.getDescripcion().equals(selectServicioDs.getDescripcion())) {
						cfd.getServicioConstanciaDsList().remove(selectServicioDs);
						break;
					}
				}
			}
			
		}
		
		recalculoServicioDs();//llamo al metodo ya con lista actualizada 
		
		PrimeFaces.current().ajax().update("form:dt-constanciaFacturaDs:dt-serviciosDs");
		
	}
	
	//Calculo el subTotal antes de eliminar un servicio
	
	public BigDecimal subTotalEntradaVigencias(List<ServicioConstancia> lista) {
		
		BigDecimal subTotal = new BigDecimal(0);
		
		for(ServicioConstancia sc: lista) {
			subTotal = subTotal.add(sc.getCosto());
		}
		
		return subTotal;
	}
	
	public BigDecimal subTotalServicios(List<ServicioConstanciaDs> lista) {
		BigDecimal subTotal = new BigDecimal(0);
		
		for(ServicioConstanciaDs scd: lista) {
			subTotal = subTotal.add(scd.getCosto());
		}
		
		return subTotal;
		
	}
	
	public void recalculoServicioDs() {//calculo el subtotal con la lista actualizada despues de eliminar un servicio 
		
		subTotalServicios = new BigDecimal(0);
		BigDecimal sum;
		
		for(ConstanciaFacturaDs cf: listaServicios) {
			
			sum = subTotalServicios(cf.getServicioConstanciaDsList());	
			subTotalServicios = subTotalServicios.add(sum);
		}
		
		sumaGeneral();
		
	}
	
	public void recalculoEntradas() {
		
		subTotalEntrada = new BigDecimal(0);
		
		BigDecimal sum;
		
		for(ConstanciaFactura cf: listaEntradas) {
			
			sum = subTotalEntradaVigencias(cf.getServicioConstanciaList());
			subTotalEntrada = subTotalEntrada.add(sum);
			
		}
		
		sumaGeneral();
		
	}
	
	public void recalculoVigencias() {
		
		subTotalVigencias = new BigDecimal(0);
		
		BigDecimal sum;
		
		for(ConstanciaFactura cf: listaVigencias) {
			
			sum = subTotalEntradaVigencias(cf.getServicioConstanciaList());
			subTotalVigencias = subTotalVigencias.add(sum);
			
		}
		
		sumaGeneral();
		
	}
	
	public void sumaGeneral() {
		
		subTotalGeneral = subTotalEntrada.add(subTotalServicios.add(subTotalVigencias)).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		ivaTotal = subTotalGeneral.multiply(new BigDecimal(iva.getValor())).setScale(2, BigDecimal.ROUND_HALF_UP);
		
		total = subTotalGeneral.add(ivaTotal).setScale(2,BigDecimal.ROUND_HALF_UP);
		
		FormatUtil formato = new FormatUtil();
		
		montoLetra = formato.getMontoConLetra(total.doubleValue());
	}
	
	/*public void cerrarSesion() {
		
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		
		response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
		
	}*/
	
}
