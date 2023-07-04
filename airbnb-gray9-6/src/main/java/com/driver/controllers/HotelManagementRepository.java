package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import io.swagger.models.auth.In;

import java.util.*;
public class HotelManagementRepository {
    Map<String, Hotel> hotelMap = new HashMap<>();
    Map<Integer, User> userMap = new HashMap<>();
    Map<Integer, List<Booking>> bookingMap = new HashMap<>();


    public String addHotel(Hotel hotel) {
        String name = hotel.getHotelName();
        if(hotelMap.containsKey(name) || name == null)
            return "FAILURE";
        else{
            hotelMap.put(name,hotel);
            return "SUCCESS";
        }
    }

    public Integer addUser(User user) {
        int no = user.getaadharCardNo();
        userMap.put(no,user);
        return no;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        if(hotelMap.containsKey(hotelName)){
            Hotel hotel = hotelMap.get(hotelName);
            List<Facility> list = hotel.getFacilities();
            for(Facility facility : newFacilities){
                if(!list.contains(facility))
                    list.add(facility);
            }
            hotelMap.put(hotelName,hotel);
            return hotel;
        }
        return null;
    }

    public String getHotelWithMostFacilities() {
        int count = 0;
        for(Hotel hotel : hotelMap.values()){
            count = Math.max(hotel.getFacilities().size() , count);
        }
        if(count == 0)
            return "";
        Set<String> list = new TreeSet<>();
        for(Hotel hotel : hotelMap.values()){
            if(hotel.getFacilities().size() == count){
                list.add(hotel.getHotelName());
            }
        }
        for(String s : list){
            return s;
        }
        return "";
    }

    public int bookARoom(Booking booking) {
        if(hotelMap.containsKey(booking.getHotelName())){
            Hotel hotel = hotelMap.get(booking.getHotelName());
            if(hotel.getAvailableRooms() >= booking.getNoOfRooms()){
                booking.setBookingId(String.valueOf(UUID.randomUUID()));
                booking.setAmountToBePaid(hotel.getPricePerNight() * booking.getNoOfRooms());
                hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());
                hotelMap.put(hotel.getHotelName() , hotel);
                if(bookingMap.containsKey(booking.getBookingAadharCard())){
                    List<Booking> bookingList = bookingMap.get(booking.getBookingAadharCard());
                    bookingList.add(booking);
                    bookingMap.put(booking.getBookingAadharCard(),bookingList);
                } else{
                    List<Booking> bookingList = new ArrayList<>();
                    bookingList.add(booking);
                    bookingMap.put(booking.getBookingAadharCard(),bookingList);
                }
                return booking.getAmountToBePaid();
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int getBookings(Integer aadharCard) {
        if(bookingMap.containsKey(aadharCard))
            return bookingMap.get(aadharCard).size();
        else
            return 0;
    }
}
