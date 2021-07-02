package omae.wa.mou.shindeiru.recipe;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

public class RecipeFinder {

    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeFinder() {
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe)
                recipes.add(recipe);
        });
    }

    public Optional<Recipe> findRecipe(ItemStack[] items) {
        tryMaxShift(items);

        for (Recipe recipe : recipes) {
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;

                Row row = getRowForShaped(shapedRecipe);

                if (Arrays.equals(row.getItems(), items)) {
                    return Optional.of(shapedRecipe);
                }
            }
        }
        return Optional.empty();
    }

    private Row getRowForShaped(ShapedRecipe shapedRecipe) {
        Row row = new Row();

        Map<Character, ItemStack> ingredients = shapedRecipe.getIngredientMap();

        String[] grid = shapedRecipe.getShape();

        int index = 0;

        for (String s : grid) {
            char[] chars = s.toCharArray();
            for (int charIndex = 0; charIndex < chars.length; charIndex++) {
                row.setItem(ingredients.get(chars[charIndex]), index, charIndex);
            }
            index++;
        }
        return row;
    }

    private void leftShift(ItemStack[] itemStacks) {
        if (itemStacks[0] == null && itemStacks[3] == null && itemStacks[6] == null) {
            itemStacks[0] = itemStacks[1];
            itemStacks[3] = itemStacks[4];
            itemStacks[6] = itemStacks[7];
            itemStacks[1] = itemStacks[2];
            itemStacks[4] = itemStacks[5];
            itemStacks[7] = itemStacks[8];
            itemStacks[2] = null;
            itemStacks[5] = null;
            itemStacks[8] = null;
        }
    }

    public void tryMaxShift(ItemStack[] itemStacks) {
        for (int i = 0; i <= 2; i++) {
            upShift(itemStacks);
            leftShift(itemStacks);
        }
    }

    private void upShift(ItemStack[] itemStacks) {
        if (itemStacks[0] == null && itemStacks[1] == null && itemStacks[2] == null) {
            itemStacks[0] = itemStacks[3];
            itemStacks[1] = itemStacks[4];
            itemStacks[2] = itemStacks[5];
            itemStacks[3] = itemStacks[6];
            itemStacks[4] = itemStacks[7];
            itemStacks[5] = itemStacks[8];
            itemStacks[6] = null;
            itemStacks[7] = null;
            itemStacks[8] = null;
        }
    }
}