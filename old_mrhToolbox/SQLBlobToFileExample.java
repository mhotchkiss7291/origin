import java.sql.*;
import java.util.Random;
import java.io.*;

public class SQLBlobToFileExample {

    public static void main(String[] args) throws SQLException {

        //Set up SQL parameters
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (Exception e) {
            System.exit(1);
        }
        Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test");
        Statement s = c.createStatement();

        //Clean up stuff before the test
        try {
            s.executeUpdate("DROP TABLE mytable");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Create the table and set up for insert
        s.executeUpdate("CREATE TABLE mytable (COL1 BLOB)");
        PreparedStatement ps = c.prepareStatement("INSERT INTO mytable VALUES(?)");

        //Generate a random bunch of binary data
        Random random = new Random();
        byte[] inByteArray = new byte[500000];
        random.nextBytes(inByteArray);

        //Insert the Blob
        ps.setBytes(1, inByteArray);
        ps.executeUpdate();

        //Query for the Blob after insertion
        ResultSet rs = s.executeQuery("SELECT * FROM mytable");
        rs.next();
        Blob blob = rs.getBlob(1);

        //Set up for writing out the file
        File f = new File("/home/mhotchkiss/BlobFile.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Get the Blob into a suitable way for writing
        // and this was tricky to find a way
        InputStream in = blob.getBinaryStream();
        BufferedInputStream binst = new BufferedInputStream(in);
        ByteArrayOutputStream boutinst = new ByteArrayOutputStream();
        int start = 0;
        int length = 1024;
        byte[] buff = new byte[length];

        try {
            while (binst.read(buff, start, length) != -1) {
                boutinst.write(buff, start, length);
            }
            binst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Write it to the file
        try {
            fos.write(boutinst.toByteArray());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Just for exercise, now that the Blob is defined as an Object, put it back
        ps.setBlob(1, blob);
        ps.execute();

        //Close the connection
        c.close();
    }
}
