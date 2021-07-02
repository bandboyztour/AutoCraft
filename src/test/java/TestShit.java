import org.bukkit.inventory.ItemStack;

public class TestShit {


    public static void main(String[] args) {
        // DS
    }

    private void leftShift(ItemStack[] itemStacks) {
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

    public void tryMaxShift(ItemStack[] itemStacks) {
        while (hasTopCellsNull(itemStacks)) {
            upShift(itemStacks);
        }
        while (hasLeftCellsNull(itemStacks)) {
            leftShift(itemStacks);
        }
    }

    private boolean hasLeftCellsNull(ItemStack[] itemStacks) {
        return (itemStacks[0] == null && itemStacks[3] == null && itemStacks[6] == null);
    }

    private boolean hasTopCellsNull(ItemStack[] itemStacks) {
        return (itemStacks[0] == null && itemStacks[1] == null && itemStacks[2] == null);
    }

    private void upShift(ItemStack[] itemStacks) {
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
