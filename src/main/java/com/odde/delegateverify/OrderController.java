package com.odde.delegateverify;

public class OrderController {
    private final OrderModel orderModel;
    private final SystemLogger logger;

    public OrderController(OrderModel orderModel, SystemLogger logger) {
        this.orderModel = orderModel;
        this.logger = logger;
    }

    public void save(Order order) {
        orderModel.save(order, this::insertMessage, this::updateMessage);
    }

    private void updateMessage(Order order)
    {
        logger.log(String.format("update order id:%d with %d successfully!", order.getId(), order.getAmount()));
    }

    private void insertMessage(Order order)
    {
        logger.log(String.format("insert order id:%d with %d successfully!", order.getId(), order.getAmount()));
    }

    public void deleteAmountMoreThan100()
    {
        orderModel.delete(o -> o.getAmount() > 100);
    }
}
