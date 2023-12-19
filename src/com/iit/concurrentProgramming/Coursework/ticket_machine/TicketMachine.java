package com.iit.concurrentProgramming.Coursework.ticket_machine;

import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.ErrorMessage.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.TicketMachine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class representing a ticket printing machine that ensures concurrent and safe operation.
 *
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 26/11/2023
 * @Description: The `TicketMachine` class ensures the concurrent and safe operation of a ticket printing machine.
 * It incorporates a fixed-size thread pool to handle concurrent tasks and a ReentrantLock for synchronization.
 * The class tracks toner and paper levels, along with refill counts. Printing tasks are submitted to the thread pool,
 * acquiring a lock, checking for resource availability, printing a ticket, and updating paper and toner levels.
 * If resources are insufficient, threads wait and are later notified when resources are replenished.
 * Paper refill and toner replacement tasks are also submitted to the thread pool for concurrent execution.
 * These tasks simulate the replacement process, with sleep intervals representing the time taken for refilling.
 */


public class TicketMachine implements ServiceTicketMachine, Printer {
    @lombok.NonNull
    private int currentPaperLevel = 0;
    private int  currentTonerLevel = 0 ;
    private int tonerRefillCount = 0;
    private int paperRefillCount = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition tonerAvailability = lock.newCondition();
    private final Condition paperAvailability = lock.newCondition();
    private final Condition resourceAvailability = lock.newCondition();
    public static ArrayList<Ticket> passengers = new ArrayList<>();

    public TicketMachine(List<Ticket> passengers) {
        passengerQueue(passengers);
    }

    /**
     * Checks if there is sufficient paper and toner for printing.
     *
     * @return "INSUFFICIENT_TONER_AND_PAPER": if both paper and toner are inadequate,<br>
     * "INSUFFICIENT_PAPER" : if paper is insufficient,<br>
     * "INSUFFICIENT_TONER": if toner is insufficient,<br>
     * "PRINTABLE": if both paper and toner are sufficient.<br>
     */
    private String isResourceAvailable() {
        if (currentPaperLevel < PAPER_PER_TICKET & currentTonerLevel < MIN_TONER_LEVEL) {
            return INSUFFICIENT_TONER_AND_PAPER;
        } else if (currentPaperLevel < PAPER_PER_TICKET) {
            return INSUFFICIENT_PAPER;
        } else if (currentTonerLevel < MIN_TONER_LEVEL) {
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

                if (passengers.isEmpty()) {
                    System.out.println(ANSI_RED + NO_PASSENGERS_MSG + ANSI_RESET);
                    return;
                } else if (currentPaperLevel < PAPER_PER_TICKET && paperRefillCount == PAPER_TECH_MAX_REPLACE_COUNT) {
                    System.out.println(ANSI_RED + PAPER_REFILL_SKIP_MSG + ANSI_RESET);
                    break;
                } else if (currentTonerLevel < MIN_TONER_LEVEL & tonerRefillCount == TONER_TECH_MAX_REFILL_COUNT) {
                    System.out.println(ANSI_RED + TONER_REFILL_SKIP_MSG + ANSI_RESET);
                    break;
                } else{
                    resourceAvailability.await();
                }
            }
            if (!passengers.isEmpty()) {
                this.currentTonerLevel--;
                this.currentPaperLevel--;
                System.out.println(newTicket + ANSI_GREEN + ticket + ANSI_RESET);
                passengers.remove(ticket);
                paperAvailability.signalAll();
                tonerAvailability.signalAll();
            }
        } catch (InterruptedException e) {
            System.out.println(ANSI_RED + TICKET_PRINTING_THREAD_INTERRUPTED_MSG + ANSI_RESET);
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
                if (passengers.isEmpty()) {
                    System.out.println(ANSI_RED + PAPER_REPLACE_SKIP_MSG + ANSI_RESET);
                    break;
                } else {
                    System.out.println(ANSI_BLUE + SHEETS_IN_TRAY_MSG + PAPER_LEVEL_MSG + currentPaperLevel + ANSI_RESET);
                    paperAvailability.await();
                }
            }
            if (!passengers.isEmpty() & currentPaperLevel < PAPER_PER_TICKET) {
                System.out.println(ANSI_YELLOW + REFILL_WAITING + ANSI_RESET);
                currentPaperLevel += SHEETS_PER_PACK;
                System.out.println(ANSI_PURPLE + REFILL_COMPLETE_MSG + ANSI_RESET);
                paperRefillCount++;
                resourceAvailability.signalAll();
                System.out.println(ANSI_BLUE + PAPER_LEVEL_MSG + currentPaperLevel + ANSI_RESET);
            }

        } catch (InterruptedException e) {
            System.out.println(ANSI_RED + REFILL_TICKET_TECH_THREAD_INTERRUPTED_MSG + ANSI_RESET);
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
            while (currentTonerLevel >= MAXIMUM_TONER_LEVEL) {
                if (passengers.isEmpty()) {
                    System.out.println(ANSI_RED + TONER_REPLACE_SKIP_MSG + ANSI_RESET);
                    break;
                } else {
                    System.out.println(ANSI_BLUE + TONER_NOT_REPLACEABLE_MSG + TONER_LEVEL_MSG + currentTonerLevel + ANSI_RESET);
                    tonerAvailability.await();
                }
            }
            if (!passengers.isEmpty() & currentTonerLevel < MIN_TONER_LEVEL){
                System.out.println(ANSI_YELLOW + REFILL_WAITING + ANSI_RESET);
                currentTonerLevel += MAXIMUM_TONER_LEVEL;
                System.out.println(ANSI_PURPLE + TONER_REPLACED_MSG + ANSI_RESET);
                tonerRefillCount++;
                System.out.println(ANSI_BLUE + TONER_LEVEL_MSG + currentTonerLevel + ANSI_RESET);
                resourceAvailability.signalAll();
            }

        } catch (InterruptedException e) {
            System.out.println(ANSI_RED + REFILL_TONER_TECH_THREAD_INTERRUPTED_MSG + ANSI_RESET);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Helper method to enqueue passengers by adding them to the passengers list.
     *
     * @param tickets List of tickets representing passengers.
     */
    private void passengerQueue(List<Ticket> tickets) {
        passengers.addAll(tickets);
    }
}
