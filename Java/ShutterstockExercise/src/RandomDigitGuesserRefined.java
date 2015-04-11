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

public class RandomDigitGuesserRefined {

	public static final void main(String[] args) {

		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(9999);
		log("Random: " + randomInt);

		String guess = args[0];
		log("Guess: " + args[0]);

		// Convert random int to String
		String strI = "" + randomInt;

		String reportString = "";

		int i = 0;

		String[] guess_digit = new String[4];

		// Populate guess_digit array
		while (i < 4) {
			guess_digit[i] = guess.substring(i, i + 1);
			i++;
		}

		// Is digit found in place?
		// Mark an x
		int j = 0;
		while (j < 4) {
			if (strI.indexOf(guess_digit[j]) == j) {
				reportString += "x";
			}
			j++;
		}

		// Is digit found in anywhere?
		// Mark an o
		int k = 0;
		while (k < 4) {
			if (strI.contains(guess_digit[k])
					&& !(strI.indexOf(guess_digit[k]) == 0)) {
				reportString += "o";
			}
			k++;
		}
		
		log(reportString);	

	}

	private static void log(String aMessage) {
		System.out.println(aMessage);
	}
}
