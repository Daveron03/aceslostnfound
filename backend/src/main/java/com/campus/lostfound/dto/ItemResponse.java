package com.campus.lostfound.dto;

import com.campus.lostfound.model.Status;

import java.time.Instant;
import java.util.UUID;

/**
 * Data Transfer Object for item responses
 */
public class ItemResponse {

    private UUID id;
    private String title;
    private String description;
    private String photoUrl;
    private Status status;
    private Instant createdAt;

    // Default constructor
    public ItemResponse() {
    }

    // Constructor
    public ItemResponse(UUID id, String title, String description, String photoUrl, Status status, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
