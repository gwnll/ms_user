package tourGuide.dto;

public class NearbyAttraction {

    public String attractionName;
    public Location attractionLocation;
    public Location userLocation;
    public double distance;
    public int rewardsPoints;

    public NearbyAttraction(String attractionName, Location attractionLocation, Location userLocation, double distance, int rewardsPoints) {
        this.attractionName = attractionName;
        this.attractionLocation = attractionLocation;
        this.userLocation = userLocation;
        this.distance = distance;
        this.rewardsPoints = rewardsPoints;
    }

    public double getDistance() {
        return distance;
    }
}
