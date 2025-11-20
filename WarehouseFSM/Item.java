public class Item {
    protected int quantity;

    public Item(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int value) {
        this.quantity = value;
    }

    public void updateQuantity(int value) {
        this.quantity += value;
    }
}
