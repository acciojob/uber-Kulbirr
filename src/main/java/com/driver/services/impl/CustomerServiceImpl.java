package com.driver.services.impl;


import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		//unlock if code fails
//		List<TripBooking> trips = customer.getTrips();
//
//		for(TripBooking tripBooking : trips) {
//			if(tripBooking.getStatus() == TripStatus.CONFIRMED){
//				tripBooking.setStatus(TripStatus.CANCELED);
//			}
//		}
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		TripBooking tripBooking = new TripBooking();
		Driver driver = null;
		List<Driver> driverList = driverRepository2.findAll();

		for(Driver driver1 : driverList){
			if(driver1.getCab().getAvailable() == true){
				if((driver == null) || (driver.getDriverId() > driver1.getDriverId())){
					driver = driver1;
				}
			}
		}

		if(driver == null){
			throw new Exception("No cab available!");
		}

		Customer customer = customerRepository2.findById(customerId).get();
		tripBooking.setCustomer(customer);
		tripBooking.setDriver(driver);
		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		driver.getCab().setAvailable(false);
		tripBooking.setDistanceInKm(distanceInKm);

		int rate = driver.getCab().getPerKmRate();
		tripBooking.setBill(distanceInKm * rate);

		customer.getTrips().add(tripBooking);
		customerRepository2.save(customer);

		driver.getTrips().add(tripBooking);
		driverRepository2.save(driver);

		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> tripBookingOptional = tripBookingRepository2.findById(tripId);

		TripBooking tripBooking = tripBookingOptional.get();

		tripBooking.setStatus(TripStatus.CANCELED);
		tripBooking.setBill(0);
		tripBooking.getDriver().getCab().setAvailable(true);

		tripBookingRepository2.save(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> tripBookingOptional = tripBookingRepository2.findById(tripId);

		TripBooking tripBooking = tripBookingOptional.get();

		tripBooking.setStatus(TripStatus.COMPLETED);

		Driver driver = tripBooking.getDriver();
		driver.getCab().setAvailable(true);

		List<TripBooking> tripBookingList = new ArrayList<>();
		tripBookingList = driver.getTrips();
		for (TripBooking tripBooking1 : tripBookingList){
			if(tripBooking1.getTripBookingId() == tripBooking.getTripBookingId()){
				tripBookingList.remove(tripBooking1);
			}
		}
		driverRepository2.save(driver);
		tripBookingRepository2.save(tripBooking);
	}
}
