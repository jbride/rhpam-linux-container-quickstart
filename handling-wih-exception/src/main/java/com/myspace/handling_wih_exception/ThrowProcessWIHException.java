package com.myspace.handling_wih_exception;

import org.kie.api.runtime.process.ProcessWorkItemHandlerException;
import org.kie.api.runtime.process.ProcessWorkItemHandlerException.HandlingStrategy;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;


public class ThrowProcessWIHException implements WorkItemHandler {

    private static final String COUNTER = "COUNTER";
    
    private String processId;
    private HandlingStrategy strategy;
    private int retries;
    
    public ThrowProcessWIHException(String processId, HandlingStrategy strategy) {
        super();
        this.processId = processId;
        this.strategy = strategy;
    }
    
    public ThrowProcessWIHException(String processId, String strategy) {
        super();
        this.processId = processId;
        this.strategy = HandlingStrategy.valueOf(strategy);
    }

    public ThrowProcessWIHException(String processId, String strategy, int retries) {
        super();
        this.processId = processId;
        this.strategy = HandlingStrategy.valueOf(strategy);
        this.retries = retries;
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager){
        
        if (processId != null && strategy != null) {
            if(retries > 0) {

                // Relevant when strategy is:  RETRY

                Integer counter = (Integer)workItem.getParameter(COUNTER);
                if(counter == null) {
                    System.out.println("Initializing workItem param with name "+COUNTER+" : Retries = "+retries);
                    counter = 0;
                }
                
                if (counter < retries) {
                    System.out.println(workItem.getId() + " : throwing a ProcessWIHException to processId = "+processId+" and service task strategy = "+strategy+" : counter = "+counter);
                    counter++;
                    workItem.getParameters().put(COUNTER, counter);
                    throw new ProcessWorkItemHandlerException(processId, strategy, new RuntimeException("On purpose"));
                } else {
                    //manager.completeWorkItem(workItem.getId(), workItem.getParameters());
                    //manager.abortWorkItem(workItem.getId());
                    throw new RuntimeException("Still not working after the following retries: "+retries);
                }
            }else {

                // Relevant when strategy is:  Complete

                System.out.println(workItem.getId() + " : throwing a ProcessWIHException to processId = "+processId+" and service task strategy = "+strategy);
                throw new ProcessWorkItemHandlerException(processId, strategy, new RuntimeException("On purpose"));
            }
        }
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        

    }

}
