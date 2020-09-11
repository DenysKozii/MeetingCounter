package com.denyskozii.meetingcounter.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @UniqueElements
    @NotBlank(message = "Must not be blank")
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "hereAmount")
    private Long hereAmount;

    @NonNull
    @Column(name = "longitude")
    private Double longitude;

    @NonNull
    @Column(name = "latitude")
    private Double latitude;

    @NonNull
    @Column(name = "availableDistance")
    private Double availableDistance;

    @Column(name = "start_date", columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "finish_date", columnDefinition = "DATE")
    private LocalDate finishDate;


    @ToString.Exclude
    @ManyToMany(mappedBy = "meetings",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meeting)) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(getTitle(), meeting.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }
}