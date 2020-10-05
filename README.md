# cs0320 Term Project 2020

**Team Members:** Jefferson Bernard, Victor Kalev, Jake Nieto, Dylan Brady

**Team Strengths and Weaknesses:** 
* Jefferson Bernard
  * Strengths
    * Good at identifying bugs in programs
    * Good communication skills
    * Understands how to think from the client’s perspective
    * Willing to take risks to better project
  * Weaknesses: 
    * UI/UX Design; especially accounting for accessibility
    * Following through on ambitious ideas
    * Reaching out and networking for the program’s benefit
* Victor Kalev
  * Strengths
    * Good at working with others to reach a product that everyone enjoys
    * Willing to spend extra time for a more thoughtful user experience
    * Flexible work schedule
    * Places the client’s needs over personal preference
  * Weaknesses: 
    * Sometimes overly ambitious goals for the product
    * Can take a little too much time to visualize concepts / code
    * Still learning HTML / CSS / JS
* Dylan Brady
  * Strengths
    * Good at working with others and team player
    * Flexible schedule
    * I feel like I think outside the box
  * Weaknesses
    * Gets caught up on some of the small details
    * Not incredibly familiar with Java
* Jake Nieto
    * Strengths
      * Good at working with others 
      * Enjoys debugging  
      * Realistic when it comes to ideas so that they are executable
      * Planning
    * Weaknesses:
      * Busy schedule 
      * Rigid when it comes to algorithmic design
      * Poor HTML, CSS, and JS skills 

**Project Idea(s):** _Fill this in with three unique ideas! (Due by March 2)_
### Idea 1: CovCal 
_Approved - I really like the idea of using a priority queue for this_

Getting someone to cover your shift at work can be a big challenge. It always seems like no one is willing to take your shift, even Sarah whom you covered 3 times last month. Our idea is to create a web app that allows co-workers to create a group so they can request times for people to cover their shifts. It would take on a similar style to when-to-meet and google calendar. A core feature would be that it keeps track of the number of times you've covered shifts for certain people. Additionally, all people that you have helped in the past that still haven't covered one of your shifts, would receive email notifications that you need someone to cover your shift. Hopefully this would make it so that co-workers will feel more inclined to cover shifts of workers who have helped them in the past. 

Another core feature would be the “coverage request priority queue”. Each worker would be given a “rank” that is dependent on the number of shifts they have covered in the past.  To incentivize workers to cover the shifts of “more helpful workers” (workers that have covered many shifts) , the rank of each worker would be calculated using a “Page Rank” like algorithm. Thus a user’s rank is not only dependent on the number of shifts that he/she has covered, but also the rank of the individuals that he/she has covered as well. A worker would be incentivized to cover the shift of someone at the top of the priority queue because then his/her rank will increase by a larger amount than if he/she helped someone at the bottom; now the next time that he/she needs coverage, he/she will have a high rank value and thus be displayed at the top of the  “coverage request priority queue”. 

Features:
* Dynamically updating calendar 
* Email notifications to users who still "owe" you a shift.
* Potential high priority feature when a user REALLY needs a shift covered.
* PageRank based “coverage request priority queue”.
* History chart that displays the total number of shifts that all workers have covered.

Challenges:
* Getting a server and database working so that all users can have access to the same calendar.
* UX/UI design so that it parallels the simplicity and ease of google calendars.
* Connecting it to whatever current program the manager uses to schedule employees.
* Getting users to want to use this because a lot of times people like to get their shifts covered but never cover other peoples.

Potential Additional Features:
  * Smart notifications: Only notify individuals who aren’t already working at the same time that someone needs coverage. 
  * G-Cal extension: So you can overlay your G-Cal on top of the coverage calendar.
  * Slack extension


### Idea 2: EventMap
_Rejected - this would be hard to test and Facebook events does actually do this_

An event can really have a lot of effect on the relationships you make with people, and the experiences you have in life. A big problem I see with applications, such as facebook events and instagram, is that there really is not a great way to expand your circle naturally.

I propose that a party and event planning application would be much more useful if it was centered around a map of your location. The host of the event would create the event, and send out invites to anybody in their friends list. Each host would have their own rating and reviews based on the opinion of past guests to their events. Additionally, there would be a group system. It would be possible to create a group, for example a “chess club” or “Brown University Fencing Team” and pick a location that your group is based out of. These groups could act as hosts themselves and even have other groups that they are associated with. If an event were to be created, let's say a “pick-up rugby tournament”, the host could look at the map and see what groups are near their event’s location. For example, if this rugby tournament was held on the main green, and “Brown Beekeeping Society” was based out of the Faunce building, the host could see them on the map and invite the group as a whole. This geolocational focus of the application has been used in many recent social medias and has proven to greatly increase user retention. Tik Tok and Snapchat are two examples of this.

During the event, the host will be able to customize what live statistics of the event the invited users can see. For example, the host of the rugby tournament may want the total number of how many people have come to the event (This will be calculated using the GPS feature of a phone, similar to snapchat). This will give people a good idea of what the event is currently like before even deciding to go!

After the event, the host is able to customize a review notification to the users that attended the event. This will be similar to uber/lyft, but it will be optional to the host, and will be much more customizable due to the wider array of events.

Features:
* Links to instagram in order to get recommended friends/groups
* Host events
* Create and join groups
* Algorithms to recommend friends and groups you may like
* A feed of your close friends and what they are planning to attend, and their reviews
Challenges:
* The GPS aspect will be tough
* Having a large variety of statistics on the events
* Location privacy is a problem (although the location features are optional)

## CovCal Project Documentation
Project Specification: https://tinyurl.com/covcalspecs

Project Mockup: https://tinyurl.com/covcalmockup

Project Presentation Slides: https://tinyurl.com/covcalslides

**Mentor TA:** Jeffrey Kennan (jeffrey_kennan@brown.edu)

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 13)_

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule for on or before April 29 once you are assigned an adversary TA)_

## How to Build and Run

_Installing Node.js_

If you enter the `npm` command into the terminal and do not get a 'command not found' error then you should skip this section.

To download a pre-built installer for Node.js, follow the installation instructions here:
https://nodejs.org/en/download/

(Optional: You can use the following link to install `nvm` node version manager. This will allow you to easily switch between node versions and install/upgrade versions more seamlessly than other installation methods.)
https://github.com/nvm-sh/nvm/blob/master/README.md#installation-and-update

Once that's installed, your `npm` (and potentially `nvm`) command(s) should be working.

_After Cloning CovCal_

Once you have cloned the CovCal repository, `cd` into the directory and run `npm install`. This sets up all node modules and packages needed to execute our project.

Backend:
To run our Spark Java server, open up a new tab in your terminal and enter the command `mvn package`, followed by `./run`.

Frontend:
While the Spark server is running in the background, return to your original tab in the terminal and run `npm start` to build the project. It will automatically open a new window in your current browser and direct you to the CovCal signin page.
