package com.theawesomeengineer.taskmanager.mapper;

import com.theawesomeengineer.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Mapper to convert between JPA Entity (Task) and OpenAPI generated models.
 * This keeps our database layer separate from our API layer.
 */
@Component
public class TaskMapper {

    /**
     * Convert JPA Task entity to OpenAPI Task model
     */
    public com.theawesomeengineer.taskmanager.model.Task toModel(Task entity) {
        if (entity == null) {
            return null;
        }

        com.theawesomeengineer.taskmanager.model.Task model =
            new com.theawesomeengineer.taskmanager.model.Task();

        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setCompleted(entity.getCompleted());
        model.setCreatedAt(toOffsetDateTime(entity.getCreatedAt()));
        model.setUpdatedAt(toOffsetDateTime(entity.getUpdatedAt()));

        return model;
    }

    /**
     * Convert LocalDateTime to OffsetDateTime for API response
     */
    private OffsetDateTime toOffsetDateTime(java.time.LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}
