package com.cocoon.util.payment;

import yapily.ApiClient;
import yapily.Configuration;
import yapily.auth.HttpBasicAuth;
import yapily.sdk.InstitutionsApi;

public class ApiClientUtils {
    public static ApiClient basicAuth() {
        ApiClient applicationClient = new ApiClient();
        // Configure the API authentication
        HttpBasicAuth basicAuth = (HttpBasicAuth) applicationClient.getAuthentication("basicAuth");
        basicAuth.setUsername(Constants.APPLICATION_ID);
        basicAuth.setPassword(Constants.APPLICATION_SECRET);
        InstitutionsApi institutionsApi = new InstitutionsApi();
        institutionsApi.setApiClient(applicationClient);
        return applicationClient;
    }
}
