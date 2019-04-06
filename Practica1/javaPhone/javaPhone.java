
public class javaPhone extends websiteE {
	public String telefono;
	public String fax;
	public String movil;
	
	public javaPhone(String n,String u, String d, String email, String t,String f, String m) {
		super(n,u,d,email);
		this.telefono=t;
		this.fax=f;
		this.movil=m;
	}
	public void printPhone(){
		System.out.println("Los numeros de contacto de "+ name + " son: " +"\n" + telefono + "\n" + fax +"\n"+ movil);
	}
	
	public static void main (String[] args){
		javaPhone phone = new javaPhone("Marcos","www.hkh.com","Area personal marcos","gmail.com","985","fax","691");
		phone.printPhone();
		phone.printEmail();
		phone.print();
	}
}

