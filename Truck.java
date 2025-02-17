package application;

public class Truck extends Vehicle {
	//constructor for truck object
    public Truck(String plateNumber, String state, boolean isElectric) {
        super(plateNumber, state, isElectric);
    }

    @Override
    public String makeSound() {
    	return "Grrrr-Grroom!!!"; //overrides the original method to make a truck specific sound.
    }    
    
    
    @Override
    public String toString() {
    	  // Overrides the toString method to provide a string representation of the truck object
        return (isElectric() ? "Electric " : "Gas ") + "Truck [Plate Number: " + getPlateNumber() + ", State: " + getState() + "]";
    }
}