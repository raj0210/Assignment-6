/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package prdct;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author c0646395
 */
@MessageDriven(mappedName = "jms/Queue")
public class prdctListnr implements MessageListener{
    
    @Inject
    prdctList productlist;

    @Override
    public void onMessage(Message message) {

        try {
         
            if (message instanceof TextMessage) {
                String str = ((TextMessage) message).getText();
                JsonObject json = Json.createReader(new StringReader(str)).readObject();
                productlist.add(new prdct(json));

            }

        } catch (JMSException ex) {
            Logger.getLogger(prdctListnr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(prdctListnr.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
}
