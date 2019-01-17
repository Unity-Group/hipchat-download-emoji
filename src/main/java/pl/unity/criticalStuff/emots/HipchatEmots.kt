package pl.unity.criticalStuff.emots

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.upsaily.commons.utils.json.readFromJson
import org.apache.commons.io.IOUtils
import pl.unity.criticalStuff.emots.HipchatApi.Emoji
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.print.attribute.standard.Destination



class HipchatEmots(token: String) {

	private val api: HipchatApi = HipchatApi(token)

	fun downloadEmojis(destination: File) {
		val emojis = api.getEmoticons()


		emojis.forEachIndexed { i, emoji ->
			val finalDestination = File("${destination.canonicalPath}/${emoji.type}")
			finalDestination.mkdirs()
			val emojiFile = "${finalDestination.canonicalPath}/${emoji.shortcut}${emoji.extension()}"

			FileOutputStream(emojiFile).use {
				IOUtils.write(downloadEmoji(emoji), it)
			}

			println("Downloaded ${emoji.shortcut} as ${emojiFile}, ${emojis.size - i - 1} to go")
		}
	}
}

class HipchatApi(private val token: String) {

	fun getEmoticons(): List<Emoji> {
		val url = GET_EMOTICON
		val urlConnection = URL(url).openConnection() as HttpURLConnection
		urlConnection.requestMethod = "GET"
		urlConnection.doOutput = false
		urlConnection.doInput = true
		urlConnection.addRequestProperty("Authorization", "Bearer " + token)
		urlConnection.addRequestProperty("Content-type", "application/json")

		urlConnection.connect()

		return readFromJson<Response>(urlConnection.inputStream.reader().readText()).items
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	data class Response(
		val items: List<Emoji>
	)

	@JsonIgnoreProperties(ignoreUnknown = true)
	data class Emoji(
		val shortcut: String,
		val url: String,
		val type: String
	) {
		fun fullUrl() = "\\.[^.]+$".toRegex().replace(url) {
			"@4x${it.value}"
		}

		fun extension() = "\\.[^.]+$".toRegex().find(url)!!.value
	}

	companion object {
		private val API_ADDRESS = "https://api.hipchat.com"
		private val GET_EMOTICON = "$API_ADDRESS/v2/emoticon?max-results=1000"
	}
}

fun downloadEmoji(emoji: Emoji): ByteArray =
	try {
		download(emoji.fullUrl())
	} catch (e: Exception) {
		println("Failed to download full version of ${emoji.shortcut}")
		download(emoji.url)
	}


fun download(url: String): ByteArray {
	val urlConnection = URL(url).openConnection() as HttpURLConnection
	urlConnection.requestMethod = "GET"
	urlConnection.doOutput = false
	urlConnection.doInput = true

	urlConnection.connect()

	return urlConnection.inputStream.use {  it.readBytes() }
}
