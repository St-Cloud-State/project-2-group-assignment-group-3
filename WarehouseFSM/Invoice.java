import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Invoice {
    private String clientId;
    private ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>();

    Invoice(String clientId) {
        this.clientId = clientId;
    }

    void addItem(InvoiceItem item) {
        items.add(item);
    }

    float balanceDue() {
        float total = 0.0f;

        for (InvoiceItem item : items) {
            total += item.amount();
        }

        return total;
    }

    String render() {
        NumberFormat c = NumberFormat.getCurrencyInstance(Locale.US);
        StringBuilder sb = new StringBuilder();
        sb.append("INVOICE\n");
        sb.append("Client ID: ").append(clientId).append("\n\n");

        // header
        sb.append(String.format("%-10s | %-24s | %-12s | %-12s%n",
                "Quantity", "Description", "Unit price", "Amount"));
        sb.append("-----------+--------------------------+--------------+--------------\n");

        for (InvoiceItem item : items) {
            sb.append(String.format("%-10d | %-24s | %-12s | %-12s%n",
                    item.getQuantity(), item.getProductName(), c.format(item.getSalePrice()), c.format(item.amount())));
        }

        sb.append("\n");
        sb.append(String.format("%-49s %12s%n", "Balance due", c.format(balanceDue())));
        return sb.toString();
    }
}
