package com.iit.concurrentProgramming.Coursework.TicketMachine;

import com.iit.concurrentProgramming.Coursework.TicketMachine.ServiceTicketMachine;
import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.Colors;
import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.ConstantValues;
import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.SystemOutputs;
import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.Utils;
import static com.iit.concurrentProgramming.Coursework.TicketMachineSettings.ConstantValues.*;
import static com.iit.concurrentProgramming.Coursework.TicketMachineSettings.SystemOutputs.*;
import java.util.Random;

/**
 * @Author Harini Rodrigo
 * @Version 1.0
 * @Since 27/11/2023
 * @Description: This class encapsulates the behavior of a paper technician who performs paper replacement
 * for a ServiceTicketMachine. The technician runs a specified number of times, simulating real-world paper
 * replacement scenarios with random sleep intervals. The paper refill count is tracked during the process.
 */
@lombok.RequiredArgsConstructor
public class TicketTechnician extends Utils implements Runnable, Colors {

    private final String name;

    private  final ServiceTicketMachine ticketMachine;

    /**
     * The run method, required by the Runnable interface, defines the paper replacement task.
     * The technician sleeps for a random interval, simulating the time taken for paper replacement,
     * then invokes the replaceToner method of the associated ticket machine.
     * The paper refill count is incremented after each successful paper replacement.
     * Any InterruptedException during sleep is wrapped in a RuntimeException.
     */
    @Override
    public void run() {
        Random random = new Random();
            try {
                Thread.sleep(Utils.getRandomTime() + PAPER_TECH_WAITING_TIME);
                ticketMachine.refillPaper();

            } catch (InterruptedException e) {
                return;
        }

    }
}

