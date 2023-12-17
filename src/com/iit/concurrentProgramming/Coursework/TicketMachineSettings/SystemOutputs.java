package com.iit.concurrentProgramming.Coursework.TicketMachineSettings;

/**
 * @Author Harini Rodrigo
 * @Version 1.0
 * @Since 27/11/2023
 * @Description: This interface encapsulates the system responses for the ticket printing machine.
 */
public class SystemOutputs {
    /**
     * Ticket Machine System Outputs
     */

    public static final String NO_PASSENGERS_MSG = "No passengers waiting.";
    public static final String PRINT_SKIP_TICKET_MSG = "Tickets requires more paper but paper technician unavailable. ";
    public static final String FINISHED_PRINTING = "Finished Printing Tickets.";
    public static final String NOT_PRINTABLE_MSG = "Cannot print ticket. Because";
    /**
     * Toner System Outputs
     */
    public static final String TONER_REPLACE_TRY_MSG = "Trying to replace toner.";
    public static final String TONER_NOT_REPLACEABLE_MSG = "Printer has enough toner. ";
    public static final String TONER_REPLACED_MSG = "Toner replaced successfully. ";
    public static final String TONER_LEVEL_MSG = "Current toner level: ";

    public static final String TONER_REPLACE_SKIP_MSG = "Skipping toner replacement since there are no passengers";

    /**
     * Paper System Outputs
     */
    public static final String REFILL_TRY_MSG = "Trying to refill paper.";
    public static final String SHEETS_IN_TRAY_MSG = "Printer has enough paper.";
    public static final String REFILL_COMPLETE_MSG = "Paper refilled successfully. ";
    public static final String PAPER_LEVEL_MSG = "Current paper level: ";
    public static final String PAPER_REFILL_SKIP_MSG = "Skipping paper refill since there are no passengers.";

    public static final String PRINTABLE = "PRINTABLE";
    public static final String INSUFFICIENT_TONER = " not enough toner";
    public static final String INSUFFICIENT_PAPER = " not enough paper";
    public static final String INSUFFICIENT_TONER_AND_PAPER = " insufficient toner and paper";

    /**
     * Thread group names
     */
    public static final String PASSENGER_GROUP_NAME = "Passenger Group";
    public static final String TECHNICIAN_GROUP_NAME = "Technicians Group";
    public static final String PAPER_TECH_NAME = "Paper Technicians";
    public static final String TONER_TECH_NAME = "Toner Technicians";
    public static final String PAPER_TECH_THREADS_NAME = "Paper Technicians Thread";
    public static final String TONER_TECH_THREADS_NAME = "Toner Technicians Thread";
}
