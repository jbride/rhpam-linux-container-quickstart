package com.myspace.handling_wih_exception;

import org.kie.api.runtime.process.ProcessWorkItemHandlerException;
import org.kie.api.runtime.process.ProcessWorkItemHandlerException.HandlingStrategy;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;


public class ThrowProcessWIHException implements WorkItemHandler {
    
    private String processId;
    private HandlingStrategy strategy;
    
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

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        if (processId != null && strategy != null) {
    
            System.out.println("executeWorkItem() .... about to throw a ProcessWIHException to processId = "+processId+" and service task strategy = "+strategy);        
            throw new ProcessWorkItemHandlerException(processId, strategy, new RuntimeException("On purpose"));
        }
        
        manager.completeWorkItem(workItem.getId(), null);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        

    }

}
