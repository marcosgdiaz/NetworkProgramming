import java.io.*;
import java.util.*;
import java.net.*;

class tftp1Cliente extends Frame1 implements Runnable, Cons1 {
	String file, host; // parámetros de activación
	FileOutputStream o;
	PipedInputStream i;
	Timer1 t; // referencia al gestor de timers
	Line1 l;
	LinkedList<DatagramPacket> listrec; // lista de paquetes recibidos
	int event, state = espera; // variable de evento y estado
	
	tftp1Cliente (String f, String h, PipedInputStream inp, Timer1 ti,
			LinkedList<DatagramPacket> ll ,DatagramSocket s, Line1 li) {
		file = f;
		host = h;
		i = inp;
		t = ti;
		listrec= ll;
		sock = s;
		l=li;
		try {
			o = new FileOutputStream("copiaencliente_" + file);
			a = InetAddress.getByName(host);
			Thread tc = new Thread(this);
			tc.start();
		} catch (IOException e) {System.out.println("tftp1Cliente excepcion: " + e);}
	} //constructor tftp1Cliente
	
	public void run () {
		try {
			remoteTID = ServerPort; // primera transición
			SendDP(sock, (sent=RRQ(file, "netascii")));
			seqnum=1;
			t.startTimers();
			state = recibiendo;
			while (state != espera) { // bucle de espera de eventos
				event = i.read();
				switch (event) {
					case frame: 
						synchronized (listrec) {rec=listrec.removeFirst();}
						if (firstDATAframe()){ remoteTID=rec.getPort();} // guarda nuevo TID remoto
						if (rec.getPort() != remoteTID) { // trama no llega del servidor
							SendDP(sock, ERROR(0, "wrong TID"));
							break;
						 	}
						if (code(rec)==DATA) {
							if ((seqnum(rec)) == seqnum) { // trama de datos nueva
								t.startTimers();
								o.write(dat(rec));
								SendDP(sock, (sent=ACK()));
								seqnum++;
								if (seqnum > 65535) seqnum=0;
							} else {
								SendDP(sock, (sent=ACK())); // envía (re)asentimiento
							}
							if (state == recibiendo) { t.startTout(); }
							if (rec.getLength() < 516) { // última trama de datos
							state = espera;
							t.stopTout();
							l.finalizar();
							}
						}
						
						if (code(rec)==ERROR) {
							printframe(rec);
							state = espera;
							t.stopTimers();
							l.finalizar();
						 	}
						break;// procesa la trama
					case close: 
						state = espera;
						break;// retorna a estado inicial
					case tout: 
						if (state == recibiendo) {
							SendDP(sock, sent);
							t.startTout();
						}
						break; // retransmite
					default: break;
				}
			}
		o.close();
		} catch (IOException ex ) {ex.printStackTrace(); }
		
		} // run
	
}