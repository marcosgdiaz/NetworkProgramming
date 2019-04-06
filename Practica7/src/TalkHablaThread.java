import java.io.*;
import java.net.*;

public class TalkHablaThread extends Thread{
	private DatagramSocket MiSocket;
	private InetAddress MaquinaServidor;
	private int Port;
	private String Alias;
	
	public TalkHablaThread(DatagramSocket s,InetAddress a,int p,String alias) {
		MiSocket=s;
		MaquinaServidor=a;
		Port=p;
		Alias=alias;
	}
	
	public void run() {
		try {
		BufferedReader Teclado = new BufferedReader(new InputStreamReader(System.in)); // flujo de entrada del teclado
		Boolean activo=true;
		String mensaje,comando,texto;
		byte[] BufferSalida;
		DatagramPacket PaqueteSalida;
		while(activo) {
			System.out.println("salir, listar o hablar con:");
			mensaje=Teclado.readLine();
			switch(mensaje) {
			case "salir":
				activo=false;
				comando="SALIR-"+Alias;
				BufferSalida=comando.getBytes();
				PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,MaquinaServidor,Port);
				MiSocket.send(PaqueteSalida); 
				break;
			case	 "listar":
				comando="LISTAR-"+Alias;
				BufferSalida=comando.getBytes();
				PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,MaquinaServidor,Port);
				MiSocket.send(PaqueteSalida); 
				break;
			default:
				System.out.print("Mensaje para "+mensaje+" :");
				texto=Teclado.readLine();
				comando="HABLAR-"+Alias+"-"+mensaje+"-"+texto;
				BufferSalida=comando.getBytes();
				PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,MaquinaServidor,Port);
				MiSocket.send(PaqueteSalida); 
				break;
		}
		}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
