public class InvoiceItem {
    private int quantity;
    private String productName;
    private float salePrice;

    InvoiceItem(int quantity, String productName, float salePrice) {
        this.quantity = quantity;
        this.productName = productName;
        this.salePrice = salePrice;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public float getSalePrice() {
        return this.salePrice;
    }

    public float amount() {
        return this.salePrice * quantity;
    }
}
