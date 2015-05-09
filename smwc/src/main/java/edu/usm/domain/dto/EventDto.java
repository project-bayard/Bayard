package edu.usm.domain.dto;

import edu.usm.domain.Event;

import java.io.Serializable;

public class EventDto implements Serializable {

    public Event convertToEvent() {
        return new Event();
    }
}
