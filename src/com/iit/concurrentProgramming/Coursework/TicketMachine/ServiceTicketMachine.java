package com.iit.concurrentProgramming.Coursework.TicketMachine;

import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.ConstantValues;

/**
 * ServiceTicketMachine interface defines the contract for a concurrent ticket printing machine.
 * It includes methods for refilling paper and toner.
 *
 * Methods:
 * - refillPaper(): Refills paper in the ticket machine.
 * - refillToner(): Refills toner in the ticket machine.
 *
 * The interface is designed to be implemented by classes that provide concurrent implementations of these methods.
 *
 * @Author: Harini Rodrigo
 * @Project: Concurrent Programming Coursework
 * @Version 1.0
 * @Since 26/11/2023
 * @Description: The `ServiceTicketMachine` interface defines the contract for a ticket printing machine
 * that provides methods for refilling paper and replacing toner.
 * Implementing classes are expected to provide concurrent implementations of these methods.
 */
public interface ServiceTicketMachine {
    void refillPaper();
    void refillToner();
}
