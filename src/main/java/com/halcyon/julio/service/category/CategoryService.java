package com.halcyon.julio.service.category;

import com.halcyon.julio.dto.category.NewCategoryDto;
import com.halcyon.julio.model.Category;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.User;
import com.halcyon.julio.repository.ICategoryRepository;
import com.halcyon.julio.service.auth.AuthService;
import com.halcyon.julio.service.channel.ChannelService;
import com.halcyon.julio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final AuthService authService;

    public Category create(NewCategoryDto dto) {
        Channel channel = channelService.findById(dto.getChannelId());
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        Category category = new Category(dto.getTitle());
        category.setChannel(channel);

        return categoryRepository.save(category);
    }

    public String deleteById(Long categoryId) {
        Category category = findById(categoryId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!category.getChannel().getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this category.");
        }

        categoryRepository.delete(category);
        return "Category deleted successfully.";
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with this id not found."));
    }

    public List<Category> findAllByChannelId(Long channelId) {
        Channel channel = channelService.findById(channelId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        return categoryRepository.findCategoriesByChannel(channel);
    }
}