package org.gontuseries.springcore;

public class Restaruant {

	IHotDrink hotDrink;

	Restaruant(IHotDrink hotDrink) {
		this.hotDrink = hotDrink;
	}

	public void prepareHotDrink() {
		hotDrink.prepareDrink();
	}

}
