package com.iit.concurrentProgramming.Coursework.ticket.info;

import java.util.Date;

/**
 * @Author: Harini Rodrigo
 * @Version 1.0
 * @Since 26/11/2023
 * @Description: This class is used to store the travel information.
 */
@lombok.Getter
@lombok.ToString
@lombok.AllArgsConstructor
public class TravelInfo {
    private String origin;
    private String destination;
    private Date travelDate;
}
