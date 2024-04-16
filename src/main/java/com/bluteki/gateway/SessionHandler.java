package com.bluteki.gateway;

import com.bluteki.gateway.ussd.GatewayRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionHandler {

    public static final Map<String, GatewayRequest> sessionInfoMap = Collections
            .synchronizedMap(new HashMap<String, GatewayRequest>());

    public SessionHandler() {
    }

    public static GatewayRequest getSessionInfo(String msisdn) {
        return sessionInfoMap.get(msisdn);
    }

    public static boolean isMsisdnExists(String msisdn) {
        return sessionInfoMap.containsKey(msisdn);
    }

    public static void putSessionInfo(String msisdn, GatewayRequest request) {
        synchronized (sessionInfoMap) {
            sessionInfoMap.put(msisdn, request);
        }
    }

    public static GatewayRequest removeSessionInfo(String msisdn) {
        GatewayRequest request = null;
        synchronized (sessionInfoMap) {
            request = sessionInfoMap.remove(msisdn);
            return request;
        }
    }
}
