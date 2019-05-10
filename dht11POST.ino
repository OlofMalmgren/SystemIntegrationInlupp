#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <DHT.h>


#define DHT_TYPE DHT11
#define DHT_PIN 2
static DHT dht(DHT_PIN, DHT_TYPE);

const char* ssid = "";
const char* password =  "";
 
void setup() {
  Serial.begin(115200);
  delay(4000);
  WiFi.begin(ssid, password); 
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi..");
  }
 
  Serial.println("Connected to the WiFi network");
}
 
void loop() {
 float temp = dht.readTemperature();
 if(WiFi.status()== WL_CONNECTED){
  
   HTTPClient http;   
   http.begin(String("http://...")+temp); 
   http.addHeader("Content-Type", "text/plain");             
   int httpResponseCode = http.POST("temp");  
    
   if(httpResponseCode>0){
    String response = http.getString();
    Serial.println(httpResponseCode);
    Serial.print(response);  
    Serial.println(temp);
   }else{
    Serial.print("Error on sending POST: ");
    Serial.println(httpResponseCode);
   }
   http.end();
 
 }else{
    Serial.println("Error in WiFi connection");   
 }

 
  delay(10000);
}
