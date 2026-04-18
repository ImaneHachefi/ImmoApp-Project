package main.immoapp;

import org.springframework.boot.SpringApplication;

public class TestImmoApp {

    public static void main(String[] args) {
        SpringApplication.from(ImmoApp::main).with(TestcontainersConfiguration.class).run(args);
    }

}
