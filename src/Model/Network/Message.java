package Model.Network;

import java.io.Serializable;

/**
 * Message to commiunicate between server and client at Game
 */
public class Message implements Serializable {
    private int header;
    private Object body;
    private Object body2;
    private Object body3;

    /**
     * Counstructor with one body
     * 
     * @param header
     * @param body
     */
    public Message(int header, Object body) {
        this.header = header;
        this.body = body;
    }

    /**
     * * Counstructor with two body
     * 
     * @param header
     * @param body
     * @param body2
     */
    public Message(int header, Object body, Object body2) {
        this.header = header;
        this.body = body;
        this.body2 = body2;
    }

    /**
     * * Counstructor with three body
     * 
     * @param header
     * @param body
     * @param body2
     * @param body3
     */
    public Message(int header, Object body, Object body2, Object body3) {
        this.header = header;
        this.body = body;
        this.body2 = body2;
        this.body3 = body3;
    }

    public Object getBody() {
        return body;
    }

    public int getHeader() {
        return header;
    }

    public Object getBody2() {
        return body2;
    }

    public Object getBody3() {
        return body3;
    }
}