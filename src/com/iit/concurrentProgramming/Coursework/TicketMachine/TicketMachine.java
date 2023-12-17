package com.iit.concurrentProgramming.Coursework.TicketMachine;

import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.Colors;
import com.iit.concurrentProgramming.Coursework.TicketMachineSettings.ConstantValues;
import lombok.NonNull;

import static com.iit.concurrentProgramming.Coursework.TicketMachineSettings.ConstantValues.*;
import static com.iit.concurrentProgramming.Coursework.TicketMachineSettings.SystemOutputs.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Harini Rodrigo
 * @Project: Concurrent Programming Coursework
 * @Version 1.0
 * @Since 26/11/2023
 * @Description: The `TicketMachine` class ensures the concurrent and safe operation of a ticket printing machine.
 * It incorporates a fixed-size thread pool to handle concurrent tasks and a ReentrantLock for synchronization.
 * The class tracks toner and paper levels, along with refill counts. Printing tasks are submitted to the thread pool,
 * acquiring a lock, checking for resource availability, printing a ticket, and updating paper and toner levels.
 * If resources are insufficient, threads wait and are later notified when resources are replenished.
 * Paper refill and toner replacement tasks are also submitted to the thread pool for concurrent execution.
 * These tasks simulate the replacement process, with sleep intervals representing the time taken for refilling.
 */


public class TicketMachine implements ServiceTicketMachine, Printer, Colors {
    @lombok.NonNull
    private int currentPaperLevel, currentTonerLevel;
    private ReentrantLock lock = new ReentrantLock();
    private final Condition tonerAvilability = lock.newCondition();
    private final Condition paperAvilability = lock.newCondition();
    private final Condition resourceAvilability = lock.newCondition();
    public static ArrayList<Ticket> passengers = new ArrayList<>();
    private int tonerRefillCount = 0;
    private int paperRefillCount = 0;
    public TicketMachine(@NonNull int currentPaperLevel, @NonNull int currentTonerLevel) {
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        passengerQuesue(TicketPrintingSystem.getTickets());
    }

    /**
     * Checks if there is sufficient paper and toner for printing.
     *
     * @return "INSUFFICIENT_TONER_AND_PAPER": if both paper and toner are inadequate,<br>
     *         "INSUFFICIENT_PAPER" : if paper is insufficient,<br>
     *         "INSUFFICIENT_TONER": if toner is insufficient,<br>
     *         "PRINTABLE": if both paper and toner are sufficient.<br>
     */
    private String isResourceAvailable() {
        if(currentPaperLevel <= 0 & currentTonerLevel <= MINIMUM_TONER_LEVEL) {
            return INSUFFICIENT_TONER_AND_PAPER;
        } else if(currentPaperLevel <= 0) {
            return INSUFFICIENT_PAPER;
        } else if(currentTonerLevel <= MINIMUM_TONER_LEVEL) {
            return INSUFFICIENT_TONER;
        }
        return PRINTABLE;
    }

    /**
     * Concurrently prints a ticket using a fixed-size thread pool.
     * Submits printing tasks to the thread pool for concurrent execution.
     * Each task acquires a lock, checks for sufficient paper and toner levels,
     * prints a ticket, and updates paper and toner levels.
     * If resources are unavailable, the thread waits and is later notified when resources are replenished.
     *
     * @param ticket the ticket to be printed
     */
    @Override
    public void printTicket(Ticket ticket) {
        try {
            lock.lock();

            while (!isResourceAvailable().equals(PRINTABLE)) {
                if(passengers.size() == 0) {
                    System.out.println(ANSI_RED + NO_PASSENGERS_MSG + ANSI_RESET);
                    return;
                }else {
//                    if (isResourceAvailable().equals(INSUFFICIENT_TONER_AND_PAPER)) {
//                        System.out.println(ANSI_YELLOW + NOT_PRINTABLE_MSG + INSUFFICIENT_TONER_AND_PAPER + ANSI_RESET);
//                    } else if (isResourceAvailable().equals(INSUFFICIENT_PAPER)) {
//                        System.out.println(ANSI_YELLOW + NOT_PRINTABLE_MSG + INSUFFICIENT_PAPER + ANSI_RESET);
//                    } else{
//                        System.out.println(ANSI_YELLOW +NOT_PRINTABLE_MSG + INSUFFICIENT_TONER + ANSI_RESET);
//                    }
                    resourceAvilability.await(1000, TimeUnit.MILLISECONDS);
                }
            }
            if(passengers.size() != 0) {
                this.currentTonerLevel--;
                this.currentPaperLevel--;
                System.out.println(ANSI_GREEN + ticket + ANSI_RESET);
                passengers.remove(ticket);
                paperAvilability.signalAll();
                tonerAvilability.signalAll();
            }
        }  catch (InterruptedException e) {
            return;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Concurrently refills paper in the ticket machine using a single-threaded executor.
     * Submits a paper refill task to the executor for concurrent execution.
     * The task acquires a lock, checks if the paper resource is below the full tray limit,
     * simulates paper replacement with a sleep to represent the time taken,
     * increments the paper refill count, and notifies waiting threads that the lock is released.
     * The paper refill process repeats until the maximum refill count is reached.
     */
    @Override
    public void refillPaper() {
        System.out.println(REFILL_TRY_MSG);
        try {
            lock.lock();

            while ((currentPaperLevel + SHEETS_PER_PACK) >= FULL_PAPER_TRAY) {
                if(passengers.size() == 0) {
                    System.out.println(ANSI_RED + PAPER_REFILL_SKIP_MSG + ANSI_RESET);
                    break;
                }else {
                    System.out.println(ANSI_BLUE + SHEETS_IN_TRAY_MSG + PAPER_LEVEL_MSG + currentPaperLevel + ANSI_RESET);
                    paperAvilability.await(PAPER_TECH_WAITING_TIME, TimeUnit.MILLISECONDS);
                }
            }
            if(passengers.size() != 0 & currentPaperLevel == 0) {
                currentPaperLevel += SHEETS_PER_PACK;
                System.out.println(ANSI_BLUE + REFILL_COMPLETE_MSG + PAPER_LEVEL_MSG + currentPaperLevel + ANSI_RESET);
                paperRefillCount++;
                paperAvilability.signalAll();
            }
            if(passengers.size() != 0 & paperRefillCount == PAPER_TECH_MAX_REPLACE_COUNT) {
                System.out.println(ANSI_RED + PRINT_SKIP_TICKET_MSG + ANSI_RESET);
                return;
            }
        } catch (InterruptedException e) {
            return;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Concurrently replaces toner in the ticket machine using a single-threaded executor.
     * Submits a toner replacement task to the executor for concurrent execution.
     * The task acquires a lock, checks if paper and toner resources are available using the isResourceAvailable method,
     * simulates toner replacement with a sleep to represent the time taken,
     * increments the toner refill count, and notifies waiting threads that the lock is released.
     * The toner replacement process repeats until the maximum refill count is reached.
     */
    @Override
    public void refillToner() {
        System.out.println(TONER_REPLACE_TRY_MSG);

        try {
            lock.lock();
            while (currentTonerLevel + MINIMUM_TONER_LEVEL >= MAXIMUM_TONER_LEVEL) {
                if(passengers.size() == 0) {
                    System.out.println(ANSI_RED + TONER_REPLACE_SKIP_MSG + ANSI_RESET);
                    break;
                } else {
                    System.out.println(ANSI_BLUE + TONER_NOT_REPLACEABLE_MSG + TONER_LEVEL_MSG + currentTonerLevel + ANSI_RESET);
                    tonerAvilability.await(TONER_TECH_WAITING_TIME, TimeUnit.MILLISECONDS);
                }
            }
            if(passengers.size() != 0) {
                currentTonerLevel += MINIMUM_TONER_LEVEL;
                System.out.println(ANSI_BLUE + TONER_REPLACED_MSG + TONER_LEVEL_MSG + currentTonerLevel + ANSI_RESET);
                tonerRefillCount++;
                tonerAvilability.signalAll();
            }
            if(TicketMachine.passengers.size() != 0 & tonerRefillCount == TONER_TECH_MAX_REFILL_COUNT) {
                System.out.println(ANSI_RED + PAPER_REFILL_SKIP_MSG + ANSI_RESET);
                return;
            }
        } catch (InterruptedException e) {
            return;
        } finally {
            lock.unlock();
        }
    }
  private void passengerQuesue(List<Ticket> tickets) {
            for (Ticket ticket : tickets) {
                passengers.add(ticket);
            }
    }
}
