package newpackage;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Service")
public class Service {

    
    @POST
    @Path("/temp/{temp}")
    @Produces (MediaType.APPLICATION_XML)
    public String setHelloWorld(@PathParam("temp") float temp){
        Properties p = new Properties();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            p.load(new FileInputStream("C:\\Users\\LarsB\\Documents\\databasfil\\Settings.properties.txt"));
        }catch(Exception e){
            e.printStackTrace();
        }
        try(Connection con = DriverManager.getConnection(
           p.getProperty("connection"), p.getProperty("name"), p.getProperty("password"));){
             PreparedStatement st = con.prepareStatement("INSERT INTO message (temp) VALUES (?) ");
             st.setFloat(1, temp);
             st.executeUpdate();
             
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "temp added";
    }
    
    
    @GET
    @Path("/tempNow")
    @Produces (MediaType.APPLICATION_XML)
    public Temperature getTemperature(){
        Temperature t = new Temperature();
        Properties p = new Properties();
        float temp = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            p.load(new FileInputStream("C:\\Users\\LarsB\\Documents\\databasfil\\Settings.properties.txt"));
        }catch(Exception e){
            e.printStackTrace();
        }
        try(Connection con = DriverManager.getConnection(
           p.getProperty("connection"), p.getProperty("name"), p.getProperty("password"));){
             PreparedStatement st = con.prepareStatement("SELECT temp FROM message WHERE id =(SELECT max(id) FROM message);");
             ResultSet rs = st.executeQuery();
            while(rs.next()){
                t.setTemp(rs.getFloat("temp"));
                System.out.println(temp);
            }
             
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }
}
