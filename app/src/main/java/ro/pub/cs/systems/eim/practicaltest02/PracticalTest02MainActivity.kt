package ro.pub.cs.systems.eim.practicaltest02

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread

class PracticalTest02MainActivity : AppCompatActivity() {
    var serverThread: ServerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startServerButton = findViewById<Button>(R.id.start_server)
        startServerButton.setOnClickListener {
            val serverPort = findViewById<EditText>(R.id.server_port).text.toString()
            if (serverPort.isNotEmpty() && serverPort.toIntOrNull() != null) {
                if (serverThread != null && serverThread!!.isRunning) {
                    serverThread!!.stopServer()
                }
                serverThread = ServerThread(serverPort.toInt())
                serverThread?.startServer()
            }
        }

        val getInformationButton = findViewById<Button>(R.id.get_info)
        getInformationButton.setOnClickListener {
            val address = findViewById<EditText>(R.id.address).text.toString()
            val port = findViewById<EditText>(R.id.client_port).text.toString()
            val city = findViewById<EditText>(R.id.city).text.toString()
            val information = findViewById<EditText>(R.id.info).text.toString()

            if (address.isNotEmpty() && port.isNotEmpty() && city.isNotEmpty() && information.isNotEmpty()) {
                val clientThread = ClientThread(
                    address,
                    port.toInt(),
                    city,
                    information,
                    findViewById(R.id.information)
                )
                clientThread.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serverThread?.stopServer()
    }
}