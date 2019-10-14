QuerySearchOnly.java -- 
search takes as input an origin city, a destination city, a Boolean value to indicate if the user wants to search only for direct flights or if one stop is ok, the date (just day of the month, since our dataset comes from July 2015), and the  maximum number of itineraries to be returned. For one hop flights, different carriers can be used for the flights.
The command returns a list of itineraries sorted by ascending total actual_time. If two itineraries have the same time, then  returns the iteinerary with smaller flight ID.

Query.java --
The following are the functions are included for the flight service system:

create: takes in a new username, password, and initial account balance as input. It creates a new user account with the initial balance. It returns an error if negative balance provided, or if the username already exists. Usernames and passwords are checked case-insensitively. 


login: takes in a username and password, and checks that the user exists in the database and that the password matches.


search: See QuerySearcOnly.java


book: lets a user book an itinerary by providing the itinerary number as returned by a previous search.
The user must be logged in to book an itinerary, and must enter a valid itinerary id that was returned in the last search that was performed within the same login session. Once the user logs out (by quitting the application), logs in (if they previously were not logged in), or performs another search within the same login session, then all previously returned itineraries are invalidated and cannot be booked.
If booking is successful, then assign a new reservation ID to the booked itinerary. Each reservation can contain up to 2 flights (in the case of indirect flights), and each reservation has a unique ID that incrementally increases by 1 for each successful booking.


pay: allows a user to pay for an existing reservation.
It first checks whether the user has enough money to pay for all the flights in the given reservation. If successful, it updates the reservation to be paid.


reservations: lists all reservations for user.
Each reservation has a unique identifier in the entire system, starting from 1 and increasing by 1 after a reservation has been made.
The user must be logged in to view reservations. The itineraries is displayed using similar format as that used to display the search results, and they are shown in increasing order of reservation ID under that username. Cancelled reservations are not displayed.


cancel: lets a user to cancel an existing reservation. The user must be logged in to cancel reservations and must provide a valid reservation ID. 


quit leaves the interactive system and logs out the current user (if logged in).
