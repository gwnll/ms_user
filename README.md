[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) 

# TourGuide

TourGuide is a Spring Boot application that has been a centerpiece in TripMasters app portfolio. It allows users to see tourist attractions and to get package deals on hotel stays and admission to various attractions.
***
## Modules complémentaires
- [Module Rewards](https://github.com/gwnll/ms_rewards)
- [Module GPS](https://github.com/gwnll/ms_gps)
## Technologies
- Java
- Gradle
- Spring 
- Docker
## Endpoints
#### POST http://localhost:80/addUser
> add user to internalUserMap for testing
> ``@RequestBody User``
#### POST http://localhost:80/setUserPreferences
>  ``@RequestBody UserPreferences userPreferences, @RequestParam String userName``
#### GET http://localhost:80/getUserPreferences
> ``@RequestParam String userName``
#### GET http://localhost:80/getAllUsers
> No parameter needed
#### GET http://localhost:80/getTripDeals
> ``@RequestParam String userName``
#### GET http://localhost:80/getLocation
> ``@RequestParam String userName``
#### GET http://localhost:80/getRewards
> ``@RequestParam String userName``
#### GET http://localhost:80/getNearbyAttractions
> get the 5 nearest attractions depending on user's current location
> ``@RequestParam String userName``
#### GET http://localhost:80/getAllCurrentLocations
> get all current locations from all users
