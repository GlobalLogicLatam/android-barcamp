package com.globallogic.barcamp.domain;

/**
 * Created by Gonzalo.Martin on 10/28/2016
 */
public class Room {

    private String name;

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        return name != null ? name.equals(room.name) : room.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
