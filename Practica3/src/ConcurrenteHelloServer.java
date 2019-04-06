import java.io.*; import java.net.*; import java.util.logging.*;import java.util.concurrent.*;

public class ConcurrenteHelloServer {
	private final static Logger auditLogger= Logger.getLogger("requests");
	private final static Logger errorLogger= Logger.getLogger("errors");

 	public static void main (String args[]) {
 		ServerSocket serv = null;
 		try {
 			serv = new ServerSocket(22569);
 			auditLogger.info ("ServidorEcoConcurrente created at port 22569");
 			ExecutorService pool = Executors.newFixedThreadPool(3);
 			while (true) {
 				Socket conn = serv.accept();
 				System.out.println("Connection accepted from "+conn.getInetAddress()+" at port "+conn.getPort());
 				pool.submit(new HelloThread(conn));
 			}
 		} catch (IOException e) {
 			errorLogger.log(Level.SEVERE, "excepcion", e);
 		} 
 		finally {
 			try {
 				if (serv!=null) serv.close();
 			} catch (Exception ex) {}
 		} 
 	}
}