package com.riders.thelab.core.common.bus

/**
 * Custom annotation to recognise the subscribe function automatically
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Listen
