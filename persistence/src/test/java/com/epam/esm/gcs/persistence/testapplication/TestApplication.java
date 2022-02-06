package com.epam.esm.gcs.persistence.testapplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootApplication
@ActiveProfiles(profiles = "test")
@ComponentScan(basePackages = "com.epam.esm.gcs")
public class TestApplication {
}
