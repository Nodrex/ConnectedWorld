#include <SoftwareSerial.h>

int bluetoothTx = 4;
int bluetoothRx = 5;

SoftwareSerial bluetooth(bluetoothTx, bluetoothRx);

void setup()
{
  //Setup usb serial connection to computer
  Serial.begin(9600);

  //Setup Bluetooth serial connection to android
  bluetooth.begin(115200);
  bluetooth.print("$$$");
  delay(100);
  bluetooth.println("U,9600,N");
  bluetooth.begin(9600);
}

void loop()
{
  //Read from bluetooth and write to usb serial

bool done =false;
  
  /*if*/while(bluetooth.available())
  {
    char toSend = (char)bluetooth.read();
    Serial.print(toSend);
    done = true;
    delay(100);
  }

  if(done){
    Serial.println("");
    done = false;
  }

  //Read from usb serial to bluetooth
  if(Serial.available())
  {
    char toSend = (char)Serial.read();
    //bluetooth.print(toSend);
  }
}
