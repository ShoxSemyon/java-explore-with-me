package com.example.main.utils;

import com.example.main.events.dto.EventShortDto;

import java.util.Comparator;

public class EventViewComparable implements Comparator<EventShortDto> {

    @Override
    public int compare(EventShortDto o1, EventShortDto o2) {
        return Long.compare(o1.getViews(), o2.getViews());
    }
}
