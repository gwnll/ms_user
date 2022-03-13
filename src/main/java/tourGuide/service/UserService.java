package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.dto.*;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardsCentralProxy;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final GpsUtilProxy gpsUtilProxy;
    private final RewardsCentralProxy rewardsCentralProxy;
    private final TripPricer tripPricer = new TripPricer();
    public final Tracker tracker;
    boolean testMode = true;

    public UserService(GpsUtilProxy gpsUtilProxy, RewardsCentralProxy rewardsCentralProxy) {
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardsCentralProxy = rewardsCentralProxy;

        if(testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
        return visitedLocation;
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    public List<User> getAllUsers() {
        return internalUserMap.values().stream().collect(Collectors.toList());
    }

    public void addUser(User user) {
        if(!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public List<Provider> getTripDeals(User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    public VisitedLocation trackUserLocation(User user) {
        VisitedLocation visitedLocation = getUserLocation(user);
        user.addToVisitedLocations(visitedLocation);
        rewardsCentralProxy.calculateRewards(user);
        return visitedLocation;
    }

    public List<NearbyAttraction> getNearByAttractions(VisitedLocation visitedLocation, User user) {
        List<NearbyAttraction> nearbyAttractions = new ArrayList<>();
        for (Attraction attraction : gpsUtilProxy.getAttractions()) {
            Location location = visitedLocation.location;
            double distance = gpsUtilProxy.getDistance(attraction.latitude, attraction.longitude, location.latitude, location.longitude);
            NearbyAttraction nearByAttraction = new NearbyAttraction(attraction.attractionName, attraction, visitedLocation.location, distance, rewardsCentralProxy.getRewardPoints(attraction.attractionId.toString(), user.getUserId().toString()));
            nearbyAttractions.add(nearByAttraction);
        }
        List<NearbyAttraction> sortedNearbyAttractions = nearbyAttractions.stream()
                .sorted(Comparator.comparing(NearbyAttraction::getDistance))
                .limit(5)
                .collect(Collectors.toList());

        return sortedNearbyAttractions;
    }

    public List<UserCurrentLocation> getAllCurrentLocations() {
        List<UserCurrentLocation> userCurrentLocations = new ArrayList<>();
        List<User> users = getAllUsers();
        for (User user : users) {
            UUID userID = user.getUserId();
            VisitedLocation userLocation = user.getLastVisitedLocation();
            UserCurrentLocation userCurrentLocation = new UserCurrentLocation(userID.toString(), userLocation.location);
            userCurrentLocations.add(userCurrentLocation);
        }
        return userCurrentLocations;
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    private final Map<String, User> internalUserMap = new HashMap<>();
    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);

            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i-> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
