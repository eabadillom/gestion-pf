package mx.com.ferbo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class LectorHuella  {
	 public static void main(String[] args) {
	        String serverAddress = "localhost"; // Dirección del servidor C#
	        int serverPort = 1030; // Puerto del servidor C#

	        try {
	            Socket socket = new Socket(serverAddress, serverPort);

	            OutputStream out = socket.getOutputStream();
	            PrintWriter writer = new PrintWriter(out, true);

	            // Enviar datos al servidor C#
	            String mensaje = "Mensaje desde Java";
	            writer.println(mensaje);

	            // Leer la respuesta del servidor C#
	            InputStream in = socket.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String respuesta = reader.readLine();
	            System.out.println("Respuesta del servidor C#: " + respuesta);

	            // Cerrar la conexión	
	            socket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

}
