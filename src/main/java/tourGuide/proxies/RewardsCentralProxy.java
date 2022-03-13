package tourGuide.proxies;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.user.User;

import java.util.List;

@FeignClient(name = "rewardsCentral", url = "localhost:8081")

public interface RewardsCentralProxy {

    @PostMapping("/calculateRewards")
    public void calculateRewards(@RequestBody User user);

    @GetMapping("/getRewardPoints")
    public int getRewardPoints(@RequestParam String attractionId, @RequestParam String userId);

}
