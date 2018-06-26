package com.odde.delegateverify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Test
    public void exist_order_should_update() {
        //TODO
        OrderModel model = new OrderModel() {
            @Override
            public void save(Order order, Consumer<Order> insertCallback, Consumer<Order> updateCallback) {
                updateCallback.accept(order);
            }

            @Override
            public void delete(Predicate<Order> predicate) {

            }
        };

        SystemLogger mockSystemLogger = mock(SystemLogger.class);
        OrderController orderController = new OrderController(model, mockSystemLogger);

        orderController.save(new Order() {{
            setId(91);
            setAmount(100);
        }});

        ArgumentCaptor<String> captor = forClass(String.class);
        verify(mockSystemLogger).log(captor.capture());
        assertThat(captor.getValue()).contains("update", "91", "100");
    }

    @Test
    public void no_exist_order_should_insert() {
        //TODO
        OrderModel model = mock(OrderModel.class);

        Order order = new Order() {{
            setId(91);
            setAmount(100);
        }};
        doAnswer(invocation -> {
            Consumer insertCallback = invocation.getArgument(1);
            insertCallback.accept(order);
            return null;
        }).when(model).save(any(Order.class), any(Consumer.class), any(Consumer.class));

        SystemLogger mockSystemLogger = mock(SystemLogger.class);
        OrderController orderController = new OrderController(model, mockSystemLogger);

        orderController.save(order);

        ArgumentCaptor<String> captor = forClass(String.class);
        verify(mockSystemLogger).log(captor.capture());
        assertThat(captor.getValue()).contains("insert", "91", "100");
    }

    @Test
    public void verify_lambda_function_of_delete() {
        //TODO
//        OrderModel model = new OrderModel() {
//            @Override
//            public void save(Order order, Consumer<Order> insertCallback, Consumer<Order> updateCallback) {
//
//            }
//
//            @Override
//            public void delete(Predicate<Order> predicate) {
//                assertThat(predicate.test(new Order(){{
//                    setAmount(99);
//                }})).isFalse();
//                assertThat(predicate.test(new Order(){{
//                    setAmount(101);
//                }})).isTrue();
//            }
//        };
        OrderModel model = mock(OrderModel.class);
        SystemLogger mockSystemLogger = mock(SystemLogger.class);
        OrderController orderController = new OrderController(model, mockSystemLogger);

        orderController.deleteAmountMoreThan100();

        ArgumentCaptor<Predicate> captor = forClass(Predicate.class);
        verify(model).delete(captor.capture());
        assertThat(captor.getValue().test(new Order() {{
            setAmount(99);
        }})).isFalse();
        assertThat(captor.getValue().test(new Order() {{
            setAmount(101);
        }})).isTrue();
    }
}
