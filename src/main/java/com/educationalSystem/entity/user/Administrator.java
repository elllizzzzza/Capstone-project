package com.educationalSystem.entity.user;

import com.educationalSystem.entity.parts.Room;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrator extends User{

}
