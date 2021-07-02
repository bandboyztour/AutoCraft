import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getPluginManager;

//@Plugin(name = "shit", version = "1.0")
public class SecondMain extends JavaPlugin implements Listener {

    private List<Recipe> recipes;

    @Override
    public void onEnable() {
        recipes = new ArrayList<>();
        getServer().recipeIterator().forEachRemaining( recipe -> {
            if ((recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe) && !recipes.contains(recipe)){
                recipes.add(recipe);
            }
        });
        getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        if (block.getType() == Material.DROPPER && event.getAction() == Action.LEFT_CLICK_BLOCK){
            Player player = event.getPlayer();
            Dropper dropper = (Dropper) block.getState();

            recipes.forEach(recipe -> {
                if (ifItemStackInArrayEquals(dropper.getInventory().getContents(), getRecipeContents(recipe))){
                    player.sendMessage(recipe.getResult().toString());
                    event.setCancelled(true);
                    return;
                }
            });
            player.sendMessage("кажется дело пахнет керосином");
            event.setCancelled(true);
        }
    }

    public ItemStack[] getRecipeContents(Recipe recipe){
        ItemStack[] itemStacks = new ItemStack[8];
        if (recipe instanceof ShapedRecipe){
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            int i = 0;
            for (Map.Entry<Character, ItemStack> entry : shapedRecipe.getIngredientMap().entrySet()) {
                itemStacks[i] = entry.getValue();
                i++;
            }
        }
        if (recipe instanceof ShapelessRecipe){
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
            int i = 0;
            for (ItemStack is : shapelessRecipe.getIngredientList()) {
                itemStacks[i] = is;
                i++;
            }
        }
        return itemStacks;
    }

    public boolean ifItemStackInArrayEquals(ItemStack[] itemStacks1, ItemStack[] itemStacks2){
        for (int i = 0; i <= 8; i++){
            if (itemStacks1[i].getType() != itemStacks2[i].getType()){
                return false;
            }
        }
        return true;
    }

    public void makeNullItemStackInArrayAir(ItemStack[] itemStacks){
        for (int i = 0; i <= itemStacks.length ; i++){
            if (itemStacks[i] == null){
                itemStacks[i] = new ItemStack(Material.AIR);
            }
        }
    }
}
