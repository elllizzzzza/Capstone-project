package com.educationalSystem.entity.user;

import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.entity.parts.BorrowRecord;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Librarian extends User{
    private Long librarianId;

    public void addBook(Book book){

    }

    public void updateBook(Book book){

    }

    public void removeBook(Book book){

    }

    public void manageBorrowing(BorrowRecord record){

    }

    public void viewOverdueItems(){

    }
}
