package com.educationalSystem.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("LIBRARIAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Librarian extends User{

}
