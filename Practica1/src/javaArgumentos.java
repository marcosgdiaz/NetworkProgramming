
public class javaArgumentos {
	 public static void main (String[] args){
		 for(int i=0;i<args.length;i++) {
			switch(args[i]) {
				case "11":
					System.out.println("Numero once\n");
					break;
				case "12":
					System.out.println("Numero doce\n");
					break;
				case "13":
					System.out.println("Numero trece\n");
					break;
				default:
					System.out.println(args[i]);
			}
		 }
	 }
}
