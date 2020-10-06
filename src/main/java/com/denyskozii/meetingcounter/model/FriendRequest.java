package com.denyskozii.meetingcounter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@Table(name = "friend_request")
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToMany(mappedBy = "invite",
//            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<User> invitor;

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToMany(mappedBy = "accept",
//            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<User> acceptor;
    @NonNull
    private String invitorEmail;

    @NonNull
    private String acceptorEmail;

    @NonNull
    private Boolean status;

}
