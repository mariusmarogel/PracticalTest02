package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText server_port, client_ip_address, client_port, wordEditText;
    Button connect_server, get_info;
    Spinner spinner;
    TextView result;
    private ServerThread serverThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        server_port = findViewById(R.id.server_port);
        client_ip_address = findViewById(R.id.client_address);
        client_port = findViewById(R.id.client_port);
        connect_server = findViewById(R.id.server_connect);
        get_info = findViewById(R.id.get_info);
        spinner = findViewById(R.id.spinner);
        result = findViewById(R.id.result);
        wordEditText = findViewById(R.id.word);

        connect_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieves the server port. Checks if it is empty or not
                // Creates a new server thread with the port and starts it
                String serverPort = server_port.getText().toString();
                System.out.println(serverPort);
                if (serverPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                serverThread = new ServerThread(Integer.parseInt(serverPort));
                if (serverThread.getServerSocket() == null) {
                    Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                    return;
                }
                serverThread.start();
            }
        });

        get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieves the client address and port. Checks if they are empty or not
                //  Checks if the server thread is alive. Then creates a new client thread with the address, port, city and information type
                //  and starts it
                String clientAddress = client_ip_address.getText().toString();
                String clientPort = client_port.getText().toString();
                if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //todo: Change info to what is the key
                String word = wordEditText.getText().toString();
                //String info = "";
                String informationType = spinner.getSelectedItem().toString();
                if (word.isEmpty() || informationType.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                informationType = "definition";
                result.setText("");
                //todo: Change constructor
                ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), word, informationType, result);
                clientThread.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}