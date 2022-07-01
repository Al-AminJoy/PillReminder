package com.alamin.pillreminder.utils

class DataUtils {
    companion object{
        fun pillUnit (): List<String> {
            var pillTypes = ArrayList<String>()
            pillTypes.add("piece");
            pillTypes.add("drop")
            pillTypes.add("cup")
            pillTypes.add("mg")
            pillTypes.add("ml")
            return pillTypes;
        }

        fun pillTypes (): List<String> {
            var pillTypes = ArrayList<String>()
            pillTypes.add("Tablet");
            pillTypes.add("Capsule")
            pillTypes.add("Syrup")
            pillTypes.add("Injection")
            pillTypes.add("Eye Drops")
            return pillTypes;
        }

    }

}