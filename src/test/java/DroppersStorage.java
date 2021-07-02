import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DroppersStorage {
    private final List<Block> droppers = new ArrayList<>();

    public void addDropper(Block block) {
        this.droppers.add(block);
    }

    public void removeDropper(Block block) {
        for (Block dropper : droppers)
            if (ifEqualsLocationXYZ(dropper.getLocation(), block.getLocation())) {
                droppers.remove(dropper);
                return;
            }
    }

    public Optional<Block> getDropper(Location location) {
        return droppers.stream()
                .filter(block -> block.getLocation().equals(location))
                .findFirst();
    }

    public boolean ifEqualsLocationXYZ(Location location1, Location location2){
        return location1.getBlockX() == location2.getBlockX() &&
                location1.getBlockY() == location2.getBlockY() &&
                location1.getBlockZ() == location2.getBlockZ();
    }

    public int size() {
        return droppers.size();
    }
}
