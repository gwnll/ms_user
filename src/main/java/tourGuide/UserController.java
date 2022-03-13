package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.dto.VisitedLocation;
import tourGuide.service.UserService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getUserRewards")
    public List<UserReward> getUserRewards(@RequestBody User user) {
        return userService.getUserRewards(user);
    }
    @GetMapping("/getUserLocation")
    public VisitedLocation getUserLocation(@RequestBody User user) {
        return userService.getUserLocation(user);
    }

    @GetMapping("/getUser/{userName}")
    public User getUser(String userName) {
        return userService.getUser(userName);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

}
