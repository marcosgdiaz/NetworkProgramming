import java.io.*;
import java.net.*;

public class ThreadServidor extends Frame1 implements Runnable{
	FileInputStream fileinput;
	int leyendo;
	
	public ThreadServidor(DatagramPacket entrada) {
		try {
			auxsock = new DatagramSocket();
			remoteTID = entrada.getPort();
			a=entrada.getAddress();
			if (code(entrada)==RRQ) {
				System.out.println("Hilo creado en puerto "+auxsock.getLocalPort());
				System.out.println("Petición del fichero "+file(entrada));
				fileinput = new FileInputStream(file(entrada));
				seqnum=1;
				leyendo=sendDATA();
			}
			else {
				try {
					SendDP(auxsock, ERROR(0, "Modo no compatible"));
				} catch(IOException exc) {
					exc.printStackTrace();
				}
			}
		} catch(FileNotFoundException ex) {
			System.out.println("Fichero no encontrado");	
			try {
				SendDP(auxsock, ERROR(1, "No existe el fichero"));
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch(SocketException a) {
			a.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	int sendDATA () throws IOException {
		int len; 
		byte b[] = new byte[516];
		len = fileinput.read(b, 4, 512);
		if (len<0) len=0;// fin del fichero
		else {
		SendDP(auxsock, sent=DATA(b, (len+4)));
		this.seqnum++;}
		return len;
	}	 // envía tramas de datos del fichero
	
	public void run() {
		try {
			while(leyendo>0) {
				rec = new DatagramPacket(new byte[516],516);
				auxsock.setSoTimeout(8000);
				auxsock.receive(rec);
				if(seqnum(rec)==this.seqnum-1 && code(rec)==ACK) {
					leyendo=sendDATA();
					}
				else if (code(rec)==ERROR){
					printframe(rec);
					leyendo=0;
				}
				}
			System.out.println("Hilo cerrado en puerto "+auxsock.getLocalPort());
			
			} catch (SocketTimeoutException e){ //no llega asentimiento en 8 seg.
				 System.out.println("Timeout- Error en el envio del fichero");
				 leyendo=0;
			} catch (IOException ex) {
				ex.printStackTrace();
				} 
	}
}
