package com.iit.concurrentProgramming.Coursework.technician;

import com.iit.concurrentProgramming.Coursework.constants.Utils;
import com.iit.concurrentProgramming.Coursework.ticket_machine.TicketMachine;

import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.ErrorMessage.REFILL_TICKET_TECH_THREAD_INTERRUPTED_MSG;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.TicketMachine.PAPER_TECH_MAX_REPLACE_COUNT;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.TicketMachine.PAPER_TECH_WAITING_TIME;

/**
 * Class representing a paper technician responsible for paper replacement in a ServiceTicketMachine.
 *
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 27/11/2023
 * @Description: This class encapsulates the behavior of a paper technician who performs paper replacement
 * for a ServiceTicketMachine. The technician runs a specified number of times, simulating real-world paper
 * replacement scenarios with random sleep intervals. The paper refill count is tracked during the process.
 */
@lombok.RequiredArgsConstructor
public class TicketTechnician implements Technician {
    @lombok.Getter
    private final String name;
    private final TicketMachine ticketMachine;
    /**
     * The run method, required by the Runnable interface, defines the paper replacement task.
     * The technician sleeps for a random interval, simulating the time taken for paper replacement,
     * then invokes the replaceToner method of the associated ticket machine.
     * The paper refill count is incremented after each successful paper replacement.
     * Any InterruptedException during sleep is wrapped in a RuntimeException.
     */
    @Override
    public void run() {
        try {
            for (int count = 0; count < PAPER_TECH_MAX_REPLACE_COUNT; count++) {
                Thread.sleep(Utils.getRandomTime() + PAPER_TECH_WAITING_TIME);
                serviceMachine();
            }
        } catch (InterruptedException e) {
            System.out.println(ANSI_RED + REFILL_TICKET_TECH_THREAD_INTERRUPTED_MSG + ANSI_RESET);
        }
    }

    /**
     * Method implementation for servicing the machine by refilling paper.
     */
    @Override
    public void serviceMachine() {
        ticketMachine.refillPaper();
    }
}
