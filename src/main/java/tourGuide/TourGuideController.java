package tourGuide;

import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.UserCurrentLocation;
import tourGuide.dto.VisitedLocation;
import tourGuide.service.UserService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TourGuideController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @GetMapping("/getUserPreferences")
    public UserPreferences getUserPreferences(@RequestParam String userName) {
        User user = userService.getUser(userName);
        UserPreferences userPreferences = user.getUserPreferences();
        return userPreferences;
    }

    @PostMapping("/setUserPreferences")
    public UserPreferences setUserPreferences(@RequestBody UserPreferences userPreferences, @RequestParam String userName) {
        User user = userService.getUser(userName);
        user.setUserPreferences(userPreferences);
        return userPreferences;
    }

    @GetMapping("/getUserRewards")
    public List<UserReward> getUserRewards(@RequestBody User user) {
        return userService.getUserRewards(user);
    }
    @GetMapping("/getUserLocation")
    public VisitedLocation getUserLocation(@RequestBody User user) {
        return userService.getUserLocation(user);
    }

    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = userService.getUserLocation(userService.getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(userService.getUserRewards(userService.getUser(userName)));
    }

    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = userService.getTripDeals(userService.getUser(userName));
        return JsonStream.serialize(providers);
    }

    @GetMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        User user = userService.getUser(userName);
        VisitedLocation visitedLocation = userService.getUserLocation(user);
        return JsonStream.serialize(userService.getNearByAttractions(visitedLocation, user));
    }

    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        List<UserCurrentLocation> allCurrentLocations = userService.getAllCurrentLocations();
        return JsonStream.serialize(allCurrentLocations);
    }


}