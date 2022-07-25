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

* User can register an account.
* User can log in.
* User can view current gardens.
* User can create a garden.
* User can view a detailed view of a garden bed.
* User can enter "edit mode" for a garden bed.
* User can add a plant to a garden bed based on a list of crops.
* User can view all reminders for all beds.

**Optional Nice-to-have Stories**

* User can view their profile and change their profile picture.
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
| profilePic | File | the photo associated with the user's profile |
| installationID | String | the installationId of the Installation associated with the user |

**Garden**
| Property | Type | Description |
| --- | --- | --- |
| objectID | String | the ID internally associated with the garden |
| createdAt | Date | the time that the User created the garden |
| location | String | the address where the garden is located |
| photo | File | the photo associated with the Garden |
| whoseGarden | pointer to User object | the User who created the garden |
| name | String | the name associated with the bed |
| latLong | GeoPoint | the latitude/longitude coordinates where the garden is located |
| lastFrostDate | Date | the date of the last frost date of the location where the garden is located |

**Plant**
| Property | Type | Description |
| --- | --- | --- |
| objectID | String | the ID internally associated with the plant|
| name | String | the name associated with the plant |
| photo | File | the photo associated with the plant |
| recommendStartAsSeed | boolean | the app's recommendation on whether the plant should be planted as seeds or as a seedling - True for planted as seeds, False for planted as a seedling |
| weeksBeforeAfterFrostPlant | Number | the number of weeks after (before if the number is negative) the last frost date that the plant should be planted outside |
| plantingAdvice | String | advice on planting the specific kind of plant |
| weeksUntilHarvest | Number | the number of weeks after planting that it will take for the plant to be ready to harvest |
| harvestAdvice | String | advice on harvesting the specific kind of plant |

**PlantInBed**
| Property | Type | Description |
| --- | --- | --- |
| objectID | String | the ID internally associated with the plant |
| PlantType | Pointer to Plant object | the type of Plant that the PlantInBed object is |
| ThisPlantName | String | the display name associated with the PlantInBed |
| plantedInBed | Pointer to Garden object | the Garden object that the PlantInBed object is "planted" in and contained by |
| whenShouldPlant | Date | the date that the PlantInBed object should be planted, based off of the lastFrostDate Garden parameter and the weeksBeforeAfterFrostPlant Plant parameter |
| whenActuallyPlanted | Date | the date that the PlantInBed object is actually planted, represented by when the reminder that is associated with the planting of the PlantInBed object is completed |
| whenShouldHarvest | Date | the date that the PlantInBed object should be harvested, based off of the whenActuallyPlanted parameter and the weeksUntilHarvest Plant parameter |
| whenActuallyHarvested | Date | the date that the PlantInBed object is actually harvested, represented by when the reminder that is associated with the harvesting of the PlantInBed object is completed |


| photo | File | the photo associated with the plant |
| recommendStartAsSeed | boolean | the app's recommendation on whether the plant should be planted as seeds or as a seedling - True for planted as seeds, False for planted as a seedling |
| weeksBeforeAfterFrostPlant | Number | the number of weeks after (before if the number is negative) the last frost date that the plant should be planted outside |
| plantingAdvice | String | advice on planting the specific kind of plant |
| weeksUntilHarvest | Number | the number of weeks after planting that it will take for the plant to be ready to harvest |
| harvestAdvice | String | advice on harvesting the specific kind of plant |
TODO: add  Plant, and Reminder models
### Networking
* Login Screen
  * (Read/GET) Get user information based on login information
* Register Screen
  * (Create/POST) Create a new user object
* Current Garden Bed Screen
  * (Read/GET) List out the current user's gardens
* Reminders Screen
  * (Read/GET) List out the current user's reminders
  * (Delete/POST) Complete a reminder and delete it
  * (Create/POST) Generate a 'harvest' reminder when a 'plant' reminder is completed
* Profile Screen
  * (Read/GET) Display information about the current user
* Detailed View of Garden Screen
  * (Read/GET) Display the details of the current garden
  * (Read/GET) Display the reminders pertaining to the current garden
  * (Delete/POST) Complete a reminder and delete it
  * (Create/POST) Generate a 'harvest' reminder when a 'plant' reminder is completed
* Edit Mode of Garden Bed Screen
  * (Read/GET) Display the garden's current PlantInBed object
  * (Edit/POST) Change the name of a current PlantInBed object
  * (Create/POST) Add a new PlantInBed object to the garden
  * (Delete/POST) Delete a current PlantInBed object
* Creation of Garden Bed Screen
  * (Create/POST) Create a new Garden object
* Change Photo Screen
  * (Edit/POST) Replace the current profile picture/garden photo

- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]