/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entityreborn.communication;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;

/**
 *
 * @author import
 */
public class NodePoint {
    protected Socket socket;
    private boolean isInited = false;
    protected Thread owningThread;
    protected boolean alive = true;
    protected int type;

    protected void cleanup() {
        try {
            socket.setLinger(1);
            socket.close();
            
            socket = null;
        } catch (Exception e) {
            Logger.getLogger(NodePoint.class.getName()).log(Level.WARNING, "Exception while closing node:", e);
        }
    }
    
    public boolean isAlive() {
        return alive;
    }

    public void connect(String endpoint) {
        socket.setLinger(-1);
        socket.connect(endpoint);
    }

    public void disconnect(String endpoint) {
        socket.disconnect(endpoint);
    }

    public void listen(String endpoint) {
        socket.setLinger(-1);
        socket.bind(endpoint);
    }
    
    public boolean isInitialized() {
        return isInited;
    }

    protected void init(ZContext context, int type) {
        if (isInited) {
            return;
        }
        
        this.type = type;
        
        socket = context.createSocket(type);
        isInited = true;
    }
    
    public void start() {
        owningThread.start();
    }
    
    public void stop() {
        alive = false;
        
        if (this instanceof Publisher) {
            owningThread.interrupt();
        }
    }
    
    public Socket getSocket() {
        return socket;
    }
}
