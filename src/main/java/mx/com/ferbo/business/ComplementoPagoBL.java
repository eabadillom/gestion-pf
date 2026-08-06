package mx.com.ferbo.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ferbo.facturama.business.CfdiBL;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ferbo.facturama.response.CfdiInfoModel;
import com.ferbo.facturama.request.ReceiverBindingModel;
import com.ferbo.facturama.request.RelatedDocument;
import com.ferbo.facturama.request.Tax;
import com.ferbo.facturama.request.CFDIInfo;
import com.ferbo.facturama.request.Complement;
import com.ferbo.facturama.request.IssuerBindingModel;
import com.ferbo.facturama.request.PaymentBindingModel;
import com.ferbo.facturama.response.FileViewModel;
import com.ferbo.facturama.tools.FacturamaException;
import com.ferbo.mail.beans.Adjunto;
import java.util.Base64;
import java.util.Objects;

import mx.com.ferbo.business.complemento.ComplementoBL;
import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.dao.n.ComplementoPagoDAO;
import mx.com.ferbo.dao.n.EmisoresCFDISDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.DatosPago;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ComplementoPagoBL 
{
    private Logger log = LogManager.getLogger(ComplementoPagoBL.class);
    
    public static final Integer NAME_ID = 14;
    public static final String CFDI_TYPE_P = "P";
    public static final String CFDI_USE_PAGOS = "CP01";
    public static final String CURRENCY = "MXN";
    public static final String PAYMENT_METHOD = "PPD";
    public static final String TAX_NAME = "IVA";
    public static final String TAX_OBJECT = "02";
    
    private ComplementoPago complementoPago;
    
    private ClienteDAO clienteDAO;
    private EmisoresCFDISDAO emisoresDAO;
    private ComplementoPagoDAO complementoPagoDAO;
    
    private ComplementoBL complementoPagoBL;
    private CfdiBL cfdiBL = null;
    
    private Integer idCliente;
    private Integer idEmisor;
    private Usuario usuario = null;
    private String formaPago;
    private List<Pago> listPagos;

    public ComplementoPagoBL(List<Pago> listPagos, Integer idCliente, Integer idEmisor, Usuario usuario, String formaPago) {
        clienteDAO = new ClienteDAO();
        emisoresDAO = new EmisoresCFDISDAO();
        complementoPagoDAO = new ComplementoPagoDAO();
        cfdiBL = new CfdiBL();
        this.listPagos = listPagos;
        this.idCliente = idCliente;
        this.idEmisor = idEmisor;
        this.usuario = usuario;
        this.formaPago = formaPago;
        log.info("Forma de pago del cliente {}: {}", idCliente, formaPago);
    }
    
    public void timbrar() throws InventarioException, DAOException, JsonProcessingException, FacturamaException {
        CFDIInfo cfdi = new CFDIInfo();
        
        complementoPagoBL = new ComplementoBL();
        
        Cliente cliente = clienteDAO.obtenerPorId(idCliente, true);
        EmisoresCFDIS emisor = emisoresDAO.buscarPorId(idEmisor).orElseThrow(() -> new InventarioException("Emisor no encontrado."));
        
        complementoPago = listPagos.stream()
            .map(Pago::getComplementoPago)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
        
        if(complementoPago == null) 
            throw new InventarioException("El folio y serie del complemento de pago no estan asignados.");
        
        cfdi.setCfdiType(CFDI_TYPE_P);
        cfdi.setNameId(NAME_ID); 
        cfdi.setFolio(complementoPago.getNumero());
        cfdi.setSerie(complementoPago.getSerie());
        cfdi.setExpeditionPlace(emisor.getCodigoPostal());
        
        IssuerBindingModel issuerBindingModel = new IssuerBindingModel();
        issuerBindingModel.setRfc(emisor.getNb_rfc());
        issuerBindingModel.setName(emisor.getNb_emisor());
        issuerBindingModel.setFiscalRegime(emisor.getCd_regimen().getCd_regimen());
        cfdi.setIssuer(issuerBindingModel);
        
        String cp = complementoPagoBL.obtenerCP(cliente);
        ReceiverBindingModel receptor = new ReceiverBindingModel(); 
        receptor.setRfc(cliente.getCteRfc());
        receptor.setCfdiUse(CFDI_USE_PAGOS);
        receptor.setName(cliente.getNombre());
        receptor.setFiscalRegime(cliente.getRegimenFiscal().getCd_regimen());
        receptor.setTaxZipCode(cp);
        cfdi.setReceiver(receptor);
        
        Complement complements = new Complement();
        List<PaymentBindingModel> listPayments = new ArrayList();
        List<RelatedDocument> listRelatedDocuments;
        List<Tax> listTaxes;
        for(Pago pago : this.listPagos) {
            listRelatedDocuments = new ArrayList();
            listTaxes = new ArrayList();
            PaymentBindingModel payment = new PaymentBindingModel();
            Date fechaHora = pago.getFecha();
            DateUtil.setTime(fechaHora, pago.getHora().getHour(), pago.getHora().getMinute(), 0);
            log.info("Fecha de complemento de pago {}", fechaHora.toString());
            payment.setDate(fechaHora);
            payment.setPaymentForm(pago.getTipo().getNombre());
            payment.setAmount(pago.getMonto());
            payment.setCurrency(CURRENCY);

            DatosPago saldosPago = complementoPagoBL.obtenerSaldos(pago);
            RelatedDocument relatedDocument = new RelatedDocument();
            relatedDocument.setTaxObject(TAX_OBJECT);
            relatedDocument.setUuid((pago.getFactura().getCfdi() != null) ? pago.getFactura().getCfdi().getUuid() : "ABC" ); //Modificar aqui antes de subir
            relatedDocument.setPartialityNumber(pago.getParcialidad());
            relatedDocument.setSerie(pago.getFactura().getNomSerie());
            relatedDocument.setFolio(pago.getFactura().getNumero());
            relatedDocument.setCurrency(CURRENCY);
            relatedDocument.setPaymentMethod(formaPago);
            relatedDocument.setPreviousBalanceAmount(saldosPago.getSaldoAnterior());
            relatedDocument.setAmountPaid(saldosPago.getMonto());
            relatedDocument.setImpSaldoInsoluto(saldosPago.getSaldoRestante());

            BigDecimal total = pago.getMonto();
            BigDecimal factorIVA = new BigDecimal("1.16");
            BigDecimal subtotal = total.divide(factorIVA, 2, RoundingMode.HALF_UP);
            BigDecimal iva = total.subtract(subtotal);
            BigDecimal rate = new BigDecimal("0.16");
            Tax tax = new Tax();
            tax.setName(TAX_NAME);
            tax.setRate(rate.setScale(2, RoundingMode.HALF_UP));
            tax.setTotal(iva);
            tax.setBase(subtotal);
            tax.setIsRetention(false);
            listTaxes.add(tax);

            relatedDocument.setTaxes(listTaxes);
            listRelatedDocuments.add(relatedDocument);
            payment.setRelatedDocuments(listRelatedDocuments);
            listPayments.add(payment);
        }
        complements.setPayments(listPayments);
        cfdi.setComplemento(complements);
        
        CfdiInfoModel registra = cfdiBL.registra(cfdi);
        
        String idPac             = registra.getId();
        String uuid              = registra.getComplement().getTaxStamp().getUuid();
        Date   fecha             = DateUtil.getDate(registra.getComplement().getTaxStamp().getDate(),DateUtil.FORMATO_ISO_8601);
        String numCertificadoSAT = registra.getComplement().getTaxStamp().getSatCertNumber();
        
        complementoPago.setTimbrado(fecha);
        complementoPago.setPac(idPac);
        complementoPago.setUuid(uuid);
        complementoPago.setCertificadoSAT(numCertificadoSAT);
        
        complementoPagoDAO.actualizar(complementoPago);
    }
    
    public void sendMail() throws FacturamaException {
        SendMailComplementoPagoBL sendMailBO = null;
        String sContent = null;
        byte[] content = null;
        
        Adjunto adjunto = null;
        List<Adjunto> alAdjuntos = null;
        try {
            alAdjuntos = new ArrayList<Adjunto>();
            
            if(complementoPago == null)
                throw new InventarioException("No se estableció un complemento de pago para envío por correo electrónico.");
            
            FileViewModel fileXML = cfdiBL.getFile("xml", "issuedLite", complementoPago.getPac());
            sContent = fileXML.getContent();
            content = Base64.getDecoder().decode(sContent);
            adjunto = new Adjunto("ComplementoPago_" + complementoPago.getSerie() + "-" + complementoPago.getNumero() + ".xml", Adjunto.TP_ARCHIVO_XML, content);
            alAdjuntos.add(adjunto);

            FileViewModel filePDF = cfdiBL.getFile("pdf", "issuedLite", complementoPago.getPac());
            sContent = filePDF.getContent();
            content = Base64.getDecoder().decode(sContent);
            adjunto = new Adjunto("ComplementoPago_" + complementoPago.getSerie()+ "-" + complementoPago.getNumero() + ".pdf", Adjunto.TP_ARCHIVO_PDF, content);
            alAdjuntos.add(adjunto);
            
            sendMailBO = new SendMailComplementoPagoBL(idCliente);
            sendMailBO.setSerie(complementoPago.getSerie());
            sendMailBO.setFolio(complementoPago.getNumero());
            sendMailBO.setAlFiles(alAdjuntos);
            sendMailBO.setLoggedUser(usuario);
            sendMailBO.send();
        } catch(InventarioException ex) {
            log.error("Problema en el envío de correo electrónico de los documentos CFDI...", ex);
        }
    }
    
}
