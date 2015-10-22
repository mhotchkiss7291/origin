
public class StaticNonStatic {

	// non static local variable
	String name;
	
	// static local variable
	static int age;
	
	// static methods can only access static stuff 
	
	public static void main(String[] args) {
		
		sum();
		
		// Can't access non-static method
		//sendMail();

		age = 100;
		
		// Can't access non-static variable
		//name = "Groucho"; 

	}

	public static void sum() {
		int i = 100;
	}
	
	public void sendMail() {
		int y = 300;

		sum();
		sendMail();

		age = 100;
		name = "Groucho"; 
	}
}
