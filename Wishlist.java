package WarehouseFSM;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Wishlist {
    private ArrayList<WishlistItem> items;

    public Wishlist() {
        items = new ArrayList<WishlistItem>();
    }

    public WishlistItem findItem(String productId) {
        for (WishlistItem item : items) {
            if (item.getProductId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    public boolean insertItem(WishlistItem item) {
        items.add(item);
        return true;
    }

    public boolean removeItem(WishlistItem item) {
        for (WishlistItem it : items) {
            if (item.getProductId().equals(it.getProductId())) {
                items.remove(it);
                return true;
            }
        }
        return false;
    }

    public Iterator<WishlistItem> getItems() {
        return items.iterator();
    }

    // ✅ Clears all items from the wishlist
    public void clear() {
        items.clear();
    }

    // ✅ Returns the items as a List so GUI can use for-each loops
    public List<WishlistItem> getItemsList() {
        return items;
    }
}
