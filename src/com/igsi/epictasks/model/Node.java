package com.igsi.epictasks.model;

public class Node {
    public Task task;
    public Node prev;
    public Node next;

    public Node(Node prev, Task task, Node next) {
        this.prev = prev;
        this.task = task;
        this.next = next;
    }
}
