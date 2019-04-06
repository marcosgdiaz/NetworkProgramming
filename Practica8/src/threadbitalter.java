import java.net.*;
import java.io.*;

public class threadbitalter extends Thread{
	private DatagramPacket dpin;
	SocketAddress direccion;
	public threadbitalter(DatagramPacket d) {
		dpin = d;
		direccion = d.getSocketAddress();
	}
	
	public void run() {
		try {
			DatagramSocket conn = new DatagramSocket();
			System.out.println("Hilo creado en puerto "+conn.getLocalPort());
			Byte num = null;
			//data[0]=(byte) ((num.intValue()+1)%2);
			//conn.send(dpprim);
			while(true) {
				byte[] datarec = dpin.getData();
				String mensaje = new String(datarec,1,dpin.getLength()-1);
				num = datarec[0];
				datarec[0]=(byte) ((num.intValue()+1)%2);
				DatagramPacket dpout = new DatagramPacket(datarec, dpin.getLength(), direccion);
				conn.send(dpout);
				if(mensaje.equals(".")) break;
				dpin = new DatagramPacket(new byte[256], 256);
				conn.receive(dpin);
			}
			System.out.println("Hilo cerrado en puerto "+conn.getLocalPort());
			conn.close();
		} catch(SocketException ex) {System.out.println("Error en la asignación del puerto");}
		catch(IOException ex) {System.out.println("Error en el envío del datagrama");}
	}
}
