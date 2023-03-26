# My Med App !

### A CPSC 210 Project

### Project Overview

<p>The design goal of this project entails implementing a working application that enables users to efficiently track 
personal prescription data over time with the aim of improving medicine adherence. Overall, the interface aims to aid a user demographic of patients that 
regularly take medication or utilize routine medical practices (eg. diabetes therapy/injection) with independent
administration of medication and monitoring; data abstraction includes representing general information including dosage, 
precautions and potential side effects of prescriptions, as well as personal tools that the user can utilize to 
track the quantity of their prescription over time and symptoms that can be recorded for future reference.
Holistically, the program will be able to represent the patient user's personal information inclusive of diagnoses, preferred
pharmacy/healthcare provider, overall list of prescriptions, prescription history and schedule of medications 
to be administered in the next 24-hour period. Within the list of prescriptions, each individual instance of 
medicine/prescription will include attributes such as treatment overview (purpose and usage), quantity of prescription, 
future refill dates, historical refill dates and the ability to customize input of the amount of treatments left. 
Potential methods will allow the patient to indicate whether they missed a dose or administered multiple amounts within
the scheduled timeframe, enabling respective incrementation up or down of medicine quantity. The list of prescriptions
will also include methods allowing a patient to update the list, while the scheduling class will include capacities to
schedule pills according to specified dosage times (eg. every twelve hours, every 24 hours), accept input from the user 
regarding time dosage was taken, inform the patient of the last time a pill was administered and reset a 
prescription schedule depending on circumstances when dosages are not followed. 

### Personal Input 
<p>The idea for this project was originated from my personal difficulties with remembering to take my prescribed
medication on a daily schedule. Often, I struggle with recalling whether I took a pill at a certain time, and 
quantifying the amount of pills I have left to take and when to request my next refill. This application aims to aid 
similar individuals who have trouble with self-monitoring prescription medication through conveniently providing 
information on schedule, inventory and dosage instructions.</p>

## User Stories

###### As a user, I want to be able to:

* Add all of my prescriptions (X) to my patient account(Y):
  * Inclusive of being able to make a new prescription and add it to the list of prescriptions

* Remove a prescription from the list of prescriptions in a patient account 

* View attributes for a patient (Y) inclusive of:
  * list of prescriptions (listOfX)
  * daily dosage schedule (inclusive of every prescription (X) in prescription list)
  * symptoms

* View attributes of each prescription (X) inclusive of: 
  * imprint id
  * number of pills left in the bottle
  * treatment instructions (eg. split pill, ingest orally, before meal, before sleep)
  * schedule to take dosage 
  * buffer time between dosages
  * frequency of dosages per day 
  * last prescription refill date 
  * expected next prescription refill date 
  
* Edit/change the dosage schedule for each individual prescription (X)

* Choose to save the holistic state of my prescription account, inclusive of prescriptions and their individual
  attributes to file

* Access and reload the state of my prescription account, inclusive of their individual attributes from file 
  * (eg. schedule, dosage amount etc.) 


//sources cited:
https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
https://docs.oracle.com/javase/8/docs/api/java/time/LocalTime.html
https://www.educative.io/answers/how-to-convert-a-double-to-int-in-java
https://dencode.com/en/date/iso8601
http://stleary.github.io/JSON-java/index.html