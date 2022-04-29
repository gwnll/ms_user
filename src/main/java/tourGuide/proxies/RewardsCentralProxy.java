package tourGuide.proxies;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.request.CalculateRewardsRequest;
import tourGuide.user.UserReward;

import java.util.List;

@FeignClient(name = "rewardsCentral", url = "localhost:8081")

public interface RewardsCentralProxy {

    @PostMapping("/calculateRewards")
    public List<UserReward> calculateRewards(@RequestBody CalculateRewardsRequest request);

    @GetMapping("/getRewardPoints")
    public int getRewardPoints(@RequestParam String attractionId, @RequestParam String userId);

}
