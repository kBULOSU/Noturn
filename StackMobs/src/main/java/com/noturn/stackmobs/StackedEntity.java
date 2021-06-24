package com.noturn.stackmobs;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class StackedEntity {

    @NonNull
    @Getter
    private final Entity entity;

    public StackedEntity(@NonNull Entity entity) {
        this.entity = entity;
        StackMobsAPI.setAi(entity);
    }

    public boolean hasStackSizeTag() {
        return entity.hasMetadata(StackMobsAPI.STACK_SIZE_TAG);
    }

    public int getSize() {
        if (!hasStackSizeTag()) {
            return 1;
        }

        return entity.getMetadata(StackMobsAPI.STACK_SIZE_TAG).get(0).asInt();
    }

    public void setSize(int newSize) {
        List<MetadataValue> values = entity.getMetadata(StackMobsAPI.STACK_SIZE_TAG);

        for (MetadataValue meta : values) {
            entity.removeMetadata(StackMobsAPI.STACK_SIZE_TAG, meta.getOwningPlugin());
        }

        entity.setMetadata(StackMobsAPI.STACK_SIZE_TAG, new FixedMetadataValue(StackMobsPlugin.INSTANCE, newSize));
    }

    public boolean isStackingPrevented() {
        return entity.hasMetadata(StackMobsAPI.PREVENT_STACK_TAG)
                && entity.getMetadata(StackMobsAPI.PREVENT_STACK_TAG).get(0).asBoolean();
    }
}
