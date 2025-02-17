package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.*;
import java.util.Iterator;





public class MainController {
	
	// FXML UI elements mapped from the FXML file
    @FXML private ComboBox<String> vehicleTypeComboBox;
    @FXML private ComboBox<String> powerTypeComboBox;
    @FXML private ComboBox<ParkingLot> parkingLotComboBox; //adding parking lot dropdown back
    @FXML private TextField plateNumberInput;
    @FXML private TextField stateInput;
    @FXML private Label messageLabel; // Label for displaying successful actions
    @FXML private Label errorLabel; // Label for displaying errors or successful actions
    @FXML private Button submitButton;
    @FXML private Button unParkButton;
    @FXML private Button clearButton;
    @FXML private ListView<String> ryderLotListView;
    @FXML private ListView<String> fenwayLotListView;
    @FXML private ListView<String> northLotListView;
    @FXML private ListView<String> columbusGarageListView;
    
    // list of parking lots available in the system
    private List<ParkingLot> parkingLots = new ArrayList<>();
    // new hash set to keep track of parked vehicles by plate and state combination
    private Set<String> parkedVehicles = new HashSet<>(); 

    
    @FXML
    
    // Initialization method to set up UI and populate data
      
    public void initialize() {
    	
    	// Create a few parking lots with fixed numbers of spots.
    	setupParkingLots(); 
        
        
        // populate combobox with parking lots 
        parkingLotComboBox.getItems().addAll(parkingLots);
        
        //populate comboboxes for selecting vehicle types and powertypes
        vehicleTypeComboBox.getItems().addAll("Car", "Truck", "Motorcycle", "Van");
        powerTypeComboBox.getItems().addAll("Gas", "Electric");

        // set up button actions
        submitButton.setOnAction(event -> handleParkAction());
        unParkButton.setOnAction(event -> handleUnParkAction());
        clearButton.setOnAction(event -> handleClearAction(true));
                
    }
    
    
    
    
// Method to create parking lots with several occupied spots and add them to the list.
    private void setupParkingLots() {
    	//create parking lots
        ParkingLot lot1 = new ParkingLot("Ryder Parking Lot (Staff Lot)", 6);
        ParkingLot lot2 = new ParkingLot("Fenway Parking Lot (Guest Lot)", 4);
        ParkingLot lot3 = new ParkingLot("North Parking Lot (Student Lot)", 6);
        ParkingLot lot4 = new ParkingLot("Columbus Parking Garage (Open Lot)", 8);

        parkingLots.add(lot1);
        parkingLots.add(lot2);
        parkingLots.add(lot3);
        parkingLots.add(lot4);
        
        //create five default vehicles for demo
        List<Vehicle> defaultVehicles = Arrays.asList(
            new Car("ABC123", "MA", false),
            new Car("ELE007", "VT", true),
            new Motorcycle("XYZ789", "CA", false),
            new Truck("BBD692", "CT", false),
            new Van("VAN000", "FL", false)
    	);
        
        //park each default vehicle using the standard parking method - findFirstAvailableSpot
        for (Vehicle vehicle : defaultVehicles) {
        	
        	ParkingLotSpot newSpot = findFirstAvailableSpot(vehicle.getClass().getSimpleName(), vehicle.isElectric());
            if (newSpot != null) {
                newSpot.getParkingSpot().parkVehicle(vehicle);
            } else {
                System.out.println("No available spot found for vehicle: " + vehicle);
            }
        }
        
        //update the UI parking lot lists with the details on parked vehicles
        updateAllParkingSpots();
     }
   
    
 
    
    
// Parking Methods for click buttons - park, unpark, clear
      
// PARK A VEHICLE for submit button
    private void handleParkAction() {
    	//get the selections from the user
        ParkingLot selectedLot = parkingLotComboBox.getValue(); // get selected parking lot
        String plateNumber = plateNumberInput.getText().trim();
        String vehicleType = vehicleTypeComboBox.getValue();
        String state = stateInput.getText().trim().toUpperCase();
        String powerType = powerTypeComboBox.getValue();
        String vehicleKey = plateNumber + ":" + state; // unique vehicle identifier addition
        
        // validation checks on user selections
        
        //check if parking lot is selected
        if (selectedLot == null) {
            setErrorMessage("Please select a parking lot.");
            return;
        }
        
        // verify the vehicle key is not already parked
        if (isVehicleAlreadyParked(vehicleKey)) {
            setErrorMessage("This vehicle is already parked in the lot.");
            return;
        }
        
        //call validateInputs method to ensure the user gave us good data
        if (!validateInputs(vehicleType, state, powerType, plateNumber)) {
            return;
        }
        
        boolean isElectric = powerType.equals("Electric");
        
        //create an object  vehicle with selected attributes
        Vehicle vehicle = createVehicle(vehicleType, plateNumber, state, isElectric);
        
        //find a spot for the vehicle
        ParkingLotSpot newSpot = findFirstAvailableSpotInLot(vehicleType, isElectric, selectedLot);
        
        //and finally park it!
        if (newSpot != null) {
        	
        	// park the vehicle with the Parkable interface
        	Parkable parkableSpot = newSpot.getParkingSpot();
        	parkableSpot.parkVehicle(vehicle);
            //get the sound vehicle makes
            String sound = vehicle.makeSound();
            //print the success message
            setMessage(sound + "\n" + vehicle + " has been parked in " + newSpot.getParkingLot().getName() + " at spot " + newSpot.getParkingSpot().getSpotNumber());
            
            // add the vehicle key to the parkedVehicles key set once successfully parked
            parkedVehicles.add(vehicleKey);
            
            //refresh the UI parking spot lists
            updateAllParkingSpots();
            handleClearAction(false);
        } else {
            setErrorMessage("No spots are available for this vehicle, please park on the street.");
        }
    }
    
    // method to check if already parked by going through keys (plate+state)
    private boolean isVehicleAlreadyParked(String vehicleKey) {
        if (parkedVehicles.contains(vehicleKey)) {
            return true; //already parked :(
        }
        
        //cross validation across ALL parking lots
        for (ParkingLot lot : parkingLots) {
            for (ParkingSpot spot : lot.getParkingSpots()) {
                Vehicle parkedVehicle = spot.getParkedVehicle();
                if (parkedVehicle != null) {
                    String parkedKey = parkedVehicle.getPlateNumber() + ":" + parkedVehicle.getState();
                    if (vehicleKey.equals(parkedKey)) {
                        return true; // vehicle is actually parked somewhere
                    }
                }
            }
        }
        return false; //vehicle is not parked anywhere!
    }


    
    
    
    
	// method to find an available spot in ANY LOT that matches the vehicle. For initialization of default vehicle parking process.
    private ParkingLotSpot findFirstAvailableSpot(String vehicleType, boolean isElectric) {
        Iterator<ParkingLot> iterator = parkingLots.iterator();
        while (iterator.hasNext()) {
            ParkingLot lot = iterator.next();
            for (ParkingSpot spot : lot.getParkingSpots()) {
                if (spot.isAvailable() && isCompatibleSpot(spot, vehicleType, isElectric)) {
                    return new ParkingLotSpot(lot, spot);
                }
            }
        }
        return null; // No available spot found for the vehicle in any lot
    }
    
 
    
    
    //method to find an available spot within the selected lot. For user interactive parking process.
    private ParkingLotSpot findFirstAvailableSpotInLot(String vehicleType, boolean isElectric, ParkingLot selectedLot) {
        Iterator<ParkingSpot> iterator = selectedLot.getParkingSpots().iterator();
        
        while (iterator.hasNext()) {
            ParkingSpot spot = iterator.next();
            if (spot.isAvailable() && isCompatibleSpot(spot, vehicleType, isElectric)) {
                return new ParkingLotSpot(selectedLot, spot);
            }
        }
        return null; // No available spot found for the vehicle in the selected lot
    }

    
       
    // method to Check if the parking spot is compatible with the vehicle type and power type
    private boolean isCompatibleSpot(ParkingSpot spot, String vehicleType, boolean isElectric) {
        // Check size compatibility based on vehicle type
        boolean isSizeCompatible = false;
        
        switch (vehicleType) {
            case "Truck":
                // Trucks can only park in Large spots
                isSizeCompatible = spot.getSize().equals("Large");
                break;
            case "Van":
                // Vans can only park in Large spots
                isSizeCompatible = spot.getSize().equals("Large");
                break;
            case "Motorcycle":
                // Motorcycles can park in any spot (Small, Standard, Large)
                isSizeCompatible = true;
                break;
            case "Car":
                // Cars can park in Standard or Large spots, but not Small
                isSizeCompatible = spot.getSize().equals("Standard") || spot.getSize().equals("Large");
                break;
            
        }

     // Now check if the spot is compatible based on whether the vehicle is electric or not
        boolean isEvCompatible = (isElectric && spot.isEvCapable()) || (!isElectric && !spot.isEvCapable());
        return isSizeCompatible && isEvCompatible;
    }

    
    
    
    // method to Create a vehicle object based on the user's input
    private Vehicle createVehicle(String vehicleType, String plateNumber, String state, boolean isElectric) {
        switch (vehicleType) {
            case "Truck": return new Truck(plateNumber, state, isElectric);
            case "Motorcycle": return new Motorcycle(plateNumber, state, isElectric);
            case "Van": return new Van(plateNumber, state, isElectric);
            case "Car": return new Car(plateNumber, state, isElectric);
            default: 
            	throw new IllegalArgumentException("Cannot find that vehicle type");
        }
    }
    
    
    
    
    
// UNPARK A VEHICLE 
    //method to unpark a vehicle for unpark button
    private void handleUnParkAction() {

    	    	    	
    	//get the selected listview (associated with the parking lot), plate and state identifier of the vehicle
        ListView<String> selectedListView = getSelectedListView();
        String plateNumber = plateNumberInput.getText().trim();
        String state = stateInput.getText().trim().toUpperCase();
        String vehicleKey = plateNumber + ":" + state; // vehicle key for tracking

        // If no parking lot is selected, set an error message
        if (selectedListView == null) {
            setErrorMessage("Please select a parking lot to unpark a vehicle.");
            return;
        }

        // Get the selected vehicle information from the ListView
        String selectedVehicleInfo = selectedListView.getSelectionModel().getSelectedItem();
        if (selectedVehicleInfo == null) {
            setErrorMessage("Please select a vehicle to unpark.");
            return;
        }
        
                
        //flag to validate vehicle was found and succesfully unparked
        boolean vehicleFound = false;
        
        //loop through all lots to find the vehicle and to remove it.
        for (ParkingLot lot : parkingLots) {
            for (ParkingSpot spot : lot.getParkingSpots()) {
                Vehicle parkedVehicle = spot.getParkedVehicle();
                
                // check if this spot has the vehicle we are trying to unpark
                if (parkedVehicle != null && parkedVehicle.toString().equals(selectedVehicleInfo)) {
                    
                	//unpark the vehicle!
                	spot.removeVehicle();
                    
                	//success message
                    if (spot.isAvailable()) {
                        setMessage("Vehicle with plate " + parkedVehicle.getPlateNumber() + " has been unparked.");
                        
                     // Remove the key from the set only after successful removal    
                        parkedVehicles.remove(vehicleKey); 
                        updateAllParkingSpots();
                        vehicleFound = true;
                        break;
                    } else {
                        setErrorMessage("Unable to remove the vehicle from the spot. Please try again.");
                        return;
                    }
                }
            }
            if (vehicleFound) break;
        }
        
		//feedback to the user if requested vehicle is not found
        if (!vehicleFound) {
            setErrorMessage("No matching vehicle found to unpark. Please check the details and try again.");
        }
    }

	
	//helper method to get the listview /parking lot based on the selection in the UI
    private ListView<String> getSelectedListView() {
        if (ryderLotListView.getSelectionModel().getSelectedItem() != null) {
            return ryderLotListView;
        } else if (fenwayLotListView.getSelectionModel().getSelectedItem() != null) {
            return fenwayLotListView;
        } else if (northLotListView.getSelectionModel().getSelectedItem() != null) {
            return northLotListView;
        } else if (columbusGarageListView.getSelectionModel().getSelectedItem() != null) {
            return columbusGarageListView;
        }
        return null;
    }
    
 
    private void handleClearAction(Boolean clearMessages) {
        parkingLotComboBox.getSelectionModel().clearSelection();
        vehicleTypeComboBox.getSelectionModel().clearSelection();
        powerTypeComboBox.getSelectionModel().clearSelection();

        plateNumberInput.clear();
        stateInput.clear();

        if (clearMessages) {
            setMessage(""); // Clear the message label
            setErrorMessage("");
        }
    }
    
    
    
    
// UI Methods
    
//method to update all parking lots and parking spots with updated information
    private void updateAllParkingSpots() {

        // Clear all list views
        ryderLotListView.getItems().clear();
        fenwayLotListView.getItems().clear();
        northLotListView.getItems().clear();
        columbusGarageListView.getItems().clear();

        // loop thru parking lots to populate lists with parking spot information
        for (ParkingLot lot : parkingLots) {
            for (ParkingSpot spot : lot.getParkingSpots()) {
            	// TODO: integrate the CSS styles somehow ("available-spot" and "occupied-spot")
                String spotInfo;
                if (!spot.isAvailable()) {
                    // If spot is occupied, show the vehicle info
                    Vehicle parkedVehicle = spot.getParkedVehicle(); 
                    spotInfo = parkedVehicle.toString();
                    
                } else {
                    // If spot is empty, show the spot info
                    spotInfo = spot.toString();
                }

                // Add the spot info to the appropriate ListView base on parkign lot name
                if (lot.getName().equals("Ryder Parking Lot (Staff Lot)")) {
                    ryderLotListView.getItems().add(spotInfo);
                } else if (lot.getName().equals("Fenway Parking Lot (Guest Lot)")) {
                    fenwayLotListView.getItems().add(spotInfo);
                } else if (lot.getName().equals("North Parking Lot (Student Lot)")) {
                    northLotListView.getItems().add(spotInfo);
                } else if (lot.getName().equals("Columbus Parking Garage (Open Lot)")) {
                    columbusGarageListView.getItems().add(spotInfo);
                }
            }
        }
    }
    
 
    
    
    
    
// Validation Methods
    //method to ensure the inputs from the users are accurate before attempting to park
    private boolean validateInputs(String vehicleType, String state, String powerType, String plateNumber) {
		
    	//collect validation errors
    	List<String> validationErrors = new ArrayList<>();
		
		if (vehicleType == null) validationErrors.add("Please select a vehicle type.");
		if (state == null || !state.matches("^[A-Z]{2}$")) validationErrors.add("Invalid state abbreviation.");
		if (powerType == null) validationErrors.add("Please select a power type.");
		if (plateNumber == null || !plateNumber.matches("^[A-Z0-9]{6}$")) validationErrors.add("Invalid plate number.");
		
		
		//show collected validation errrors if any
		if (!validationErrors.isEmpty()) {
			setErrorMessage(String.join("\n", validationErrors));
			return false;
		}
		
		setMessage("");
		return true;
	}


    private void setErrorMessage(String error) {
    	errorLabel.setText(error);
        messageLabel.setText("");
    }
    
    private void setMessage(String message) {
    	errorLabel.setText("");
        messageLabel.setText(message);
    }
    


}
