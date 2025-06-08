package pl.impreska.dto;

import pl.impreska.model.AttendanceStatus;
import pl.impreska.model.DrinkingStatus;

public class UpdateParticipantStatusRequest {
    private AttendanceStatus attendanceStatus;
    private DrinkingStatus drinkingStatus;
    private String drink;

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public DrinkingStatus getDrinkingStatus() {
        return drinkingStatus;
    }

    public void setDrinkingStatus(DrinkingStatus drinkingStatus) {
        this.drinkingStatus = drinkingStatus;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }
}
