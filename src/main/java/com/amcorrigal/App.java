package com.amcorrigal;

import java.awt.*;
import java.util.Date;

public class App {

    private Robot robot;
    private final int jiggleDelayMinutes;
    private final long jiggleDelayMS;
    private final int jiggleTimeMS;
    private final int jiggleDistancePixels;

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Need 3 arguments: [jiggle delay in minutes] [jiggle time in ms] [jiggle distance in pixels]");
            System.out.println("Exiting.");
            System.exit(1);
        }
        new App(
                Integer.valueOf(args[0]),
                Integer.valueOf(args[1]),
                Integer.valueOf(args[2])).run();
    }

    public App(int jiggleDelayMinutes, int jiggleTimeMS, int jiggleDistancePixels) {
        this.jiggleDelayMinutes = jiggleDelayMinutes;
        this.jiggleTimeMS = jiggleTimeMS;
        this.jiggleDistancePixels = jiggleDistancePixels;
        jiggleDelayMS = convertJiggleDelayToMS(jiggleDelayMinutes);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Can not run app: " + e.getMessage());
            System.exit(1);
        }
    }

    private long convertJiggleDelayToMS(int jiggleDelay) {
        return jiggleDelay * 60 * 1000;
    }

    public void run() {
        System.out.println("Running Jiggle (Ctrl-c to kill) ...");
        while (true) {
            try {
                performJiggle();
                sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep() throws InterruptedException {
        System.out.println(String.format("[*] Sleeping for %d minute(s)", jiggleDelayMinutes));
        long startTime = System.currentTimeMillis();
        long timeNow = startTime;
        long runTime = calculateRunTime(startTime, timeNow);
        while (runTime < (jiggleDelayMS - 1000)) {
            timeNow = System.currentTimeMillis();
            runTime = calculateRunTime(startTime, timeNow);
            long remainingTimeSeconds = (jiggleDelayMS - runTime) / 1000;
            System.out.println("[*] Time until next Jiggle: " + remainingTimeSeconds);
            Thread.sleep(1000);
        }
    }

    private long calculateRunTime(long startTime, long timeNow) {
        return timeNow - startTime;
    }

    private void performJiggle() throws InterruptedException {
        move(1);
        Thread.sleep(jiggleTimeMS);
        move(-1);
        System.out.println(String.format("[*] Just Jiggled @ %s", new Date().toString()));
    }

    private void move(int direction) {
        Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
        int xMovement = currentMouseLocation.x + (jiggleDistancePixels * direction);
        int yMovement = currentMouseLocation.y - (jiggleDistancePixels * direction);
        robot.mouseMove(xMovement, yMovement);
    }

}
