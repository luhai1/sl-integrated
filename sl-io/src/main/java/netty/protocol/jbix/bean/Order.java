package netty.protocol.jbix.bean;

public class Order {
    private Long orderNumber;
    private Customer customer;
    private Address billTo;
    private Address shipTo;
    private Shipping shipping;
    private Float total;

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getBillTo() {
        return billTo;
    }

    public void setBillTo(Address billTo) {
        this.billTo = billTo;
    }

    public Address getShipTo() {
        return shipTo;
    }

    public void setShipTo(Address shipTo) {
        this.shipTo = shipTo;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order [orderNumber=" + orderNumber + ", customer=" + customer
                + ", billTo=" + billTo + ", shipping=" + shipping.toString()
                + ", shipTo=" + shipTo + ", total=" + total + "]";
    }

}
