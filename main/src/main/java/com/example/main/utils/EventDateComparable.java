package com.example.main.utils;

import com.example.main.events.dto.EventShortDto;

import java.util.Comparator;

public class EventDateComparable implements Comparator<EventShortDto> {

    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        return o1.getEventDate().compareTo(o2.getEventDate());
    }
}
