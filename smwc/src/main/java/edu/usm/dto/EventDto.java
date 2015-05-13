package edu.usm.dto;

import edu.usm.domain.Event;

import java.io.Serializable;

public class EventDto extends BasicEntityDto implements Serializable {

    public Event convertToEvent() {
        return new Event();
    }
}
