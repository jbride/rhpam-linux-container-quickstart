package com.redhat.naps.service.commands;


import java.util.Date;

import org.kie.api.executor.Command;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;
import org.kie.api.executor.Reoccurring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloExecutorCommand implements Command, Reoccurring {

    private final static Logger log = LoggerFactory.getLogger(HelloExecutorCommand.class);

    private long nextScheduleTimeAdd = Long.parseLong(System.getProperty("com.redhat.naps.helloExecutorCommand.nextScheduleTimeAdd.millis"));


    @Override
    public ExecutionResults execute(CommandContext ctx) throws Exception {

        ExecutionResults executionResults = new ExecutionResults();
        log.info("execute() number of data elements in ctx = "+ctx.getData().size());
        return executionResults;
    }

    @Override
    public Date getScheduleTime() {

        log.info("nextScheduleTimeAdd = "+nextScheduleTimeAdd);
        if (nextScheduleTimeAdd < 0) {
            return null;
        }

        long current = System.currentTimeMillis();

        Date nextSchedule = new Date(current + nextScheduleTimeAdd);
        log.info("getScheduleTime() Next schedule for job {} is set to {}", this.getClass().getSimpleName(), nextSchedule);

        return nextSchedule;
    }
    
}
