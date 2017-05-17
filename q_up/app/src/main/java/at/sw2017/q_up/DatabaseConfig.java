package at.sw2017.q_up;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DatabaseConfig {
    String result = "";
    InputStream inputStream;

    // these must be filled from config file:
    //private String server_url;
    //private String server_dbname;
    private String server_user;
    private String server_pw;

    public DatabaseConfig() {
        //server_url = "";
        //server_dbname = "";
        server_user = "";
        server_pw = "";
    }

    public String loadConfigValues() throws IOException {
        try {
            Properties prop = new Properties();
            String propFileName = "dbconfig.properties";

            inputStream = QUpApp.getContext().getAssets().open(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            //server_url = prop.getProperty("url");
            //server_dbname = prop.getProperty("database");
            server_user = prop.getProperty("user");
            server_pw = prop.getProperty("pw");
            /*
            System.out.println("url: " + server_url);
            System.out.println("database: " + server_dbname);
            System.out.println("user: " + server_user);
            System.out.println("pw: " + server_pw);
            */
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result;
    }
/* unused
    public String getUrl() {
        return server_url;
    }

    public String getDBname() {
        return server_dbname;
    }
*/
    public String getUser() {
        return server_user;
    }

    public String getPw() {
        return server_pw;
    }
}
