/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package prdct;


import cnnct.Cnnct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 *
 * @author c0646395
 */
@Singleton
public class prdctList {
    
    
     public List <prdct> productList = new ArrayList<prdct>();

    public prdctList() {
        prdct product = new prdct();

        try (Connection conn = (Connection) Cnnct.getConnection()) {
            String query = "SELECT * FROM product";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                product.productId = rs.getInt("productId");
                product.name = rs.getString("name");
                product.description = rs.getString("description");
                product.quantity = rs.getInt("quantity");

                productList.add(product);

            }

        } catch (SQLException ex) {
            Logger.getLogger(prdctList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    public prdct getByID( int id){
    
        prdct data = null;
        for(prdct product: productList){
            if(product.getProductID() == id){
                data = product;
            }
        }
        
        return data;
    
    }
    
    public void add(prdct product) throws Exception {

        int data = doUpdate("INSERT INTO product (productId, name,description,quantity) VALUES (?,?,?,?)", String.valueOf(product.productId), product.name , product.description,String.valueOf(product.quantity));
        
        if(data > 0){
            productList.add(product);
            
        }else{
            throw new Exception("Error");
        }
        
    }

    public void remove(prdct product) throws Exception {
     
       remove(product.productId);

    }

    public void remove(int id) throws Exception {

       int data =  doUpdate("DELETE FROM product where productId=? ", String.valueOf(id));
         if(data > 0){
           prdct oldData = getByID(id);
           productList.remove(oldData);
            
        }else{
            throw new Exception("Error ");
        }

    }

    public void set(int id, prdct product) throws Exception {
        int data = doUpdate("UPDATE product SET name=?,description=?,quantity=? where productId=?", product.name, product.description, String.valueOf(product.quantity), String.valueOf(product.productId));

         if(data > 0){
           prdct oldData = getByID(id);
           oldData.setName(product.name);
           oldData.setDescription(product.getDescription());
           oldData.setQuantity(product.getQuantity());
            
        }else{
            throw new Exception("Error ");
        }
    }

    public JsonArray toJSON(){
        
        JsonArray ja = null;
        
        for(prdct product : productList){
             JsonObject json = Json.createObjectBuilder()
                    .add("productId", product.productId)
                    .add("name", product.name)
                    .add("description",  product.description)
                    .add("quantity", product.quantity)
                    .build();
         ja.add(json);
        }
        
        return ja;
    }
    
    
    
    private int doUpdate(String query, String... parameter) {
        int change = 0;
        try (Connection conn = (Connection) Cnnct.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= parameter.length; i++) {
                pstmt.setString(i, parameter[i - 1]);
            }
            change = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(prdctList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return change;
    }
    
    
    
}
