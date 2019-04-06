import java.io.*;
import java.lang.Thread.State;



public class BusquedaListadoFicheros {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		File destino = new File("Resultados.txt");
		if(destino.exists()) {
			destino.delete();
			destino.createNewFile();
		}
		else destino.createNewFile();
		File dir = new File(args[0]);
		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir,String name) {
				String minusculas=name.toLowerCase();
				if (minusculas.endsWith(".txt")) return true;
				else return false;
			}
		};
		String palabra = args[1];
		File[] ficheros = dir.listFiles(textFilter);
		Hebra[] thread= new Hebra[ficheros.length]; 
		for(int i=0;i<ficheros.length;i++) {
			thread[i] = new Hebra(ficheros[i],destino,palabra);
			thread[i].start();
		}
		for(int i=0;i<thread.length;i++) {
			while(thread[i].getState()!=State.TERMINATED) {}
		}
		FileInputStream in = new FileInputStream(destino.getAbsolutePath());
		BufferedReader buf = new BufferedReader(new InputStreamReader(in));
		String line=buf.readLine();
		while(line!=null) {
			System.out.println(line);
			line=buf.readLine();
		}
		buf.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
