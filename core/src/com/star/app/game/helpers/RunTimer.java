package com.star.app.game.helpers;

import java.util.Timer;
import java.util.TimerTask;

public class RunTimer {

            TimerTask task = new TimerTask() {
                public void run() {
                    System.out.println("Knock-Knock!");
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, 3000);

            for ( int i = 0; i < 5; ++i ) {
                System.out.println("Do something...");
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ie) {}
            }

            System.exit(0);
        }
    }

