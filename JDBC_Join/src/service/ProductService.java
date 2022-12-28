package service;


import DbJdbc_connection.DbJdbc;
import entity.Category;
import entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    DbJdbc db = new DbJdbc();

    Product product = null;
    List<Object> ls = new ArrayList<>();
    public void getAllProducts() {

        Product product = null;

        try {

            PreparedStatement stProduct = db.connection.prepareStatement(
                    "select p.\"productId\", p.\"productName\" ,p.\"categoryId\",c.\"categoryName\",p.\"price\",p.\"description\"  from products as p\n" +
                    "inner join categories as c\n" +
                    "on p.\"categoryId\" = c.\"categoryId\" ORDER BY \"productId\" ASC ");
            ResultSet rsProduct = stProduct.executeQuery();

            while (rsProduct.next()) {
                Integer productId = rsProduct.getInt("productId");
                String productName = rsProduct.getString("productName");
                Integer categoryId = rsProduct.getInt("categoryId");
                String categoryName = rsProduct.getString("categoryName");
                int price = rsProduct.getInt("price");
                String description = rsProduct.getString("description");


                product = new Product(productId,productName,new Category(categoryId,categoryName),price,description);
                ls.add(product);
            }
            if (ls.isEmpty()) {
                System.out.println("Product is empty");
            } else {
                ls.forEach(item -> System.out.println(item));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void productInsert(Product product) {

        try {
            PreparedStatement insertStatement = db.connection.prepareStatement
                    ("insert into public.products(\"categoryId\", \"productName\",\"price\",\"description\")values(?,?,?,?)");
            /*insertStatement.setInt(1, product.getProductId());*/
            insertStatement.setInt(1, product.getCategory().getCategoryId());
            insertStatement.setString(2, product.getProductName());
            insertStatement.setInt(3, product.getPrice());
            insertStatement.setString(4, product.getDescription());
            insertStatement.executeUpdate();
            System.out.println("Product is added");
            getAllProducts();

            insertStatement.close();
            db.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void productDelete(int productId) {

        List<Integer> ls = new ArrayList<>();
        try {
            PreparedStatement list = db.connection.prepareStatement("select productId from product");
            ResultSet rsList = list.executeQuery();
            while (rsList.next()) {
                ls.add(rsList.getInt("productId"));
            }
            if (ls.contains(productId)) {
                PreparedStatement deleteStatement = db.connection.prepareStatement("delete from product where productId = ?");
                deleteStatement.setInt(1, productId);
                deleteStatement.executeUpdate();
                deleteStatement.close();
            } else {
                System.out.println("Product is not found");
            }
            db.connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}