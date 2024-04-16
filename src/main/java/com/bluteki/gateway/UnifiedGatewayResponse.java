package com.bluteki.gateway;


import com.bluteki.gateway.ussd.GatewayRequest;
import com.bluteki.gateway.ussd.Ussd;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringWriter;

public class UnifiedGatewayResponse {
    private static final Logger LOGGER = Logger.getLogger(UnifiedGatewayResponse.class);

    public void sendResponse(GatewayRequest gatewayRequest, HttpServletResponse httpRes, String message, String type) {
        String unifiedType = convertType(type);
        if (unifiedType == null) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        String responseContent = null;
        if (unifiedType.equals("2") || unifiedType.equals("3")) {
            // Prepare XML response for Truteq gateway (type 2 or 3)
            Ussd ussd = prepareUssdObject(gatewayRequest, message, unifiedType);
            try {
                responseContent = getXMLBuffer(ussd);
            } catch (JAXBException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // Prepare plain text response for Flares gateway (type FC or FB)
            httpRes.setHeader("Freeflow", type);
            responseContent = message;
        }

        try {
            writeOutput(httpRes, responseContent);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logResponse(gatewayRequest, message, unifiedType);
    }

    private String convertType(String type) {
        switch (type) {
            case "FC":
                return "2";
            case "FB":
                return "3";
            default:
                return null;
        }
    }

    private Ussd prepareUssdObject(GatewayRequest gatewayRequest, String message, String type) {
        Ussd ussd = new Ussd();
        ussd.setMsisdn(gatewayRequest.getMsisdn());
        ussd.setSessionId(String.valueOf(gatewayRequest.getSessionId()));
        ussd.setMsg(message);
        ussd.setType(type);
        return ussd;
    }

    private String getXMLBuffer(Ussd ussd) throws JAXBException {
        JAXBContext contextObj = JAXBContext.newInstance(Ussd.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty("jaxb.formatted.output", true);
        StringWriter writer = new StringWriter();
        marshallerObj.marshal(ussd, writer);
        return writer.toString();
    }

    private void writeOutput(HttpServletResponse response, String message) throws IOException {
        ServletOutputStream printWriter = null;
        try {
            response.setContentType("UTF-8");
            printWriter = response.getOutputStream();
            printWriter.write(message.getBytes("UTF-8"));
        } catch (Exception e) {
            LOGGER.error("Failed to send response", e);
        } finally {
            if (printWriter != null) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

    private void logResponse(GatewayRequest gatewayRequest, String message, String type) {
        LOGGER.info("MSISDN:" + gatewayRequest.getMsisdn() + "::" + "SESSION ID:" + gatewayRequest.getSessionId() + "::" + "Response:" + message + " (Type: " + type + ")");
    }
}

