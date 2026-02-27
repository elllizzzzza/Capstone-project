package com.educationalSystem.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("LIBRARIAN")
@Data
@NoArgsConstructor
public class Librarian extends User{
}
