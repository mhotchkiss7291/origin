import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.StringTokenizer;

public class Vs {

	public static void main(String[] args) {
		Vs vs = new Vs();
		vs.processFile();
	}

	void processFile() {

		File f;
		FileReader in = null;
		String DataString = null;

		try {
			f = new File("vs_report.txt");
			in = new FileReader(f);
			int size = (int) f.length();
			char[] data = new char[size];
			int chars_read = 0;

			while (chars_read < size) {
				chars_read += in.read(data, chars_read, size - chars_read);
			}

			DataString = new String(data);

		} catch (IOException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.getStackTrace();
			}
		}

		try {
			parseLines(DataString);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void parseLines(String DataString) throws IOException {

		StringTokenizer sst = new StringTokenizer(DataString, "\n", true);

		String s = null;
		int count = 0;
		String[] lines = new String[sst.countTokens()];

		String title = null;
		String personel = null;
		String rating = null;
		String year = null;
		String genre = null;
		String runtime = null;
		String outline = null;

		while (sst.hasMoreTokens()) {
			s = sst.nextToken();
			lines[count++] = s;
		}

		int i = 0;
		StringBuffer sb = new StringBuffer();
		String buf;

		while (i < count) {

			title = null;
			personel = null;
			rating = null;
			year = null;
			genre = null;
			runtime = null;
			outline = null;
			buf = lines[i].trim();

			if (buf.length() >= 44) {
				title = buf.substring(15, 44);
			} else {
				title = "";
			}

			if (buf.length() >= 69) {
				personel = buf.substring(44, 69);
			} else {
				personel = "";
			}

			if (buf.length() >= 71) {
				rating = buf.substring(69, 71);
			} else {
				rating = "";
			}
			if (buf.length() >= 74) {
				year = buf.substring(72, 74);
			} else {
				year = "";
			}
			if (buf.length() >= 91) {
				genre = buf.substring(75, 91);
			} else {
				genre = "";
			}
			if (buf.length() >= 103) {
				runtime = buf.substring(100, 103);
			} else {
				runtime = "";
			}

			outline = title.trim() + "||||" + personel.trim() + "||"
					+ rating.trim() + "||" + year.trim() + "||"
					+ runtime.trim() + "||" + genre.trim() + "\n";
			i++;
			i++;

			sb.append(outline);
		}

		Writer output = new BufferedWriter(new FileWriter("file_rec.txt"));

		try {
			output.write(sb.toString());
		} finally {
			output.close();
		}
	}
}

