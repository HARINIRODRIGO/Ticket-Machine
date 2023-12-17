package com.iit.concurrentProgramming.Coursework.TicketMachine.TicketDetailInfo;

/**
 * @Author: Harini Rodrigo
 * @Project: Concurrent Programming Coursework
 * @Version 1.0
 * @Since: 26/11/2023
 * @Description: This class is used to store the passenger information
 * */
@lombok.Getter
@lombok.ToString
@lombok.AllArgsConstructor

public class PassengerInfo {
private String name;
private String IDNumber;
private String gender;
private String phoneNumber;
private String email;
}
