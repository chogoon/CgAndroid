package com.chogoon.cglib

import com.google.gson.*
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type
import java.util.*

open class DateTimeTypeConverter : JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    override fun serialize(src: DateTime, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(ISODateTimeFormat.dateTime().print(src))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DateTime {
        return try {
            DateTime(json.asLong * 1000)
        } catch (e : JsonSyntaxException){
            ISODateTimeFormat.dateTimeParser().parseDateTime(json.asString)
        } catch (e : UnsupportedOperationException){
            val date = context.deserialize<Date>(json, Date::class.java)
            DateTime(date)
        } catch (e : NumberFormatException){
            DateTime(json.asString)
        }
    }
}