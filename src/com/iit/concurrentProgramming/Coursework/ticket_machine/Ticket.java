package com.iit.concurrentProgramming.Coursework.ticket_machine;

import com.iit.concurrentProgramming.Coursework.ticket_info.PassengerInfo;
import com.iit.concurrentProgramming.Coursework.ticket_info.TravelInfo;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Class representing a ticket issued by a ticket machine.
 *
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 26/11/2023
 * @Description: The Ticket class serves as a data structure to store information about a ticket issued by a ticket machine.
 * It includes essential details related to the ticket, allowing for easy retrieval and management of ticket information.
 */
@lombok.Getter
@lombok.AllArgsConstructor
public class Ticket {
    private BigDecimal ticketPrice;
    private UUID ticketNumber;
    private final TravelInfo travelInfo;
    private final PassengerInfo passengerInfo;

    /**
     * Overrides the toString method to provide a formatted string representation of the Ticket object.
     *
     * @return A string representation of the Ticket object.
     */
    @Override
    public String toString() {
        return "[ Passenger Name = " + passengerInfo.getName()  +
                "] "+" Passenger Info => "+ passengerInfo +" Ticket Info =>" +" TicketNumber = " + ticketNumber  +
                ", TicketPrice = " + ticketPrice +
                ", Travel = " + travelInfo.getOrigin() + " to " + travelInfo.getDestination();
    }
}
