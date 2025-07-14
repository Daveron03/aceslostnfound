package com.campus.lostfound.controller;

import com.campus.lostfound.dto.CreateItemRequest;
import com.campus.lostfound.dto.ItemResponse;
import com.campus.lostfound.model.Status;
import com.campus.lostfound.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller for Item operations
 */
@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * GET /api/items - Get all items
     */
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * GET /api/items/lost - Get lost items
     */
    @GetMapping("/lost")
    public ResponseEntity<List<ItemResponse>> getLostItems() {
        List<ItemResponse> items = itemService.getLostItems();
        return ResponseEntity.ok(items);
    }

    /**
     * GET /api/items/found - Get found items
     */
    @GetMapping("/found")
    public ResponseEntity<List<ItemResponse>> getFoundItems() {
        List<ItemResponse> items = itemService.getFoundItems();
        return ResponseEntity.ok(items);
    }

    /**
     * GET /api/items/{id} - Get item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable UUID id) {
        Optional<ItemResponse> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/items - Create a new item
     */
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest request) {
        ItemResponse createdItem = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    /**
     * PUT /api/items/{id}/found - Mark item as found
     */
    @PutMapping("/{id}/found")
    public ResponseEntity<ItemResponse> markItemAsFound(@PathVariable UUID id) {
        Optional<ItemResponse> updatedItem = itemService.markItemAsFound(id);
        return updatedItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/items/{id} - Delete an item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id) {
        boolean deleted = itemService.deleteItem(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/items/stats - Get item statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long lostCount = itemService.getItemCount(Status.LOST);
        long foundCount = itemService.getItemCount(Status.FOUND);

        Map<String, Object> stats = Map.of(
                "lostCount", lostCount,
                "foundCount", foundCount,
                "totalCount", lostCount + foundCount);

        return ResponseEntity.ok(stats);
    }
}
