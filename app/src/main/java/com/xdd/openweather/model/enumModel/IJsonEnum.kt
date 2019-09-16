package com.xdd.openweather.model.enumModel

import com.google.gson.*
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

interface IJsonEnum {
    companion object {
        private val jsonEnums: Map<Class<out IJsonEnum>, ICompanion<out IJsonEnum>> =
            mapOf(
                LocationEnum::class.java to LocationEnum.Companion,
                WeatherElementEnum::class.java to WeatherElementEnum.Companion
            )

        // for conversion from enum class to name that can be recognized by remote server
        val converterFactory = object : Converter.Factory() {
            override fun stringConverter(
                type: Type,
                annotations: Array<Annotation>,
                retrofit: Retrofit
            ): Converter<*, String>? {
                return if (jsonEnums.containsKey(type)) {
                    Converter<IJsonEnum, String> { it.remoteName }
                } else {
                    super.stringConverter(type, annotations, retrofit)
                }
            }
        }

        // for conversion between enum class and Json
        fun decorateGsonBuilder(builder: GsonBuilder) {
            for ((enumClass, enumCompanion) in jsonEnums) {
                builder.registerTypeAdapter(enumClass, enumCompanion.jsonTypeAdapter)
            }
        }
    }

    interface ICompanion<T : IJsonEnum> {
        val jsonTypeAdapter: JsonAdapter<T>
    }

    abstract class JsonAdapter<T : IJsonEnum> : JsonSerializer<T>, JsonDeserializer<T> {
        protected abstract val enums: Array<T>

        private val enumMap by lazy {
            enums.map { it.remoteName to it }.toMap()
        }

        override fun serialize(
            src: T,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return context.serialize(src.remoteName)
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): T {
            return enumMap[json.asString] ?: error("Unknown jsonElement:$json for map:$enumMap")
        }
    }

    val remoteName: String
}