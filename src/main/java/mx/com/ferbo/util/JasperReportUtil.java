package mx.com.ferbo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.OutputStream;
import java.nio.Buffer;

import javassist.bytecode.ByteArray;
import mx.com.ferbo.utils.IOUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class JasperReportUtil {
	public void createPdf(String fileName, Map<String, Object> parameters, String path) throws IOException {
		FacesContext context = null;
		HttpServletResponse response = null;
		OutputStream output = null;
		JasperDesign design = null;
		JasperReport report = null;
		JasperPrint jasperPrint = null;
		try {
			context = FacesContext.getCurrentInstance();
			response = (HttpServletResponse) context.getExternalContext().getResponse();
			output = response.getOutputStream();
			//output = new ByteArrayOutputStream();
			String disposition = String.format("attachment; filename=\"%s\"", fileName);
			response.setHeader("Content-Disposition", disposition);
			response.addHeader("Content-Disposition", disposition);
			response.setContentType("application/pdf");
			design = JRXmlLoader.load(path);
			report = JasperCompileManager.compileReport(design);
			jasperPrint = JasperFillManager.fillReport(report, parameters);
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			
			context.responseComplete();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (JRException ex) {
			ex.printStackTrace();
		}finally {
			output.flush();
			output.close();
		}
	}
	public StreamedContent getPdf(String fileName, Map<String, Object> parameters, String path) throws IOException {
		StreamedContent respuesta = null;
		ByteArrayOutputStream output = null;
		JasperDesign design = null;
		JasperReport report = null;
		JasperPrint jasperPrint = null;
		try {
			output = new ByteArrayOutputStream();
			design = JRXmlLoader.load(path);
			report = JasperCompileManager.compileReport(design);
			jasperPrint = JasperFillManager.fillReport(report, parameters);
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			byte[] buffer = output.toByteArray();
			respuesta = DefaultStreamedContent.builder().contentType("application/pdf").name("Actor_List").stream(() -> new ByteArrayInputStream(buffer)).build();
		} catch (JRException ex) {
			ex.printStackTrace();
		}
		
		return respuesta;
	}
	
	public void createXlsx(String fileName, Map<String, Object> parameters, String path) throws IOException {
		
		FacesContext context = null;
		HttpServletResponse response = null;
		OutputStream output = null;
		JasperDesign design = null;
		JasperReport report = null;
		JasperPrint jasperPrint = null;

		try {
			context = FacesContext.getCurrentInstance();
			response = (HttpServletResponse) context.getExternalContext().getResponse();
			output = response.getOutputStream();
			String disposition = String.format("attachment; filename=\"%s\"", fileName);
			response.setHeader("Content-Disposition", disposition);
			response.addHeader("Content-Disposition", disposition);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			design = JRXmlLoader.load(path);
			report = JasperCompileManager.compileReport(design);
			jasperPrint = JasperFillManager.fillReport(report, parameters);
			parameters.put("IS_IGNORE_PAGINATION", true);
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
			OutputStreamExporterOutput outputExporter = new SimpleOutputStreamExporterOutput(output);
			exporter.setExporterOutput(outputExporter);
			exporter.setConfiguration(reportConfig);
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			context.responseComplete();
			FacesContext.getCurrentInstance().responseComplete();
		} catch (JRException ex) {
			ex.printStackTrace();
		} finally {
			IOUtil.close(output);
		}
	}

}
