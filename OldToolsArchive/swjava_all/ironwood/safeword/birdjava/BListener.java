import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class BListener {

	private boolean DEBUG = false;

	private Vector getHostInfo() {

		FileReader in = null;
		String directory = "./etc";
		String filename = "dat1";
		String DataString = null;
		Object object = null;
		Object object_2_ = null;

		try {
			File f = new File(directory, filename);
			in = new FileReader(f);
			int size = (int) f.length();
			char[] data = new char[size];
			int chars_read = 0;

			while( chars_read < size ) {
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
			}
		}
		int FirstIndex = DataString.indexOf("\n");
		int SecondIndex = DataString.indexOf("\n", FirstIndex + 1);
		String IP = DataString.substring( 0, FirstIndex );
		String Port = DataString.substring(FirstIndex + 1, SecondIndex);
		Vector v = new Vector();
		v.setSize( 2);

		v.setElementAt(IP, 0);
		v.setElementAt(Port, 1);
		return v;
	}

	public String getRequestType(Vector v) {
		return (String) v.elementAt(0);
	}

	public void go() {

		Object object = null;
		Object object_7_ = null;
		InputStream inputstream = null;
		ObjectInputStream objectinputstream = null;
		OutputStream outputstream = null;
		Object object_8_ = null;
		Object object_9_ = null;
		File file = new File("./etc/log");

		if (!file.exists()) {
			System.out.println("Log file does not exist");
			System.exit(0);
		}
		File file_10_ = new File("./etc/dat1");

		if (!file_10_.exists()) {
			System.out.println("dat1 file does not exist");
			System.exit(0);
		}
		File file_11_ = new File("./etc/dat2");

		if (!file_11_.exists()) {
			System.out.println("dat2 file does not exist");
			System.exit(0);
		}
		File file_12_ = new File("./etc/recipients");

		if (!file_12_.exists()) {
			System.out.println("recipients file does not exist");
			System.exit(0);
		}

		Vector vector = new Vector();
		Vector vector_13_ = new Vector();
		Vector vector_14_ = new Vector();
		vector_14_ = getHostInfo();
		String string = (String) vector_14_.elementAt(0);
		int i = Integer.parseInt((String) vector_14_.elementAt(1));
		ServerSocket serversocket;

		try {
			serversocket = new ServerSocket(i);
		} catch (IOException ioexception) {
			System.out.println("Can't construct ServerSocket");
			ioexception.printStackTrace();
			return;
		}

		while( true ) {

			try {
				Socket socket = serversocket.accept();
				String string_15_ = socket.getInetAddress().toString();

				try {
					inputstream = socket.getInputStream();
				} catch (IOException ioexception) {
					System.out.println("Exception in getInputStream");
					ioexception.printStackTrace();
				}

				try {
					objectinputstream = new ObjectInputStream(inputstream);
				} catch (StreamCorruptedException streamcorruptedexception) {
					System.out.println("Corrupted ObjectInputStream");
					streamcorruptedexception.printStackTrace();
				} catch (IOException ioexception) {
					System.out.println("IOException ObjectInputStream");
					ioexception.printStackTrace();
				}

				try {
					vector = (Vector) objectinputstream.readObject();
				} catch (ClassNotFoundException classnotfoundexception) {
					System.out.println("Class not found in readObject");
					classnotfoundexception.printStackTrace();
				}

				if (DEBUG) {
					System.out.println("request = " + vector);
				}

				String string_16_ = getRequestType(vector);

				if (string_16_.equals("GETPIN")) {
					Pin pin = new Pin(getCardID(vector));
					vector_13_.addElement(pin.get());
				}

				if (DEBUG) {
					System.out.println("reply = " + vector_13_);
				}

				try {
					outputstream = socket.getOutputStream();
				} catch (IOException ioexception) {
					System.out.println("IOException getOutputStream");
					ioexception.printStackTrace();
				}

				ObjectOutputStream objectoutputstream = 
						new ObjectOutputStream(outputstream);

				try {
					objectoutputstream.writeObject(vector_13_);
				} catch (IOException ioexception) {
					System.out.println("IO Exception writeObject");
					ioexception.printStackTrace();
				}

				vector.removeAllElements();
				vector_13_.removeAllElements();
				objectoutputstream.close();
				outputstream.close();
				objectinputstream.close();
				inputstream.close();
				socket.close();

			} catch (IOException ioexception) {
				System.out.println("Listen exception..");
			}
		}
	}

	public String getCardID(Vector vector) {
		return (String) vector.elementAt(2);
	}

	public static void main(String[] strings) {
		BListener blistener = new BListener();
		blistener.go();
	}
}
