package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
    private final String address;
    private final int port;
    private final String word;
    private final String informationType;

    private final TextView textView;

    private Socket socket;

    public ClientThread(String address, int port, String name, String informationType, TextView textView) {
        this.address = address;
        this.port = port;
        this.word = name;
        this.informationType = informationType;
        this.textView = textView;
    }

    @Override
    public void run() {

        try {
            // tries to establish a socket connection to the server
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            //todo: Write other specific data (ex:city) !!! Don't forget flush
            // information type to the server
            printWriter.println(word);
            printWriter.flush();
            printWriter.println(informationType);
            printWriter.flush();
            String information;

            // reads the information from the server
            while ((information = bufferedReader.readLine()) != null) {
                final String finalizedInformation = information;
                // updates the UI with the information. This is done using postt() method to ensure it is executed on UI thread
                textView.post(() -> textView.setText(finalizedInformation));

            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
