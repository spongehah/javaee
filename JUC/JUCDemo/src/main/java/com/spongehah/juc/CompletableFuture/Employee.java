package com.spongehah.juc.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer id;
    private String name;
    private Integer age;
    private Double salary;

    public static List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(1,"fer3", 20,6243.12),
                new Employee(2, "ewf3e", 20,7423.21 ),
                new Employee(3, "htr4", 20,5435.53),
                new Employee(4,"set34", 20,8543.12),
                new Employee(5, "j4ry5ger",6243, 6421.22)
        );
    }
}