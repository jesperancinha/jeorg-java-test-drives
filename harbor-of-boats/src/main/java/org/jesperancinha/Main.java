package org.jesperancinha;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = null;
        out.println("--------------------------------------------");
        out.println("--- Starting first test with a multiple of 5");
        out.println("--------------------------------------------");
        try {
            service = Executors.newCachedThreadPool();
            Ship.performSweepTask(service, 10);
        } finally {
            Objects.requireNonNull(service).shutdown();
        }
        var terminated = service.awaitTermination(1, SECONDS);
        assert terminated;
        out.println("--------------------------------------------");
        out.println("--- Starting first test with a non multiple of 5");
        out.println("--------------------------------------------");
        try {
            service = Executors.newCachedThreadPool();
            Ship.performSweepTask(service, 16);
        } finally {
            Objects.requireNonNull(service).shutdown();
        }
        service.shutdownNow();
        terminated = service.awaitTermination(1, SECONDS);
        assert terminated;
    }
}