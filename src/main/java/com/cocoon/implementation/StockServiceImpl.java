package com.cocoon.implementation;

import com.cocoon.entity.InvoiceProduct;
import com.cocoon.entity.Stock;
import com.cocoon.enums.InvoiceType;
import com.cocoon.repository.StockRepository;
import com.cocoon.service.StockService;
import org.springframework.stereotype.Service;


@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void saveToStockByPurchase(InvoiceProduct invoiceProduct) {

        if (invoiceProduct.getInvoice().getInvoiceType().equals(InvoiceType.PURCHASE)){
            Stock productStock = Stock.builder()
                    .product(invoiceProduct.getProduct())
                    .quantity(invoiceProduct.getQty())
                    .price(invoiceProduct.getPrice())
                    .remainingQuantity(invoiceProduct.getQty())
                    .invoiceDate(invoiceProduct.getInvoice().getInvoiceDate())
                    .build();
            stockRepository.save(productStock);
        }
    }

    @Override
    public int updateStockBySale(InvoiceProduct invoiceProduct) {

        int profit = 0;
        if (invoiceProduct.getInvoice().getInvoiceType().equals(InvoiceType.SALE)) {
            profit = 0;
            int soldProductQty = invoiceProduct.getQty();
            while (soldProductQty > 0) {
                Stock queuedProductStock = stockRepository.findFirstByProduct_IdAndRemainingQuantityNot(invoiceProduct.getProduct().getId(), 0);
                if (soldProductQty < queuedProductStock.getRemainingQuantity()) {
                    queuedProductStock.setRemainingQuantity(queuedProductStock.getRemainingQuantity() - soldProductQty);
                    profit =  (soldProductQty * (invoiceProduct.getPrice() - queuedProductStock.getPrice()));

                    stockRepository.save(queuedProductStock);
                    break;
                } else {
                    soldProductQty -= queuedProductStock.getRemainingQuantity();
                    profit = (queuedProductStock.getRemainingQuantity() * (invoiceProduct.getPrice() - queuedProductStock.getPrice()));

                    queuedProductStock.setRemainingQuantity(0);
                    stockRepository.save(queuedProductStock);
                }
            }
        }
        return profit;

    }



}


