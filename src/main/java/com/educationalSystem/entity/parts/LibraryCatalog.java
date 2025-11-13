package com.educationalSystem.entity.parts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryCatalog {
    private List<Book> books;

    public List<Book> searchBook(String title){
        return new ArrayList<>();
    }

    public boolean checkAvailability(Book book){
        return false;
    }
}
