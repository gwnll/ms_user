package tourGuide.proxies;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.dto.Attraction;
import tourGuide.dto.VisitedLocation;

import java.util.List;

@FeignClient(name = "gpsutil", url = "localhost:8082")
public interface GpsUtilProxy {

    @GetMapping("/getAttractions")
    public List<Attraction> getAttractions();

    @GetMapping("/getVisitedLocation/{id}")
    public VisitedLocation getVisitedLocation(@PathVariable String id);

    @GetMapping("/getDistance")
    public double getDistance(@RequestParam double lat1, @RequestParam double long1,
                              @RequestParam double lat2, @RequestParam double long2);

    @GetMapping("/near")
    public boolean near(@RequestParam double lat1, @RequestParam double long1,
                        @RequestParam double lat2, @RequestParam double long2,
                        @RequestParam int proximityBuffer);

}
