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
 * @Author: Harini Rodrigo
 * @Project: Concurrent Programming Coursework
 * @Version 1.0
 * @Since 27/11/2023
 * @Description: This class encapsulates the behavior of a toner technician who performs toner replacement
 * for a ServiceTicketMachine. The technician runs a specified number of times, simulating real-world toner
 * replacement scenarios with random sleep intervals. The toner refill count is tracked during the process.
 */
@lombok.RequiredArgsConstructor
public class TonerTechnician extends Utils implements Runnable, Colors {
    @lombok.Getter
    private final String name;
    private  final ServiceTicketMachine ticketMachine;


    /**
     * The run method, required by the Runnable interface, defines the toner replacement task.
     * The technician sleeps for a random interval, simulating the time taken for toner replacement,
     * then invokes the replaceToner method of the associated ticket machine.
     * The toner refill count is incremented after each successful toner replacement.
     * Any InterruptedException during sleep is wrapped in a RuntimeException.
     */
    @Override
    public void run() {
        Random random = new Random();
            try {
                Thread.sleep( Utils.getRandomTime()+ TONER_TECH_WAITING_TIME);
                ticketMachine.refillToner();
            } catch (InterruptedException e) {
                return;
        }

    }
}
