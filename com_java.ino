#include <NewPing.h>

#define MAX_DESTANCE  50
#define STOP_DESTANCE 40
#define TRIG_PIN      10
#define ECHO_PIN      11

NewPing sonar(TRIG_PIN,ECHO_PIN,MAX_DESTANCE);


int StoreReading=0;

int Led=13;

void setup() 
{
  // put your setup code here, to run once:
  pinMode(Led, OUTPUT);
  digitalWrite(Led, LOW);
  Serial.begin(9600);
  Serial.setTimeout(50);
  delay(50);
}

void loop() 
{
  // put your main code here, to run repeatedly:
   StoreReading=sonar.ping_cm();
   delay(200);

   Serial.println(StoreReading);
   
  String text = Serial.readString();
  if(text[0]=='1')
  {
    digitalWrite(Led, HIGH); // Turn LED OFF
    delay(100);
  }
  else
  {
    digitalWrite(Led, LOW); // Turn LED OFF
    delay(100);
  }

}
