#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <WiFiUdp.h>
#include <DHT.h>
#include <ESP8266HTTPClient.h>
#include <WebSocketsServer.h>
#include <AzureIoTHub.h>
#include <AzureIoTProtocol_MQTT.h>
#include <AzureIoTUtility.h>

#include "config.h"

WebSocketsServer webSocket = WebSocketsServer(80);
static IOTHUB_CLIENT_LL_HANDLE iotHubClientHandle;
static int interval = DEVICE_SEND_INTERVAL;
unsigned long currentMillis = 0;
unsigned long prevMillis = 0;
static bool messagePending = false;
static bool messageSending = true;


float temp;
float hum;
String temp_str;
static DHT dht1(D5, DHT11);

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
      webSocket.broadcastTXT(temp_str);
      break;
  }
}

void setup() 
{  
  initSerial();
  initWifi();
  initTime();   
  iotHubClientHandle = initIotHub();
  webSocket.begin();
  webSocket.onEvent(onWebSocketEvent);
}

void loop() {
  temp = dht1.readTemperature();
  hum = dht1.readHumidity();
  webSocket.loop();
  currentMillis = millis();
  
  if (!messagePending && messageSending)
  {
    if((currentMillis - prevMillis) >= interval) {
      prevMillis = currentMillis;
      
      char messagePayload[MESSAGE_MAX_LEN];
      readMessage(messagePayload);
      sendMessage(iotHubClientHandle, messagePayload);    
    }
  }
  IoTHubClient_LL_DoWork(iotHubClientHandle);
  delay(10);
}
