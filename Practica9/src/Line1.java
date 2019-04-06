import java.net.*;
import java.io.*;
import java.util.*;

class Line1 implements Runnable, Cons1 {
	LinkedList <DatagramPacket> listrec;
	DatagramSocket sock;
	PipedOutputStream o;
	boolean bucle;

	Line1 ( PipedOutputStream ot, LinkedList<DatagramPacket> ll,
			DatagramSocket s) {
		o= ot;
		listrec= ll;
		sock= s;
		bucle=true;
		Thread l = new Thread(this);
		l.start();
	} //Line1 constructor
	
	public void finalizar() {bucle=false;} 
	
	public void run () {
		while (bucle) {
			try {
				DatagramPacket rec = new DatagramPacket(new byte[516],516);
				sock.setSoTimeout(2000);
				sock.receive(rec); // espera llegada de datagrama
				synchronized (listrec){
					listrec.addLast(rec); //deja datagrama recibido en la lista
				}
				o.write((byte)frame); // envia evento “frame”
				o.flush();
			} catch (SocketTimeoutException e){ //no llega asentimiento en 2 seg.
				// bucle=false;
			} catch (IOException e) {
				System.out.println("Line1: " + e);
			} 
		} //while
		System.out.println("Line1 finalizado");
	} //run
} // class Line1

