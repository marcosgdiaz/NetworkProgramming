import java.io.*; import java.net.*; import java.util.logging.*;

class HelloThread extends Thread {
	private final static Logger auditLogger= Logger.getLogger("requests");
	private final static Logger errorLogger= Logger.getLogger("errors");
	private Socket c;
	private String line;
	
	HelloThread (Socket conn ) {
		c = conn; 
	}
	public void run () {
		try {
			PrintWriter p = new PrintWriter(c.getOutputStream(), true);
			//p.println("You have connected successfully.");
			auditLogger.info (this.toString()+ " Connection accepted");
			LineNumberReader r = new LineNumberReader(new InputStreamReader(c.getInputStream()));
			while (!c.isClosed()) {
				line=r.readLine();
				while(line!=null) {
					if(this.line.equals(".")) {
						c.close();
						break;
					}
					else {
					p.println(line);
					}
					line=r.readLine();
				}
			}
			
		} catch (IOException e) {
			errorLogger.info(this.toString()+ " excepcion: " + e);
		} finally {
			try {
				//if (c!=null) c.close();
				auditLogger.info(this.toString()+ " fin conexion con cliente ");
			} catch (Exception ex) {}
		} // finally
	}
}