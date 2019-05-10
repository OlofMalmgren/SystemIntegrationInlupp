void initSerial()
{
  Serial.begin(115200);
  delay(2000);    
}

void initWifi()
{
  Serial.print("Connecting to WIFI Network. Please wait.");
  WiFi.begin(WIFI_SSID, WIFI_PASS);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
 
  }
  Serial.println("");
  Serial.print("SSID: ");
  Serial.println(WIFI_SSID);
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());
  Serial.println(""); 
}

void initTime()
{
    Serial.println("Connecting to NTP servers. Please wait...");
    time_t epochTime;
    configTime(0, 0, "pool.ntp.org", "time.nist.gov");

    while (true)
    {
        epochTime = time(NULL);

        if (epochTime <= 28900)
        {
            delay(2000);
        }
        else
        {
            Serial.printf("Timestamp: %lu.\r\n", epochTime);
            Serial.println("");
            break;
        }
    }
}

char* GetConnectionString() 
{  
static char *connectionString ="HostName=OlofIoTHub2018.azure-devices.net;DeviceId=TempSensor;SharedAccessKey=YSr7yCe+f9KK3TqfLtptUSZYOgtWB+xnqntSWMWh7mQ=";
 
  return connectionString;
}

IOTHUB_CLIENT_LL_HANDLE initIotHub()
{
  Serial.println("Connecting to Azure IOT Hub. Please wait...");
  IOTHUB_CLIENT_LL_HANDLE iotHubClientHandle;

  iotHubClientHandle = IoTHubClient_LL_CreateFromConnectionString(GetConnectionString(), MQTT_Protocol);
  if (iotHubClientHandle == NULL)
  {
      Serial.println("ERROR: Failed on IoTHubClient_CreateFromConnectionString.");
      while (1);
  }

  IoTHubClient_LL_SetDeviceMethodCallback(iotHubClientHandle, deviceMethodCallback, NULL);
  IoTHubClient_LL_SetDeviceTwinCallback(iotHubClientHandle, twinCallback, NULL);

  Serial.print("DeviceId: ");
  Serial.println(DEVICE_ID);
  Serial.print("Status: Connected");
  Serial.println("");   
  Serial.println("");
  Serial.println("MESSAGE INFORMATION");
  Serial.println("----------------------------------------------------");
  return iotHubClientHandle;
}
