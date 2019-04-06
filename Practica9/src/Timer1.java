import java.io.*;

class Timer1 implements Runnable, Cons1 {
	static final int plazo=2, fin=4;
	int[] timer = {0,0} ; // array con contadores de los dos timers
	PipedOutputStream o; // stream para envío de timeouts
	boolean bucle;
	
	Timer1 (PipedOutputStream ot) {
		o = ot;
		bucle=true;
		Thread t = new Thread(this); t.start();
	} //Timer constructor
	
	synchronized void startTout () { timer[tout] = plazo; }
	
	synchronized void stopTout () { 
		timer[tout] = 0;
		bucle=false;
		}
	
	synchronized void startTimers (){ // arranca los dos timers
		timer[tout] = plazo;
		timer[close] = plazo*fin;
	}
	
	synchronized void stopTimers () { // para los dos timers
		timer[tout] = 0;
		timer[close] = 0;
		bucle=false;
	}
	
	public void run () {
		try {
			while (bucle) {
				Thread.sleep (1000); // espera 1 segundo
				synchronized (this) {
					for (int i=0; i < timer.length; i++) { // todos los timers
						if (timer[i] > 0)
							if (--timer[i] == 0) { // decrementa timer[i] y compara
								o.write((byte)i); 
								o.flush(); // envía timeout
							}
					} 
				} 
			}
			System.out.println("Timer finalizado");
		} catch (IOException e) {System.out.println("Timer1: " + e);
		} catch (InterruptedException e) {System.out.println("Timer1: "+e); }
	} 
} 
