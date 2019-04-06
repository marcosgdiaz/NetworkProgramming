import java.net.*; import java.io.*;import java.util.concurrent.ExecutorService;import java.util.concurrent.Executors;

public class jhttp4 {
	private File baseDirectory;
	private String indexFileName= "index.html";
	private ServerSocket server;
	private static final int numThreads = 10;
	
	public jhttp4(File basedir, int port, String indexfich) {
		try {
			baseDirectory= basedir;
			if (!baseDirectory.isDirectory()) {
				throw new IOException(baseDirectory + " no es un directorio");
			}
			indexFileName = indexfich;

			server = new ServerSocket(port);
			ExecutorService pilahebras= Executors.newFixedThreadPool(numThreads);
			while (true) {
				Socket request = server.accept();
				Runnable r= new requestprocessor4 (baseDirectory, indexFileName, request);
				pilahebras.submit(r);
			} // while
		} catch (IOException ex) {System.out.println("jhttp4 Excepcion: " + ex); }
} // constructor jhttp4
 
	public static void main(String[] args) {
		File baseDir;
		String nomfich= "index.html";
		int port;
		try {
			baseDir = new File(args[0]);
		} catch (Exception ex) {
			System.out.println("Uso: java jhttp4 directorio_base [port indexfile]");
			return;
			}
		try {
			port = Integer.parseInt(args[1]);
			if (port < 0 || port > 65535) port = 80;
		} catch (Exception ex) { port = 80;}
		if (args.length==3) nomfich=args[2];
		new jhttp4(baseDir,port,nomfich);
	} // main
	} // class jhttp4