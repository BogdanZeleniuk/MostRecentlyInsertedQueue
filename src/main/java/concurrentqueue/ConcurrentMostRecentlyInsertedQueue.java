package concurrentqueue;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by Admin on 15.09.2016.
 */
public class ConcurrentMostRecentlyInsertedQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable {

    private static final long serialVersionUID = 1L;

    private AtomicInteger capacity;
    private AtomicInteger countOfNodes = new AtomicInteger(0);
    private volatile Node head = new Node<E>(null,null);
    private volatile Node tail = head;

    private class Node<E>{
        private volatile E element;
        private volatile Node next;

        private final AtomicReferenceFieldUpdater<Node, Object> elementUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Node.class, Object.class, "element");

        private final AtomicReferenceFieldUpdater<Node,Node> nextUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");

        public Node() {
        }

        public Node(E element, Node next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() {
            return element;
        }

        public boolean casElement(E expect, E update) {
            return elementUpdater.compareAndSet(this, expect, update);
        }

        public void setElement(E element) {
            elementUpdater.set(this,element);
        }

        public Node getNext() {
            return next;
        }

        public boolean casNext(Node<E> expect, Node<E> update){
            return nextUpdater.compareAndSet(this, expect, update);
        }

        public void setNext(Node next) {
            nextUpdater.set(this, next);
        }
    }

    public ConcurrentMostRecentlyInsertedQueue(int capacity) {
        this.capacity = new AtomicInteger(capacity);
    }

    private final AtomicReferenceFieldUpdater<ConcurrentMostRecentlyInsertedQueue, Node> tailUpdater =
            AtomicReferenceFieldUpdater.newUpdater(ConcurrentMostRecentlyInsertedQueue.class, Node.class, "tail");
    private final AtomicReferenceFieldUpdater<ConcurrentMostRecentlyInsertedQueue, Node> headUpdater =
            AtomicReferenceFieldUpdater.newUpdater(ConcurrentMostRecentlyInsertedQueue.class,  Node.class, "head");

    public boolean casHead(Node<E> expect, Node<E> update){
        return headUpdater.compareAndSet(this,expect,update);
    }

    public boolean casTail(Node<E> expect, Node<E> update){
        return tailUpdater.compareAndSet(this, expect, update);
    }

    public Iterator<E> iterator() {
        return null;
    }

    public int size() {
        return countOfNodes.intValue();
    }

    public boolean offer(E element) {
        if (element == null)
            throw new NullPointerException("Element can not be null!");
        Node<E> elementForAdding = new Node<E>(element, null);
        for (;;) {
            Node<E> expected = tail;
            Node<E> updated = expected.getNext();
            if (expected == tail && countOfNodes.intValue()<capacity.intValue()) {
                if (updated == null) {
                    if (expected.casNext(updated, elementForAdding)) {
                        casTail(expected, elementForAdding);
                        countOfNodes.incrementAndGet();
                        return true;
                    }
                } else {
                    casTail(expected, updated);
                }
            }
            else if (expected == tail && countOfNodes.intValue()>=capacity.intValue()) {
                if (updated == null) {
                    if (expected.casNext(updated, elementForAdding)) {
                        poll();
                        casTail(expected, elementForAdding);
                        countOfNodes.incrementAndGet();
                        return true;
                    }
                } else {
                    casTail(expected, updated);
                }
            }
        }
    }

    public E poll() {
        for(;;) {
            Node<E> beforeFirstNode = head;
            Node<E> tailOnPolling = tail;
            Node<E> firstNode = beforeFirstNode.getNext();
            if (beforeFirstNode == head) {
                if (beforeFirstNode == tailOnPolling) {
                    if (firstNode == null)
                        return null;
                    else
                        casTail(tailOnPolling, firstNode);
                } else if (casHead(beforeFirstNode, firstNode)) {
                    E element = firstNode.getElement();
                    if (element != null) {
                        firstNode.setElement(null);
                        countOfNodes.decrementAndGet();
                        return element;
                    }
                }
            }
        }
    }

    public E peek() {
        for(;;) {
            Node<E> beforeFirstNode = head;
            Node<E> tailOnPolling = tail;
            Node<E> firstNode = beforeFirstNode.getNext();
            if (beforeFirstNode == head) {
                if (beforeFirstNode == tailOnPolling) {
                    if (firstNode == null)
                        return null;
                    else
                        casTail(tailOnPolling, firstNode);
                } else if (casHead(beforeFirstNode, firstNode)) {
                    E element = firstNode.getElement();
                    if (element != null) {
                        firstNode.setElement(null);
                        countOfNodes.decrementAndGet();
                        return element;
                    }
                    else
                        casHead(beforeFirstNode, firstNode);
                }
            }
        }
    }

    @Override
    public void clear() {
        while (!isEmpty()){
            poll();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node tmp = head.getNext();
        sb.append("[");
        while (tmp != null) {
            if (tmp == tail)
                sb.append(tmp.getElement()).append("");
            else
                sb.append(tmp.getElement()).append(", ");
            tmp = tmp.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}
