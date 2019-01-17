package pl.unity.criticalStuff.emots

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.io.File
import org.apache.commons.cli.HelpFormatter
import java.lang.RuntimeException


object Start {
	@JvmStatic
	fun main(vararg args: String) {
		val options = Options().apply {
			addRequiredOption("t", "token", true,
				"HipchatApi token with at least view group privalages")
			addRequiredOption("d", "destination", true,
				"Target dir to download emojis to, this tool will attempt to create it if it not exists")
		}

		val commandLine = try {
			DefaultParser().parse(options, args)
		} catch (e: Exception) {
			HelpFormatter().printHelp("hipchat-download-emoji.sh", options)
			System.exit(1)
			throw RuntimeException("This code is unreachable, but compiler doesnt understand it (shrug)")
		}

		val token = commandLine.getOptionValue("token")!!
		val targetDir = commandLine.getOptionValue("destination")!!

		HipchatEmots(token).downloadEmojis(File(targetDir))
	}
}

