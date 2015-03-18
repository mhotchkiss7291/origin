package com.digitalglobe.test.fit.fixtures;

import java.sql.*;
import java.io.*;
import fit.ColumnFixture;
import java.sql.Clob;
import java.sql.DriverManager;
import java.io.*;

public class ClobToFileFixture extends ColumnFixture {
    public String Filepath = null;
    public String Url = null;
    public String Driver = null;
    public String Username = null;
    public String Password = null;
    public String Sql = null;

    public String writeClob() {
        try {
            Class.forName(Driver);
            Connection c = DriverManager.getConnection(Url, Username, Password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(Sql);
            rs.next();
            Clob clob = rs.getClob(1);
            BufferedReader bufReader = new BufferedReader(clob.getCharacterStream());
            BufferedWriter  bufWriter = new BufferedWriter(new FileWriter(Filepath));
            String line = null;
            while ((line = bufReader.readLine()) != null) {
                bufWriter.write(line);
                bufWriter.newLine();
            }
            bufReader.close();
            bufWriter.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return "Success";
    }
}
