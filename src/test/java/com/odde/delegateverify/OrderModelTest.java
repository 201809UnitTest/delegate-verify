package com.odde.delegateverify;

import org.junit.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class OrderModelTest {

    private Repository repository = mock(Repository.class);

    MyOrderModel myOrderModel = new MyOrderModel(repository);
    Order order = new Order();
    Consumer mockInsert = mock(Consumer.class);
    Consumer mockUpdate = mock(Consumer.class);

    @Test
    public void insert_order() {
        //TODO
        MyOrderModel myOrderModel = new MyOrderModel(repository);
    }
    
    @Test
    public void update_order() {
        //TODO
        givenExistsOrder(order);

        myOrderModel.save(order, mockInsert, mockUpdate);

        verify(repository).update(order);
        verify(mockUpdate).accept(order);
        verify(repository, never()).insert(order);
        verify(mockInsert, never()).accept(order);
    }

    private void givenExistsOrder(Order order) {
        when(repository.isExist(order)).thenReturn(true);
    }

}
