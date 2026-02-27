package com.educationalSystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookConfig {

    @Value("#{'${app.book.categories}'.split('-')}")
    private String[] categories;

    public String[] getCategories(){
        return categories;
    }
}
