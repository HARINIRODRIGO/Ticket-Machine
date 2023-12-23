package com.iit.concurrentProgramming.Coursework.ticket_machine;


import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.ANSI_RESET;
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
@lombok.RequiredArgsConstructor
public class TicketMachine implements ServiceTicketMachine, Printer {

    private int currentPaperLevel = 0;
    private int currentTonerLevel = 0;
    private int tonerRefillCount = 0;
    private int paperRefillCount = 0;

    private final ThreadGroup paperTecGroup;
    private final ThreadGroup tonerTechGroup;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition tonerAvailability = lock.newCondition();
    private final Condition paperAvailability = lock.newCondition();
    private final Condition resourceAvailability = lock.newCondition();
    public static ArrayList<Ticket> passengers = new ArrayList<>();
    public TicketMachine(List<Ticket> passengers, ThreadGroup paperTec, ThreadGroup tonerTec){
        passengerQueue(passengers);
        this.paperTecGroup = paperTec;
        this.tonerTechGroup = tonerTec;
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
        if (currentPaperLevel < MIN_PAPER_LEVEL & currentTonerLevel < MIN_TONER_LEVEL) {
            return INSUFFICIENT_TONER_AND_PAPER;
        } else if (currentPaperLevel < MIN_PAPER_LEVEL) {
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
                    threadErrorMessage(NO_PASSENGERS_MSG,true);
                    break;
                }
                else if(isResourceAvailable().equals(INSUFFICIENT_PAPER)) {

                    if(noPaperTechs()){ threadErrorMessage(PAPER_REFILL_SKIP_MSG,true);
                        break;
                    }
                } else if (isResourceAvailable().equals(INSUFFICIENT_TONER) & noTonerTechs()) {
                        threadErrorMessage(TONER_REFILL_SKIP_MSG,true);
                        break;
                }
                resourceAvailability.await();
            }

            if (!passengers.isEmpty() & currentTonerLevel >= MIN_TONER_LEVEL & currentPaperLevel >= MIN_PAPER_LEVEL) {
                this.currentTonerLevel -= TONER_PER_TICKET;
                this.currentPaperLevel -= MIN_PAPER_LEVEL;
                System.out.println(ANSI_GREEN + ticket + ANSI_RESET);
                passengers.remove(ticket);
                paperAvailability.signalAll();
                tonerAvailability.signalAll();
                resourceAvailability.signalAll();
            }
        } catch (InterruptedException e) {
            threadErrorMessage(TICKET_PRINTING_THREAD_INTERRUPTED_MSG ,true);

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
        try {
            lock.lock();

            while ((currentPaperLevel + SHEETS_PER_PACK) >= MAX_PAPERS) {
                if (passengers.isEmpty()) {
                    threadErrorMessage(PAPER_REPLACE_SKIP_MSG,true);
                    break;
                }else if(noTonerTechs() & currentTonerLevel < MIN_TONER_LEVEL){
                    threadErrorMessage(PAPER_REPLACE_SKIP_MSG,true);
                    break;
                }
                else {
                    tecThreadMessages(SHEETS_IN_TRAY_MSG);
                    paperAvailability.await();
                }
            }
            if (!passengers.isEmpty() & currentPaperLevel < MIN_PAPER_LEVEL & !noPaperTechs() ) {
                System.out.println(PAPER_REFILLING );
                currentPaperLevel += SHEETS_PER_PACK;
                paperRefillCount++;
                paperAvailability.signalAll();
                resourceAvailability.signalAll();
                tecThreadMessages((PAPER_REPLACED_MSG));
            }
        } catch (InterruptedException e) {
            threadErrorMessage(REFILL_TICKET_TECH_THREAD_INTERRUPTED_MSG,true);
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

        try {
            lock.lock();
            while (currentTonerLevel >= MIN_TONER_LEVEL) {
                if (passengers.isEmpty()) {
                    threadErrorMessage(TONER_REPLACE_SKIP_MSG,true);
                    break;
                } else if (noPaperTechs() & currentPaperLevel < MIN_PAPER_LEVEL){
                    threadErrorMessage(TONER_REPLACE_SKIP_MSG,true);
                    break;
                } else{
                    threadErrorMessage((TONER_NOT_REPLACEABLE_MSG ),false);
                    tonerAvailability.await();
                }
            }
                if (!passengers.isEmpty() & currentTonerLevel < MIN_TONER_LEVEL & !noTonerTechs()) {
                System.out.println(TONER_REFILLING);
                currentTonerLevel += MAXIMUM_TONER_LEVEL;
                tonerRefillCount++;
                tonerAvailability.signalAll();
                resourceAvailability.signalAll();
                tecThreadMessages((TONER_REPLACED_MSG));
            }
        } catch (InterruptedException e) {
            threadErrorMessage(REFILL_TONER_TECH_THREAD_INTERRUPTED_MSG,true);

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

    private void threadErrorMessage(String str, boolean special){
        System.out.println((special ? ANSI_RED : ANSI_RESET)
                    + str + " | Paper Tec Attempts:" + paperRefillCount + " | Toner Tec Attempts: " + tonerRefillCount+ " | Remaining passengers: " + passengers.size() + " | Remaining Paper Level: " + currentPaperLevel + " | Remaining Toner Level: " + currentTonerLevel + ANSI_RESET);
        }
    private void tecThreadMessages(String str) {
        System.out.println( (ANSI_BLUE + "[" + Thread.currentThread().getName() + "]: "
                        + str
                        + " | Waiting Passengers Count: " + passengers.size() +" | "+ PAPER_LEVEL_MSG + currentPaperLevel +" | "+TONER_LEVEL_MSG + currentTonerLevel + " | Paper Techs Count: "
                        + paperTecGroup.activeCount() + " | Toner Techs Count: " + tonerTechGroup.activeCount() +" |"+ "| Refilled Paper count: " +paperRefillCount + " | Refilled Toner count: " +tonerRefillCount +
                        ANSI_RESET)
                );
    }
    private boolean noPaperTechs() {
        return paperTecGroup.activeCount() == 0;
    }
    private boolean noTonerTechs() {
        return tonerTechGroup.activeCount() == 0;
    }
}
