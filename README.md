
# NGO Management Application

This is an application that creates a social network, and a platform for Non Profit Organizations 
to manage schedules, volunteers, future events along with keeping track of all the expenditures and rewards.

## UBC CPSC 210 Term Project

### Phase 1 :

For UBC Computer Science's "Software Construction" course (CPSC 210), students are required 
to design a Java desktop application. I decided to go wih a managing application that would help NGO's as 
well as people who are interested in starting or working for one by keeping a database of all the NGO's, 
their owners, their staff, volunteers, donors, work timings as well as applications for volunteering.

### It can be used by 3 types of users:
* NGOs/NGO Founders: These will be people who want to start an NGO.
* NGO Staff: This will consist of all the permanent volunteers/applicants/executives of the NGO.
* Donors: These are people who are interested in donating funds in a particular NGO. They will be able to 
search for them and their cause and donate accordingly.(TO BE IMPLEMENTED)

#### The following are in depth descriptions of each role and its tasks:

* Founder:
    * Creating a Member with a Name, Age, City, Investment
    * Creating an organization with a Name, City, Fund, Number of Hours 
    required, number of Volunteers and Purpose
    * Creating Events in the organization using investment funds while assigning
    one head executive volunteer to take charge of the event
    * Accepting Volunteer applicants based on their fulfillment of
    the requirements
    * Assigning accepted volunteers to Events 
    * Promoting volunteers to Executive Volunteers based on the number of
    hours they have worked on, and the number of events they have been assigned.
    
* Volunteer (Applicant):
    * Creating a Member with a Name, Age, City, and Time Commitment
    * Looking for Organizations to apply for by searching on the basis of
    Name, City, Hours Required, and Purpose
    * Applying based on time requirement
    * Once accepted by the founder, looking for events and joining
    * Getting rewards by hourly rate as well as on the basis of number of events
    * Being eligible for promotions once they've worked for a designated number of 
    hours and taken part in a particular number of events.
    * Displaying events, organization details, hours worked, along with rewards etc.
   
* Volunteer (Executive):
    * Getting paid higher in comparison to the volunteers
    * Being assigned as head for events and managing the events
    * Displaying events, and other stats related to hours worked, organization and rewards.

## Phase 2 (Data Persistence) :

* As a Founder, I can save my user as a JSONObject in the database with my name, age, city, investment and organization
* As a Volunteer, I can save my user as a JSONObject in the database with my name, age, city, time commitment,
    hours worked, number of events, status and a boolean for Executive Position
* All the Founders, Volunteers and Organizations are saved as separate JSONArrays inside the database
* As a Founder and Volunteer, I also have the ability to load my data from the database to a state he/she left it at

---
## Phase 3 (GUI) :

##### Main Menus:
* As a user, I can load and save my database from the menu bar which is visible from every menu of the database manager.
    I am also prompted an alert hen i try to load from the database to avoid unwanted overrides. 
    
* For the Audio/Visual components, I've added sounds for different buttons/ menu bar items and error messages along with
    an image icon on the main menu

* The information about each volunteer, founder, organization, event etc is displayed in their individual menus

* I've also included a search for users(volunteers and founders) during login, events and organizations during applying 
    and added the required checks.
    
* The user can hop on from main menus to sub menus and come back to te previous ones using back buttons. There are a total
    of 15 menus included in the program, each with different properties.

##### Submenus: 
* Creation Menus: This menu has founder/volunteer text input fields along with buttons to create the users
                                or clear the fields, similarly for event creation as well.
* Display Menus: These menus have a list panel which display the objects which are selectable. These also 
                                have buttons to go further into user specific menus or to go back.
* Main Function Menus: These menus give the users to choose from a set of actions based on the type of user.
                                    These further lead to more function menus, display menus or creation menus etc. 
                                    
### Phase 4 (UML Diagram and Design) :
#### Task 2 :
 I chose to make one of my classes robust by having the methods throw some exceptions
and further tested out the exceptions. The following are the methods along with their classes:
* Class: Founder
    * Methods and Exceptions:
        * promoteVolunteer() : NotPromotableException
        * createEvent() : NotEnoughFundsException
        * addVolunteerToEvent() : EventCapacityFullException
* Class: Organization
    * Methods and Exceptions:
        * addApplicant() : NotEnoughTimeException

#### Task 3 : 
#####If given the chance to improve the design of my project, I would have refactored and made the following changes:
* Having another abstract class Member which would be extended by both Founder and Volunteer.
* Using the map interface for the applications in the organization class so that the applicant along with his/ her 
is saved in the same mapped object of type <Volunteer,Status>.
* Avoiding repetition in the display menus by method overloading and having different implementations on the basis
of the parameters its called with.
           # NGO_Management_System
