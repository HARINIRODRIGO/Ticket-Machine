package com.iit.concurrentProgramming.Coursework.ticket.machine;


import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Colors.ANSI_RESET;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.ErrorMessage.*;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.TicketMachine.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

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
        if (currentPaperLevel < PAPERS_PER_TICKET & currentTonerLevel < MIN_TONER_LEVEL) {
            return INSUFFICIENT_TONER_AND_PAPER;
        } else if (currentPaperLevel < PAPERS_PER_TICKET) {
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
                    System.out.println(threadMessages(NO_PASSENGERS_MSG,"Error"));
                    break;
                }
                else if(isResourceAvailable().equals(INSUFFICIENT_PAPER) & noPaperTechs()){
                        System.out.println(threadMessages(PAPER_REFILL_SKIP_MSG,"Error"));
                        break;
                    }
                else if (isResourceAvailable().equals(INSUFFICIENT_TONER) & noTonerTechs()) {
                    System.out.println(threadMessages(TONER_REFILL_SKIP_MSG,"Error"));
                    break;
                }
                resourceAvailability.await(1000, MILLISECONDS);
            }

            /*
                * Assuming only one paper and a toner will use when printing a ticket
             */
            if (!passengers.isEmpty() & currentTonerLevel >= TONERS_PER_TICKET & currentPaperLevel >= PAPERS_PER_TICKET) {
                this.currentTonerLevel -= TONERS_PER_TICKET;
                this.currentPaperLevel -= PAPERS_PER_TICKET;
                System.out.println(threadMessages(ticket.toString(),"Print Ticket"));
                passengers.remove(ticket);
                paperAvailability.signalAll();
                tonerAvailability.signalAll();
                resourceAvailability.signalAll();
                System.out.println(threadMessages(PRINTING_TICKET_MSG,"Success"));
            }
        } catch (InterruptedException e) {
            System.out.println(threadMessages(TICKET_PRINTING_THREAD_INTERRUPTED_MSG,"Warning"));
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
                    System.out.println(threadMessages(PAPER_REPLACE_SKIP_MSG,"Warning"));
                    break;
                }else if(noTonerTechs() & currentTonerLevel < TONERS_PER_TICKET){
                    System.out.println(threadMessages(IMPOSSIBLE_PAPER_REFILLING,"Error"));
                    break;
                }
                else {
                    System.out.println(threadMessages(SHEETS_IN_TRAY_MSG,"Info"));
                    paperAvailability.await(1000, MILLISECONDS);
                }
            }
            if (!passengers.isEmpty() & currentPaperLevel < PAPERS_PER_TICKET & !noPaperTechs() ) {
                System.out.println(ANSI_RESET +PAPER_REFILLING + ANSI_RESET);
                currentPaperLevel += SHEETS_PER_PACK;
                paperRefillCount++;
                paperAvailability.signalAll();
                resourceAvailability.signalAll();
                System.out.println(threadMessages(PAPER_REPLACED_MSG,"Success"));
            }
        } catch (InterruptedException e) {
            System.out.println(threadMessages(REFILL_TICKET_TECH_THREAD_INTERRUPTED_MSG,"Warning"));
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
                    System.out.println(threadMessages(TONER_REPLACE_SKIP_MSG,"Warning"));
                    break;
                } else if (noPaperTechs() & currentPaperLevel < PAPERS_PER_TICKET){
                    System.out.println(threadMessages(IMPOSSIBLE_TONER_REFILLING,"Error"));
                    break;
                } else{
                    System.out.println(threadMessages(TONER_NOT_REPLACEABLE_MSG,"Info"));
                    paperAvailability.await(1000, MILLISECONDS);
                }
            }
                if (!passengers.isEmpty() & currentTonerLevel < MIN_TONER_LEVEL & !noTonerTechs()) {
                System.out.println(ANSI_RESET + TONER_REFILLING + ANSI_RESET);
                currentTonerLevel += MAXIMUM_TONER_LEVEL;
                tonerRefillCount++;
                tonerAvailability.signalAll();
                resourceAvailability.signalAll();
                System.out.println(threadMessages(TONER_REPLACED_MSG,"Success"));
            }
        } catch (InterruptedException e) {
            System.out.println(threadMessages(REFILL_TONER_TECH_THREAD_INTERRUPTED_MSG,"Warning"));
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

    private String threadMessages(String mes, String mesType){
        String generatedMessage = switch (mesType) {
            case "Error" -> ANSI_RED + "[ Current Thread: " + Thread.currentThread().getName() + "]  " + mes;
            case "Info" -> ANSI_GREEN + "[ Current Thread: " + Thread.currentThread().getName() + "] " + mes;
            case "Warning" -> ANSI_YELLOW + "[ Current Thread: " + Thread.currentThread().getName() + "]  " + mes;
            case "Success" -> ANSI_BLUE + "[ Current Thread: " + Thread.currentThread().getName() + "]  " + mes;
            case "Print Ticket" -> ANSI_RESET + "[ Current Thread: " + Thread.currentThread().getName() + "]  " + mes;
            default -> "[ Current Thread: " + Thread.currentThread().getName() + "] .  " + mes;
        };
        generatedMessage += String.format(MESSAGE, passengers.size(), currentTonerLevel, currentPaperLevel, tonerRefillCount, paperRefillCount, (TONER_TECH_MAX_REFILL_COUNT * tonerTechGroup.activeCount()) - tonerRefillCount,(PAPER_TECH_MAX_REPLACE_COUNT * paperTecGroup.activeCount()) - paperRefillCount );
        return generatedMessage;
    }

    private boolean noPaperTechs() {
        return paperTecGroup.activeCount() == 0;
    }
    private boolean noTonerTechs() {
        return tonerTechGroup.activeCount() == 0;
    }
}
