package mx.com.ferbo.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTool {
	
	public static byte[] resizeImage(byte[] originalBytes, int newWidth, int newHeight) throws IOException {
	    // 1. Leer la imagen original desde el byte[]
	    BufferedImage original = ImageIO.read(new ByteArrayInputStream(originalBytes));

	    // 2. Crear una nueva imagen con las dimensiones deseadas
	    BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());

	    // 3. Escalar la imagen original hacia la nueva
	    Graphics2D g2d = resized.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
	    g2d.dispose();

	    // 4. Convertir de vuelta a byte[]
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    ImageIO.write(resized, "jpg", outputStream);
	    return outputStream.toByteArray();
	}

}
