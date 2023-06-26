package com.nickcoblentz.montoya.soap;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.proxy.http.*;

public class AnnotateSoapHttpHandler implements ProxyRequestHandler/*, ProxyResponseHandler*/ {
    private MontoyaApi _api;
    private String _actionTagStart = "<a:Action";
    private String _actionTagEnd = ">";
    private String _soapMethodEnd = "<";

    public AnnotateSoapHttpHandler(MontoyaApi api)
    {
        _api = api;
    }


    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {

        handleRequest(interceptedRequest);
        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        handleRequest(interceptedRequest);
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }

    private void handleRequest(InterceptedRequest interceptedRequest)
    {
        _api.logging().logToOutput("handle called");
        _api.logging().logToOutput("URL: "+interceptedRequest.url());
        if(_api.scope().isInScope(interceptedRequest.url()))
        {
            _api.logging().logToOutput("is in scope");
            if (_api.utilities().byteUtils().convertToString(interceptedRequest.toByteArray().getBytes()).toLowerCase().contains("application/soap+xml")) {
                _api.logging().logToOutput("Found soap+xml");
                String body = _api.utilities().byteUtils().convertToString(interceptedRequest.body().getBytes());
                int actionTagStartIndex = body.indexOf(_actionTagStart);
                _api.logging().logToOutput("actionTagStartIndex: " + actionTagStartIndex);
                if (actionTagStartIndex > 0) {
                    int actionTagEndIndex = body.indexOf(_actionTagEnd, actionTagStartIndex + _actionTagStart.length());
                    _api.logging().logToOutput("actionTagEndIndex: " + actionTagEndIndex);
                    if (actionTagEndIndex > actionTagStartIndex) {
                        int soapMethodEndIndex = body.indexOf(_soapMethodEnd, actionTagEndIndex);
                        _api.logging().logToOutput("soapMethodEndIndex: " + soapMethodEndIndex);
                        if (soapMethodEndIndex > actionTagEndIndex) {
                            String annotation = body.substring(actionTagEndIndex+1, soapMethodEndIndex).replace("http://tempuri.org/","");
                            _api.logging().logToOutput("Annotating: " + annotation);
                            interceptedRequest.annotations().setNotes(annotation);
                        }
                    }
                }
            }
        }
    }

    /*
    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {
        interceptedResponse.annotations().setNotes("test3");
        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        interceptedResponse.annotations().setNotes("test4");
        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
    }

    */

}
