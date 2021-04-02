package netty.protocol.jbix.bean;

public class OrderFactory {

    public static Order create(Long number){
       Order order = new Order();
       Customer customer = new Customer();
       Address billTo = new Address();
       Address shipTo = new Address();
       customer.setCustomerNumber(number);
       customer.setCustomerName("luhai");
        billTo.setCity("hefei");
        billTo.setCountry("CN");
        billTo.setPostCode("3401");
        shipTo.setCity("anqing");
        shipTo.setCountry("CN");
        shipTo.setPostCode("3408");
        order.setCustomer(customer);
        order.setBillTo(billTo);
        order.setShipTo(shipTo);
        order.setShipping(Shipping.INTERNATIONAL_PRESS);
        order.setTotal(1106F);
        order.setOrderNumber(number);
       return order;
    }
}
