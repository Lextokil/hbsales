package br.com.hbsis.itempedido;

public class InvoiceItemDTOSet {
    private int amount;
    private String itemName;

    public InvoiceItemDTOSet() {
    }

    public InvoiceItemDTOSet(int amount, String itemName) {
        this.amount = amount;
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
