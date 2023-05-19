package com.riders.thelab


import kotools.types.collection.NotEmptySet
import kotools.types.collection.notEmptySetOf
import kotools.types.number.PositiveInt
import kotools.types.number.StrictlyPositiveInt
import kotools.types.number.toStrictlyPositiveInt
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import kotlin.random.Random
import kotlin.random.nextInt

fun main1() {
    val test: StrictlyPositiveInt = 42.toStrictlyPositiveInt().getOrThrow()
    val pierre: NotBlankString =
        "Pierre".toNotBlankString().getOrThrow()

    val e: NotEmptySet<String> = notEmptySetOf("1")
    val ec: NotEmptySet<String> = notEmptySetOf("1", "2", "3")

    print(test)
    print(pierre)

    // Bad
    val day: StrictlyPositiveInt = Random.nextInt(1..31)
        .toStrictlyPositiveInt()
        .getOrThrow()
    val result: NotBlankString = "Day $day".toNotBlankString()
        .getOrThrow()
    println(result)

    // Good
    val resultGoot: NotBlankString = Random.nextInt(1..31)
        .toStrictlyPositiveInt()
        .mapCatching { "Day $it".toNotBlankString().getOrThrow() }
        .getOrThrow()

    println(resultGoot)
}


// ---------- Internal code ----------

inline fun <T> notBlankString(builder: NotBlankStringBuilder.() -> T): Result<T> {
    return runCatching { NotBlankStringBuilder.builder() }
}

object NotBlankStringBuilder {
    fun String.bind(): NotBlankString = toNotBlankString().getOrThrow()
}

// ---------- Sample ----------
data class Activity(val activityName: NotBlankString)
data class Person(
    val firstName: NotBlankString,
    val lastName: NotBlankString,
    val age: PositiveInt,
    val activities: NotEmptySet<Activity>
) {
    override fun toString(): String = "$firstName $lastName $age $activities"
}

fun main3() {
    notBlankString { "Pi'erre".bind() to "Bourne".bind() }
        .map {
            Person(
                it.first,
                it.second,
                23.toStrictlyPositiveInt().getOrThrow(),
                notEmptySetOf(
                    Activity("Producer".toNotBlankString().getOrThrow()),
                    Activity("Beat maker".toNotBlankString().getOrThrow()),
                )
            )
        }.let(::println)

    /*val result: Result<Person> = notBlankString {
        Person(firstName = "Pi'erre".bind(), lastName = "Bourne".bind())
    }*/

    // println(result) // Success(Pi'erre Bourne)
}


// ---------- Internal code ----------

inline fun <T> result(builder: ResultBuilder.() -> T): Result<T> {
    return runCatching { ResultBuilder.builder() }
}

object ResultBuilder {
    fun String.toNotBlankString(): NotBlankString =
        kotools.types.text.toNotBlankString().getOrThrow()
}

// ---------- Sample ----------

data class Artist(val firstName: NotBlankString, val lastName: NotBlankString) {
    override fun toString(): String = "$firstName $lastName"
}

fun main() {
    val result: Result<Artist> = result {
        Artist(firstName = "Pi'erre".toNotBlankString(), lastName = "Bourne".toNotBlankString())
    }
    println(result) // Success(Pi'erre Bourne)
}
