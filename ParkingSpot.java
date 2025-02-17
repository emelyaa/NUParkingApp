package application;

public class ParkingSpot implements Parkable {
    private int spotNumber;
    private Vehicle parkedVehicle;
    private boolean evCapable;
    private String size;

    // Constructor to initialize the spot number, EV capability, and size
    public ParkingSpot(int spotNumber, boolean evCapable, String size) {
        this.spotNumber = spotNumber;
        this.evCapable = evCapable;
        this.size = size;
        this.parkedVehicle = null;
    }

    // Parkable interface implementation
    
    @Override
    //method to park a vehicle in this spot
    public void parkVehicle(Vehicle vehicle) {
        if (isAvailable() && canPark(vehicle)) {
            //park the vehicle
        	this.parkedVehicle = vehicle;
        	//print success message
            System.out.println(vehicle + " is parked at spot " + spotNumber);
        } else if (!isAvailable()) {
        	
        	//otherwise show the error
            System.out.println("Parking spot " + spotNumber + " is already occupied.");
        } else {
            System.out.println(vehicle + " cannot be parked at spot " + spotNumber + " (incompatible spot).");
        }
    }

    @Override
    // method to remove a vehicle from the spot
    public void removeVehicle() {
        if (!isAvailable()) {
            System.out.println(parkedVehicle + " has been removed from spot " + spotNumber);
            this.parkedVehicle = null; // Make the spot available by removing the vehicle
        } else {
            System.out.println("Parking spot " + spotNumber + " is already empty.");
        }
    }
    
    @Override    
    //method to check if parking spot is available 
    public boolean isAvailable() {
        return parkedVehicle == null; //if no vehicle then the spot is available
    }

    //method to check if vehicle can park
    public boolean canPark(Vehicle vehicle) {
        // Check size compatibility
        if (vehicle instanceof Truck && !size.equals("Large")) {
            return false;
        }
        if (vehicle instanceof Van && !size.equals("Large")) {
            return false;
        }
        // Check EV compatibility
        if (vehicle.isElectric() && !evCapable) {
            return false; // Electric vehicles can only park in EV-capable spots
        }
        return true; // Cars and motorcycles can park as long as size and EV compatibility are OK
    }
    
    //getters

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public String getPlateNumber() {
        return parkedVehicle != null ? parkedVehicle.getPlateNumber() : null;
    }

    public String getSize() {
        return size;
    }

    public boolean isEvCapable() {
        return evCapable;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    
    
    @Override
    
    //override to string method to display parking spot information
    public String toString() {
        String plateInfo = (parkedVehicle != null) ? ", Occupied by: " + parkedVehicle.getPlateNumber() : ", Empty";
        return "Spot " + spotNumber + " (" + (isAvailable() ? "Available" : "Occupied") + ", EV Capable: " + evCapable + ", Size: " + size + plateInfo + ")";
    }



}

