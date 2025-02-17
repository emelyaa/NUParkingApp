package application;

public class Car extends Vehicle {
	
	//constructor for car object
	    public Car(String plateNumber, String state, boolean isElectric) {
        super(plateNumber, state, isElectric);
    }

    
    
    @Override
    public String makeSound() {
    	return "Beep - Beep!!!"; //overrides the original method to make a car specific sound.
    }
    
    
    @Override
    // Overrides the toString method to provide a string representation of the Car object
    public String toString() {
        return (isElectric() ? "Electric " : "Gas ") + "Car [Plate Number: " + getPlateNumber() + ", State: " + getState() + "]";
    }
}
