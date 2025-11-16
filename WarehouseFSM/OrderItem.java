public class OrderItem extends Item {
    private String productId;
    
    public OrderItem(String productId, int quantity) {
        super(quantity);
        this.productId = productId;
    }

    public String getProductId() {
        return this.productId;
    }

    @Override
    public String toString() {
        return String.format("Product ID: %s, Quantity: %d", productId, quantity);
    }
}
