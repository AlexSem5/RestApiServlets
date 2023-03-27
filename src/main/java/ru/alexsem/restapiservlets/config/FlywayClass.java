package ru.alexsem.restapiservlets.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;

/**
 * Singleton
 */
public class FlywayClass {
    private static Flyway flyway;
    
    private FlywayClass() {
    }
    
    public static Flyway getInstance() {
        if (flyway == null) {
            flyway = Flyway.configure()
                           .dataSource(
                                   "jdbc:mysql://localhost:3306/restapiservlets?serverTimezone=UTC",
                                   "root",
                                   "password")
                           .load();
        }
        return flyway;
    }
}
