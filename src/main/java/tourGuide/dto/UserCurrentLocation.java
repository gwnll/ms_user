package tourGuide.dto;

public class UserCurrentLocation {

    public String userId;
    public Location location;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UserCurrentLocation(String userId, Location location) {
        this.userId = userId;
        this.location = location;
    }
}
