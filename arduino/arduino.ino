#include <SoftwareSerial.h>

#define DEBUG true

#define LIGHT_BULB 7 //light bulb pin

SoftwareSerial esp8266(2,3); // make RX Arduino line is pin 2, make TX Arduino line is pin 3.
                             // This means that you need to connect the TX line from the esp to the Arduino's pin 2
                             // and the RX line from the esp to the Arduino's pin 3

bool on = false;
                             
void setup()
{

  for(int i=0; i<14; i++){
    if(i == 2 || i == 3) continue;
    pinMode(i,OUTPUT); 
  }

  
  pinMode(LIGHT_BULB,OUTPUT); 
  digitalWrite(LIGHT_BULB, HIGH);
  
  Serial.begin(115200);
  esp8266.begin(115200); // your esp's baud rate might be different

  sendCommand("AT+RST\r\n",100,DEBUG); // reset module 2000 , 500
  sendCommand("AT+CWMODE=1\r\n",100,DEBUG); // configure as access point 1000 , 500
  sendCommand("AT+CWJAP=\"NODREX\",\"vergamoicnobt\"\r\n",3000,DEBUG); // , 1000 , 500
  delay(10000); // , 5000
  sendCommand("AT+CIFSR\r\n",10000,false); // get ip address 
  sendCommand("AT+CIPMUX=1\r\n",100,DEBUG); // configure for multiple connections 1000 , 500
  sendCommand("AT+CIPSERVER=1,80\r\n",100,DEBUG); // turn on server on port (80) 1000 , 500
  Serial.println("");
  Serial.println("Server Ready");
}

void loop()
{
  if(esp8266.available() > 0 ) // check if the esp is sending a message 
  { 
    if(esp8266.find("+IPD,")){
      //delay(100);
     //delay(1000); // wait for the serial buffer to fill up (read all the serial data) 
     //get the connection id so that we can then disconnect
     int connectionId = esp8266.read()-48;
     if(on){
        digitalWrite(LIGHT_BULB, HIGH);
        on = false;
        //sendHTTPResponse(connectionId,"6,d");
      }else{
        digitalWrite(LIGHT_BULB, LOW);
        on = true;
        //sendHTTPResponse(connectionId,"8,d");
      }
       // make close command
       String closeCommand = "AT+CIPCLOSE="; 
       closeCommand+=connectionId; // append connection id
       closeCommand+="\r\n";
     sendCommand(closeCommand,1000,false);
    }
  }
}
 
/*
* Name: sendData
* Description: Function used to send data to ESP8266.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the esp8266 (if there is a reponse)
*/
String sendData(String command, const int timeout, boolean debug)
{
    String response = "";
    
    int dataSize = command.length();
    char data[dataSize];
    command.toCharArray(data,dataSize);
           
    esp8266.write(data,dataSize); // send the read character to the esp8266
    
    long int time = millis();
    
    while( (time+timeout) > millis())
    {
      while(esp8266.available())
      {
        
        // The esp has data so display its output to the serial window 
        char c = esp8266.read(); // read the next character.
        response+=c;
      }  
    }
    
    if(debug)
    {
      //Serial.print(response);
      //Serial.println("Done");
    }
    return response;
}
 
/*
* Name: sendHTTPResponse
* Description: Function that sends HTTP 200, HTML UTF-8 response
*/
void sendHTTPResponse(int connectionId, String content)
{
     // build HTTP response
     String httpResponse;
     String httpHeader;
     // HTTP Header
     httpHeader = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n"; 
     httpHeader += "Content-Length: ";
     httpHeader += content.length();
     httpHeader += "\r\n";
     httpHeader +="Connection: close\r\n\r\n";
     httpResponse = httpHeader + content + " "; // There is a bug in this code: the last character of "content" is not sent, I cheated by adding this extra space
     sendCIPData(connectionId,httpResponse);
}
 
/*
* Name: sendCIPDATA
* Description: sends a CIPSEND=<connectionId>,<data> command
*
*/
void sendCIPData(int connectionId, String data)
{
   String cipSend = "AT+CIPSEND=";
   cipSend += connectionId;
   cipSend += ",";
   cipSend +=data.length();
   cipSend +="\r\n";
   //sendCommand(cipSend,1000,DEBUG);
   sendCommand(cipSend,500,DEBUG);
   //sendData(data,1000,DEBUG);
   sendData(data,500,DEBUG);
   //Serial.println("responce sent");
}
 
/*
* Name: sendCommand
* Description: Function used to send data to ESP8266.
* Params: command - the data/command to send; timeout - the time to wait for a response; debug - print to Serial window?(true = yes, false = no)
* Returns: The response from the esp8266 (if there is a reponse)
*/
String sendCommand(String command, const int timeout, boolean debug)
{
    String response = "";    
    esp8266.print(command); // send the read character to the esp8266
    long int time = millis();
    //while( (time+timeout) > millis()){
      while(esp8266.available())
      {
        // The esp has data so display its output to the serial window 
        char c = esp8266.read(); // read the next character.
        response+=c;
        delay(200);
      }  
    //}
    if(!debug)
    {
      Serial.print(response);
    }
    return response;
}

