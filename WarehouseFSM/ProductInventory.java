// Name: Sohaib Akram
// Group 3
// Purpose: This class contains many products.  It can get, 
// add, or delete products and modify the list.

import java.util.*;

public class ProductInventory {
    private ArrayList<Product> products;
    private static ProductInventory productInventory;

    private ProductInventory() {
        this.products = new ArrayList<Product>();
    }

    public static ProductInventory instance() {
        if (productInventory == null)
            productInventory = new ProductInventory();
        return productInventory;
    }

    public boolean insertProduct(Product product) {
        for (Product p : this.products) {
            if (product.getId().equals(p.getId())) {
                return false;
            }
        }
        this.products.add(product);
        return true;
    }

    public Product searchProduct(String productId) {
        for (Product p : this.products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    public Iterator<Product> getProducts() {
        return this.products.iterator();
    }

    // New helper
    public void updateProductQuantity(String productId, int additionalQuantity) {
        Product product = searchProduct(productId);
        if (product != null) {
            product.updateQuantity(additionalQuantity);
        }
    }
}
