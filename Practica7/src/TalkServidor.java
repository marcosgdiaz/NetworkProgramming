import java.net.*;
import java.util.*;
import java.util.logging.*;

public class TalkServidor {
	private final static int PuertoServidor= 7777;
	private DatagramSocket MiSocket;
	Hashtable<String, DatosEstacion> Estaciones;
	private final static Logger auditLogger= Logger.getLogger("requests");
	private final static Logger errorLogger= Logger.getLogger("errors");
	
	public TalkServidor() { // constructor
		try {
			MiSocket = new DatagramSocket(PuertoServidor);
			Estaciones = new Hashtable<String, DatosEstacion>();
		} catch(Exception e) { errorLogger.log(Level.SEVERE, "TalkServidor error: ", e);}
		auditLogger.info("TalkServidor Activado");
	} // constructor ServidorTalk
	
	void Registrar(String estacion, InetAddress ia, int port) {
		String comando;
		DatosEstacion datos= new DatosEstacion(ia,port);
		try {
			if(Estaciones.containsKey(estacion))
				comando = "ERROR"; // el alias ya existe
			else {
				comando = "OK";
				Estaciones.put(estacion, datos); //añade estacion a la tabla
			}
		// construye el paquete de respuesta al cliente
		byte[] BufferSalida = comando.getBytes();
		DatagramPacket PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,datos.getIP(),datos.getPuerto());
		MiSocket.send(PaqueteSalida); // envia comando de registro al servidor
		} catch (NullPointerException er) {
			errorLogger.log(Level.SEVERE, "Error en Hashtable: ", er );
		} catch(Exception e) {
			errorLogger.log(Level.SEVERE, "Error al registrar: ", e );
		}
	} //Registrar
	
	void EliminarRegistro(String estacion) {
		try {
			DatosEstacion datos = (DatosEstacion) Estaciones.get(estacion);
			String comando="TalkServidor-#FIN#";
			byte[] BufferSalida = comando.getBytes();
			DatagramPacket PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,datos.getIP(),datos.getPuerto());
			MiSocket.send(PaqueteSalida); // envia mensaje de FIN al cliente
			Estaciones.remove(estacion); // elimina estacion de la tabla hash
		} catch(Exception e) {
		 errorLogger.log(Level.SEVERE,"Error en la eliminacion del registro: ",e);
		}
	} // EliminarRegistro
	
	void ListarEstaciones(String estacion) {
		String comando;
		DatosEstacion datosDestino;
		try {
			datosDestino = (DatosEstacion) Estaciones.get(estacion);
			Enumeration<String> lista = Estaciones.keys();
			while(lista.hasMoreElements()) {
				comando = "TalkServidor-"+(String) lista.nextElement();
				byte [] BufferSalida= comando.getBytes();
				DatagramPacket PaqueteSalida= new DatagramPacket(BufferSalida,BufferSalida.length,datosDestino.getIP(),datosDestino.getPuerto());
				MiSocket.send(PaqueteSalida); // enviar el alias al cliente
			} //while
		} catch(Exception e) {
		 errorLogger.log(Level.SEVERE, "Error en el listado de las estaciones: ", e );
		}
	} // ListarEstaciones
	
	void RetransmitirMensaje(String mensaje, String origen, String destino) {
		String comando;
		DatosEstacion datos;
		try {
			if(Estaciones.containsKey(destino)) { //la estacion destino existe
				comando = origen + "-" + mensaje; //se envia el mensaje
				datos = (DatosEstacion) Estaciones.get(destino);
			} else { // la estacion destino no existe
				comando = "TalkServidor-ERROR, no existe "+destino;
				datos = (DatosEstacion) Estaciones.get(origen);
			} // else
			byte[] BufferSalida = comando.getBytes(); // construir paquete
			DatagramPacket PaqueteSalida = new DatagramPacket(BufferSalida,BufferSalida.length,datos.getIP(),datos.getPuerto());
			MiSocket.send(PaqueteSalida); // envia paquete al cliente
		 	} catch(Exception e) {
		 		errorLogger.log(Level.SEVERE, "Error en la conversacion: ", e );
		 	}
		} // RetransmitirMensaje
	
	public static void main(String args[]){
		String recibido, comando, origen, destino, texto;
		String[] peticion;
		boolean salir = false;
		TalkServidor Servidor = new TalkServidor();
		while(!salir) {
			try {
				byte[] BufferEntrada = new byte[1024]; //crea el datagrama para leer
				DatagramPacket PaqueteEntrada = new DatagramPacket(BufferEntrada,BufferEntrada.length);
				Servidor.MiSocket.receive(PaqueteEntrada); // espera mensajes
				recibido = new String(BufferEntrada,0,PaqueteEntrada.getLength());
				auditLogger.info("recibido: "+recibido);
				peticion = recibido.split("-");
				comando = peticion[0]; // extrae comando
				if(comando.equals("REGISTRAR")) {
					origen = peticion[1];
					Servidor.Registrar(origen, PaqueteEntrada.getAddress(),PaqueteEntrada.getPort());
				} else if(comando.equals("LISTAR")) {
					origen = peticion[1];
					Servidor.ListarEstaciones(origen);
				} else if(comando.equals("HABLAR")) {
					origen = peticion[1];
					destino = peticion[2];
					texto = peticion[3];
					Servidor.RetransmitirMensaje(texto,origen,destino);
				} else if(comando.equals("SALIR")) {
					origen = peticion[1];
					Servidor.EliminarRegistro(origen);
				}
			} catch(Exception e){
				errorLogger.log(Level.SEVERE, "Error en main: ", e );
				salir= true;
			} 
		} 
	}
}