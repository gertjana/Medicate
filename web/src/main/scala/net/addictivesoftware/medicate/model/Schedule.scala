package net.addictivesoftware.medicate.model

/**
 * Enumeration of times in a day a @see Dose of @see Medicine can be taken
 */
object Schedule extends Enumeration {
    type Schedule = Value
    val Wakeup, Breakfast, Lunch, Dinner, BeforeSleep = Value
}