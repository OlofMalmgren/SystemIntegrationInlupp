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
        ResultSet rs = stmtCost.executeQuery("select * from test");
        String allTemps = "";
        while(rs.next())
        {
            float buff = rs.getFloat("temp");
            allTemps += Float.toString(buff) + "\n";
        }
        return allTemps;
    }
}
