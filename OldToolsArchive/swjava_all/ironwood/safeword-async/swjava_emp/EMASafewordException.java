import java.util.*;
import java.io.*;
import java.awt.*;

public class EMASafewordException extends Exception {
	String problem = "Unknown";

	EMASafewordException(String s) {
		problem = s;
	}

	public String toString() {
		return "EMASafewordException -->" + problem;
	}
}
