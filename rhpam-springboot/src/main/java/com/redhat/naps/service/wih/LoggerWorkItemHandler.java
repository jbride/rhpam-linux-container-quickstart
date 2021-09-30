package com.redhat.naps.service.wih;

import java.util.Map;
import java.util.Map.Entry;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("Log")
public class LoggerWorkItemHandler implements WorkItemHandler {

    private final static Logger log = LoggerFactory.getLogger(LoggerWorkItemHandler.class);

    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        log.info("Executing work item " + workItem);
        Map<String, Object> params = workItem.getParameters();
        for(Entry entry : params.entrySet()) {
            log.info("key = "+entry.getKey()+" : value = "+entry.getValue());
        }
        manager.completeWorkItem(workItem.getId(), null);
    }

    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        log.info("Aborting work item " + workItem);
        manager.abortWorkItem(workItem.getId());
    }
    
}
