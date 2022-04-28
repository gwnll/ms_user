package tourGuide;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.dto.NearbyAttraction;
import tourGuide.dto.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardsCentralProxy;
import tourGuide.service.UserService;
import tourGuide.user.User;
import tripPricer.Provider;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {

    @Autowired
    UserService userService;

    @Autowired
    RewardsCentralProxy rewardsCentralProxy;

    @Autowired
    GpsUtilProxy gpsUtilProxy;

	@Before
	public void setLocale() {
		Locale.setDefault(Locale.US);
	}

	@Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(0);
		UserService userService = new UserService(gpsUtilProxy, rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		userService.trackUserLocation(user);
		userService.tracker.stopTracking();
		while (user.getVisitedLocations().size() == 0) {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
			}
		}
		Assertions.assertThat(user.getVisitedLocations()).isNotEmpty();
	}

	@Test
	public void addUser() {
		InternalTestHelper.setInternalUserNumber(0);
		UserService userService = new UserService(gpsUtilProxy, rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		userService.addUser(user);
		userService.addUser(user2);

		User retrievedUser = userService.getUser(user.getUserName());
		User retrievedUser2 = userService.getUser(user2.getUserName());

		userService.tracker.stopTracking();

		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}

	@Test
	public void getAllUsers() {
		InternalTestHelper.setInternalUserNumber(0);
		UserService userService = new UserService(gpsUtilProxy, rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		userService.addUser(user);
		userService.addUser(user2);

		List<User> allUsers = userService.getAllUsers();

		userService.tracker.stopTracking();

		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		InternalTestHelper.setInternalUserNumber(0);
		UserService userService = new UserService(gpsUtilProxy, rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		userService.trackUserLocation(user);

		while(user.getVisitedLocations().isEmpty()) {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		userService.tracker.stopTracking();

		Assertions.assertThat(user.getVisitedLocations()).isNotEmpty();
	}

	@Test
	public void getNearbyAttractions() {
		InternalTestHelper.setInternalUserNumber(0);
		UserService userService = new UserService(gpsUtilProxy, rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		userService.trackUserLocation(user);

		while(user.getVisitedLocations().isEmpty()) {
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		VisitedLocation visitedLocation = user.getLastVisitedLocation();
		List<NearbyAttraction> attractions = userService.getNearByAttractions(visitedLocation, user);

		userService.tracker.stopTracking();

		assertEquals(5, attractions.size());
	}

	@Test
	public void getTripDeals() {
		InternalTestHelper.setInternalUserNumber(0);
		UserService userService = new UserService(gpsUtilProxy, rewardsCentralProxy);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = userService.getTripDeals(user);

		userService.tracker.stopTracking();

		assertEquals(5, providers.size());
	}


}
