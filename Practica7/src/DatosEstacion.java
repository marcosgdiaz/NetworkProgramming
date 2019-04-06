import java.net.InetAddress;

class DatosEstacion {
	private InetAddress DireccionIP;
	private int Puerto;
	
	public DatosEstacion (InetAddress dirIP, int pto) {
		DireccionIP= dirIP;
		Puerto= pto;
	}
	
	public InetAddress getIP(){
		return DireccionIP;
	}
	public int getPuerto (){
		return Puerto;
	}
} // DatosEstación