package omae.wa.mou.shindeiru.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import omae.wa.mou.shindeiru.autocraft.AutoCraft;
import omae.wa.mou.shindeiru.database.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutoCraftManager {
    private final List<AutoCraft> droppers = new ArrayList<>();

    private final Plugin plugin;
    private final DataBase dataBase;

    public AutoCraftManager(Plugin plugin, DataBase dataBase) {
        this.plugin = plugin;
        this.dataBase = dataBase;

        try {
            firstInitialize();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addAutoCraft(AutoCraft autoCraft) {
        this.droppers.add(autoCraft);
    }

    public void removeAutoCraft(AutoCraft autoCraft) {
        for (AutoCraft dropper : this.droppers) {
            Location location1 = dropper.getDispenser().getLocation();
            Location location2 = autoCraft.getDispenser().getLocation();

            if (location1.getBlockX() == location2.getX()
                    && location1.getY() == location2.getY()
                    && location1.getZ() == location2.getZ()) {
                droppers.remove(autoCraft);
                return;
            }
        }
    }

    public void removeAutoCraft(Location location2) {
        for (AutoCraft dropper : this.droppers) {
            Location location1 = dropper.getDispenser().getLocation();

            if (location1.getBlockX() == location2.getX()
                    && location1.getY() == location2.getY()
                    && location1.getZ() == location2.getZ()) {
                droppers.remove(dropper);
                return;
            }
        }
    }

    public Optional<AutoCraft> getAutoCraft(Location location2) {
        for (AutoCraft dropper : this.droppers) {
            if (dropper == null) return Optional.empty();
            if (location2 == null) return Optional.empty();

            Location location1 = dropper.getDispenser().getLocation();
            if (location1.getBlockX() == location2.getX()
                    && location1.getY() == location2.getY()
                    && location1.getZ() == location2.getZ()) {
                return Optional.of(dropper);
            }
        }

        return Optional.empty();
    }

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (AutoCraft dropper : this.droppers) {
                dropper.setActive(dropper.getDispenser().getBlock().isBlockIndirectlyPowered());

                if (dropper.isActive()) {
                    dropper.tick();
                }

            }
        }, 0, 20L);
    }

    private void firstInitialize() throws SQLException {
        ResultSet resultSet = dataBase.select("SELECT * FROM autocraft;");

        while (resultSet.next()) {
            String[] coordinate = resultSet.getString(1).split(",");

            int x, y, z;

            try {
                x = Integer.parseInt(coordinate[0]);
                y = Integer.parseInt(coordinate[1]); //
                z = Integer.parseInt(coordinate[2]);
            } catch (NumberFormatException ex) {
                continue;
            }
            World world1 = Bukkit.getWorld(resultSet.getString(2));


            if (world1 == null) continue;
            if (!world1.getChunkAt(new Location(world1, x,x,z)).isLoaded()){
                world1.loadChunk(world1.getChunkAt(new Location(world1, x,x,z)));
            }

            Block block = world1.getBlockAt(x, y, z);

            if (block == null) continue;
            if (!block.getState().getType().equals(Material.DISPENSER)) continue;

            AutoCraft autoCraft = AutoCraft.getInstance(block);

            addAutoCraft(autoCraft);
        }
    }
}
