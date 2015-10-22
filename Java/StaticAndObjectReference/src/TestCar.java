
public class TestCar {

	public static void main(String[] args) {
		
		Car a = new Car();
		Car b = new Car();
		Car c = new Car();

		a.model = "A";
		b.model = "B";
		c.model = "C";
		
		System.out.println(a.model);
		System.out.println(b.model);
		System.out.println(c.model);
		
		// Bad move
		// a will be dereferenced and go to garbage collection
		a = b;
		b = c;
		c = a;
		
		System.out.println(a.model);
		System.out.println(b.model);
		System.out.println(c.model);
		
		a.model = "xxxx";

		System.out.println(a.model);
		System.out.println(b.model);
		System.out.println(c.model);
		
	}

}
