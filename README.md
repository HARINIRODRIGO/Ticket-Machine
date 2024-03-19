<div id="message-text-e913e1c0-8931-4b5e-8674-72c04fa0c6ba" class="markdown prose w-full flex flex-col break-words dark:prose-invert"><h1>Ticket-Machine: Java Implementation of the FSP Model</h1>
<h2>1. TicketMachine Class</h2>
<p>The <code>TicketMachine</code> class serves as an implementation of the <code>ServiceTicketMachine</code> interface. It leverages the <code>Ticket</code> class to represent tickets and manages ticket paper and toner levels. The implementation ensures the capability to print tickets and facilitates the refilling process. The monitor pattern is employed to guarantee mutual exclusion, enhancing the concurrent execution of threads.</p>
<h2>2. Passenger Class</h2>
<p>The <code>Passenger</code> class is designed to represent individuals engaged in purchasing and printing tickets. This class utilizes the <code>Ticket</code> class to store and convey ticket information. The implementation includes random sleep intervals between printing requests, simulating real-world scenarios.</p>
<h2>3. TicketPaperTechnician Class</h2>
<p>The <code>TicketPaperTechnician</code> class emulates a technician responsible for refilling the Ticket Machine's paper supply. The class attempts to refill paper three times, incorporating random sleep intervals to simulate varying real-world conditions.</p>
<h2>4. TicketTonerTechnician Class</h2>
<p>The <code>TicketTonerTechnician</code> class represents a technician tasked with replacing the Ticket Machine's toner cartridge. Similar to the paper technician, toner replacement attempts are made three times with random sleep intervals to mimic realistic operational challenges.</p>
<h2>5. TicketPrintingSystem</h2>
<p>The <code>TicketPrintingSystem</code> class serves as the orchestrator, coordinating the interactions among the <code>TicketMachine</code>, <code>Passengers</code>, and <code>Technicians</code>. This involves creating necessary threads and thread groups to simulate concurrent execution. Additionally, it ensures all threads complete execution before printing the final Ticket Machine status, providing a comprehensive overview of the system's state.</p>
<h3>Usage</h3>
<p>To utilize the Ticket Printing System, instantiate the <code>TicketPrintingSystem</code> class, which will automatically initiate the threads for <code>TicketMachine</code>, <code>Passengers</code>, and <code>Technicians</code>. Observe the final Ticket Machine status after the completion of all threads.</p>
<h3>Implementation Details</h3>
<p>The Java implementation adheres to the Formal Methods Specification (FSP) model, emphasizing correctness and synchronization through the monitor pattern. The use of random sleep intervals adds a touch of realism to the simulation, enhancing the robustness of the system under diverse conditions.</p>

<hr>
</div>
