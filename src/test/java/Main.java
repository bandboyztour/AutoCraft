//
//public class Main extends JavaPlugin implements Listener {
//
//    private final Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, "AutoCraft");
//    private List<Recipe> recipes;
//
//    @Override
//    public void onEnable() {
//        Bukkit.getPluginManager().registerEvents(this, this);
//        recipes = new ArrayList<>();
//        final Iterator<Recipe> iterator = getServer().recipeIterator();
//        iterator.forEachRemaining(recipe -> {
//            if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
//                recipes.add(recipe);
//            }
//        });
//    }
//
//    @EventHandler
//    public void fuckOff(AsyncPlayerChatEvent event) {
//        Player player = event.getPlayer();
//        player.openInventory(inventory);
//    }
//
//    @EventHandler
//    public void onInventoryClose(InventoryCloseEvent event) {
//        Inventory inventory = event.getInventory();
//        event.getPlayer().sendMessage(findRecipe(inventory.getContents()).toString());
//    }
//
//    private ItemStack findRecipe(ItemStack[] items) {
//        List<ItemStack> shitThatWeHave = new ArrayList<>();
//        for (ItemStack itemStack : items) {
//            if (itemStack != null) {
//                itemStack.setAmount(1);
//                shitThatWeHave.add(itemStack);
//            }
//        }
//        for (Recipe recipe : recipes) {
//            List<ItemStack> ingridients = new ArrayList<>();
//            if (recipe instanceof ShapedRecipe) {
//                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
//                for (Map.Entry<Character, ItemStack> entry : shapedRecipe.getIngredientMap().entrySet()) {
//                    ItemStack itemStack = entry.getValue();
//                    if (itemStack != null) {
//                        itemStack.setAmount(1);
//                        ingridients.add(itemStack);
//                    }
//                }
//            }
//            if (recipe instanceof ShapelessRecipe) {
//                ShapelessRecipe shapedRecipe = (ShapelessRecipe) recipe;
//                for (ItemStack itemStack : shapedRecipe.getIngredientList()) {
//                    if (itemStack != null) {
//                        itemStack.setAmount(1);
//                        ingridients.add(itemStack);
//                    }
//                }
//            }
//
//
//            if (ingridients.size() == shitThatWeHave.size() &&
//                    ingridients.containsAll(shitThatWeHave)) {
//                return recipe.getResult();
//            }
//        }
//        return new ItemStack(Material.AIR);
//    }
//
//}