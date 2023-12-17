package com.iit.concurrentProgramming.Coursework.ticket_machine;

/**
 * The `Printer` interface defines the contract for a printer that is responsible for printing tickets.
 *
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 26/11/2023
 * @Description: The `Printer` interface outlines the behavior of a ticket printer.
 * Implementing classes are expected to provide concrete implementations of the printTicket method.
 */
public interface Printer {
    /**
     * Prints the specified ticket.
     *
     * @param ticket The ticket to be printed.
     */
    void printTicket(Ticket ticket);
}
