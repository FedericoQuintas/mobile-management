package com.challenge.mobilemanagement.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@EqualsAndHashCode
@Table("phone_events")
public class PhoneEventPersistentModel implements Persistable {

    @Id
    private String model;
    private Integer version;
    private String username;
    private String event_type;
    private Instant event_time;

    public PhoneEventPersistentModel(String model, String username, String event_type, Integer version, Instant event_time) {
        this.username = username;
        this.event_type = event_type;
        this.event_time = event_time;
        this.model = model;
        this.version = version;
    }

    @Override
    public Object getId() {
        return model;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
