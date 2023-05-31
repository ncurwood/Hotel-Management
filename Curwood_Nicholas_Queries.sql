/* Scenario 1: Clock In/ Clock Out employees*/
INSERT INTO Shifts (`Employee-ID`, Date, `Start Time`)
VALUES (1, '05/21/2023', '08:00');
COMMIT;

UPDATE Shifts
SET `End Time` = '05:00'
WHERE `Employee-ID` = 1;
COMMIT;
/* Scenario 2: Make a Booking (For a new guest)*/
INSERT INTO Guest (`Guest Card Number`, `Guest First Name`, `Guest Last Name`)
VALUES ('G003', 'Jefferey', 'Greene');
COMMIT;

INSERT INTO Booking (`Room Number`, `Building-ID`, Date, `Guest Card Number`, `Computer-ID`, `Employee-ID`, `Package Name`)
VALUES (102, 'B1', '05/21/2023', 'G003', 1, 3, 'Deluxe');
COMMIT;

/* Scenario 3: Cancelling a package and a booking*/
UPDATE Booking
SET `Package Name` = 'Standard'
WHERE `Building-ID` = 'B1' AND Date = '05/21/2023' AND `Room Number` = 102;
COMMIT;

DELETE FROM Booking
WHERE `Building-ID` = 'B1' AND Date = '05/21/2023' AND `Room Number` = 102;
COMMIT;

/* Scenario 4: Update Room Status After Checking Out */
UPDATE Room
SET `Room Status` = 'Unready'
WHERE `Room Number` = 102 AND `Building-ID` = 'B1';
COMMIT;

/* Scenario 5: Cash in Loyalty Points */
UPDATE Loyalty_Program
SET `Loyalty Points` = 0
WHERE `Guest Card Number` = 'G001';
COMMIT;

/* Scenario 6: Update Personal Information*/
UPDATE Employee
SET `Employee Phone` = '111-111-1111'
WHERE `Employee-ID` = 1;
COMMIT;

/* Scenario 7: Changing Shifts 
(Scenario: A mistake was made and the hours of two employees got mixed up,
Change the shifts so that the employees have the correct hours)*/
UPDATE Shifts
SET `Employee-ID` = 2
WHERE `Employee-ID` = 1 AND Date = '05/21/2023';
COMMIT;

-- Query 1: Find how soon the elevator license expires
SELECT MIN(`License Expiration`) AS `Earliest Expiration`
FROM Elevator;

-- Query 2: Find how many guests have a certain package
SELECT COUNT(*) AS `Guest Count`
FROM Booking
WHERE `Package Name` = 'Deluxe';

-- Query 3: Find the total guest capacity of all the restaurants combined
SELECT SUM(`Guest Capacity`) AS `Total Guest Capacity`
FROM Restaurant;

-- Query 4: Find how many guests have stayed in the building
SELECT COUNT(DISTINCT `Guest Card Number`) AS `Guest Count`
FROM Booking;

-- Query 5: Find how many employees are working on a given date
SELECT COUNT(*) AS `Employee Count`
FROM Shifts
WHERE Date = '05/18/2023';