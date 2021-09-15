package com.company.service;

import java.util.List;
import java.util.Arrays;

import org.jbpm.springboot.security.SpringSecurityIdentityProvider;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.identity.IdentityProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/* 
 * This is a substitute for:   org.jbpm.springboot.security.SpringSecurityUserGroupCallback
 * In order for SpringSecurityUserGroupCallback to utilize roles from JWT (when using keycloak), need to ensure the following setting is configured in the SSO client of the keycloak SSO Realm:
 *     "fullScopeAllowed":true
 */
public class KeycloakUserGroupCallback implements UserGroupCallback {

    private final static Logger log = LoggerFactory.getLogger(KeycloakUserGroupCallback.class);

    private SpringSecurityIdentityProvider provider;

    public KeycloakUserGroupCallback(IdentityProvider x) {

        this.provider = (SpringSecurityIdentityProvider)x;
        log.info("constructor() provider = "+provider);
    }

    @Override
    // When a task is created, verify that groupIds / roles assigned to that task are actually registered in Identity Provider
    public boolean existsGroup(String groupId) {
        log.info("existsGroup() group = "+groupId);
        if (groupId.equals("Administrators") || groupId.equals("interviewer"))
            return true;
        else
            return false;
    }

    @Override
    // When a task is created, verify that userId assigned to task is actually registered in Identity Provider
    public boolean existsUser(String userId) {
        log.info("Checking User : " + userId);
        if (userId.equals("kieserver") || userId.equals("wbadmin") || userId.equals("user") || userId.equals("Administrator"))
            return true;
        else
            return false;
    }

    @Override
    // List all roles associated with an authenticated user attempting to manage tasks
    public List<String> getGroupsForUser(String userId) {
        List<String> groupList = provider.getRoles();
        log.info("getGroupsForUser "+userId+" : " + Arrays.toString(groupList.toArray()));
        return groupList;
    }

}
