package com.cocoon.service;


import com.cocoon.entity.InvoiceProduct;

import java.util.Map;

public interface StockService {

    void saveToStockByPurchase(InvoiceProduct invoiceProduct);
   int updateStockBySale(InvoiceProduct invoiceProduct);
}
