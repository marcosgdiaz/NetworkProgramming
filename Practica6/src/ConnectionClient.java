import java.nio.*; import java.nio.channels.*;
import java.net.*; import java.io.IOException;

public class ConnectionClient {

	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		try {
			SocketAddress address = new InetSocketAddress (host,port);
			SocketChannel client = SocketChannel.open(address);
			ByteBuffer buffernet = ByteBuffer.allocate(100); //leer del servidor
			WritableByteChannel out= Channels.newChannel(System.out);
			client.configureBlocking(false); 
			while(true) {
				int n2 = client.read(buffernet);
				if(n2>0) {
					buffernet.flip();
					out.write(buffernet);
					buffernet.clear();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
