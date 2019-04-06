
public class websiteE extends website{
	public String email;
	
	public websiteE(String n,String u, String d, String email) {
		super(n,u,d);
		this.email=email;
	}
	public void printEmail() {
		System.out.println
		("To contact " + name + " send msg to " + email);
		}

}
