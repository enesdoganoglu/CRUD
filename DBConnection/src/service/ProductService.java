package service;

import db_connection.DB;
import entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public void getAllProducts() {
        DB db = new DB();
        List<Product> ls = new ArrayList<>();
        Product product = null;
        try {
            /*String sqlLogin = "select * from user where email='' +email+ and password = '' +userPassword+";
            Statement sýnýfýný, sql sorgularý sýrasýnda 'sql injection' tipindeki zafiyetleri önleyebilmek için kullanýyoruz.
            Bu önlem statement sýnýfý ile tam olarak alýnamamaktadýr. Bunun yerine 'PreparedStatement' sýnýfý kullanýlmalýdýr.
            Deðerler boþ geçilirse veya '+userEmail+' formatýnda tanýmlanýrsa(yukarýdaki sqlLogin gibi) yapýlacak injection ataðýnda true döner
            ve hacker sisteme giriþ yapmýþ olur.
            Bu durumlarda PreparedStatement sýnýfý ile beraber '+userEmail+' kullanmak yerine '?' formatý kullanýlýr. Ve gönderilecek veriler arka planda kontrol edilir.
            Bu sayede database' de kayýtlý olmayan bir kullanýcýnýn 'sql injection' ile eriþmesi engellenebilir.*/

            PreparedStatement stProduct = db.connection.prepareStatement("Select * from product");
            ResultSet rsProduct = stProduct.executeQuery();

            while (rsProduct.next()) {
                Integer productId = rsProduct.getInt("productId");
                String productName = rsProduct.getString("productName");
                String productCategory = rsProduct.getString("productCategory");
                Integer price = rsProduct.getInt("price");
                String description = rsProduct.getString("description");

                product = new Product(productId, productName, productCategory, price, description);
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

    //Soru --> productInsert() metodu yazýnýz ve verileri veritabanýna kaydediniz.
    //productInsert metodu içerisinde deðiþkenleri tek tek tanýmlamak yerine Product sýnýfýndan bir deðiþken tanýmlayarak o deðiþken sayesinde
    //ilgili property'lerin getter metotlarýna ulaþarak bu iþlemi gerçekleþtirebiliriz. (productInsert(Product product))
    public void productInsert(Product product) {
        DB db = new DB();
        try {
            PreparedStatement insertStatement = db.connection.prepareStatement
                    ("insert into product(productName, productCategory, price, description) " +
                            "values(?,?,?,?)");
            insertStatement.setString(1, product.getProductName());
            insertStatement.setString(2, product.getProductCategory());
            insertStatement.setInt(3, product.getPrice());
            insertStatement.setString(4, product.getDescription());
            insertStatement.executeUpdate();
            System.out.println("Product is added");

            insertStatement.close();
            db.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Soru --> productDelete() metodunuz yazýnýz ve veritabanýndan bir veriyi siliniz.
    public void productDelete(int productId) {
        DB db = new DB();
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
    //Soru --> productUpdate() metodunu yazýnýz ve veritabanýndan bir veriyi güncelleyiniz.
    public void productUpdate(Product product) {
        DB db = new DB();
        List<Integer> ls = new ArrayList();
        try {
            PreparedStatement list = db.connection.prepareStatement("select productId from product");
            ResultSet rsList = list.executeQuery();
            while (rsList.next()) {
                ls.add(rsList.getInt("productId"));
            }
            if (ls.contains(product.getProductId())) {
                PreparedStatement updateStatement = db.connection.prepareStatement("update product set price= ? where productId = ?");

                updateStatement.setInt(1, product.getPrice());
                updateStatement.setInt(2, product.getProductId());
                updateStatement.executeUpdate();
                updateStatement.close();

                db.connection.close();
            }
            else{
                System.out.println("Product is not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Soru --> productById() metodunu yazýnýz ve ilgili id'ye sahip productýn bütün özelliklerini gösteriniz.
    public void productById(int productID){
        DB db = new DB();
        List<Integer> ls = new ArrayList<>();
        Product product = null;
        try {
            PreparedStatement list = db.connection.prepareStatement("select productId from product");
            ResultSet rsList = list.executeQuery();
            while (rsList.next()) {
                ls.add(rsList.getInt("productId"));
            }
            ls.forEach(item -> System.out.println(item));
            if(ls.contains(productID)){
                PreparedStatement productIdStatement = db.connection.prepareStatement("select * from product where productId=?");
                productIdStatement.setInt(1, productID);

                ResultSet productIdRs = productIdStatement.executeQuery();
                while(productIdRs.next()){
                    Integer productId = productIdRs.getInt("productId");
                    String productName = productIdRs.getString("productName");
                    String productCategory = productIdRs.getString("productCategory");
                    Integer price = productIdRs.getInt("price");
                    String description = productIdRs.getString("description");

                    product = new Product(productId,productName,productCategory,price,description);
                    System.out.println(product);
                }
                productIdRs.close();
                db.connection.close();
            }else {
                System.out.println("Product is not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + " -->Error");
        }
    }

    //Soru --> insertAll() metodunu yazýnýz. Birden çok kayýt yapabilen bir metot olacak.
    //Üretilen birden çok nesneyi tek seferde yazdýrabilmeli
    public void insertAll(List<Product> products){
        DB db = new DB();
        if (products.isEmpty()){
            System.out.println("List is empty");
        }else {
            try{
                PreparedStatement preparedStatement = db.connection.prepareStatement(
                        "insert into product(productName, productCategory, price, description) "
                                +"values(?,?,?,?)");
                //products.forEach(item -> item.);
                for(Product item : products) {
                    preparedStatement.setString(1, item.getProductName());
                    preparedStatement.setString(2, item.getProductCategory());
                    preparedStatement.setInt(3, item.getPrice());
                    preparedStatement.setString(4, item.getDescription());
                    preparedStatement.executeUpdate();
                }

                getAllProducts();
                preparedStatement.close();
                db.connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //veritabaný adý --> jdbc_join
    //tablolarýn ismi products, categories
    //products --> productId, categoryId, name, price, description
    //categories --> categoryId, name
}

