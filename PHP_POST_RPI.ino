#include <ESP8266WiFi.h>
#include <WebSocketsServer.h>
#include <DHT.h>

#define DHT_TYPE DHT11
#define DHT_PIN 2
long previousMillis = 0; 
float temp;
float hum;
String temp_str;

static DHT dht(DHT_PIN, DHT_TYPE);
WebSocketsServer webSocket = WebSocketsServer(80);
const char* ssid     = "";
const char* password = "";

const char* host = "";


void onWebSocketEvent(uint8_t num, WStype_t type, uint8_t * payload, size_t length) {
 
  switch(type) {
 
    case WStype_DISCONNECTED:
      Serial.printf("[%u] Disconnected!\n", num);
      break;
 
    case WStype_CONNECTED:
      {
        IPAddress ip = webSocket.remoteIP(num);
        Serial.printf("[%u] Connection from ", num);
        Serial.println(ip.toString());
      }
      break;
 
    case WStype_TEXT:
      
      Serial.printf("[%u] Text: %s\n", num, payload);
      temp_str = String(temp);
      webSocket.sendTXT(num, temp_str);
      break;
  }
}


void setup() {
  Serial.begin(115200);
  delay(100);

  // We start by connecting to a WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

 while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP()); 
}

void loop() {
  webSocket.loop();
    unsigned long currentMillis = millis();
  
  
  if(currentMillis - previousMillis > 1800000){
        previousMillis = currentMillis;
  WiFiClient client;
  const int httpPort = 80;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }

 float temp = dht.readTemperature();
 String data = "temp=" + String(temp);

   Serial.print("Requesting POST: ");
   // Send request to the server:
   client.println("POST /testphp-mysql.php HTTP/1.1");
   client.println("Host: ");
   client.println("Accept: */*");
   client.println("Content-Type: application/x-www-form-urlencoded");
   client.print("Content-Length: ");
   client.println(data.length());
   client.println();
   client.print(data);

   delay(500); // Can be changed
  if (client.connected()) { 
    client.stop();  // DISCONNECT FROM THE SERVER
  }
  Serial.println();
  Serial.println("closing connection");
   }
}
