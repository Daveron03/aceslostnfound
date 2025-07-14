package com.campus.lostfound.repository;

import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Item entity
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    /**
     * Find all items ordered by creation date (newest first)
     */
    List<Item> findAllByOrderByCreatedAtDesc();

    /**
     * Find items by status ordered by creation date (newest first)
     */
    List<Item> findByStatusOrderByCreatedAtDesc(Status status);

    /**
     * Find lost items (convenience method)
     */
    @Query("SELECT i FROM Item i WHERE i.status = 'LOST' ORDER BY i.createdAt DESC")
    List<Item> findLostItems();

    /**
     * Find found items (convenience method)
     */
    @Query("SELECT i FROM Item i WHERE i.status = 'FOUND' ORDER BY i.createdAt DESC")
    List<Item> findFoundItems();

    /**
     * Count items by status
     */
    long countByStatus(Status status);
}
