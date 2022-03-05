package com.cocoon.util.payment_old_one;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value={"hibernate_Lazy_Initializer"}, ignoreUnknown = true)
@ToString
public class PaymentRequest{

    private String type = "DOMESTIC_PAYMENT";
    private String reference = "Bills Coffee Shop";


    private String paymentIdempotencyId = "1d54cf71bfe44b1b8e64547ae45455d96y";

    private Amount AmountObject = new Amount();

    private Payee PayeeObject = new Payee();

}
