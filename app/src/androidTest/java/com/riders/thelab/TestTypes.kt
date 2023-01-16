package com.riders.thelab

import kotools.types.collection.NotEmptySet
import kotools.types.collection.notEmptySetOf
import kotools.types.number.StrictlyPositiveInt
import kotools.types.number.toStrictlyPositiveInt
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    val test: StrictlyPositiveInt = 42.toStrictlyPositiveInt().getOrThrow()
    val pierre: NotBlankString =
        "Pierre".toNotBlankString().getOrThrow()

    val e : NotEmptySet<String> = notEmptySetOf("1")
    val ec : NotEmptySet<String> = notEmptySetOf("1", "2","3")

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