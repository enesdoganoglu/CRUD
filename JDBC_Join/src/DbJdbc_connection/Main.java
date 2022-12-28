package DbJdbc_connection;

import entity.Category;
import entity.Product;
import service.ProductService;

public class Main {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        //productService.getAllProducts();
        Category category = new Category(3,"Otomobil");
        Product product1 = new Product(null, "El feneri", category,300,"Elektronik");
        productService.productInsert(product1);


    }
}
