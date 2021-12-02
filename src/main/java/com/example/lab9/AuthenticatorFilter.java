package com.example.lab9;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.*;

@Provider
public class AuthenticatorFilter implements javax.ws.rs.container.ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();
        //Access allowed for all
        if (!method.isAnnotationPresent(PermitAll.class)) {
            //Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Access blocked for all users !!").build());
                return;
            }

            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            //Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            //If no authorization information present; block access
            if (!Optional.ofNullable(authorization).isPresent() || authorization.isEmpty()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("You cannot access this resource").build());
                return;
            }

            //Get encoded username and password
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
            //Decode username and password
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword));

            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            //Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                //Is user valid?
                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                            .entity("You cannot access this resource").build());
                    return;
                }
            }
        }
    }

    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;
        String userRole = "";

        if (username.equals("ADMIN") && password.equals("ADMIN")) {
            userRole = "ADMIN";
        } else if (username.equals("MODERATOR") && password.equals("MODERATOR")) {
            userRole = "OWNER";
        } else userRole = "USER";

        if (rolesSet.contains(userRole)) {
            isAllowed = true;
        }
        return isAllowed;
    }
}


