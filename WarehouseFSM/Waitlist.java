import java.util.ArrayList;
import java.util.Iterator;

public class Waitlist {
    private ArrayList<WaitlistItem> items;

    public Waitlist() {
        items = new ArrayList<>();
    }

    public WaitlistItem findItem(String clientId) {
        for (WaitlistItem item : items) {
            if (item.getClientId().equals(clientId)) {
                return item;
            }
        }
        return null;
    }

    public boolean insertItem(WaitlistItem item) {
        items.add(item);
        return true;
    }

    public Iterator<WaitlistItem> getItems() {
        return items.iterator();
    }

    // New helper methods
    public void removeItem(WaitlistItem item) {
        items.remove(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
