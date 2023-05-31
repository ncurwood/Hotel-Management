Our Database is a basic Hotel Management System.
The Seven scenarios we implemented provided functionalities for:

* Clocking in/out

* Making a Booking

* Cancelling a Booking

* Updating a room's status

* Updating an Employee's personal phone

* Change shifts with another employee

* Cashing in Loyalty Points for guests for a free booking





To download the software:
Note: I used IntelliJ, however it may be a different process for other IDE's

1. Open a new or existing project and add each of the .java files to your development environment.

2. Add the profile.png to the project folder (not within a subfolder of the project, but the folder itself)

3. If you don't already have one connected, add the .jar file I provided to the project dependencies.
	* In IntelliJ, you can do this by following file -> Project Structure -> Module and adding it 	to the list of dependencies

4. Now we need to add the database, go to mySQL and add the database creation code as well as the practice inserts into the default port (3306). I have provided the relevant code in the submission for convenience.

5. Next, we need to add the datasource:
	* Open the database menu (in IntelliJ it is a tab on the right)
	* Next, add a new datasource with the mySQL driver
	* Name the database (recommended hotelDB), provide your username (typically "root") and 		password, and under the part that says Database and affects the URL, name it 			myHotelSystem
	* Before leaving this page, I recommend testing the connection in order to make sure 			everything is configured correctly. Click ok to apply changes

6. Finally, go into LoginPanel and around line 60 to 62, we define the url, username, and password.
	* Change the username and password to reflect the ones you used for the datasource.

7. Note: this was not a critical warning for me, however it may be necessary to inject the mySQL dialect into hotelSystem.







Tips to use the UI:

	If you feel comfortable maneuvering the practice data, feel free to skip this part. However I highly recommend taking a look at the tips.

	* The Login Page:
	The UI opens with a login page that will request an Employee-ID and password. You can grab this from the employee table, however you can also just use 
		- ID: 1, Password: pass123
	
	* When writing dates, remember to write them in the form:
		- YYYY-MM-DD
		- If this is not followed, the queries won't know what date to look for and skew the 			results

	* Guest Card Numbers:
		- G001
		- G002
		- You can also make up a card number for bookings and it will be added to the database

	* Each task updates or inserts data into the following tables
		- Clock In/Out: Inserts/Updates data in Shifts table
		- Make Booking: Inserts data in Booking, If the guest card is new, Guest and Loyalty Points
		- Cancel Booking: Deletes data from Booking
		- Update Room Status: Updates data in Rooms table
		- Cash In Loyalty Points: Updates data in Loyalty Points Table and Booking table
		- Update Personal Phone: Updates data in Employee table
		- Change Shifts: Updates data in Shifts table

