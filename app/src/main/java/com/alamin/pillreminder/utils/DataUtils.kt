package com.alamin.pillreminder.utils

class DataUtils {
    companion object{
        fun pillTypes (): List<String> {
            var pillTypes = ArrayList<String>()
            pillTypes.add("Pill");
            pillTypes.add("Dosage")
            pillTypes.add("Injection")
            pillTypes.add("Therapy")
            return pillTypes;
        }

        fun pillTimes (): List<String> {
            var pillTimes = ArrayList<String>()
            pillTimes.add("12 AM");
            pillTimes.add("1 AM")
            pillTimes.add("2 AM")
            pillTimes.add("3 AM")
            pillTimes.add("4 AM")
            pillTimes.add("5 AM")
            pillTimes.add("6 AM")
            pillTimes.add("7 AM")
            pillTimes.add("8 AM")
            pillTimes.add("9 AM")
            pillTimes.add("10 AM")
            pillTimes.add("11 AM")
            pillTimes.add("12 PM");
            pillTimes.add("1 PM")
            pillTimes.add("2 PM")
            pillTimes.add("3 PM")
            pillTimes.add("4 PM")
            pillTimes.add("5 PM")
            pillTimes.add("6 PM")
            pillTimes.add("7 PM")
            pillTimes.add("8 PM")
            pillTimes.add("9 PM")
            pillTimes.add("10 PM")
            pillTimes.add("11 PM")
            return pillTimes;
        }

        fun pillDosages(unit: String): List<String> {
            var pillDosages = ArrayList<String>()
            pillDosages.add("0.5 $unit");
            pillDosages.add("1 $unit")
            pillDosages.add("1.5 $unit")
            pillDosages.add("2 $unit")
            pillDosages.add("2.5 $unit")
            pillDosages.add("3 $unit")
            pillDosages.add("3.5 $unit")
            pillDosages.add("4 $unit")
            pillDosages.add("4.5 $unit")
            pillDosages.add("5 $unit")
            pillDosages.add("5.5 $unit")
            pillDosages.add("6 $unit")
            pillDosages.add("6.5 $unit")
            pillDosages.add("7 $unit")
            pillDosages.add("7.5 $unit")
            pillDosages.add("8 $unit")
            pillDosages.add("8.5 $unit")
            pillDosages.add("9 $unit")
            pillDosages.add("9.5 $unit")
            pillDosages.add("10 $unit")
            pillDosages.add("10.5 $unit")
            pillDosages.add("11 $unit")
            pillDosages.add("11.5 $unit")
            pillDosages.add("12 $unit")
            return pillDosages;
        }
    }

}