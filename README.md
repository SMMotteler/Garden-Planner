# Garden-Planner
Original App Design Project - README
===

# Garden Planner

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app allows you to plan out a garden and gives you reminders for maintenance - ex. when to plant or harvest - through push notifications.

### App Evaluation
- **Category:** Lifestyle/Productivity
- **Mobile:** App uses push notifications to remind the user to tend to their garden; app uses current location to determine frost dates for when to plant crops; might allow for photos to show the growth of the garden in a stretch feature.
- **Story:** Helps both new and experienced gardeners plan out their gardens and gives reminders for keeping up the garden.
- **Market:** People who want to start gardening and people who currently enjoy gardening.
- **Habit:** The app uses push notifications to remind the users to keep up with their garden and to check in to confirm that they've completed these activities.
- **Scope:** The scope is relatively focused to avoid being too complicated - the main functionality is to add garden beds and add plants to these garden beds, then to give reminders based on these plants.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can register an account
* User can log in
* User can view current garden beds.
* User can create a garden bed.
* User can view a detailed view of a garden bed.
* User can enter "edit mode" for a garden bed.
* User can add a plant to a garden bed based on a list of crops.
* User can view all reminders for all beds.

**Optional Nice-to-have Stories**

* User can view their profile, including stats on how long they've used the apps, the crops they've planted, etc.
* User can view a timeline of a garden bed's growth.
* User can view the profiles of other users.
    * User can see other users' garden beds.
* User can access a "recommended" crops list (based on location and perhaps surrounding crops in a bed).
* Users can receive push notifications on their beds.

### 2. Screen Archetypes

* Login
   * User can log in.
* Register
   * User can register an account.
* Stream 1
    * User can view current gardens.
* Stream 2
    * User can view all reminders for gardens.
* Detail 1
    * User can view a detailed view of a garden.
* Detail 2
  * User can enter "edit mode" for a garden bed, including actions for renaming current plants, adding plants to the garden, and deleting plants from the garden.
* Detail 3
  * User can change the photo for their profile/garden (depending on what fragment comes before this detail view).
* Creation 1
    * User can create a garden bed.
* Profile 1
    *  User can view their profile and see how long they have used the app.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Gardens
* Reminders
* Profile

**Flow Navigation** (Screen to Screen)

* Login
   * Stream 1 - Current Gardens
   * Register
* Register
   * Stream 1 - Current Gardens
* Stream 1 - Current Gardens
    * Detail 1 - Detailed Garden View
    * Creation 1 - Garden Creation
* Stream 2 - Calendar of Reminders
    * Detail 1 - Detailed Garden View (of the bed that the reminder pertains to)
* Detail 1 - Detailed Garden View
    * Detail 2 - "Edit Mode"
    * Detail 3 - Change Garden Photo
* Detail 2 - "Edit Mode"
    * Detail 1 - Detailed Garden View

* Creation 1 - Garden Creation 
    * Stream 1 - Current Gardens

* Profile 1 - Profile
    * Detail 3 - Change Profile Picture Photo
## Wireframes
Navigation Diagram

<img src="https://i.imgur.com/qj0F4XA.jpg" width=600>

Detailed Wireframes

<img src="https://i.imgur.com/M4G2qk5.jpg" width=600>
<img src="https://i.imgur.com/Cqtaioy.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema
### Models
**User**
| Property | Type | Description |
| --- | --- | --- |
| objectID | String | the ID internally associated with the user |
| createdAt | Date | the time that the User created their account |
| email | String | the email associated with the user |
| username | String | the username associated with the user |
| password | String | the password associated with the user |
| profilePic | file | the photo associated with the user's profile |
| installationID | String | the installationId of the Installation associated with the user |

**Bed**
| Property | Type | Description |
| --- | --- | --- |
| objectID | String | the ID internally associated with the bed |
| createdAt | Date | the time that the User created the bed |
| name | String | the name associated with the bed |
| length | Number | how long the bed is (in feet) |
| width | Number | how wide the bed is (in feet) |
| latitude | Number | the latitude of the bed's location |
| longitude | Number | the longitude of the bed's location | 
| hardinessZone | String | the hardiness zone where the bed is located |
| bedSpots | Array of Array (2-D Array) of Pointers to BedSpot objects | the bed spots of the bed |
| reminders | ArrayList of Pointers to Reminder objects | the reminders pertaining to the beds that the user created |

TODO: add BedSpot, Plant, and Reminder models
### Networking
* Login Screen
  * (Read/GET) Get user information based on login information
* Register Screen
  * (Create/POST) Create a new user object
* Current Garden Bed Screen
  * (Read/GET) List out the current user's garden beds
* Reminders Screen
  * (Read/GET) List out the current user's reminders
* Detailed View of Garden Bed Screen
  * (Read/GET) Display the current bed's bed spots
* Edit Mode of Garden Bed Screen
  * (Read/GET) Display the state of the current bed's bed spots
  * (Edit/POST) Change the properties of the current bed's bed spots
* Creation of Garden Bed Screen
  * (Create/POST)
* Plant Addition Screen
* Plant Detail Screen
* Profile Screen

- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
