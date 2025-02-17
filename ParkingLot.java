package application;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private String name; //name of the parking lot
    private List<ParkingSpot> parkingSpots;

    //contructor for parking lot - name and #ofspots
    public ParkingLot(String name, int numberOfSpots) {
        this.name = name;
        this.parkingSpots = new ArrayList<>();
        
        //loop to create parkign sport for a this lot
        for (int i = 1; i <= numberOfSpots; i++) {
            boolean evCapable = (i % 2 == 0); // Every even-numbered spot is EV capable
            String size = (i % 3 == 0) ? "Large" : (i % 3 == 1) ? "Standard" : "Small"; // Rotate spot sizes
            parkingSpots.add(new ParkingSpot(i, evCapable, size));
        }
    }

   
    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
    
}