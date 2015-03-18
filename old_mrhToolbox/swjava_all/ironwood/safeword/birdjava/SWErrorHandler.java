import java.io.IOException;

public class SWErrorHandler {

	SWErrorHandler(String string, String string_0_) 
			throws IOException {

		if (string.equals("CLI_ERROR")) {

			Notify notify = new Notify(string_0_);

			try {
				notify.send();
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
		}

		if (string.equals("SECURITY_EMERGENCY")) {

			Notify notify = new Notify(string_0_);

			try {
				notify.send();
				System.out.println("SECURITY_EMERGENCY: " + string);
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
			System.exit(0);
		}
	}
}
