package com.mdb.ps;

import com.mdb.ps.service.ListenerInit;


public class App 
{
    public static void main(String[] args) {
        System.out.println("Starting Region Listener PoC");

        ListenerInit listenerService = new ListenerInit();
        listenerService.initializeListeners();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
