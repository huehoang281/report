package com.project.spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemContract {

    @Id
    private BigInteger id;

    private String type;
    private String name;
    private BigInteger price;
    private BigInteger quantity;
}
