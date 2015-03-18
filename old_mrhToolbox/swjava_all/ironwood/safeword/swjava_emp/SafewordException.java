import java.util.*;
import java.io.*;
import java.awt.*;

public class SafewordException extends Exception {
	String problem = "Unknown";

	SafewordException(String s) {
		problem = s;
	}

	public String toString() {
		return "SafewordException -->" + problem;
	}
}
