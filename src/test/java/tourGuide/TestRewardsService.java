package tourGuide;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tourGuide.dto.Attraction;
import tourGuide.dto.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardsCentralProxy;
import tourGuide.service.UserService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRewardsService {

    @Autowired
    UserService userService;

    @Autowired
    GpsUtilProxy gpsUtilProxy;

    @BeforeClass
    public static void setInternalUserNumberZero() {
        InternalTestHelper.setInternalUserNumber(1);
    }

    @Before
    public void setLocale() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void userGetRewards() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsUtilProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        userService.trackUserLocation(user);
        while (user.getUserRewards().size() == 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        List<UserReward> userRewards = user.getUserRewards();
        userService.tracker.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

    @Test
    public void isWithinAttractionProximity() {
        Attraction attraction = gpsUtilProxy.getAttractions().get(0);
        assertTrue(gpsUtilProxy.near(attraction.latitude, attraction.longitude, attraction.latitude, attraction.longitude, 10));
    }

    // Needs fixed - can throw ConcurrentModificationException
    @Test
    public void nearAllAttractions() {
        int proximityBuffer = Integer.MAX_VALUE;

        InternalTestHelper.setInternalUserNumber(1);

        int size = gpsUtilProxy.getAttractions().size();

        User user = userService.getAllUsers().get(0);
        userService.calculateRewards(user, proximityBuffer);
        List<UserReward> userRewards = userService.getUserRewards(user);
        userService.tracker.stopTracking();

        while (user.getUserRewards().size() < size) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        assertEquals(size, userRewards.size());
    }

}
