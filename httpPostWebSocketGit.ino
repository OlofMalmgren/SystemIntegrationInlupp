#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <ESP8266HTTPClient.h>
#include <DHT.h>


#define DHT_TYPE DHT11
#define DHT_PIN 2

float temp;
String temp_str;


static DHT dht(DHT_PIN, DHT_TYPE);
// Constants
const char* ssid = "";
const char* password = "";
 
// Globals
WebSocketsServer webSocket = WebSocketsServer(80);
 
// Called when receiving any WebSocket message
void onWebSocketEvent(uint8_t num,
                      WStype_t type,
                      uint8_t * payload,
                      size_t length) {
 
  // Figure out the type of WebSocket event
  switch(type) {
 
    // Client has disconnected
    case WStype_DISCONNECTED:
      Serial.printf("[%u] Disconnected!\n", num);
      break;
 
    // New client has connected
    case WStype_CONNECTED:
      {
        IPAddress ip = webSocket.remoteIP(num);
        Serial.printf("[%u] Connection from ", num);
        Serial.println(ip.toString());
      }
      break;
 
    // Echo text message back to client
    case WStype_TEXT:
      
      Serial.printf("[%u] Text: %s\n", num, payload);
      temp_str = String(temp);
      webSocket.sendTXT(0, temp_str);
      webSocket.sendTXT(num, payload);
      break;
 
    // For everything else: do nothing
    case WStype_BIN:
    case WStype_ERROR:
    case WStype_FRAGMENT_TEXT_START:
    case WStype_FRAGMENT_BIN_START:
    case WStype_FRAGMENT:
    case WStype_FRAGMENT_FIN:
    default:
      break;
  }
}
 
void setup() {
 
  Serial.begin(115200);
 
  Serial.println("Connecting");
  WiFi.begin(ssid, password);
  while ( WiFi.status() != WL_CONNECTED ) {
    delay(500);
    Serial.print(".");
  }
 
  Serial.println("Connected!");
  Serial.print("My IP address: ");
  Serial.println(WiFi.localIP());
 
  // Start WebSocket server and assign callback
  webSocket.begin();
  webSocket.onEvent(onWebSocketEvent);
}

void loop() {
  temp = dht.readTemperature(); 
  webSocket.loop();

   if(WiFi.status()== WL_CONNECTED){
  
   HTTPClient http;   
   http.begin(String("http:...")+temp); 
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
