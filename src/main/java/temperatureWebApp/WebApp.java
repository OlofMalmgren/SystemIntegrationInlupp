package temperatureWebApp;



import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WebApp 
{
        // Connect to database
        public static void main(String[] args) throws SQLException {
        String url = String.format("jdbc:sqlserver://sqldbbi2.database.windows.net:1433;database=systemintegration;user=olof@sqldbbi2;password={Sysinlupp2};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);
            System.out.println("=========================================");

            // Create and execute a SELECT SQL statement.
            //String selectSql = "select * from test";
            String selectSql = "select * from test";

            try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSql)) {

                // Print results from select statement
                System.out.println("Temperatures: ");
                while (resultSet.next())
                {
                    System.out.println(resultSet.getString(1) + " "
                        + resultSet.getString(2));
                }
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String test = AzureFunctions.getAllTemperatures();
            System.out.println(test);
    }
}
