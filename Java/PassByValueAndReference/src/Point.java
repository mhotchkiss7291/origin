
public class Point {

	int x;
	int y;
	
	public static void main(String[] args) {
		Point p = new Point();
		p.x=10;
		p.y=20;
		System.out.println(p.x + " == " + p.y);
		swap(p.x, p.y);
		System.out.println(p.x + " == " + p.y);
		swapVals(p);
		System.out.println(p.x + " == " + p.y);
	}
	
	// Pass by value
	public static void swap(int a, int b) {
		int temp = a;
		a = b;
		b = temp;
	}

	// Pass by reference
	public static void swapVals(Point p) {
		int temp = p.x ;
		p.y = p.x;
		p.x = temp;
	}
}
