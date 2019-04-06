import java.io.*; import java.net.*;

public class RecibirEco extends Thread {
	Socket c;
	private String line;
	
	RecibirEco(Socket s){
		this.c = s;
	}
	
	public void run() {
		try {
			LineNumberReader netIn = new LineNumberReader(new InputStreamReader(this.c.getInputStream()));
			while(true) {		
				line=netIn.readLine();
				if(line==null) {
					System.out.println("Socket Terminado");
					break;}
				
				System.out.println("\n"+line);
				
			}
			
		}catch (IOException e) {e.printStackTrace();}
		catch (Throwable e) {e.printStackTrace();}
		//catch (InterruptedException e) {e.printStackTrace();}
	}
}
