package com.cocoon.util.payment;

public class Constants {
    /**
     * Yapily Auth API url and parameters
     */
    public static final String PARAMETER_INSTITUTION_ID = "aibgb-sandbox";
    public static final String PARAMETER_APPLICATION_ID = "86e3ffea-61d8-4149-966c-3f321bebaa12";
    public static final String PARAMETER_USER_ID = "f5199cea-b807-494c-9c5f-bcc5f1204b0b";
    public static final String PARAMETER_CALLBACK_URL = "https://display-parameters.com/";

    /**
     * A default user all examples will run with
     */
    public static final String APPLICATION_USER_ID = "cocoonPayUser";

    /**
     * Placeholder for credentials created in the Applications dashboard. Hardcoded here only for
     * example purposes. Can be used to test/demonstrate code. NOT SUITABLE FOR PRODUCTION
     */
    public static final String APPLICATION_ID = "86e3ffea-61d8-4149-966c-3f321bebaa12";
    public static final String APPLICATION_SECRET = "fda7ddae-199a-437e-b14d-26fdc4b62e87";

    /**
     * Personal access token for connecting to your real Starling account via API. Viewable in
     * Starling's developer console (https://developer.starlingbank.com/personal/list)
     */
    public static final String STARLING_PERSONAL_ACCESS_TOKEN = "REPLACE WITH YOUR ACCESS TOKEN";

    /**
     * Provide your callback URL to redirect the customer back to, following the institution's
     * authorisation
     */
    public static final String CALLBACK_URL = "https://display-parameters.com/";

}
