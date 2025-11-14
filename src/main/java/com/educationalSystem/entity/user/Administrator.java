package com.educationalSystem.entity.user;

import com.educationalSystem.entity.parts.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrator extends User{
    private Long adminId;

    public void manageUsers(User user){

    }

    public boolean approveAccount(User user){
        return true;
    }

    public boolean deactivateAccount(User user){
        return true;
    }

    public void addRoom(Room room){

    }

    public void deleteRoom(Room room){

    }

    public void monitorRoomBookings(){

    }
}
