package com.epam.esm.gcs.persistence.testapplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.epam.esm.gcs")
@EntityScan(basePackages = "com.epam.esm.gcs.persistence")
public class TestApplication {
}
