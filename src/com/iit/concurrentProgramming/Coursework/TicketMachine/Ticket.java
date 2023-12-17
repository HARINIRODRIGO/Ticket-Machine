package com.iit.concurrentProgramming.Coursework.TicketMachine;

import com.iit.concurrentProgramming.Coursework.TicketMachine.TicketDetailInfo.PassengerInfo;
import com.iit.concurrentProgramming.Coursework.TicketMachine.TicketDetailInfo.TravelInfo;
import java.math.BigDecimal;

/**
 * @Author Harini Rodrigo
 * @Version 1.0
 * @Since 26/11/2023
 * @Description: The Ticket class serves as a data structure to store information about a ticket issued by a ticket machine.
 * It includes essential details related to the ticket, allowing for easy retrieval and management of ticket information.
 */
@lombok.Getter
@lombok.AllArgsConstructor

public class Ticket {
    private BigDecimal ticketPrice;
    private String ticketNumber;
    private final TravelInfo travelInfo;
    private final PassengerInfo passengerInfo;

    @Override
    public String toString() {
        return "Ticket {" +
                "Passenger Name: " + passengerInfo.getName() +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", ticketPrice: " + ticketPrice +  '\'' +
                ", travelInfo=" + travelInfo.getOrigin() + '\'' +
                ", travelInfo=" + travelInfo.getDestination() +
                '}';
    }
}