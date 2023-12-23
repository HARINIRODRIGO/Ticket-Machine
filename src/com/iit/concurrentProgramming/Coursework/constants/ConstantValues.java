package com.iit.concurrentProgramming.Coursework.constants;
import static com.iit.concurrentProgramming.Coursework.constants.ConstantValues.Constants.Global.UTILITY_CLASS;


/**
 * @Author Harini Rodrigo
 * @Version 1.0
 * @Since 27/11/2023
 * @Description: This interface encapsulates the system responses for the ticket printing machine.
 */
public final class ConstantValues {

    /**
     * Inner class to hold constants related to the system.
     */
    public static final class Constants {
        private Constants() {
            throw new IllegalStateException(UTILITY_CLASS);
        }

        /**
         * Inner class for error messages.
         */
        public static final class ErrorMessage {
            private ErrorMessage() {
                throw new IllegalStateException(UTILITY_CLASS);
            }

            public static final String REFILL_TICKET_TECH_THREAD_INTERRUPTED_MSG = "Paper refill thread interrupted.";
            public static final String PASSENGER_THREAD_INTERRUPTED_MSG = "Passenger thread interrupted.";
            public static final String REFILL_TONER_TECH_THREAD_INTERRUPTED_MSG = "Toner refill thread interrupted.";
            public static final String PAPER_REFILL_SKIP_MSG = "Maximum paper refill count reached. Paper technician unavailable.";
            public static final String TONER_REFILL_SKIP_MSG = "Maximum toner refill count reached. Toner technician unavailable.";
        }

        /**
         * Inner class for ticket machine constants.
         */
        public static final class TicketMachine {

            private TicketMachine() {
                throw new IllegalStateException(UTILITY_CLASS);
            }

            // Constants related to paper and toner levels and technician replacement counts.
            public static final int SHEETS_PER_PACK = 8;
            public static final int MAX_PAPERS = 10;
            public static final int MAXIMUM_TONER_LEVEL = 20;
            public static final int MIN_PAPER_LEVEL = 4;
            public static final int MIN_TONER_LEVEL = 10;
            public static final int TONER_PER_TICKET = 4;
            public static final int PAPER_TECH_MAX_REPLACE_COUNT = 3;
            public static final int TONER_TECH_MAX_REFILL_COUNT = 3;
            public static final int PAPER_TECH_WAITING_TIME = 1000;
            public static final int TONER_TECH_WAITING_TIME = 1000;
            // Thread group and technician names
            public static final String PASSENGER_GROUP_NAME = "Passenger Group";
            public static final String TONER_TEC_GROUP_NAME = "Toner Technicians Group";
            public static final String PAPER_TEC_GROUP_NAME = "Paper Technicians Group";
            public static final String PAPER_TECH_NAME = "Paper Technicians";
            public static final String TONER_TECH_NAME = "Toner Technicians";

            // Technician thread names
            public static final String PAPER_TECH_THREADS_NAME = "Paper Technicians Thread";
            public static final String TONER_TECH_THREADS_NAME = "Toner Technicians Thread";

            // Insufficient resources messages.
            public static final String INSUFFICIENT_TONER = " not enough toner";
            public static final String INSUFFICIENT_PAPER = " not enough paper";
            public static final String INSUFFICIENT_TONER_AND_PAPER = " insufficient toner and paper";

            // Paper refill messages.
            public static final String PAPER_REPLACED_MSG = "Paper refilled successfully. ";
            public static final String PAPER_LEVEL_MSG = "Current paper level: ";
            public static final String SHEETS_IN_TRAY_MSG = "Printer has enough paper.";
            public static final String PAPER_REPLACE_SKIP_MSG = "Skipping paper replacement since there are no passengers";
            public static final String PAPER_REFILLING = "Paper refilling in progress.... ";
            // Toner refill messages.
            public static final String TONER_NOT_REPLACEABLE_MSG = "Printer has enough toner. ";
            public static final String TONER_REPLACED_MSG = "Toner replaced successfully. ";
            public static final String TONER_LEVEL_MSG = "Current toner level: ";
            public static final String TONER_REPLACE_SKIP_MSG = "Skipping toner replacement since there are no passengers";
            public static final String TONER_REFILLING = "Toner refilling in progress.... ";
            // Print ticket messages.
            public static final String PRINTABLE = "Printable";
            public static final String TICKET_PRINTING_THREAD_INTERRUPTED_MSG = "Printing thread interrupted.";

            public static final String NO_PASSENGERS_MSG = "No passengers waiting.";

        }

        /**
         * Inner class for global constants.
         */
        public static final class Global {
            public static final String UTILITY_CLASS = "Utility class";

            private Global() {
                throw new IllegalStateException(UTILITY_CLASS);
            }
        }

        /**
         * Inner class for system colors.
         */
        public static final class Colors {
            private Colors() {
                throw new IllegalStateException(UTILITY_CLASS);
            }

            public static final String ANSI_RESET = "\u001B[0m";
            public static final String ANSI_RED = "\u001B[31m";
            public static final String ANSI_GREEN = "\u001B[32m";
            public static final String ANSI_BLUE = "\u001B[34m";
        }
    }

}
