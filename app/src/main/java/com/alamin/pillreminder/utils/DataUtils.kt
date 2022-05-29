package com.alamin.pillreminder.utils

class DataUtils {
    fun pillTypes (): List<String> {
       var pillTypes = ArrayList<String>()
        pillTypes.add("Pill");
        pillTypes.add("Dosage")
        pillTypes.add("Injection")
        pillTypes.add("Therapy")
        return pillTypes;
    }
}