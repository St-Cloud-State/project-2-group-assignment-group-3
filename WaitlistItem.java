public class WaitlistItem extends Item {
    private String clientId;

    public WaitlistItem(String clientId, int quantity) {
        super(quantity);
        this.clientId = clientId;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return String.format("Client ID: %s, Quantity: %d", clientId, quantity);
    }
}
