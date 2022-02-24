package com.cocoon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceProductDTO {

    private Long id;
    private String name;
    private int qty;
    private int price;
    private int tax;

    private ProductDTO productDTO;

    private InvoiceDTO invoiceDTO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceProductDTO)) return false;
        InvoiceProductDTO that = (InvoiceProductDTO) o;
        return getQty() == that.getQty() && getPrice() == that.getPrice() && getTax() == that.getTax() && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getQty(), getPrice(), getTax());
    }

    public String getProductQuantityUnitText(){
        return qty + " / " + productDTO.getUnit().getValue();
    }
}
