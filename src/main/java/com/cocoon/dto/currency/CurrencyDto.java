package com.cocoon.dto.currency;

import java.io.Serializable;
import java.util.ArrayList;

public class CurrencyDto implements Serializable {
    public boolean success;
    public Long timestamp;
    public String base;
    public String date;
    public Rate rates;

}
