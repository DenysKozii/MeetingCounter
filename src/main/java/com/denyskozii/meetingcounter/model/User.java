package com.denyskozii.meetingcounter.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * Date: 07.09.2020
 *
 * @author Denys Kozii
 */
@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank(message = "Must not be empty")
    @Size(min = 2, max = 20,
            message = "Length of first name should be between 2 and 20")
    private String firstName;

    @NonNull
    @NotBlank(message = "Must not be empty")
    @Size(min = 2, max = 20,
            message = "Length of last name should be between 2 and 20")
    private String lastName;

    @NonNull
    @NotBlank(message = "Must not be empty")
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    @Column(unique = true)
    private String email;

    @NonNull
    @NotBlank(message = "Must not be empty")
    @Size(min = 5, message = "Password is not strong enough")
    @Column(name = "password")
    private String password;

    @Transient
    private String confirmPassword;

    @NonNull
    @NotNull
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    private Role role;

    @Transient
    private Date expirationDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "meeting_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "meeting_id"))
    private List<Meeting> meetings ;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Meeting> myMeetings;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName="id"))
    private List<User> friends;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "invite_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName="id"))
    private List<FriendRequest> invite;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "accept_friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName="id"))
    private List<FriendRequest> accept;

}