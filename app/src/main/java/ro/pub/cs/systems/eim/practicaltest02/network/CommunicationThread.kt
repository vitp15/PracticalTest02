package ro.pub.cs.systems.eim.practicaltest02.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants
import ro.pub.cs.systems.eim.practicaltest02.utils.Utils
import ro.pub.cs.systems.eim.practicaltest02.utils.WeatherInfo
import java.io.IOException
import java.net.Socket

class CommunicationThread(
    private val server: ServerThread,
    private val socket: Socket,
) : Thread() {

    override fun run() {
        try {
            Log.v(Constants.TAG, "Connection opened with ${socket.inetAddress}:${socket.localPort}")

            val writer = Utils.getWriter(socket)
            var reader = Utils.getReader(socket)

            val city = reader.readLine()
            val information = reader.readLine()

            if (city == null || information == null) {
                writer.println("400 Bad Request")
                writer.flush()
            } else {
                var result: WeatherInfo? = null
                val data = server.getData()
                if (data.get(city) != null) {
                    result = data.get(city)!!
                } else {
                    val client = OkHttpClient()
                    val request: Request? = Request.Builder()
                        .url("https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=e03c3b32cfb5a6f7069f2ef29237d87e&lang=ro")
                        .build()
                    try {
                        client.newCall(request!!).execute().use { response ->
                            if (response.isSuccessful && response.body != null) {
                                val content = response.body!!.string()
                                Log.d(Constants.TAG, "Response after request: $content")

                                val jsonResponse = JSONObject(content)
                                val temperature: String =
                                    jsonResponse.getJSONObject("main").getString("temp")
                                val windSpeed: String =
                                    jsonResponse.getJSONObject("wind").getString("speed")
                                val condition: String =
                                    jsonResponse.getJSONArray("weather").getJSONObject(0)
                                        .getString("description")
                                val pressure: String =
                                    jsonResponse.getJSONObject("main").getString("pressure")
                                val humidity: String =
                                    jsonResponse.getJSONObject("main").getString("humidity")

                                result = WeatherInfo(
                                    temperature,
                                    windSpeed,
                                    condition,
                                    pressure,
                                    humidity
                                )
                                Log.d(Constants.TAG, "WeatherInfo: $result")
                                data.put(city, result)
                            } else {
                                Log.e(
                                    Constants.TAG,
                                    "Cererea nu a avut succes. Cod: " + response.code
                                )
                            }
                        }
                    } catch (e: IOException) {
                        Log.e(Constants.TAG, "Cererea de rețea a eșuat: " + e.message)
                        if (Constants.DEBUG) {
                            e.printStackTrace()
                        }
                    }
                }
                if (result != null) {
                    when (information) {
                        "all" -> {
                            writer.println(result.toString())
                        }
                        "temperature" -> {
                            writer.println(result.temperature)
                        }
                        "windSpeed" -> {
                            writer.println(result.windSpeed)
                        }
                        "condition" -> {
                            writer.println(result.condition)
                        }
                        "pressure" -> {
                            writer.println(result.pressure)
                        }
                        "humidity" -> {
                            writer.println(result.humidity)
                        }
                        else -> {
                            writer.println("400 Bad Request")
                        }
                    }
                } else {
                    writer.println("404 Not Found")
                }
                writer.flush()
            }

            socket.close()
            Log.v(Constants.TAG, "Connection closed")
        } catch (exception: Exception) {
            Log.e(Constants.TAG, "An exception has occurred: ${exception.message}")
            if (Constants.DEBUG) {
                exception.printStackTrace()
            }
        }
    }
}

