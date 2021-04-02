package netty.protocol.jbix;

import netty.protocol.jbix.bean.Order;
import netty.protocol.jbix.bean.OrderFactory;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class TestOrder {
    private IBindingFactory factory;
    private StringWriter stringWriter;
    private StringReader stringReader;
    private final static  String CHARSET_NAME = "utf-8";

    public  String encode2xml(Order order) throws JiBXException, IOException {
        factory = BindingDirectory.getFactory(Order.class);
        stringWriter = new StringWriter();
        IMarshallingContext marshallingContext = factory.createMarshallingContext();
        marshallingContext.setIndent(2);
        marshallingContext.marshalDocument(order,CHARSET_NAME,null,stringWriter);
        String writerStr = stringWriter.toString();
        stringWriter.close();
        System.out.println(writerStr);
        return writerStr;
    }


    public Order decode2Order(String xmlBody) throws JiBXException {
        factory = BindingDirectory.getFactory(Order.class);
        stringReader = new StringReader(xmlBody);
        IUnmarshallingContext unmarshallingContext = factory.createUnmarshallingContext();

        Order order = (Order)unmarshallingContext.unmarshalDocument(stringReader);
        return order;
    }

    public static void main(String[] args) throws JiBXException, IOException {
        TestOrder testOrder = new TestOrder();
        Order order = OrderFactory.create(123L);
        String body = testOrder.encode2xml(order);
        Order order2 = testOrder.decode2Order(body);
        System.out.println(order2);

    }

}
