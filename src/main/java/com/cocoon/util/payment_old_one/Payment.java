package com.cocoon.util.payment_old_one;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value={"hibernate_Lazy_Initializer"}, ignoreUnknown = true)
@ToString
public class Payment{

    private String applicationUserId = "otto";
    private String institutionId = "aibgb-sandbox";
    private String callback = "https://display-parameters.com/";

    private PaymentRequest PaymentRequestObject = new PaymentRequest();

}