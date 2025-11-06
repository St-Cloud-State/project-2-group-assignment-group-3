import java.util.ArrayList;
import java.util.Iterator;

public class InvoiceList {
    private ArrayList<Invoice> items;

    public InvoiceList() {
        items = new ArrayList<Invoice>();
    }

    public void insertItem(Invoice item) {
        items.add(item);
    }

    public Iterator<Invoice> getItems() {
        return items.iterator();
    }
}
