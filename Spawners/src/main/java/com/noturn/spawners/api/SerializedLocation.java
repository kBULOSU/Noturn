package com.noturn.spawners.api;

import lombok.*;
import org.bukkit.Location;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SerializedLocation implements Cloneable {

    @NonNull
    private String worldName = "world";

    private double x;

    private double y;

    private double z;

    private float yaw;

    private float pitch;

    public SerializedLocation(double x, double y, double z) {
        this("world", x, y, z, 0, 0);
    }

    public SerializedLocation(double x, double y, double z, float yaw, float pitch) {
        this("world", x, y, z, yaw, pitch);
    }

    public SerializedLocation(String worldName, double x, double y, double z) {
        this(worldName, x, y, z, 0, 0);
    }

    public static SerializedLocation of(Location location) {
        return new SerializedLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ()
        );
    }

    public int getBlockX() {
        return floorInt(x);
    }

    public int getBlockY() {
        return floorInt(y);
    }

    public int getBlockZ() {
        return floorInt(z);
    }

    /*

     */

    private int floorInt(double num) {
        int floor = (int) num;
        return (double) floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }
}
