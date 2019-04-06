import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor extends Frame1 {
	
	public static void main(String args[]) {
		
		DatagramSocket conn = null;
		try {
			conn = new DatagramSocket(ServerPort);
			System.out.println("Servidor de TFTP creado en el puerto: "+ ServerPort);
			ExecutorService pool = Executors.newCachedThreadPool();
			while(true) {
				DatagramPacket entrada = new DatagramPacket(new byte[516], 516);
				conn.receive(entrada);
				pool.submit(new ThreadServidor(entrada));
			}
			
		} catch(SocketException ex) {
			ex.printStackTrace();
			System.out.println("Error en la conexión de entrada del socket");
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
