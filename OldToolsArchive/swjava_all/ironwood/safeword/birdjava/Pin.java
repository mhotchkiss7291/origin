import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.StringTokenizer;

public class Pin {

	private String CardID = null;
	private String PIN = null;

	public Pin(String string) {
		CardID = new String(string.toUpperCase());
	}

	public String get() {

		Object object = null;
		FileReader filereader = null;
		String string = null;

		try {
			File file = new File("./etc/pins");
			filereader = new FileReader(file);
			int i = (int) file.length();
			char[] cs = new char[i];

			for (int i_0_ = 0; i_0_ < i;
						i_0_ += filereader.read(cs, i_0_, i - i_0_)) {
			}

			filereader.close();
			string = new String(cs);
		} catch (IOException ioexception) {
			System.out.println(ioexception.getClass().getName() + ": "
				 + ioexception.getMessage());
		}

		StringTokenizer stringtokenizer = 
				new StringTokenizer(string, "\n", true);
		String string_1_ = new String("PIN not found");
		Object object_2_ = null;

		while (stringtokenizer.hasMoreTokens()) {
			String string_3_ = stringtokenizer.nextToken();

			if (!string_3_.equals("\n") && string_3_.startsWith(CardID)) {
				int i = string_3_.indexOf(",");
				PIN = string_3_.substring(i + 1);
				string_1_ = "PIN found";
			}
		}

		log(string_1_ + " : CardID = " + CardID);

		try {
			filereader.close();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
		return PIN;
	}

	public void log(String string) {

		RandomAccessFile randomaccessfile = null;

		try {
			randomaccessfile = new RandomAccessFile("./etc/log", "rw");
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
			Notify notify = new Notify("Can't open log file");

			try {
				notify.send();
			} catch (IOException ioexception_4_) {
				ioexception_4_.printStackTrace();
			}
		}

		try {
			randomaccessfile.seek(randomaccessfile.length());
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}

		String string_5_ = String.valueOf(new Date()) + ": " + string + "\n";
		byte[] is = string_5_.getBytes();

		try {
			randomaccessfile.write(is);
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}

		try {
			randomaccessfile.close();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	public void logAndNotify(String string) {
		log(string);
		Notify notify = new Notify(string);

		try {
			notify.send();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}
}
