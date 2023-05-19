package com.riders.thelab.core.bus

/**
 * Custom annotation to recognise the subscribe function automatically
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Listen()
