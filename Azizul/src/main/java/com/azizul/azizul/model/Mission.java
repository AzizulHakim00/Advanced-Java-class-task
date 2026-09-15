package com.azizul.azizul.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mission")
public class Mission {

    @Id
    private String id;

    private String name;

    private String agency;

    @Field(targetType = FieldType.STRING)
    private LocalDate date;

    private Orbit orbit;

    private Status status;
}