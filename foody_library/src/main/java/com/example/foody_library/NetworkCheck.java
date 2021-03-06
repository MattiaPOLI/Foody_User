package com.example.foody_library;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkCheck extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    private boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return isOnline();
    }

}