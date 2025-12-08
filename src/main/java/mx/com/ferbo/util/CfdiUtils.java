package mx.com.ferbo.util;

import java.io.StringReader;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import mx.com.ferbo.model.Cfdi;

public class CfdiUtils {

    public static Cfdi getCFDIUUIDFromString(String xmlContent)
    throws InventarioException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NamespaceContext() {
                public String getNamespaceURI(String prefix) {
                    if ("tfd".equals(prefix))
                        return "http://www.sat.gob.mx/TimbreFiscalDigital";
                    return XMLConstants.NULL_NS_URI;
                }
                public String getPrefix(String uri) { return null; }
                public java.util.Iterator getPrefixes(String uri) { return null; }
            });

            // Extraer UUID del timbre
            String uuid = xpath.evaluate("//tfd:TimbreFiscalDigital/@UUID", doc);
            Date fechaTimbrado = DateUtil.getDate(xpath.evaluate("//tfd:TimbreFiscalDigital/@FechaTimbrado", doc), DateUtil.FORMATO_ISO_8601);
            String noCertificadoSAT = xpath.evaluate("//tfd:TimbreFiscalDigital/@NoCertificadoSAT", doc);
            
            return Cfdi.builder().uuid(uuid).fecha(fechaTimbrado).certificadoSAT(noCertificadoSAT).build();

        } catch (Exception e) {
            throw new InventarioException("Existe un problema para obtener la información del CFDI...", e);
        }
    }
}
