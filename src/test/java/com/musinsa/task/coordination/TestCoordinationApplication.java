package com.musinsa.task.coordination;

import org.springframework.boot.SpringApplication;

public class TestCoordinationApplication {

    public static void main(String[] args) {
        SpringApplication.from(CoordinationApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
