package simplequeue;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MostRecentlyInsertedQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable {

    private static final long serialVersionUID = 1L;

    private int countOfNodes;
    private int capacity;
    private Node<E> head;
    private Node<E> tail;

    private static class Node<E>{
        private E element;
        private Node<E> next;

        public Node() {
        }

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public MostRecentlyInsertedQueue(int capacity) {
        this.capacity = capacity;
        countOfNodes = 0;
        head = null;
        tail = null;
    }

    public boolean offer(E element) {
        if(countOfNodes<capacity && element != null){
            addNodeToTail(element);
            countOfNodes++;
            return true;
        }
        else if (countOfNodes>=capacity && element != null){
            poll();
            addNodeToTail(element);
            countOfNodes++;
            return true;
        }
        else if (element == null){
            throw new NullPointerException("The offered element is null");
        }
        return false;
    }

    public E poll() {
        if (isEmpty()) {
            tail = null;
            throw new NoSuchElementException("The queue is empty!");
        }
        E elementForRemoving = head.element;
        head = head.next;
        countOfNodes--;
        return elementForRemoving;
    }

    public E peek() {
        if (isEmpty())
            throw new NoSuchElementException("The queue is empty!");
        E   elementForGetting = head.element;
        head = head.next;
        countOfNodes--;
        return elementForGetting;
    }

    @Override
    public void clear() {
        while (!isEmpty()){
            poll();
        }
    }

    public boolean isEmpty(){
        return head == null;
    }

    public int size() {
        return countOfNodes;
    }

    private void addNodeToTail(E element){
        Node currentNode = tail;
        tail = new Node<E>();
        tail.element = element;
        if (isEmpty()) {
            head = tail;
        }
        else {
            currentNode.next = tail;
        }
    }

    public Iterator<E> iterator() {
        return new MostRecentlyInsertedQueueIterator<E>();
    }

    private class MostRecentlyInsertedQueueIterator<E> implements Iterator<E>{

        private Node<E> currentNode;

        public MostRecentlyInsertedQueueIterator() {
            currentNode = (Node<E>) MostRecentlyInsertedQueue.this.head;
        }

        public void remove() {
            Node<E> node = (Node<E>) MostRecentlyInsertedQueue.this.tail;
            if (node == null) throw new IllegalStateException();
            node.setElement(null);
        }

        public boolean hasNext() {
            return (currentNode != null);
        }

        public E next() {
            if(!hasNext()) throw new NoSuchElementException("There is no more elements.");
            currentNode = currentNode.getNext();
            E newElement = currentNode.element;
            return newElement;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node tmp = head;
        sb.append("[");
        while (tmp != null) {
            if (tmp == tail)
            sb.append(tmp.element).append("");
            else
                sb.append(tmp.element).append(", ");
                tmp = tmp.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
