[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) 

# TourGuide
TourGuide is a Spring Boot application that has been a centerpiece in TripMasters app portfolio. It allows users to see tourist attractions and to get package deals on hotel stays and admission to various attractions.
***
## ms_user
#### ms_user is the main app of TourGuide. It depends on ms_rewards and ms_gps to work properly. You will find the links for those modules below.
### Modules complémentaires
- [Module Rewards](https://github.com/gwnll/ms_rewards)
- [Module GPS](https://github.com/gwnll/ms_gps)
### Architectural Diagram
![alt text](https://github.com/gwnll/ms_user/blob/main/diagramme_architecture.png?raw=true)
### Technologies
- Java 1.8 JDK (Java 8)
- Gradle 4.8.1
- Spring 2.1.6
- Docker
### Deployment with Docker
1) Build the 3 different jar (ms_user, ms_rewards and ms_gps) by using ``gradlew build``
2) Create the 3 different images with docker ``build -t (image_name)``
3) You can use ``docker-compose up -d`` in the root directory of ms_user to deploy all TourGuide microservices
### Endpoints
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
