package omae.wa.mou.shindeiru.listener;

import omae.wa.mou.shindeiru.manager.AutoCraftManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import omae.wa.mou.shindeiru.autocraft.AutoCraft;
import omae.wa.mou.shindeiru.database.DataBase;
import omae.wa.mou.shindeiru.recipe.RecipeFinder;
import omae.wa.mou.shindeiru.storage.MessageStorage;
import omae.wa.mou.shindeiru.storage.YMLStorage;

import java.util.Optional;

public class AutoCraftListener implements Listener {
    private final String title;

    private final RecipeFinder recipeFinder = new RecipeFinder();

    private final AutoCraftManager autoCraftManager;
    private final MessageStorage messageStorage;
    private final DataBase dataBase;

    public AutoCraftListener(AutoCraftManager autoCraftManager, YMLStorage ymlStorage, MessageStorage messageStorage, DataBase dataBase) {
        this.autoCraftManager = autoCraftManager;
        this.title = ymlStorage.getConfig("config").getString("settings.title");
        this.messageStorage = messageStorage;
        this.dataBase = dataBase;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null && itemMeta.getDisplayName() != null) {
            if (itemMeta.getDisplayName().equalsIgnoreCase(title)) {
                addAutoCraft(block);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType().equals(Material.DISPENSER)) {
            if (autoCraftManager.getAutoCraft(block.getLocation()).isPresent()) {
                removeAutoCraft(block);
            }
        }
    }

    @EventHandler
    public void onInteractWithBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {
            if (block.getType().equals(Material.DISPENSER)) {
                autoCraftManager.getAutoCraft(block.getLocation())
                        .ifPresent(autoCraft -> {
                            event.setCancelled(true);
                            boolean opened = autoCraft.openDropper(player);
                            if (!opened)
                                player.sendMessage(messageStorage.getMessage("access-denied"));
                        });
            }
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();

        autoCraftManager.getAutoCraft(block.getLocation())
                .ifPresent(autoCraft -> event.setCancelled(true));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        autoCraftManager.getAutoCraft(inventory.getLocation())
                .ifPresent(autoCraft -> {
                    Optional<Recipe> recipe = recipeFinder.findRecipe(inventory.getContents());
                    if (recipe.isPresent()) {
                        autoCraft.setActiveRecipe(recipe.get());
                    } else {
                        autoCraft.setActiveRecipe(null);
                    }
                });
    }

    private void addAutoCraft(Block block) {
        AutoCraft autoCraft = AutoCraft.getInstance(block);
        autoCraftManager.addAutoCraft(autoCraft);

        Location location = block.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        dataBase.insert("INSERT INTO autocraft(location, world) VALUES(" +
                "\"" + x + "," + y + "," + z + "\"," +
                "\"" + location.getWorld().getName() + "\"" +
                ");");
    }

    private void removeAutoCraft(Block block) {
        Location location = block.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        dataBase.update("DELETE FROM autocraft WHERE location=\"" + x + "," + y + "," + z + "\";");

        autoCraftManager.removeAutoCraft(location);
    }
}