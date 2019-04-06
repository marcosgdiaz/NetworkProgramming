import java.net.*; import java.io.*; import java.util.*;

public class requestprocessor4 implements Runnable {
	private File baseDirectory;
	private String indexFileName= "index.html";
	private Socket connection;
	
	public requestprocessor4(File bDir, String indexFich, Socket conn) {
		try {
			baseDirectory = bDir.getCanonicalFile();
		} catch (IOException ex) { }
		if (indexFich != null) indexFileName= indexFich;
		connection= conn;
	} // constructor requestprocessor4
	
	public void run() {
		String root = baseDirectory.getPath(); // para chequeo de seguridad
		try {
			String filename;
			String contentType;
			int tamano=0;
			BufferedOutputStream raw= new BufferedOutputStream(connection.getOutputStream());
			PrintWriter out= new PrintWriter(raw);
			InputStreamReader in= new InputStreamReader(new BufferedInputStream(connection.getInputStream()),"ASCII");
			StringBuffer requestLine= new StringBuffer();
			int c;
			while (true) {
				c = in.read();
				if (c == '\r' || c == '\n') break;
				requestLine.append((char) c);
			}

			String get = requestLine.toString();
			System.out.println("recibido: "+ get); // logea la peticion 
			String [] peticion = get.split(" ");
			String method = peticion[0];
			//String version = "";
			if (method.equals("GET")) {
				filename = peticion[1];
				if (filename.endsWith("/")) filename+= indexFileName;
				contentType = URLConnection.getFileNameMap().getContentTypeFor(filename);
				File theFile = new File(baseDirectory,filename.substring(1,filename.length()));
				if (theFile.canRead() && theFile.isFile() && theFile.getCanonicalPath().startsWith(root)) {
					DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(theFile)));
					byte[] theData = new byte[(int) theFile.length()];
					tamano= theData.length;
					fis.readFully(theData);
					fis.close();
					cabeceraMIME (out, "200 OK", tamano, contentType);// envia una cabecera MIME
					raw.write(theData); // envía el fichero en modo binario
					raw.flush();
				} else { // no encuentra el fichero
					File dir = new File(root);
					FileFilter Filter = new FileFilter() {
						public boolean accept(File pathname) {
							return (pathname.isFile() && !pathname.getName().startsWith("."));
						}};	
					File[] list = dir.listFiles(Filter);
					StringBuilder sbbody= new StringBuilder ("<HTML>\r\n");
					sbbody.append("<HEAD><TITLE>File Not Found</TITLE>\r\n");
					sbbody.append("</HEAD>\r\n");
					sbbody.append("<BODY>");
					sbbody.append("<H1>HTTP Error 404: Fichero no encontrado</H1>\r\n");
					sbbody.append("<p>Lista de ficheros dísponibles:</p>\r\n<ul>");
					for (int i=0;i<list.length;i++) {
						sbbody.append("<li><a href="+list[i].getName()+">"+list[i].getName()+"</a>");
					}
					sbbody.append("</ul>\r\n</BODY></HTML>\r\n");
					String body= sbbody.toString();
					tamano= body.length();
					contentType= "text/html; charset=utf-8";//cabeceraMIME (out, "404 File Not Found", tamano, contentType);
					cabeceraMIME (out, "200 OK", tamano, contentType);
					out.write(body);
					out.flush(); 
				}}
			else { // metodo no "GET"
				StringBuilder sbbody= new StringBuilder ("<HTML>\r\n");
				sbbody.append("<HEAD><TITLE>File Not Found</TITLE>\r\n");
				sbbody.append("</HEAD>\r\n");
				sbbody.append("<BODY>");
				sbbody.append("<H1>HTTP Error 501: Not Implemented</H1>\r\n");
				sbbody.append("</BODY></HTML>\r\n");
				String body= sbbody.toString();
				tamano= body.length();
				contentType= "text/html; charset=utf-8";
				cabeceraMIME (out, "501 Not Implemented", tamano, contentType);
				out.write(body);
				out.flush();}
		} catch (IOException ex) {System.out.println("Exception Thread requesprocessor4 "+ ex);} 
		finally {
			try {
				connection.close();
				System.out.println("Cierro conexion Thread requestprocessor4 ");
			} catch (IOException ex) {
				System.out.println("Exception Thread requestprocessor4 "+ ex);
			}
			} // finally
	} // run

	void cabeceraMIME (PrintWriter wout, String resp, int tamano, String ctype){
		wout.println("HTTP/1.0 " + resp);
		Date now = new Date();
		wout.println("Date: " + now);
		wout.println ("Server: jhttp4 4.0");
		wout.println ("Content-length: "+ tamano);
		wout.println ("Content-type: "+ ctype);
		wout.println(); // envia dos saltos de linea seguidos
		wout.flush();
	}
} // class requestprocessor4 