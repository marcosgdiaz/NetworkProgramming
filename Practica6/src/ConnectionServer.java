import java.nio.*; import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.net.*; import java.util.*;
import java.text.DateFormat;

public class ConnectionServer {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		int numconnection=0;
		ServerSocketChannel serverChannel;
		Selector selector;
		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket s = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			s.bind(address);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		while(true) {
			try {
			
				int readyChannels =	selector.select();
				if(readyChannels == 0) continue;
				
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			Set<SelectionKey> readyKeys=selector.selectedKeys();
			Iterator<SelectionKey> iterador = readyKeys.iterator();
			while(iterador.hasNext()) {
				SelectionKey key = iterador.next();
				iterador.remove();
				try {
					if(key.isAcceptable()) {
						ServerSocketChannel server =(ServerSocketChannel) key.channel();
						SocketChannel conn = server.accept();
						numconnection++;
						System.out.println("Accepted connection "+numconnection);
						conn.configureBlocking(false);
						SelectionKey connKey = conn.register(selector, SelectionKey.OP_WRITE);
						ByteBuffer buffer = ByteBuffer.allocate(200);
						connKey.attach(buffer);
					}
					if(key.isWritable()) {
						String str1 = "Número de conexiones activas: ";
						String str2 = " Hora: ";
						Date date = new Date();
						DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer output = (ByteBuffer) key.attachment();
						if(Integer.parseInt(hourFormat.format(date).substring(6)) % 5 == 0) {
							output.put(str1.getBytes());
							output.put(String.valueOf(numconnection).getBytes());
							output.put(str2.getBytes());
							output.put(hourFormat.format(date).getBytes());
							output.put ((byte)'\r'); output.put ((byte)'\n');
							output.flip();
							client.write(output);
							output.compact();
						}
						
					}
				} catch (IOException ex) {
					key.cancel();
					System.out.println("Desactivo conexion, quedan "+--numconnection);
				try {
					key.channel().close();
				} catch (IOException cex) {System.out.println("Exception:)"+cex);}
				}
				}
				try {
					Thread.sleep(1000);
				}catch(InterruptedException e) {e.printStackTrace();}
			}//true
		
		}
		
	}

