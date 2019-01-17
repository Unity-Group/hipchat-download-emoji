package pl.unity.criticalStuff.emots

import com.fasterxml.jackson.core.type.TypeReference

val fieldBasedMapper by lazy { JacksonUtils.fieldBasedMapper() }

fun <T: Any> T.writeAsJson() = fieldBasedMapper.writeValueAsString(this)

inline fun <reified T: Any> readFromJson(json: String) =
	fieldBasedMapper.readerFor(object : TypeReference<T>() {  }).readValue<T>(json)


inline fun <reified T> Sequence<String>.mapFromJson(): Sequence<T> {
	val reader = JacksonUtils.fieldBasedMapper().readerFor(T::class.java)

	return map { reader.readValue<T>(it) }
}

inline fun <reified T> List<String>.mapFromJson(): List<T> {
	val reader = JacksonUtils.fieldBasedMapper().readerFor(T::class.java)

	return map { reader.readValue<T>(it) }
}
