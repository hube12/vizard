package com.seedfinding.neil;

import com.seedfinding.neil.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class GenController {
    public long time = 1000;
    public static List<Pair<Thread, TransferQueue<String>>> pairs = new ArrayList<>();
    public final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> future;

    public GenController() {

    }

    public long getTime() {
        return time;
    }

    public void register(Thread thread, TransferQueue<String> transferQueue) {
        pairs.add(new Pair<>(thread, transferQueue));
    }


    public boolean start() {
        if (future != null) {
            return false;
        }
        future = scheduler.scheduleAtFixedRate(GenController::step, time, time, TimeUnit.MILLISECONDS);
        return true;
    }

    public static boolean clearInactiveThreads() {
        return removeThreads(e -> !e.getFirst().isAlive());

    }

    public void setTime(long time) {
        this.time = time;
    }

    public static boolean clearThreads() {
        return removeThreads(e -> true);
    }

    public static boolean removeThreads(Predicate<Pair<Thread, TransferQueue<String>>> filter) {
        boolean removed = false;
        final Iterator<Pair<Thread, TransferQueue<String>>> each = pairs.iterator();
        while (each.hasNext()) {
            Pair<Thread, TransferQueue<String>> current = each.next();
            if (filter.test(current)) {
                current.getSecond().tryTransfer("kill");
                current.getFirst().interrupt();
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static void step() {
        // test if a thread has not finish yet his construction
        // technically not needed since transfer is blocking
        boolean notDone = false;
        while (!notDone) {
            notDone = true;
            for (Pair<Thread, TransferQueue<String>> pair : pairs) {
                Thread thread = pair.getFirst();
                if (thread.isAlive()) {
                    TransferQueue<String> transferQueue = pair.getSecond();
                    if (transferQueue.getWaitingConsumerCount() != 1) {
                        notDone = false;
                    }
                    if (transferQueue.getWaitingConsumerCount() > 1) {
                        throw new RuntimeException("STOP RIGHT HERE");
                    }
                }
            }
        }
        for (Pair<Thread, TransferQueue<String>> pair : pairs) {
            Thread thread = pair.getFirst();
            if (thread.isAlive()) {
                TransferQueue<String> transferQueue = pair.getSecond();
                if (transferQueue.getWaitingConsumerCount() != 1) {
                    throw new RuntimeException("ERROR NOT POSSIBLE, THREAD IS UNSTABLE, CONTINUUM IS BREAKING APART");
                }
                boolean res = transferQueue.tryTransfer("go");
                if (!res) {
                    throw new RuntimeException("ERROR NOT POSSIBLE, THREAD IS NOT RESPONDING, CONTINUUM IS BREAKING APART");
                }
            }
        }
        clearInactiveThreads();
    }

    public int stop() {
        // -1 not ok, 0 ok, 1 no task left
        if (future == null) {
            return -1;
        }
        if (future.cancel(false)) {
            future = null;
            return 0;
        }
        future = null;
        return 1;
    }


    public static void reset(){
        Instance.genController.stop();
        GenController.clearThreads();
    }
}
