# Serp task

The task:
A SERP (search results screen) with tours request is needed to be done and shown in a result list. The tour consists of
flight and hotel, and for one hotel there may be several different flight options.
For simplicity, dates, destinations and return flight are not taken into account.
By clicking on the refresh button, or when the application starts, display the search result
in the form of cards on which the name of the hotel will be written and the minimum price for
hotel including the flight cost.
When clicking on the card, show the dialog or popup menu with the choice of the option
flight and final price with this flight.
Optional: Add filter by airline.
Appearance:
No design requirements, an example of the design is shown below.
Data:


List of flights from: https://api.myjson.com/bins/zqxvw
List of hotels can be obtained from: https://api.myjson.com/bins/12q3ws
The list of airlines can be obtained from: https://api.myjson.com/bins/8d024

 
 The task was solved usin Kotlin languaje, GSON and Retrofit2 libraries
 CVVM pattern
 
 
UI result 
https://raw.githubusercontent.com/Xaviervu/SERPtask/master/screenshots/1.png

https://raw.githubusercontent.com/Xaviervu/SERPtask/master/screenshots/2.png
