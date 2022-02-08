package com.cocoon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyDTO {

    private Long id;
    private String title;
    private String address1;
    private String address2;
    private String state;
    private String zip;
    private String representative;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishmentDate;
    private boolean enabled;
    private String phone;

    /**
     * this getter returns the full address by merging the address1 field and address2 field
     * it is more appropriate for viewing purposes
     * @return
     */
    public String getFullAddress(){
        return address1 + (address2 != null ? " " + address2: "");
    }

    /**
     * here actually we are setting address1 and address2 fields based on the length
     * of the full address that user provided
     * @param fullAddress
     */
    public void setFullAddress(String fullAddress){
        if (fullAddress.length() > 254){
            int indexOfSpaceBeforeSplitLength = fullAddress.substring(0, 254).lastIndexOf(" ");
            address1 = fullAddress.substring(0, indexOfSpaceBeforeSplitLength);
            address2 = fullAddress.substring(indexOfSpaceBeforeSplitLength + 1);
        }
        else
            address1 = fullAddress;
    }
}
