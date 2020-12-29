package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

class Main {
    public static void main(String[] args) {
        //Initializing the ArrayList of flights, and its copy for research
        ArrayList<Flight> list = new ArrayList<>(FlightBuilder.createFlights()), copy = new ArrayList<Flight>(list);

        //1-st rule - A flight segment cannot contain a departure date earlier than the current one
        for (Flight flight: copy) {
            for (Segment segment: flight.getSegments())
                //If flight segment doesn't fit to the 1-st rule (using 'isBefore' method to find out) than remove this flight from list
                if (segment.getDepartureDate().isBefore(LocalDateTime.now())) list.remove(flight);
        }

        System.out.println("List of flights after applying the 1-st rule:");
        for (Flight flight: list) System.out.println(flight);


        //2-nd rule - A flight segment cannot contain a arrival date earlier than the departure one
        for (Flight flight: copy) {
            for (Segment segment: flight.getSegments())
                //If flight segment doesn't fit to the 2nd-st rule (using 'isBefore' method to find out it) than remove this flight from list
                if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) list.remove(flight);
        }

        System.out.println("List of flights after applying the 2-nd rule:");
        for (Flight flight: list) System.out.println(flight);

        //3-rd rule - A flight cannot contain a pause between segments more than 2 hours.
        for (Flight flight: copy) {
            //Summary pause between flight segments in milliseconds
            long pause = 0;
            for (int i = 0; i < flight.getSegments().size() - 1; i++) {
                Segment currentSegment = flight.getSegments().get(i),
                        nextSegment = flight.getSegments().get(i + 1);
                Date arrivalDate = makeDate(currentSegment.getArrivalDate()),
                     departureDate = makeDate(nextSegment.getDepartureDate());
                //Increasing the pause by the time difference between the arrival date of the current segment and the departure date of the next segment
                pause += departureDate.getTime() - arrivalDate.getTime();
                //2 hours are equal 1000 * 60 * 60 * 2 milliseconds
                if (pause > 7200000) {
                    list.remove(flight);
                    break;
                }
            }
        }

        copy = null;

        System.out.println("List of flights after applying the 3-nd rule:");
        for (Flight flight: list) System.out.println(flight);
    }

    //Method for returning LocalDateTime data in Date type
    public static Date makeDate (LocalDateTime d) {
        Date date = new Date(d.getYear(), d.getMonthValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond());
        return date;
    }
}