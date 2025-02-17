package application;

public class Motorcycle extends Vehicle {
	//contstuctor for motorcylce object
    public Motorcycle(String plateNumber, String state, boolean isElectric) {
        super(plateNumber, state, isElectric);
    }

    @Override
    public String makeSound() {
    	return "Weeeeee!!!";//overrides the original method to make a motorcycle specific sound.
    }
    
    @Override
    public String toString() {
    	  // Overrides the toString method to provide a string representation of the motorcycle object
        return (isElectric() ? "Electric " : "Gas ") + "Motorcycle [Plate Number: " + getPlateNumber() + ", State: " + getState() + "]";
    }
}
