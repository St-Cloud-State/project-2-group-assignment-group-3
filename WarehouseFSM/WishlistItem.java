public class WishlistItem extends Item {
    private String productId;

    public WishlistItem(String productId, int quantity) {
        super(quantity);
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return String.format("Product ID: %s, Quantity: %d",
                productId, quantity);
    }
}
