import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import omae.wa.mou.shindeiru.recipe.RecipeFinder;

public class Main2 extends JavaPlugin implements Listener {

    private final String title = "AUTO_CRAFT";

    private final RecipeFinder recipeFinder = new RecipeFinder();

    private final DroppersStorage droppersStorage = new DroppersStorage();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getTitle().equalsIgnoreCase(title)) {
            ItemStack[] items = inventory.getContents();

            System.out.println(inventory.getLocation());

            droppersStorage.getDropper(inventory.getLocation())
                    .ifPresent(block -> {
                        System.out.println("Find dropper");
                    });

            recipeFinder.findRecipe(items)
                    .ifPresent(recipe -> event.getPlayer().sendMessage(recipe.getResult().toString()));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        System.out.println(event.getItemInHand().getItemMeta().getDisplayName());

        droppersStorage.addDropper(block);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType().equals(Material.DROPPER)) {
            droppersStorage.removeDropper(block);
        }

        System.out.println(droppersStorage.size());
    }
}
