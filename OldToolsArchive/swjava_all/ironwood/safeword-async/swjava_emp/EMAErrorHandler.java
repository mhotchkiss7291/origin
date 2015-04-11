import java.io.*;

public class EMAErrorHandler {

	EMAErrorHandler(String status, String message)
			 throws IOException {

		if (status.equals("CLI_ERROR")) {
			EMANotify n = new EMANotify(message);
			try {
				n.send();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (status.equals("SECURITY_EMERGENCY")) {
			EMANotify n = new EMANotify(message);
			try {
				n.send();
				System.out.println("SECURITY_EMERGENCY: " + status);
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			// Getting out for now
			System.exit(0);
		}
	}
}

