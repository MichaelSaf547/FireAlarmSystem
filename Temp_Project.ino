#include <SimpleDHT.h>
#define LED 3
int pinDHT22 = 2;
SimpleDHT22 dht22;

void setup() {
  Serial.begin(9600);
  pinMode(LED, OUTPUT);
  pinMode(4,OUTPUT);
  Serial.setTimeout(50);
  delay(50);
}

void loop() {
  float temperature = 0;
  float humidity = 0;
  int err = SimpleDHTErrSuccess;
  if ((err = dht22.read2(pinDHT22, &temperature, &humidity, NULL)) != SimpleDHTErrSuccess) {
    Serial.print("Read DHT22 failed, err="); Serial.println(err);delay(2000);
    return;
  }
  
  Serial.println((int)temperature);
  Serial.println((int)humidity);
  
  if(temperature >= 25.0)
  {
  digitalWrite(4,HIGH);
  digitalWrite(LED, HIGH);
  delay(500);
  digitalWrite(4,LOW);
  digitalWrite(LED, LOW);
  delay(500);
  }
  delay(500);
}
