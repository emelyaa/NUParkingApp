package application;

public abstract class Vehicle {
	
	//general vehicle attributes
    private final String plateNumber;
    private final String state; // Added to represent the state the vehicle is from
    private final boolean isElectric; // Added to represent if the vehicle is electric
    
    //constructor to initialize the vehicle
    public Vehicle(String plateNumber, String state, boolean isElectric) {
        this.plateNumber = plateNumber;
        this.state = state;
        this.isElectric = isElectric;
    }

    
    //general getters
    
    public String getPlateNumber() {
        return plateNumber;
    }

    public String getState() {
        return state;
    }

    public boolean isElectric() {
        return isElectric;
    }

    
    // abstract method to make the vehicle sound - implemented the subclasses
    public abstract String makeSound();
    
    
    @Override
    public String toString() {
        return (isElectric ? "Electric " : "Gas ") + "Vehicle [Plate Number: " + plateNumber + ", State: " + state + "]";
    }
}
