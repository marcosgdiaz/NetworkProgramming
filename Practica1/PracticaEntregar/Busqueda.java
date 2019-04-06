import java.io.*;

public class Busqueda {

	public int strong(String cadena,String origen, String destino){
		int count=0;
		try {
		String line;
		FileInputStream in = new FileInputStream(origen);
		FileWriter out = new FileWriter(destino);
		PrintWriter pw = new PrintWriter(out);
		LineNumberReader buf = new LineNumberReader(new InputStreamReader(in));
		line=buf.readLine();
		while(line!=null) {
			if(line.indexOf(cadena)>-1) {
				count++;
				System.out.print("Aparicion en linea "+buf.getLineNumber()+": "+line+"\n");
				}
			String nuevacadena="<strong>"+cadena+"</strong>";
			line=line.replaceAll(cadena,nuevacadena);
			pw.println(line);
			line=buf.readLine();
		}
		buf.close();
		pw.close();
		
		}
		catch(Exception e){
	         System.out.print( "Error en el metedo "+e);
		}
		return count;
		}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		String fichero_origen=args[0];
		String fichero_destino=args[1];
		System.out.println("Introduzca la cadena a buscar:");
		BufferedReader teclado=new BufferedReader(new InputStreamReader(System.in));
		String cadena_buscar=teclado.readLine();
		Busqueda metodo = new Busqueda();
		int lineas=metodo.strong(cadena_buscar,fichero_origen,fichero_destino);
		System.out.println("Numero de lineas que aparece "+cadena_buscar+": "+lineas);
		
		}
		catch(Exception e){
			System.out.println("Error en la entrada: "+e);
		}
		
	}

}
