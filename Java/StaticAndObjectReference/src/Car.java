
public class Car {

	String model;
	int price;
	static int wheels = 4;

	public static void main(String[] args) {

		// c1 is the reference to the object
		Car c1 = new Car();
		c1.model = "Ford";
		c1.price = 10000;
		// c1.wheels = 4;
		c1.start();
		c1.accel();

		Car c2 = new Car();
		c2.model = "Chevy";
		c2.price = 15000;
		// c2.wheels = 4;
		c2.start();
		c2.accel();

		System.out.println(c1.model);
		System.out.println(c2.model);
		System.out.println(c1.price);
		System.out.println(c2.price);

		System.out.println(wheels);
		System.out.println(Car.wheels);

		// Bad practice
		System.out.println(c1.wheels);

		// non-static
		// System.out.println(price);

		fillGas(100);
		// or
		Car.fillGas(200);

		// Bad practice calling static methods on objects
		c1.fillGas(300);

	}

	public void start() {

		System.out.println(model + " starting");
	}

	public void accel() {

		System.out.println(model + " accellerating");

	}

	public static void fillGas(int quantity) {

	}
}
