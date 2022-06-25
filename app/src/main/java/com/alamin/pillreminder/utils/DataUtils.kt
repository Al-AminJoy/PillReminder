package com.alamin.pillreminder.utils

class DataUtils {
    companion object{
        fun pillTypes (): List<String> {
            var pillTypes = ArrayList<String>()
            pillTypes.add("Tablet");
            pillTypes.add("Capsule")
            pillTypes.add("Syrup")
            pillTypes.add("Injection")
            pillTypes.add("Eye Drops")
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

        fun pillDosages(): List<String> {
            var pillDosages = ArrayList<String>()
            pillDosages.add("0.5");
            pillDosages.add("1")
            pillDosages.add("1.5")
            pillDosages.add("2")
            pillDosages.add("2.5")
            pillDosages.add("3")
            pillDosages.add("3.5")
            pillDosages.add("4")
            pillDosages.add("4.5")
            pillDosages.add("5")
            pillDosages.add("5.5")
            pillDosages.add("6")
            pillDosages.add("6.5")
            pillDosages.add("7")
            pillDosages.add("7.5")
            pillDosages.add("8")
            pillDosages.add("8.5")
            pillDosages.add("9")
            pillDosages.add("9.5")
            pillDosages.add("10")
            pillDosages.add("10.5")
            pillDosages.add("11")
            pillDosages.add("11.5")
            pillDosages.add("12")
            return pillDosages;
        }
    }

}