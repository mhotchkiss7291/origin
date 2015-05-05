package org.gontuseries.springcore;

public class Tea implements IHotDrink {

	@Override
	public void prepareDrink() {
		System.out.println("Dear Customer, we are preparing Tea for you.");
	}

}
