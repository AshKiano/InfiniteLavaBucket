package com.ashkiano.infinitelavabucket;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.ArrayList;
import java.util.List;

public class InfiniteLavaBucket extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getCommand("infinitelava").setExecutor(new BucketCommand());
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        Metrics metrics = new Metrics(this, 19490);
        this.getLogger().info("Thank you for using the InfiniteLavaBucket plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://support.ashkiano.com");
    }

    public class BucketCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                ItemStack bucket = new ItemStack(Material.LAVA_BUCKET);
                ItemMeta meta = bucket.getItemMeta();

                List<String> lore = new ArrayList<>();
                lore.add("Infinite Lava");

                if (meta != null) {
                    meta.setLore(lore);
                    bucket.setItemMeta(meta);
                }

                player.getInventory().addItem(bucket);
                player.sendMessage("You've received an infinite lava bucket!");
            }

            return true;
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.hasItemMeta() && handItem.getItemMeta().hasLore()) {
            List<String> lore = handItem.getItemMeta().getLore();
            if (lore != null && lore.contains("Infinite Lava")) {

                event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.LAVA);

                Bukkit.getScheduler().runTaskLater(this, () -> {
                    ItemStack infiniteBucket = new ItemStack(Material.LAVA_BUCKET);
                    ItemMeta meta = infiniteBucket.getItemMeta();

                    if (meta != null) {
                        List<String> newLore = new ArrayList<>();
                        newLore.add("Infinite Lava");
                        meta.setLore(newLore);
                        infiniteBucket.setItemMeta(meta);
                    }

                    player.getInventory().setItemInMainHand(infiniteBucket);
                }, 1L);
            }
        }
    }
}
