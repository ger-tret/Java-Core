package com.trainee.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@MockitoBean(types = {CustomLinkedList.class})
public class CustomLinkedListTests {

    private CustomLinkedList<String> linkedList;
    @Autowired
    void CustomLinkedListTests(CustomLinkedList<String> linkedList){
        this.linkedList = linkedList;
    }

    @BeforeEach
    void setUp(){
        linkedList = new CustomLinkedList<>();
    }

    @Test
    void test_adding_first() {
        linkedList.addFirst("f");
        Assertions.assertEquals("f", linkedList.getFirst());
    }

    @Test
    void test_adding_last() {
        linkedList.addLast("l");
        Assertions.assertEquals("l", linkedList.getLast());
    }

    @Test
    void test_adding_at_index_throws_OutOfBoundsException(){
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> linkedList.add(12, "e"));
    }

    @Test
    void test_getting_size_after_adding_element(){
        linkedList.addFirst("f");
        Assertions.assertEquals(1, linkedList.size());
    }

    @Test
    void test_getting_by_index(){
        linkedList.addFirst("f");
        linkedList.addLast("l");
        Assertions.assertEquals("l", linkedList.get(1));
    }

    @Test
    void test_removing_first(){
        linkedList.addFirst("f");
        Assertions.assertEquals("f", linkedList.removeFirst());
    }

    @Test
    void test_removing_last(){
        linkedList.addFirst("l");
        Assertions.assertEquals("l", linkedList.removeLast());
    }

    @Test
    void test_removing_by_index(){
        linkedList.addFirst("f");
        linkedList.addLast("l");
        Assertions.assertEquals("f", linkedList.remove(0));
    }

}
