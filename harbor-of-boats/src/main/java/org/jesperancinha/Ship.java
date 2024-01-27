package org.jesperancinha;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Ship {
    static private void waitForAllCyclicBarrierPartiesToFinish(CyclicBarrier cyclicBarrier, int id) throws BrokenBarrierException, InterruptedException, TimeoutException {
        out.printf("Cyclic barrier has %s parties and %s waiting%n", cyclicBarrier.getParties(), cyclicBarrier.getNumberWaiting());
        out.printf("Current is %s%n with id %d", Thread.currentThread(), id);
        cyclicBarrier.await(5, SECONDS);
        out.println("Cyclic Barrier passed!");
    }


    /**
     * It will send 15 Parties to perform on a cyclic barrier of 5
     * This means that the program will only end with multiples of 5
     * Something different than that and the program will halt performance
     * @param executorService {@link ExecutorService} The Executor service used
     * @param upperLimit {@link Integer} Upper limit of party generation
     */
    static public void performSweepTask(ExecutorService executorService, Integer upperLimit) {
        var cyclicBarrier = new CyclicBarrier(5);
        IntStream.range(0, upperLimit).forEach(id -> executorService.submit(() -> {
            try {
                waitForAllCyclicBarrierPartiesToFinish(cyclicBarrier, id);
            } catch (BrokenBarrierException | InterruptedException | TimeoutException e) {
                out.printf("Exception %s is thrown!%n", e);
                throw new RuntimeException(e);
            }
        }));
    }

}