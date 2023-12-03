package models.dataStructures;

public class SinglyLinkedList<E> {
    private Node<E> head = null;
    private Node<E> tail = null;
    private int size;

    public SinglyLinkedList() {
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(E e) {
        head = new Node<>(e, head);
        if (size == 0)
            tail = head;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) return null;
        E output = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0)
            tail = null;
        return output;
    }

    public E first() {
        if (isEmpty()) return null;
        return head.getElement();
    }
    public String toString() {
        StringBuilder result = new StringBuilder();
        int sizeOfList = size;
        Node<E> current = this.head;
        while (sizeOfList > 0) {
            result.append(current.getElement().toString());
            current = current.getNext();
            result.append(" ");
            sizeOfList--;
        }
        return "List: " + result;
    }
}