package newpackage;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;



public class Client {
    private final static ClientConfig config = new DefaultClientConfig();
    private final static com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(config);
    private final static WebResource service = client.resource(
    UriBuilder.fromUri("http://localhost:19566/sysintinlupp").build());
    
     public static void main(String[] args) {
           getTemperatur();
        }
        public static void sendToAzure(float temp){
        String url = String.format("jdbc:sqlserver://sqldbbi2.database.windows.net:1433;database=systemintegration;user=olof@sqldbbi2;password=Sysinlupp2;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);
            System.out.println("=========================================");

            

            try (Statement statement = connection.createStatement();) {

                String selectSql = "INSERT INTO test(temp) VALUES('"+temp+"');";
                statement.executeUpdate(selectSql);
                System.out.println("Temperatures: ");
                
                String selectSql2 = "select * from test";
                ResultSet resultSet2 = statement.executeQuery(selectSql2);
                while (resultSet2.next())
                {
                    System.out.println(resultSet2.getString(1) + " "
                        + resultSet2.getString(2));
                }
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    public static void getTemperatur(){
        Temperature temp = service.path("rest")
        .path("Service/tempNow").accept(MediaType.APPLICATION_XML).get(Temperature.class);
        System.out.println("Temp: " + temp.getTemp());
        float f = temp.getTemp();
        sendToAzure(f);
    }

}
