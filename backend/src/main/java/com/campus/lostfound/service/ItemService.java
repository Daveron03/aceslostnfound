package com.campus.lostfound.service;

import com.campus.lostfound.dto.CreateItemRequest;
import com.campus.lostfound.dto.ItemResponse;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Status;
import com.campus.lostfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer for Item operations
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Get all items ordered by creation date (newest first)
     */
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get items by status
     */
    public List<ItemResponse> getItemsByStatus(Status status) {
        return itemRepository.findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get lost items
     */
    public List<ItemResponse> getLostItems() {
        return getItemsByStatus(Status.LOST);
    }

    /**
     * Get found items
     */
    public List<ItemResponse> getFoundItems() {
        return getItemsByStatus(Status.FOUND);
    }

    /**
     * Create a new item
     */
    public ItemResponse createItem(CreateItemRequest request) {
        Item item = new Item();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setPhotoUrl(request.getPhotoUrl());
        item.setStatus(Status.LOST); // New items are always lost initially

        Item savedItem = itemRepository.save(item);
        return convertToResponse(savedItem);
    }

    /**
     * Mark an item as found
     */
    public Optional<ItemResponse> markItemAsFound(UUID itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            item.setStatus(Status.FOUND);
            Item savedItem = itemRepository.save(item);
            return Optional.of(convertToResponse(savedItem));
        }

        return Optional.empty();
    }

    /**
     * Get item by ID
     */
    public Optional<ItemResponse> getItemById(UUID itemId) {
        return itemRepository.findById(itemId)
                .map(this::convertToResponse);
    }

    /**
     * Delete an item
     */
    public boolean deleteItem(UUID itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    /**
     * Get count of items by status
     */
    public long getItemCount(Status status) {
        return itemRepository.countByStatus(status);
    }

    /**
     * Convert Item entity to ItemResponse DTO
     */
    private ItemResponse convertToResponse(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getPhotoUrl(),
                item.getStatus(),
                item.getCreatedAt());
    }
}
