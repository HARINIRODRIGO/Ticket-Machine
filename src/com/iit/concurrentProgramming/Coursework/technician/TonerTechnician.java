package com.iit.concurrentProgramming.Coursework.technician;

import com.iit.concurrentProgramming.Coursework.constants.Utils;
import com.iit.concurrentProgramming.Coursework.ticket_machine.ServiceTicketMachine;

import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.ErrorMessage.REFILL_TONER_TECH_THREAD_INTERRUPTED_MSG;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.TicketMachine.TONER_TECH_WAITING_TIME;

/**
 * Class representing a toner technician responsible for toner replacement in a ServiceTicketMachine.
 *
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 27/11/2023
 * @Description: This class encapsulates the behavior of a toner technician who performs toner replacement
 * for a ServiceTicketMachine. The technician runs a specified number of times, simulating real-world toner
 * replacement scenarios with random sleep intervals. The toner refill count is tracked during the process.
 */
@lombok.RequiredArgsConstructor
public class TonerTechnician implements Technician {
    @lombok.Getter
    private final String name;
    private final ServiceTicketMachine ticketMachine;

    /**
     * The run method, required by the Runnable interface, defines the toner replacement task.
     * The technician sleeps for a random interval, simulating the time taken for toner replacement,
     * then invokes the replaceToner method of the associated ticket machine.
     * The toner refill count is incremented after each successful toner replacement.
     * Any InterruptedException during sleep is wrapped in a RuntimeException.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(Utils.getRandomTime() + TONER_TECH_WAITING_TIME);
            serviceMachine();
        } catch (InterruptedException e) {
            System.out.println(ANSI_RED + REFILL_TONER_TECH_THREAD_INTERRUPTED_MSG + ANSI_RESET);
        }
    }

    /**
     * Method implementation for servicing the machine by refilling toner.
     */
    @Override
    public void serviceMachine() {
        ticketMachine.refillToner();
    }
}
