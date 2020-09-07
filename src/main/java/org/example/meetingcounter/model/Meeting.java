package org.example.meetingcounter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
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
    @NotBlank(message = "Must not be blank")
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "hereAmount")
    private Long hereAmount;

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
