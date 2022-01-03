/*
 * ITI INTAKE 42 EMBEDDED SYSTEM 
 * JAVA COURSE 
 * GROUP 8
 * STUDENTS ARE 1-Michael Safwat Sobhy Nakhla --> "LEADER"
 *              2-Abdelrahman Mahmoud Mohamed Saleh
 *              3-Abdelrahman Omar Mohamed Shafik
 *              4-Mostafa Hani Imam
 *              5-mohamed maged abdrabuh
 * DATE IS 3 JAN 2022 
 */
/*We write this code to get the temperature and humidity values 
 * by using DHT22 sensor ,Arduino Nano,Buzzer , Red LED 220Oham Resistor also We link the Arduino IDE with Netbeans IDE 
 * Therefore,We display the result on GUI JAVA Software by using JAVAFX
 * First we defined the input and output pins,then using the serial module to display and read data from 
 * the serial monitor or the Gui.finally we checked our cases
 * 
*/
#include <SimpleDHT.h> //including the DHT sensor library to use it for geting the temperature and humidity values 

// configuration all pin numbers 
#define LED 3 // setting the LED on pin three
int pinDHT22 = 2; //setting the sensor on pin two 

/*
 * Decleration variables for Temperature , Humidity ,Error variables if the sensor failed to measure the metrics
 * Flag for changing the state of the system
 * IncomingByte to store the reading value from the Gui or the serial monitor
 */
float temperature ;
float humidity ;
int err = SimpleDHTErrSuccess;
int flag ;
char incomingByte ;
SimpleDHT22 dht22;

/*
 * This function will print the value of the temperature and humidity on the Gui or the serial monitor
 * Therefore we will read a char to take an action if we like
 */
void WriteAndRead_SerialMonitor()
{
  /*
   * Printing the value of Temperature
   */
  Serial.println((int)temperature); 
  
  /*
   * Printing the value of Humidity
   */
  Serial.println((int)humidity);
  incomingByte = Serial.read(); //it just for receiving a byte from the Gui or the serial monitor to change the state of the system
}

/*
 * This function to activate the alarm system 
 * by by flashing the buzzer and the Red LED
 */
 void Alarm()
 {
    digitalWrite(4, HIGH);
    digitalWrite(LED, HIGH);
    delay(500);
    digitalWrite(4, LOW);
    digitalWrite(LED, LOW);
    delay(500);
 }

void setup() {
  Serial.begin(9600); // we using the bandrate 9600 to transmit and receive data between Arduino and serial monitor
  pinMode(LED, OUTPUT); //set the led as an output 
  pinMode(4,OUTPUT); //set the buzzer as an output
  Serial.setTimeout(50); //sets the maximum milliseconds to wait for serial data.We put 50 milliseconds
  delay(50);
}

void loop() {
  
  /*
  * Intialization our variables 
  */
  temperature = 0;
  humidity = 0;
  err = SimpleDHTErrSuccess;
  flag = 0;
  incomingByte ='0';
  
  //Checking if the reading value from the sensor is an error we will make a delay for 2000 milliseconds
  
  if ((err = dht22.read2(pinDHT22, &temperature, &humidity, NULL)) != SimpleDHTErrSuccess) 
  {
     delay(2000);
  }
  
  /*
   * calling  WriteAndRead_SerialMonitor() to write and read from the serial monitor or the Gui
   */
  WriteAndRead_SerialMonitor();
  
  /*
   * checking the temperature value and the incomingByte 
   * if the temperature more than or equal 26 or the incomingByte equal one 
   * we will set the flag variable to one and calling the Alarm function
   */ 
  if (temperature >= 26.0 || incomingByte == '1')
  {
     flag = 1;
  }
  while(flag == 1)
  {
      Alarm(); // calling the Alarm function to activate the buzzer and red led
      if ((err = dht22.read2(pinDHT22, &temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
      {
         delay(500);
      }
      
    /*
     * calling  WriteAndRead_SerialMonitor() to write and read from the serial monitor or the Gui
     */
      WriteAndRead_SerialMonitor();
      
    /*
     * checking the incomingByte if it will equal two it will break the Alarm mode
     * by seting his value to three
     */
      if(incomingByte == '2')
      {
         flag = 3; 
      }
  }
  while(flag==3)
  {
     if ((err = dht22.read2(pinDHT22, &temperature, &humidity, NULL)) != SimpleDHTErrSuccess)
     {
        delay(500);
     }
     
  /*
   * calling  WriteAndRead_SerialMonitor() to write and read from the serial monitor or the Gui
   */
    WriteAndRead_SerialMonitor();
    
    if(incomingByte == '3')
    {
       flag=0;
    }   
  }/*
    *So the temperature and humidity reading could be taken continously every half a second and we syncrounus it with our Gui
    */
    delay(500);
  }



  
