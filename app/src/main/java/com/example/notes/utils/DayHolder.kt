package com.example.notes.utils

sealed class DayHolder<DayState>(
    val mondayState: DayState? = null,
    val fridayState: DayState? = null,
    val tuesdayState: DayState? = null,
    val saturdayState: DayState? = null,
    val thursdayState: DayState? = null,
    val wednesdayState: DayState? = null,
    val sundayState: DayState? = null
) {
    class Monday<DayState>(mondayState: DayState?): DayHolder<DayState>(mondayState = mondayState)
    class Friday<DayState>(fridayState: DayState?): DayHolder<DayState>(fridayState = fridayState)
    class Tuesday<DayState>(tuesdayState: DayState?): DayHolder<DayState>(tuesdayState = tuesdayState)
    class Saturday<DayState>(saturdayState: DayState?): DayHolder<DayState>(saturdayState = saturdayState)
    class Thursday<DayState>(thursdayState: DayState?): DayHolder<DayState>(thursdayState = thursdayState)
    class Wednesday<DayState>(wednesdayState: DayState?): DayHolder<DayState>(wednesdayState = wednesdayState)
    class Sunday<DayState>(sundayState: DayState?): DayHolder<DayState>(sundayState = sundayState)

    companion object {
        fun getDayState(dayOfWeek: DayOfWeek): DayHolder<DayState> = when (dayOfWeek) {
            DayOfWeek.Monday -> Monday(null)
            DayOfWeek.Friday -> Friday(null)
            DayOfWeek.Tuesday -> Tuesday(null)
            DayOfWeek.Saturday -> Saturday(null)
            DayOfWeek.Thursday -> Thursday(null)
            DayOfWeek.Wednesday -> Wednesday(null)
            DayOfWeek.Sunday -> Sunday(null)
        }
    }
}

data class DayState(
    val pickedDay: DayOfWeek,
)