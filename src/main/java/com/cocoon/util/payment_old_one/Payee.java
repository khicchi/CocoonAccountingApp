package com.cocoon.util.payment_old_one;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value={"hibernate_Lazy_Initializer"}, ignoreUnknown = true)
@ToString
public class Payee{

    private String name = "BILLS COFFEE LTD";

    private List<TypeIdentification> accountIdentifications = Arrays.asList(new TypeIdentification("ACCOUNT_NUMBER", "99998888"), new TypeIdentification("SORT_CODE", "556677"));

    private Address AddressObject = new Address();
}