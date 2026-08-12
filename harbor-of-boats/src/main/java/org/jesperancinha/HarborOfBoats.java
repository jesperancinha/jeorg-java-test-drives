package org.jesperancinha;

import java.util.concurrent.Executors;

import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.SECONDS;

public class HarborOfBoats {
    public static void main(String[] args) throws InterruptedException {
        try (var service = Executors.newCachedThreadPool()) {
            out.println("--------------------------------------------");
            out.println("--- Starting first test with a multiple of 5");
            out.println("--------------------------------------------");
            Ship.performSweepTask(service, 10);
            service.shutdown();
            var terminated = service.awaitTermination(1, SECONDS);
            assert terminated;
        }
        try (var service = Executors.newCachedThreadPool()) {
            out.println("--------------------------------------------");
            out.println("--- Starting first test with a non multiple of 5");
            out.println("--------------------------------------------");
            Ship.performSweepTask(service, 16);
            service.shutdown();
            service.shutdownNow();
            var terminated = service.awaitTermination(1, SECONDS);
            assert terminated;
        }
    }
}