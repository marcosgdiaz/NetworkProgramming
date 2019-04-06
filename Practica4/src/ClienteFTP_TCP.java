import java.io.*; import java.net.*;

public class ClienteFTP_TCP {
	private DataInputStream EntradaBytes; 
	private BufferedOutputStream SalidaBytes; 
	private BufferedReader EntradaTexto; 
	private PrintWriter SalidaTexto;
	private String host;
	private String peticion;
	private String nombre_fichero;
	
	public ClienteFTP_TCP(String[] a) {
		this.host = a[0];
		this.peticion = a[1];
		if (a.length>2) {
			this.nombre_fichero = a[2];
		}
	}
	
	public void run() {
		try {
			Socket s = new Socket(host,7777);
			String line;
			EntradaBytes = new DataInputStream(s.getInputStream()); 
			SalidaBytes= new BufferedOutputStream(s.getOutputStream());
			EntradaTexto = new BufferedReader(new InputStreamReader(s.getInputStream()));
			SalidaTexto = new PrintWriter(s.getOutputStream(), true);
			switch (peticion) {
				case "dir":
					SalidaTexto.println("DIR");
					line = EntradaTexto.readLine();
					while (!line.equals("#FIN#")) {
						System.out.println(line);
						line = EntradaTexto.readLine();
					}
					System.out.println("#FIN#");
					break;
				case "get":
					SalidaTexto.println("GET "+nombre_fichero);
					line=EntradaTexto.readLine();
					if(line.equals("OK"))
						System.out.println(line);
						RecibirFichero(nombre_fichero);
					break;
				case "put":
					SalidaTexto.println("PUT "+nombre_fichero);
					EnviarFichero(nombre_fichero);
					break;
				default:
					System.out.println("La petición no es correcta");
					break;
			}
			s.close();
			
			
		}catch (UnknownHostException e) { System.out.println(e);
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void main(String[] args) {
			ClienteFTP_TCP cliente = new ClienteFTP_TCP(args);
			cliente.run();
		}
	
	void EnviarFichero(String nombrefichero) { 
		File fich= new File(nombrefichero); 
		long tamano;
		try { 
			if(fich.isFile()) {
				//SalidaTexto.println("OK"); // enviar contestacion
				tamano = fich.length(); // obtener el tamaño del fichero 
				SalidaTexto.println(tamano); // enviar el tamaño 
				SalidaTexto.flush();
				String resp = EntradaTexto.readLine(); // lee READY 
				if(resp.equals("READY")) EnviarBytes (fich, tamano); // enviar el fichero
			} else { SalidaTexto.println("ERROR");}
		} catch(Exception e) {System.out.println("Error en el envio del fichero: " + e); }
		
	} 
	
	void EnviarBytes(File fich, long size) { try {
		BufferedInputStream fichbis= new BufferedInputStream( new FileInputStream(fich));
		// lee el fichero completo de golpe
		DataInputStream fichdis= new DataInputStream (fichbis); 
		byte[] buffer= new byte [(int) fich.length()]; 
		fichdis.readFully (buffer);
		SalidaBytes.write (buffer);
		SalidaBytes.flush();
		fichbis.close();
		} catch(Exception e) {
		System.out.println("Error en el envio del fichero binario: "+e); }
	}
	
	void RecibirFichero(String nombrefichero) { 
		try {
		// leer el tamaño del fichero
			long tamano = Integer.parseInt(EntradaTexto.readLine());
			SalidaTexto.println("READY"); // enviar comando READY al cliente 
			SalidaTexto.flush();
			RecibirBytes(nombrefichero, tamano);
		} catch(Exception e) {System.out.println("Error en la recepción del fichero: " + e);}
		} 
	
	void RecibirBytes(String nomfich, long size){
		int dato;
		nomfich="copiacliente_"+nomfich; 
		System.out.println("cambio nombre fichero a: "+nomfich);
		try {
			BufferedOutputStream fichbos= new BufferedOutputStream(new FileOutputStream(nomfich)); 
			for(long i= 0; i<size; i++) {
				dato = EntradaBytes.readByte();
				fichbos.write(dato); }
			fichbos.close();
			} catch(Exception e) {System.out.println("Error en la recepcion del fichero binario: " + e); }
		}

}
