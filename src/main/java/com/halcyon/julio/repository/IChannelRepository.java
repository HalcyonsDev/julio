package com.halcyon.julio.repository;

import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findChannelsByMembersIn(Collection<List<User>> members);
    List<Channel> findChannelsByMembersContaining(User member);
    List<Channel> findChannelsByOwner(User owner);
}