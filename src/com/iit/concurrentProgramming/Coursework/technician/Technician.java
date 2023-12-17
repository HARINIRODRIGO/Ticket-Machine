package com.iit.concurrentProgramming.Coursework.technician;

/**
 * @Author: Harini Rodrigo
 * @Version 1.0
 * @Since: 26/11/2023
 * @Description: This interface defines the behavior of a technician.<br>
 * It extends the Runnable interface to enable running as a thread.
 */
public interface Technician extends Runnable {

    /**
     * Method signature for servicing the machine.
     */
    void serviceMachine();
}
