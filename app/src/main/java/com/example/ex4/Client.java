package com.example.ex4;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TCP client that allows connecting with a remote server and send to it.
 * Implemented as a singleton.
 */
public class Client {
    private static Client self = null;
    private Socket sock;
    private static final Lock mutex = new ReentrantLock(true);

    private Client() {
    }

    /**
     * Get instance of the client, works as a thread-safe singleton.
     * @return Client self.
     */
    public static Client getInstance() {
        mutex.lock();
        if (self == null) {
            self = new Client();
        }
        mutex.unlock();
        return self;
    }

    /**
     * Connects to the server provided at the ip and the port.
     * @param ip
     * @param port
     */
    public void connect(final String ip, final int port) {
        // Starts a new thread, give it an anonymous runnable the opens the listening thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                mutex.lock();
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    sock = new Socket(serverAddr, port);
                } catch (IOException e) {
                    Log.e("TCP", "C: Error", e);
                    System.out.println((e.toString()));
                } finally {
                    mutex.unlock();
                }
            }
        }).start();
    }

    /**
     * Sends the given message to the server.
     * @param toSend
     */
    public void send(final String toSend) {
        // Starts a new thread with an anonymous runnable that's sends the data.
        new Thread(new Runnable() {
            @Override
            public void run() {
                mutex.lock();
                try {
                    if (sock != null) {
                        PrintWriter sender = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
                        sender.println(toSend);
                        sender.flush();
                    }
                } catch (IOException e) {
                    Log.e("TCP", "C: Error", e);
                    System.out.println((e.toString()));
                } finally {
                    mutex.unlock();
                }
            }
        }).start();
    }

    /**
     * disconnects the connection to the server.
     */
    public void disconnect() {
        mutex.lock();
        try {
            this.sock.close();
        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
            System.out.println((e.toString()));
        }
        mutex.unlock();
    }
}
