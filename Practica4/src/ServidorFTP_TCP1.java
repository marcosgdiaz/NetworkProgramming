import java.io.*; import java.net.*;

class ServidorFTP_TCP1 extends Thread {
	private Socket SocketPeticion;
	private DataInputStream EntradaBytes; 
	private BufferedOutputStream SalidaBytes; 
	private BufferedReader EntradaTexto; 
	private PrintWriter SalidaTexto;
	
	public ServidorFTP_TCP1(Socket conn) { // constructor 
		SocketPeticion = conn;
			} // constructor ServidorFTP_TCP1 ...
	
	public void run() { 
		String peticion;
		String nombrefichero; 
		//boolean finalizar = false;
		//while(!finalizar) {
			try { // esperar peticiones de los clientes
	// obtener los flujos de entrada y salida del socket // para la transferencia binaria
				EntradaBytes = new DataInputStream(SocketPeticion.getInputStream()); 
				SalidaBytes= new BufferedOutputStream(SocketPeticion.getOutputStream()); // obtener los flujos de entrada y salida del socket
	// para la transferencia de texto 
				EntradaTexto = new BufferedReader(new InputStreamReader(SocketPeticion.getInputStream()));
				SalidaTexto = new PrintWriter(SocketPeticion.getOutputStream(), true); 
				peticion = EntradaTexto.readLine(); 
				if (peticion.equals("DIR")) ListarFTP();
				else if(peticion.indexOf("PUT") == 0) {
					nombrefichero = peticion.substring(4, peticion.length());
					RecibirFichero(nombrefichero); } 
				else if(peticion.indexOf("GET") == 0) {
					nombrefichero = peticion.substring(4, peticion.length()); 
					EnviarFichero(nombrefichero);
				} 
				
				SocketPeticion.close();
			} catch(Exception e) { 
				System.out.println("Excepcion: " + e);
				//finalizar=true;
			}
			System.out.println("ServidorFTP_TCP, fin peticion de "+SocketPeticion.getInetAddress());
		//}// while(!finalizar) 
		} // run
	
	void ListarFTP() { int i;
		File nomdir= new File("Servidor"); 
		File[] listfich= nomdir.listFiles(); 
		try {
	// enviar la lista de ficheros 
			for(i= 0; i < listfich.length; i++)
				if (listfich[i].isFile())
					SalidaTexto.println(listfich[i].getName());
			SalidaTexto.println("#FIN#");
			SalidaTexto.flush(); 
			} catch(Exception e) {System.out.println("Error en el listado del sitio FTP: "+e); }
	} // ListarFTP
	
	void EnviarFichero(String nombrefichero) { 
		File fich= new File("Servidor/"+nombrefichero); 
		long tamano;
		try { 
			if(fich.isFile()) {
				SalidaTexto.println("OK");// enviar contestacion
				tamano = fich.length(); // obtener el tamaño del fichero 
				SalidaTexto.println(tamano); // enviar el tamaño 
				SalidaTexto.flush();
				String resp = EntradaTexto.readLine(); // lee READY 
				if(resp.equals("READY")) EnviarBytes (fich, tamano); // enviar el fichero
			} else { SalidaTexto.println("Error en el envio");}
		} catch(Exception e) {System.out.println("Error en el envio del fichero: " + e); }
		
	} // EnviarFichero
	
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
	} // EnviarBytes
	
	void RecibirFichero(String nombrefichero) { 
		try {
		// leer el tamaño del fichero
		long tamano = Integer.parseInt(EntradaTexto.readLine());
		SalidaTexto.println("READY"); // enviar comando READY al cliente 
		SalidaTexto.flush();
		RecibirBytes(nombrefichero, tamano);
		} catch(Exception e) {System.out.println("Error en la recepción del fichero: " + e);}
		} // RecibirFichero
	
	void RecibirBytes(String nomfich, long size){
		int dato;
		nomfich="Servidor/"+nomfich; 
		System.out.println("Localización: "+nomfich);
		try {
			BufferedOutputStream fichbos= new BufferedOutputStream(new FileOutputStream(nomfich)); 
			for(long i= 0; i<size; i++) {
				dato = EntradaBytes.readByte();
				fichbos.write(dato); }
			fichbos.close();
			} catch(Exception e) {System.out.println("Error en la recepcion del fichero binario: " + e); }
		} // RecibirBytes
	
	} // class ServidorFTP_TCP1
