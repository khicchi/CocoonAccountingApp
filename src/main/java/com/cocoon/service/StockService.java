package com.cocoon.service;


import com.cocoon.entity.InvoiceProduct;

import java.util.Map;

public interface StockService {

    void saveToStockbyPurchase(InvoiceProduct invoiceProduct);
   int updateStockbySale(InvoiceProduct invoiceProduct);
}
