
#define LIGHT_BULB 7
                        
void setup()
{
  pinMode(LIGHT_BULB,OUTPUT);  
}
 
void loop()
{
   digitalWrite(LIGHT_BULB, HIGH);
   delay(1000);
   digitalWrite(LIGHT_BULB, LOW);
}

