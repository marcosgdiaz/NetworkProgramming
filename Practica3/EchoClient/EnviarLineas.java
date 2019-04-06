import java.io.*; import java.net.*;

public class EnviarLineas extends Thread {
	Socket c;
	private String line;
	
	EnviarLineas(Socket s){
		this.c = s;
		
	}
	
	public void run() {
		try {
		PrintWriter netOut = new PrintWriter(this.c.getOutputStream(), true);
		LineNumberReader sysIn = new LineNumberReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("linea ('.' para acabar): ");
			line = sysIn.readLine();
//			if (this.line.equals(".")) {
//				netOut.println(this.line);
//				netOut.close();
//				sysIn.close();
//				break;}
			netOut.println(line);
			if(line.equals(".")) break;
		}
		} catch (IOException e) {e.printStackTrace();}
	}
}
