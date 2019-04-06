
public class website {
	String name; // campos
	protected String url;
	private String descr;
	public website(String n, String u, String d) { // constructor
	name = n;
	url = u;
	descr = d;
	}
	public void mod_name(String n) {
		name = n;
	}
	public void mod_url(String u) {
		url = u;
	}
	public void mod_descr(String d) {
		descr = d;
	}
	public void print() { // método
		System.out.println(name + " at " + url +" "+ descr);
	}
}
