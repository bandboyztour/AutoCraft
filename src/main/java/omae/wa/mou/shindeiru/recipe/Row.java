package omae.wa.mou.shindeiru.recipe;

import org.bukkit.inventory.ItemStack;

public class Row {
    private final ItemStack[][] rows;

    public Row() {
        rows = new ItemStack[3][3];
    }

    public void setItem(ItemStack item, int x, int y) {
        this.rows[x][y] = item;
    }

    public ItemStack getItem(int x, int y){
        return rows[x][y];
    }

    public ItemStack[] getItems() {
        ItemStack[] result = new ItemStack[9];

        int index = 0;

        for (ItemStack[] row : rows) {
            for (ItemStack itemStack : row) {
                result[index] = itemStack;
                index++;
            }
        }

        return result;
    }
}
