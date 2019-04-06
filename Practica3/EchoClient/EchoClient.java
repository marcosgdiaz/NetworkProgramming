import java.io.*; import java.net.*;

public class EchoClient {
	public static void main (String args[]) {
		String host = "localhost";
				if (args.length > 0) { host=args[0]; }
		try {
			Socket s = new Socket(host, 22569);
			RecibirEco hilo2 = new RecibirEco(s);
			hilo2.start();
			EnviarLineas hilo1 = new EnviarLineas(s);
			hilo1.start();
		} catch (UnknownHostException e) { System.out.println(e);
		} catch (IOException e) {e.printStackTrace();}
	}
}