import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class TalkEscuchaThread extends Thread{
	private DatagramSocket MiSocket;
	private String Alias;
	private Boolean activo=true;
	
	public TalkEscuchaThread( DatagramSocket s, String a) {
		MiSocket=s;
		Alias=a;
	}
	
	public void run() {
		try {
			String[] mensaje;
			while(activo) {
				System.out.println(Alias+"--esperando mensaje");
				byte [] BufferEntrada = new byte[1024]; // crea el datagrama para leer
				DatagramPacket PaqueteEntrada = new DatagramPacket(BufferEntrada,BufferEntrada.length);
				MiSocket.receive(PaqueteEntrada);
				String comando = new String(BufferEntrada, 0, PaqueteEntrada.getLength());
				mensaje=comando.split("-");
				if(mensaje[1].equals("#FIN#")){
					activo=false;
				} else {
				System.out.print("mensaje recibido de "+mensaje[0]+": ");
				System.out.println(mensaje[1]);
				}
			}
				
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

}
