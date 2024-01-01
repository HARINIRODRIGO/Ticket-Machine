package com.iit.concurrentProgramming.Coursework.ticket.machine;

/**
 * The `ServiceTicketMachine` interface defines the contract for a concurrent ticket printing machine.
 * It includes methods for refilling paper and toner.
 *
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 26/11/2023
 * @Description: The `ServiceTicketMachine` interface defines the contract for a ticket printing machine
 * that provides methods for refilling paper and replacing toner.
 * Implementing classes are expected to provide concurrent implementations of these methods.
 */
public interface ServiceTicketMachine {
    /**
     * Refills paper in the ticket machine.
     */
    void refillPaper();

    /**
     * Refills toner in the ticket machine.
     */
    void refillToner();
}
