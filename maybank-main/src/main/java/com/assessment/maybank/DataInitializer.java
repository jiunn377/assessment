package com.assessment.maybank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${assessment.init.datasql.path}")
    private String initDataSqlPath;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running data initializer");
        executeSqlScript(initDataSqlPath);
    }

    private void executeSqlScript(String scriptPath) {
        Resource resource = resourceLoader.getResource("classpath:data.sql");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sql.append(line);

                // Assuming each SQL line command ends with a semicolon
                if (line.endsWith(";")) {
                    jdbcTemplate.execute(sql.toString());
                    // Clear the builder for the next SQL command
                    sql.setLength(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}