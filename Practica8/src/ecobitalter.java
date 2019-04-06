import java.net.*;
import java.io.*;

public class ecobitalter {
	static final byte Idle=0, WaitAck=1;
	public static void main (String args[]) {
		//boolean salir=false;
		String host = "localhost";
		if (args.length>0) host=args[0];
		int remotePort=7777; //puerto inicial del servidor
		byte state=Idle;
		int numlin=0;
		if (args.length > 0) { host=args[0]; }
		try {
			InetAddress a = InetAddress.getByName(host);
			DatagramSocket theSocket = new DatagramSocket();
			DatagramPacket dpout= null;
			BufferedReader lin= new BufferedReader (new InputStreamReader(System.in));
			while (true) {
				if (state==Idle) {
					System.out.println("introduzca linea (acabar con '.'):");
					String line= lin.readLine();
					byte[] dataenv={(byte)(numlin%2)};
					line= (new String (dataenv))+line;
					dataenv= line.getBytes();
					dpout= new DatagramPacket(dataenv, dataenv.length, a, remotePort);
					theSocket.send(dpout); // envia un datagrama al servidor y
					state=WaitAck; // cambia de estado para esperar asentimiento
				} else {
					try {
						theSocket.setSoTimeout(2000); // genera una excepción tras 2 seg de espera por un datagrama de asentimiento
						DatagramPacket dpin= new DatagramPacket(new byte[256], 256);
						theSocket.receive(dpin);
						remotePort= dpin.getPort(); // puerto de la hebra del servidor
						dpout.setPort(remotePort); // que atiende a ese cliente
						byte[] datarec=dpin.getData();
						if (datarec[0]==(byte)((numlin+1)%2)){ // Ack esperado
							System.out.println("recibido ack esperado: "+datarec[0]);
							String s = new String(datarec, 1, dpin.getLength()-1);
							System.out.println("eco "+numlin+" de "+dpin.getAddress()+" : " + s);
							if (s.equals(".")) {
								System.out.println("Conexion finalizada");
								break; //recibe el eco la línea final
							}
							numlin++;
							state=Idle;
						 } else { // Ack no esperado
							 System.out.println("recibido ack NO esperado: "+datarec[0]+" - retranmision");
							 theSocket.send(dpout);
						 }
						 } catch (SocketTimeoutException e){ //no llega asentimiento en 2 seg.
							 System.out.println("timeout - retranmision");
							 theSocket.send(dpout);
						 }
				}
				
			} //while
			theSocket.close();
		} catch (UnknownHostException e) {System.out.println(e);
		} catch	(IOException e) {} 
	} 	
}
