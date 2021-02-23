package com.rleon.springbatchP01txtFileToDataBase.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;;

@Data
@ToString
@AllArgsConstructor
public class Dishes {

    private String name;
    private String country;
    private String characteristics;

}
