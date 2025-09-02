package com.trainee.task;

public class CustomLinkedList<E> {

    transient int size = 0;
    transient ListNode<E> first;
    transient ListNode<E> last;

    static class ListNode<E> {
        E data;
        ListNode<E> next, previous;

        ListNode(ListNode<E> next, ListNode<E> prev, E object) {
            this.data = object;
            this.next = next;
            this.previous = prev;
        }
    }

    private boolean checkIndexPosition(int index){
        if (index >= 0 && index <= size){
            return true;
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    private void linkFirst(E element) {
        final ListNode<E> firstListNode = first;
        final ListNode<E> eListNode = new ListNode<>(firstListNode, null, element);
        first = eListNode;
        if (firstListNode == null)
            last = eListNode;
        else
            first.previous = eListNode;
        size++;
    }

    private void linkLast(E element) {
        ListNode<E> lastListNode = last;
        ListNode<E> eListNode = new ListNode<>(null, lastListNode, element);
        last = eListNode;
        if (lastListNode == null)
            first = eListNode;
        else
            lastListNode.next = eListNode;
        size++;
    }

    private void linkBefore(E element, ListNode<E> successorNode) {
        ListNode<E> targetPlaceListNode = successorNode.previous;
        ListNode<E> eListNode = new ListNode<>(targetPlaceListNode, successorNode, element);
        successorNode.previous = eListNode;
        if (targetPlaceListNode == null)
            first = eListNode;
        else
            targetPlaceListNode.next = eListNode;
        size++;
    }

    private E unlinkFirst(ListNode<E> target) {
        E element = target.data;
        ListNode<E> l = target.next;
        target.data = null;
        target.next = null;
        first = l;
        if (l == null)
            last = null;
        else
            l.previous = null;
        size--;
        return element;
    }

    private E unlinkLast(ListNode<E> target) {
        E element = target.data;
        ListNode<E> l = target.previous;
        target.data = null;
        target.previous = null;
        last = l;
        if (l == null)
            first = null;
        else
            l.next = null;
        size--;
        return element;

    }

    private E unlinkNode(ListNode<E> target) {
        E element = target.data;
        ListNode<E> next = target.next;
        ListNode<E> prev = target.previous;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            target.previous = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.previous = prev;
            target.next = null;
        }
        target.data = null;
        size--;
        return element;
    }

    ListNode<E> getNodeByIndex(int index){
        if(index < (size + 1)){
            ListNode<E> target = first;
            for(int i = 0; i < index; i++){
                target = target.next;
            }
            return target;
        }
        else {
            ListNode<E> target = last;
            for(int i = size - 1; i > index; i--){
                target = target.previous;
            }
            return target;
        }
    }

    public int size() {
        return size;
    }

    public void addFirst(E element){
        linkFirst(element);
    }

    public void addLast(E element){
        linkLast(element);
    }

    public void add(int index, E element){
        checkIndexPosition(index);
        if(index == size)
            linkLast(element);
        else
            linkBefore(element, getNodeByIndex(index));
    }

    public E getFirst() {
        ListNode<E> f = first;
        if (f == null) {
            throw new RuntimeException("No such element");
        }
        return f.data;
    }

    public E getLast() {
        ListNode<E> l = last;
        if (l == null) {
            throw new RuntimeException("No such element");
        }
        return l.data;
    }

    public E get(int index){
        return getNodeByIndex(index).data;
    }

    public E removeFirst(){
        ListNode<E> f = first;
        if(f == null){
            throw new RuntimeException("No such element");
        }
        return unlinkFirst(f);
    }

    public E removeLast(){
        ListNode<E> l = last;
        if(l == null){
            throw new RuntimeException("No such element");
        }
        return unlinkLast(l);
    }

    public E remove(int index){
        return unlinkNode(getNodeByIndex(index));
    }


    public boolean isEmpty() {
        return size == 0 ? true : false;
    }

    public Object[] toArray() {
        Object array[] = new Object[size];
        int i = 0;
        for (ListNode<E> l = first; l != null; l = l.next) {
            array[i++] = l.data;
        }
        return array;
    }
}
