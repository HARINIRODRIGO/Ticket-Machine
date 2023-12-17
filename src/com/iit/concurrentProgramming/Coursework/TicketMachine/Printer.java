package com.iit.concurrentProgramming.Coursework.TicketMachine;
/**
 * The `Printer` interface defines the contract for a printer that is responsible for printing tickets.
 * Methods:
 * - printTicket(Ticket ticket): Prints the specified ticket.
 * Implementing classes are expected to provide concrete implementations of the printTicket method.
 * @Author: Harini Rodrigo
 * @Project: Concurrent Programming Coursework
 * @Version 1.0
 * @Since 26/11/2023
 * @Description: The `Printer` interface outlines the behavior of a ticket printer.
 */
public interface Printer {
    void printTicket(Ticket ticket);
}
