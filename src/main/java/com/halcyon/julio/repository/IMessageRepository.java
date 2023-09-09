package com.halcyon.julio.repository;

import com.halcyon.julio.model.Chat;
import com.halcyon.julio.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findMessagesByChat(Chat chat, Pageable pageable);
}