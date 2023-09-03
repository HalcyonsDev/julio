package com.halcyon.julio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "channels")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    @NonNull
    private String title;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonManagedReference
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "channels_users",
            inverseJoinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    private List<User> members;

    @OneToMany(mappedBy = "channel")
    @JsonBackReference
    private List<Chat> chats;

    @OneToMany(mappedBy = "channel")
    @JsonBackReference
    private List<Category> categories;

    @OneToMany(mappedBy = "channel")
    @JsonBackReference
    private List<InviteCode> inviteCodes;
}