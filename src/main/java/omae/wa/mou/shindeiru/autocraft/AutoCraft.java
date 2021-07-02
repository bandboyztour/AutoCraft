package omae.wa.mou.shindeiru.autocraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AutoCraft {
    private Dispenser dispenser;

    private Recipe activeRecipe;
    private boolean isActive;

    private int lvl;

    private Chest resourcesChest;
    private Chest outputChest;

    private AutoCraft() {
    }

    public static AutoCraft getInstance(Block dispenser) {
        AutoCraft autoCraft = new AutoCraft();
        autoCraft.dispenser = (Dispenser) dispenser.getState();
        autoCraft.lvl = 1;
        return autoCraft;
    }

    public void setActiveRecipe(Recipe activeRecipe) {
        this.activeRecipe = activeRecipe;
    }

    public void tick() {
        detectChests();

        if (isActive) {
            if (activeRecipe == null) return;
            if (resourcesChest == null) return;
            if (outputChest == null) return;

            List<ItemStack> items = null; // массив итемстаков которые нужны рецепту
            if (activeRecipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) activeRecipe;
                items = shapedRecipe.getIngredientMap().values()
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            if (activeRecipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) activeRecipe;
                items = shapelessRecipe.getIngredientList();
            }
            if (canGetResources(resourcesChest.getBlockInventory(), items)) {
                ItemStack[] itemStacks = new ItemStack[items.size()];
                items.toArray(itemStacks);
                resourcesChest.getBlockInventory().removeItem(itemStacks);
                outputChest.getBlockInventory().addItem(activeRecipe.getResult());
                dispenser.dispense();
            }
        }
    }

    public void updateLvl() {
        lvl++;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getLvl() {
        return lvl;
    }

    public boolean canGetResources(Inventory inventory, List<ItemStack> itemStacks) {
        Inventory buffer = Bukkit.createInventory(null, InventoryType.CHEST);

        itemStacks.forEach(buffer::addItem);

        for (ItemStack itemStack : buffer.getStorageContents()) {
            if (itemStack == null) continue;
            if (!inventory.containsAtLeast(itemStack, itemStack.getAmount())) {
                return false;
            }

        }
        return true;
    }

    public boolean openDropper(Player player) {
        if (!isActive) {
            player.openInventory(dispenser.getInventory());
            return true;
        }
        return false;
    }

    public void detectChests() {
        if (dispenser == null) return;

        Block block = dispenser.getBlock();

        BlockFace face = ((org.bukkit.material.Dispenser) block.getState().getData()).getFacing();

        Block block1 = block.getWorld().getBlockAt(
                face.getModX() + block.getLocation().getBlockX(),
                face.getModY() + block.getLocation().getBlockY(),
                face.getModZ() + block.getLocation().getBlockZ());
        if (block1.getType() == Material.CHEST) {
            Chest directionalChest = (Chest) block1.getState();
            face = face.getOppositeFace();
            block1 = block.getWorld().getBlockAt(
                    face.getModX() + block.getLocation().getBlockX(),
                    face.getModY() + block.getLocation().getBlockY(),
                    face.getModZ() + block.getLocation().getBlockZ());
            if (block1.getType() == Material.CHEST) {
                outputChest = directionalChest;
                resourcesChest = (Chest) block1.getState();
                return;
            }
        }
        outputChest = null;
        resourcesChest = null;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Recipe getActiveRecipe() {
        return activeRecipe;
    }

    public Dispenser getDispenser() {
        return dispenser;
    }
}
