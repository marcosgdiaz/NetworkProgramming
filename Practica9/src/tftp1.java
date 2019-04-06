import java.io.*;
import java.util.*;
import java.net.*;

public class tftp1 {
	static tftp1Cliente tf;
	public static void main (String args[]) throws IOException {
		if (args.length!= 3){
			throw(new RuntimeException ("Sintaxis: host get fichero" ));
		} else
			if (args[1].equals("get")){
				PipedOutputStream o = new PipedOutputStream();
				PipedInputStream i = new PipedInputStream(o);
				LinkedList<DatagramPacket> listdp = new LinkedList<DatagramPacket>();
				DatagramSocket sock = new DatagramSocket();
				//tftp1Servidor ts = new tftp1Servidor(); // Test server
				Timer1 ti = new Timer1(o); // Timer manager
				Line1 l = new Line1(o, listdp, sock); // Line Manager
				tf = new tftp1Cliente(args[2], args[0], i, ti, listdp, sock,l);
				
			} else {
				System.out.println("comando desconocido: "+args[0]+args[1]+args[2]);
				}
	} //main
} //tftp1
