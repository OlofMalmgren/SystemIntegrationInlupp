package temperatureWebApp;


import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;




public class AzureFunctions 
{
    private static Connection setupADBconnection() throws SQLException, ClassNotFoundException
    {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = String.format("jdbc:sqlserver://sqldbbi2.database.windows.net:1433;database=systemintegration;user=olof@sqldbbi2;password={Sysinlupp2};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        Connection con = DriverManager.getConnection(url);
        return con;
    }
    
    public static String getAllTemperatures() throws SQLException, ClassNotFoundException
    {
        Connection con = setupADBconnection();
        Statement stmtCost = con.createStatement();
        ResultSet rs = stmtCost.executeQuery("select * from Message");
        String allTemps = "";
        while(rs.next())
        {
            String device = rs.getNString("Device");
            float temp = rs.getFloat("Temperature");
            float hum = rs.getFloat("Humidity");
            String date = rs.getString("Created");
            String longi = rs.getString("Longitude");
            String lat = rs.getString("Latitude");
            allTemps += "Decvice: " + device + " Temperature: " + temp + " Humidity: " + hum + " Date: " + date + " Longitude: " + longi + " Latitude: " + lat +"\n";
        }
        return allTemps;
    }
    
    public static void insertNewTemp(String newValue) throws SQLException, ClassNotFoundException
    {
        Connection con = setupADBconnection();
        PreparedStatement stmt = con.prepareStatement("insert into Test(temp) values(?)");
        stmt.setString(1, newValue);
        stmt.executeQuery();
    }
    
    
}
