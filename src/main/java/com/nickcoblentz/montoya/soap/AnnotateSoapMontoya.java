package com.nickcoblentz.montoya.soap;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class AnnotateSoapMontoya implements BurpExtension {
    private MontoyaApi _api;
    private AnnotateSoapHttpHandler handler;

    @Override
    public void initialize(MontoyaApi api)
    {
        _api = api;
        _api.logging().logToOutput("Plugin Loading...");
        api.extension().setName("Annotate In-Scope SOAP Requests/Responses");
        handler = new AnnotateSoapHttpHandler(api);
        api.proxy().registerRequestHandler(handler);
        //api.proxy().registerResponseHandler(handler);
        _api.logging().logToOutput("Plugin Loaded");
    }
}
