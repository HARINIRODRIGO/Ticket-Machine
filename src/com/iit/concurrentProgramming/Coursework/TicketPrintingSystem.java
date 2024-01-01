package com.iit.concurrentProgramming.Coursework;

import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.TicketMachine.*;

import com.iit.concurrentProgramming.Coursework.technician.TicketTechnician;
import com.iit.concurrentProgramming.Coursework.ticket.info.PassengerInfo;
import com.iit.concurrentProgramming.Coursework.technician.TonerTechnician;
import com.iit.concurrentProgramming.Coursework.ticket.info.TravelInfo;
import com.iit.concurrentProgramming.Coursework.ticket.machine.Passengers;
import com.iit.concurrentProgramming.Coursework.ticket.machine.Ticket;
import com.iit.concurrentProgramming.Coursework.ticket.machine.TicketMachine;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class TicketPrintingSystem {

    private static final List<Ticket> ticketList = getTickets();

    public static void main(String[] args) {

        ThreadGroup passengerGroup = new ThreadGroup(PASSENGER_GROUP_NAME);
        ThreadGroup tonerTecGroup = new ThreadGroup(TONER_TEC_GROUP_NAME);
        ThreadGroup paperTecGroup = new ThreadGroup(PAPER_TEC_GROUP_NAME);
        TicketMachine ticketPrinter = new TicketMachine(ticketList, paperTecGroup, tonerTecGroup);
        TonerTechnician ticketTonerTechnician = new TonerTechnician(PAPER_TECH_NAME, ticketPrinter);
        TicketTechnician ticketPaperTechnician = new TicketTechnician(TONER_TECH_NAME, ticketPrinter);

        List<Passengers> passengersList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            passengersList.add(new Passengers(ticketPrinter, ticket));
        }
        List<Thread> passengerThreads = new ArrayList<>();
        for (int i = 0; i < passengersList.size(); i++) {
            passengerThreads.add(new Thread(passengerGroup, passengersList.get(i), ticketList.get(i).getPassengerInfo().getName()));
        }

        Thread tonerTechnicianThread1 = new Thread(tonerTecGroup, ticketTonerTechnician, TONER_TECH_THREADS_NAME);
        Thread paperTechnicianThread1 = new Thread(paperTecGroup, ticketPaperTechnician, PAPER_TECH_THREADS_NAME);
        Thread tonerTechnicianThread2 = new Thread(tonerTecGroup, ticketTonerTechnician, TONER_TECH_THREADS_NAME);
        Thread paperTechnicianThread2 = new Thread(paperTecGroup, ticketPaperTechnician, PAPER_TECH_THREADS_NAME);
        for (Thread thread : passengerThreads) {
            thread.start();
        }

        tonerTechnicianThread1.start();
        paperTechnicianThread1.start();
        //paperTechnicianThread2.start();
        //tonerTechnicianThread2.start();
    }

    public static List<Ticket> getTickets() {
        List<PassengerInfo> passengerInfoList = new ArrayList<>() {
            {
                add(new PassengerInfo("John", "12787987888V", "male", "0717894566", "john@gmail.com"));
                add(new PassengerInfo("Alice", "1548787445V", "female", "0718765432", "alice@gmail.com"));
                add(new PassengerInfo("Bob", "15447897445V", "male", "0719876543", "bob@gmail.com"));
                add(new PassengerInfo("Eva", "1548787445V", "female", "0717654321", "eva@gmail.com"));
                add(new PassengerInfo("Michael", "1548787445V", "male", "0716543210", "michael@gmail.com"));
                add(new PassengerInfo("Sophia", "14878791548V", "female", "0715432109", "sophia@gmail.com"));
                add(new PassengerInfo("Daniel", "4878721364V", "male", "0714321098", "daniel@gmail.com"));
                add(new PassengerInfo("Emma", "78645314681V", "female", "0713210987", "emma@gmail.com"));
                add(new PassengerInfo("William", "487741478784V", "male", "0712109876", "william@gmail.com"));
                add(new PassengerInfo("Olivia", "0770123456", "female", "0710987654", "olivia@gmail.com"));
            }
        };
        List<TravelInfo> travelInfoList = new ArrayList<>() {
            {
                add(new TravelInfo("Colombo", "Kandy", Date.valueOf("2023-11-26")));
                add(new TravelInfo("Galle", "Jaffna", Date.valueOf("2023-11-27")));
                add(new TravelInfo("Nuwara Eliya", "Trincomalee", Date.valueOf("2023-11-28")));
                add(new TravelInfo("Matara", "Anuradhapura", Date.valueOf("2023-11-29")));
                add(new TravelInfo("Badulla", "Polonnaruwa", Date.valueOf("2023-11-30")));
                add(new TravelInfo("Kurunegala", "Batticaloa", Date.valueOf("2023-12-01")));
                add(new TravelInfo("Kegalle", "Mannar", Date.valueOf("2023-12-02")));
                add(new TravelInfo("Ratnapura", "Ampara", Date.valueOf("2023-12-03")));
                add(new TravelInfo("Hambantota", "Vavuniya", Date.valueOf("2023-12-04")));
                add(new TravelInfo("Kalutara", "Kilinochchi", Date.valueOf("2023-12-05")));

            }
        };

        return new ArrayList<>() {
            {
                add(new Ticket(new BigDecimal("50.00"), UUID.randomUUID(), travelInfoList.get(0), passengerInfoList.get(0)));
                add(new Ticket(new BigDecimal("65.00"), UUID.randomUUID(), travelInfoList.get(1), passengerInfoList.get(1)));
                add(new Ticket(new BigDecimal("45.50"), UUID.randomUUID(), travelInfoList.get(2), passengerInfoList.get(2)));
                add(new Ticket(new BigDecimal("75.25"), UUID.randomUUID(), travelInfoList.get(3), passengerInfoList.get(3)));
                add(new Ticket(new BigDecimal("60.75"), UUID.randomUUID(), travelInfoList.get(4), passengerInfoList.get(4)));
                add(new Ticket(new BigDecimal("55.50"), UUID.randomUUID(), travelInfoList.get(5), passengerInfoList.get(5)));
                add(new Ticket(new BigDecimal("80.00"), UUID.randomUUID(), travelInfoList.get(6), passengerInfoList.get(6)));
                add(new Ticket(new BigDecimal("70.25"), UUID.randomUUID(), travelInfoList.get(7), passengerInfoList.get(7)));
                add(new Ticket(new BigDecimal("48.90"), UUID.randomUUID(), travelInfoList.get(8), passengerInfoList.get(8)));
                add(new Ticket(new BigDecimal("62.50"), UUID.randomUUID(), travelInfoList.get(9), passengerInfoList.get(9)));

            }
        };
    }
}
