package com.ilecreurer.drools.samples.sample1;

import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.drools.core.time.SessionPseudoClock;

public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            LOGGER.info("*** Starting ***");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss.SSSZ");

            KieServices ks = KieServices.get();
            KieContainer kc = ks.getKieClasspathContainer();
            KieSession ksession = kc.newKieSession("Sample1KS");

            EntryPoint entryPointATM = ksession.getEntryPoint("ATM Stream");

            SessionPseudoClock clock = ksession.getSessionClock();
            long currentTime = clock.getCurrentTime();

            LOGGER.info("PseudoClock starting at {}", sdf.format(new Date(currentTime)));

            // The application can also setup listeners
            ksession.addEventListener( new DebugAgendaEventListener() );
            ksession.addEventListener( new DebugRuleRuntimeEventListener() );

            /*
            Thread thread = new Thread() {
                @Override
                public void run() {
                    System.out.println("Starting firing...");
                    ksession.fireUntilHalt();
                }
            };
            thread.start();*/

            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                LOGGER.info("Inserting event...");
                Event event = new Event();
                event.setMsg("hello" + i);
                event.setTimestamp(new Date());
                event.setStatus(0);
                entryPointATM.insert( event );

                long advanceTime = event.getTimestamp().getTime() - clock.getCurrentTime();

                if (advanceTime > 0) {
                    LOGGER.debug("Advancing time by {}", advanceTime);
                    clock.advanceTime(advanceTime, TimeUnit.MILLISECONDS);
                    LOGGER.debug("Clock time: {}", sdf.format(clock.getCurrentTime()));
                } else {
                    LOGGER.warn("Not advancing time. transaction time: {}, clock time: {}",
                            event.getTimestamp().getTime(),
                            clock.getCurrentTime());
                }
                ksession.fireAllRules();

                long factCount = entryPointATM.getFactCount();
                LOGGER.debug("fact count: {}", factCount);

            }

            for (int i = 0; i < 20; i++) {
                long factCount = entryPointATM.getFactCount();
                LOGGER.debug("** fact count: {}", factCount);
                Thread.sleep(1000);

                clock.advanceTime(1000, TimeUnit.MILLISECONDS);
                ksession.fireAllRules();
            }

            LOGGER.info("Halting session...");
            ksession.halt();

            // and then dispose the session
            ksession.dispose();


        } catch (Exception e) {

        }
    }

}
