package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.location.model.Location;
import ru.practicum.event.status.EventStatus;
import ru.practicum.users.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "annotation")
    @Lob
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description")
    @Lob
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "state_event")
    @Enumerated(EnumType.STRING)
    private EventStatus state;
    @Column(name = "title")
    private String title;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Transient
    private Long view;
    @Transient
    private Long confirmedRequests;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", annotation='" + annotation + '\'' +
                ", category=" + category +
                ", createdOn=" + createdOn +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", initiator=" + initiator +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", publishedOn=" + publishedOn +
                ", view=" + view +
                ", confirmedRequests=" + confirmedRequests +
                '}';
    }
}