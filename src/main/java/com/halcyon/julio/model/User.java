package com.halcyon.julio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Token> tokens;

    @ManyToMany(mappedBy = "members")
    @JsonBackReference
    private List<Channel> channels;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    private List<Channel> ownedChannels;

    @OneToMany(mappedBy = "sender")
    @JsonBackReference
    private List<Message> messages;

    @OneToMany(mappedBy = "creator")
    @JsonBackReference
    private List<InviteCode> inviteCodes;
}