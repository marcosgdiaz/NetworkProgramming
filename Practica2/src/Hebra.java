import java.io.*;

public class Hebra extends Thread {
	private File origen;
	private File destino;
	private String palabra;
	
	
	public Hebra(File fich, File dest,String palabra) {
		this.origen=fich;
		this.destino=dest;
		this.palabra=palabra;
		
	}
	
	public void run() {
		try {
		FileInputStream in = new FileInputStream(this.origen.getAbsolutePath());
		LineNumberReader buf = new LineNumberReader(new InputStreamReader(in));
		FileWriter out = new FileWriter(this.destino.getAbsolutePath(),true);
		PrintWriter pw = new PrintWriter(out);
		String line=buf.readLine();
		synchronized(pw) {
		while(line!=null) {
			if(line.indexOf(this.palabra)>-1) {
				pw.println("Nombre del fichero: "+this.origen.getName()+" Numero de la linea: "+buf.getLineNumber());
				pw.println(line);
				}
			line=buf.readLine();			
		}
		pw.close();
		}
		buf.close();
	} catch(Exception e){
		e.printStackTrace();
		}
	}

}
