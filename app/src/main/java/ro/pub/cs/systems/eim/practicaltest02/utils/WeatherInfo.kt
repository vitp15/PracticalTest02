package ro.pub.cs.systems.eim.practicaltest02.utils

class WeatherInfo {
    val temperature: String
    val windSpeed: String
    val condition: String
    val pressure: String
    val humidity: String

    constructor(
        temperature: String,
        windSpeed: String,
        condition: String,
        pressure: String,
        humidity: String
    ) {
        this.temperature = temperature
        this.windSpeed = windSpeed
        this.condition = condition
        this.pressure = pressure
        this.humidity = humidity
    }

    override fun toString(): String {
        return "WeatherInfo(temperature='$temperature', windSpeed='$windSpeed', condition='$condition', pressure='$pressure', humidity='$humidity')"
    }
}