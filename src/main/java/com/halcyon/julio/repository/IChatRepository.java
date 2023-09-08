package com.halcyon.julio.repository;

import com.halcyon.julio.model.Category;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Chat chat SET chat.category = ?1 WHERE chat.id = ?2")
    void updateCategoryById(Category category, Long chatId);

    @Transactional
    @Modifying
    @Query("UPDATE Chat chat SET chat.title = ?1 WHERE chat.id = ?2")
    void updateTitleById(String title, Long chatId);

    List<Chat> findChatsByChannel(Channel channel);
    Optional<Chat> findByTitleAndChannel(String title, Channel channel);
}