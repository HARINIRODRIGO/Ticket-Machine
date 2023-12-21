package com.iit.concurrentProgramming.Coursework.ticket_info;

/**
 * @Author: Harini Rodrigo
 * @Version: 1.0
 * @Since: 26/11/2023
 * @Description: This class is used to store the passenger information.
 */
@lombok.Getter
@lombok.AllArgsConstructor
public class PassengerInfo {
    private String name;
    private String IDNumber;
    private String gender;
    private String phoneNumber;
    private String email;

    @Override
    public String toString() {
        return  "name='" + name + '\'' +
                ", ID Number='" + IDNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", phone Number='" + phoneNumber + '\'' +
                ", email='" + email;
    }
}
