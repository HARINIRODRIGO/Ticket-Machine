package com.iit.concurrentProgramming.Coursework.TicketMachine;

import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.Colors;

/**
 * @Author: Harini Rodrigo
 * @Project: Concurrent Programming Coursework
 * @Version 1.0
 * @Since 27/11/2023
 * @Description: Passengers class represents a concurrent entity interacting with a ticket machine.
 */
@lombok.AllArgsConstructor
public class Passengers implements Runnable, Colors {
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
                throw new RuntimeException(e);
            }

    }
}
