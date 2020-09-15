//package com.denyskozii.meetingcounter.model;
//
//import lombok.Data;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import lombok.ToString;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
//import java.util.*;
//
//@Entity
//@Data
//@Table(name = "userGoogle")
//@RequiredArgsConstructor
//public class UserGoogle  {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NonNull
//    @NotBlank(message = "Must not be empty")
//    @Size(min = 2, max = 20,
//            message = "Length of first name should be between 2 and 20")
//    private String firstName;
//
//    @NonNull
//    @NotBlank(message = "Must not be empty")
//    @Size(min = 2, max = 20,
//            message = "Length of last name should be between 2 and 20")
//    private String lastName;
//
//    @NonNull
//    @NotBlank(message = "Must not be empty")
//    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
//    @Column(unique = true)
//    private String email;
//
//    @NonNull
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @ToString.Exclude
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "meeting_user",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "meeting_id"))
//    private List<Meeting> meetings = new ArrayList<>();
//
//    public UserGoogle() {}
//
//    @Override
//    public boolean equals(Object o) {
//
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserGoogle user = (UserGoogle) o;
//        return Objects.equals(email, user.email) &&
//                Objects.equals(firstName, user.firstName) &&
//                Objects.equals(lastName, user.lastName) &&
//                role == user.role;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(email, firstName, lastName, role);
//    }
//}