package com.iit.concurrentProgramming.Coursework.ticket_machine;

import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.ErrorMessage.PASSENGER_THREAD_INTERRUPTED_MSG;

/**
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 27/11/2023
 * @Description: Passengers class represents a concurrent entity interacting with a ticket machine.
 */
@lombok.AllArgsConstructor
public class Passengers implements Runnable {
    private final TicketMachine ticketMachine;
    private final Ticket ticket;

    /**
     * Overrides the run method from the Runnable interface.
     * Simulates a passenger getting a ticket from the ticket machine, prints relevant information,
     * and introduces a sleep to simulate additional passenger activity.
     */
    @Override
    public void run() {
        try {
            ticketMachine.printTicket(ticket);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(ANSI_RED + PASSENGER_THREAD_INTERRUPTED_MSG + ANSI_RESET);
        }
    }
}