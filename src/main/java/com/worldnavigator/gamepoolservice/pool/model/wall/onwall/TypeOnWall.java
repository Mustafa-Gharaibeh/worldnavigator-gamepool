package com.worldnavigator.gamepoolservice.pool.model.wall.onwall;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TypeOnWall {
    DOOR("DOOR", "door", Boolean.FALSE, Boolean.TRUE),
    SELLER("Seller", "seller", Boolean.FALSE, Boolean.FALSE),
    PAINTING("Painting", "painting", Boolean.TRUE, Boolean.FALSE),
    MIRROR("You See a silhouette of you", "mirror", Boolean.TRUE, Boolean.FALSE),
    CHEST("Chest", "chest", Boolean.TRUE, Boolean.TRUE),
    NOTHING("Wall", "nothing", Boolean.FALSE, Boolean.FALSE);

    @JsonProperty("lookDescription")
    private final String lookDescription;

    @JsonProperty("checkName")
    private final String checkName;

    @JsonProperty("Acquirable")
    private final Boolean isAcquirable;

    @JsonProperty("Lockable")
    private final Boolean isLockable;

    TypeOnWall(
            @JsonProperty("lookDescription") String lookDescription,
            @JsonProperty("checkName") String checkName,
            @JsonProperty("Acquirable") Boolean isAcquirable,
            @JsonProperty("Lockable") Boolean isLockable) {
        this.lookDescription = lookDescription;
        this.checkName = checkName;
        this.isAcquirable = isAcquirable;
        this.isLockable = isLockable;
    }

    public String getLookDescription() {
        return lookDescription;
    }

    public String getCheckName() {
        return checkName;
    }

    public Boolean isAcquirable() {
        return isAcquirable;
    }

    public Boolean isLockable() {
        return isLockable;
    }
}
