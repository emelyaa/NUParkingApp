package application;


public class ParkingLotSpot {
	
	private ParkingLot parkingLot;
	private ParkingSpot parkingSpot;
	
    public ParkingLotSpot(ParkingLot parkingLot, ParkingSpot parkingSpot) {
        this.parkingLot = parkingLot;
        this.parkingSpot = parkingSpot;
    }

    public ParkingLot getParkingLot() {
    	return parkingLot;
    }
    
    public ParkingSpot getParkingSpot() {
    	return parkingSpot;
    }


}