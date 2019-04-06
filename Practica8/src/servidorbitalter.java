import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class servidorbitalter {
	public static void main(String args[]) {
		DatagramSocket conn = null;
		try {
			conn = new DatagramSocket(7777);
			System.out.println("Servidor de eco creado en el puerto: "+conn.getLocalPort());
			ExecutorService pool = Executors.newCachedThreadPool();
			while(true) {
				DatagramPacket entrada = new DatagramPacket(new byte[256], 256);
				conn.receive(entrada);
				System.out.println("Nuevo cliente");
				pool.submit(new threadbitalter(entrada));
			}
			
		} catch(SocketException ex) {
			ex.printStackTrace();
			
			//System.out.println("Error en la conexión de entrada del socket");
		} catch(IOException ex) {
			System.out.println("Error en la recepción del paquete");
		}
		finally {
			conn.close();
		}
	}
}
