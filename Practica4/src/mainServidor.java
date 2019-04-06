import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class mainServidor {
	
	public static void main(String args[]) { 
		ServerSocket SSocketEscucha = null;
		try {
			SSocketEscucha = new ServerSocket(7777); 
			System.out.println("ServidorFTP_TCP created at port "+
					SSocketEscucha.getLocalPort());
			ExecutorService pool = Executors.newFixedThreadPool(3);
			while(true) {
				System.out.println("ServidorFTP_TCP esperando peticiones"); 
				Socket SocketPeticion = SSocketEscucha.accept();
				System.out.println("ServidorFTP_TCP, peticion de "+ SocketPeticion.getInetAddress());
				pool.submit(new ServidorFTP_TCP1(SocketPeticion));
				}
			} catch (IOException e) { System.out.println(e); }
		finally {
 			try {
 				if (SSocketEscucha!=null) SSocketEscucha.close();
 			} catch (Exception ex) {}
 		} 
	} 

}
