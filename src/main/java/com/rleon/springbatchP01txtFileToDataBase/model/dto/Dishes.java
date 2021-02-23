package com.rleon.springbatchP01txtFileToDataBase.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Dishes {

    private String name;
    private String origin;
    private String characteristics;

}
