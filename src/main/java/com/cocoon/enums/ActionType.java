package com.cocoon.enums;

import lombok.Getter;

@Getter
public enum ActionType {
    COMPANY_LISTED(1L, "All companies listed", "getCompanies"), COMPANY_CREATED(2L, "New Company Created", "saveCompany"), COMPANY_UPDATED(3L, "Company Created", "updateCompany"), COMPANY_DELETED(4L, "Company Deleted", "deleteCompany"), COMPANY_ACTIVATED(5L, "Company Activated", "getOpenPage"), COMPANY_DEACTIVATED(6L, "Company De-Activated", "getClosePage"),
    USER_LISTED(7L, "All users listed", "findUsers"), USER_CREATED(8L, "New User created", "createUser"), USER_UPDATED(9L, "User updated", "updateUser"), USER_DELETED(10L, "User deleted", "deleteUser"),
    CLIENT_VENDOR_LISTED(11L, "All client & vendors listed", "readAllClientVendor"), CLIENT_VENDOR_CREATED(12L, "New Client-Vendor created", "saveClient"), CLIENT_VENDOR_UPDATED(13L, "Client-Vendor updated", "updateClientVendor"), CLIENT_VENDOR_DELETED(14L, "Client-vendor deleted", "deleteClientVendor"),
    PRODUCT_LISTED(15L, "All products listed", "getAllProducts"), PRODUCT_CREATED(16L, "New Product created", "saveProduct"), PRODUCT_UPDATED(17L, "Product updated", "updateProduct"), PRODUCT_DELETED(18L, "Product deleted", "deleteProduct"),
    PURCHASE_INVOICE_LISTED(19L, "All Purchase Invoices listed", "PurchaseInvoiceController.invoiceList"), PURCHASE_INVOICE_CREATED(20L, "New Purchase Invoice created", "PurchaseInvoiceController.createInvoice"), PURCHASE_INVOICE_UPDATED(21L, "Purchase invoice updated", "PurchaseInvoiceController.updateInvoice"), PURCHASE_INVOICE_DELETED(22L, "Purchase invoice deleted", "PurchaseInvoiceController.deleteInvoiceById"),
    SALES_INVOICE_LISTED(23L, "All Sales Invoices listed", "InvoiceController.invoiceList"), SALES_INVOICE_CREATED(24L, "New Sales Invoice created", "InvoiceController.createInvoice"), SALES_INVOICE_UPDATED(25L, "Sales invoice updated", "InvoiceController.updateInvoice"), SALES_INVOICE_DELETED(26L, "Sales invoice deleted", "InvoiceController.deleteInvoiceById");

    private final Long id;
    private final String value;
    private final String methodName;

    ActionType(Long id, String value, String methodName) {
        this.id = id;
        this.value = value;
        this.methodName = methodName;
    }
}
