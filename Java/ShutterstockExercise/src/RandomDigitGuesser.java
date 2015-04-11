import java.util.Random;

/*
 * The exercise was the generate a random number integer of 
 * four digits long and then take a guess as input and
 * determine two characteristics of the two integers:
 * 
 * 1. Does each digit of the Guess four digit integer
 * appear in its numerical place. If it does, 
 * mark an x in the reportString as being found.
 * 
 * 2. Does each digit of the Guess four digit integer
 * appear anywhere in the four digits. If it does,
 * mark an "o" in the reportSting as being found.
 * 
 * 3. Otherwise don't mark anything in the reportString.
 */

public class RandomDigitGuesser {

	public static final void main(String[] args) {

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(9999);
		log("Random: " + randomInt);

		String guess = args[0];
		log("Guess: " + args[0]);

		// Convert random int to String
		String strI = "" + randomInt;

		String reportString = "";

		String guess_digit1 = guess.substring(0, 1);
		String guess_digit2 = guess.substring(1, 2);
		String guess_digit3 = guess.substring(2, 3);
		String guess_digit4 = guess.substring(3, 4);

		// Does the value of the digit match in its place?
		if (strI.indexOf(guess_digit1) == 0) {
			reportString += "x";
		}
		if (strI.indexOf(guess_digit2) == 1) {
			reportString += "x";
		}
		if (strI.indexOf(guess_digit3) == 2) {
			reportString += "x";
		}
		if (strI.indexOf(guess_digit4) == 3) {
			reportString += "x";
		}

		// Does the value of the digit match anywhere in the integer 
		if (strI.contains(guess_digit1) && !(strI.indexOf(guess_digit1) == 0)) {
			reportString += "o";
		}
		if (strI.contains(guess_digit2) && !(strI.indexOf(guess_digit2) == 1)) {
			reportString += "o";
		}
		if (strI.contains(guess_digit3) && !(strI.indexOf(guess_digit3) == 2)) {
			reportString += "o";
		}
		if (strI.contains(guess_digit4) && !(strI.indexOf(guess_digit4) == 3)) {
			reportString += "o";
		}

		log(reportString);

	}

	private static void log(String aMessage) {
		System.out.println(aMessage);
	}
}
