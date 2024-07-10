package com.example.rest_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    private String name;

    private String number;

    private String category;

    public String toString() {
        return "name = " + this.name + "\nnumber = " + this.number + "\ncategory = " + this.category;
    }
}
