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

public class Client {
    private static Client self = null;
    private Socket sock;
    private static final Lock mutex = new ReentrantLock(true);

    private Client() {
    }

    public static Client getInstance() {
        mutex.lock();
        if (self == null) {
            self = new Client();
        }
        mutex.unlock();
        return self;
    }

    public void connect(final String ip, final int port) {
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

    public void send(final String toSend) {
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
