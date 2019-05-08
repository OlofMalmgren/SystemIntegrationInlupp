package newpackage;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "temperature") 
public class Temperature implements Serializable{
    private static final long serialVersionUID = 1L;
    private float temp;
    
    public Temperature(){
        
    }
    
    public Temperature(float temp){
        this.temp = temp;
    }
    
    @XmlElement 
    public void setTemp(float temp){
        this.temp = temp;
    }
    
    public float getTemp(){
        return temp;
    }
}
