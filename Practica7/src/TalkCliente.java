import java.io.*;
import java.net.*;


public class TalkCliente {
	public static void main(String args[]) {
		String hostdestino= "localhost";
		int PuertoServidor = 7777;

		try {
			InetAddress MaquinaServidor = InetAddress.getByName(hostdestino);
			BufferedReader Teclado = new BufferedReader(new InputStreamReader(System.in)); // flujo de entrada del teclado
			DatagramSocket MiSocket = new DatagramSocket(); // crear el socket
			System.out.print("Introduce tu Alias: ");
			String Alias = Teclado.readLine();
			String comando = "REGISTRAR-" + Alias;
			byte[] BufferSalida = comando.getBytes();
			DatagramPacket PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,MaquinaServidor,PuertoServidor);
			MiSocket.send(PaqueteSalida); // enviar comando de registro al servidor
			System.out.println("espera confirmacion de registro del servidor");
			byte [] BufferEntrada = new byte[1024]; // crea el datagrama para leer
			DatagramPacket PaqueteEntrada = new DatagramPacket(BufferEntrada,BufferEntrada.length);
			MiSocket.receive(PaqueteEntrada);
			comando = new String(BufferEntrada, 0, PaqueteEntrada.getLength());
			if(comando.equals("ERROR")) {
				System.out.println("error al registrar el cliente, el alias ya existe");
			} else {
				TalkHablaThread hablador= new TalkHablaThread(MiSocket,MaquinaServidor,PuertoServidor,Alias); // arranca TalkHablaThread
				TalkEscuchaThread escuchador= new TalkEscuchaThread(MiSocket, Alias);//arranca TalkEscuchaThread
				hablador.start();
				escuchador.start();
				hablador.join(); //espera a que acaben las dos threads
				escuchador.join();
				MiSocket.close();
				System.out.println("fin de TalkCliente "+ Alias);
			}
		} catch(Exception e) {
			System.out.println("TalkCliente: ha ocurrido un error: " + e);
		}
	} 
}