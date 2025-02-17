package application;

public class Van extends Vehicle {
	//constructor for van object
    public Van(String plateNumber, String state, boolean isElectric) {
        super(plateNumber, state, isElectric);
    }

    
    @Override
    public String makeSound() {
    	return "Vrooom!!!";//overrides the original method to make a van specific sound.
    }
    
    
    @Override
    public String toString() {
    	  // Overrides the toString method to provide a string representation of the van object
        return (isElectric() ? "Electric " : "Gas ") + "Van [Plate Number: " + getPlateNumber() + ", State: " + getState() + "]";
    }
}
