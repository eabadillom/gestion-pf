package mx.com.ferbo.util;

import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class CfdiUtils {

    public static String getCFDIUUIDFromString(String xmlContent) {
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

            return (uuid == null || uuid.isEmpty()) ? null : uuid;

        } catch (Exception e) {
            // Log opcionalmente el error
            return null;
        }
    }
}
