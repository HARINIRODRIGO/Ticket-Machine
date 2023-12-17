# Ticket-Machine
## Java Implementation of the FSP Model

(1) TicketMachine Class:

Implement the ServiceTicketMachine interface.
Use the Ticket class for representing tickets.
Manage ticket paper and toner levels, allowing printing tickets and refilling.
Implement the monitor pattern to ensure mutual exclusion.
(2) Passenger Class:

Represent a passenger purchasing and printing tickets.
Utilize the Ticket class for ticket information.
Use random sleep intervals between printing requests.
(3) TicketPaperTechnician Class:

Represent a paper technician refilling the Ticket Machine.
Attempt to refill paper three times with random sleep intervals.
(4) TicketTonerTechnician Class:

Represent a toner technician replacing the Ticket Machine's toner cartridge.
Attempt toner replacement three times with random sleep intervals.
(5) TicketPrintingSystem :

Coordinate all components, including Ticket Machine, Passengers, and Technicians.
Create necessary threads and thread groups.
Ensure all threads complete execution before printing the final Ticket Machine status.
