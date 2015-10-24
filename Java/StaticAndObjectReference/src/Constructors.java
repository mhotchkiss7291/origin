
public class Constructors {

	public static void main(String[] args) {

		Car a = new Car();
		Car b = new Car();
		Car c = new Car();
		
		a.model = "BMW";
		a.price = 2132;

		Car d = new Car("Hundai", 12345);
		System.out.println(d.model);
		System.out.println(d.price);

		Car e = new Car("XXX", 3131);
	}

}
