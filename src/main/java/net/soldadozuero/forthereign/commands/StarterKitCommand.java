package net.soldadozuero.forthereign.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class StarterKitCommand {
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public StarterKitCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
         dispatcher.register(Commands.literal("kit").then(Commands.literal("starter")
                 .executes((command) -> {
                     return kitStarter(command.getSource());
                 }))
        );
    }

    private int kitStarter(CommandSourceStack source) {
        ItemStack douradinha = new ItemStack(Items.GOLDEN_SHOVEL);
        ServerPlayer player = (ServerPlayer) source.getEntity();

        assert player != null;
        if(checkCooldown(player.getUUID())) {
            player.level.playSound((player), player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((
                            player.getRandom().nextFloat() - player.getRandom().nextFloat()
                    ) * 0.7F + 1.0F) * 2.0F);
            player.getInventory().add(new ItemStack(Items.COBBLESTONE, 192));
            player.getInventory().add(new ItemStack(Items.DARK_OAK_LOG, 64));
            player.getInventory().add(new ItemStack(Items.COOKED_PORKCHOP, 32));
            player.getInventory().add(new ItemStack(Items.IRON_SWORD));
            player.getInventory().add(new ItemStack(Items.IRON_PICKAXE));
            EnchantmentHelper.setEnchantments(
                    Map.of(
                            Enchantments.BLOCK_EFFICIENCY, 3, Enchantments.UNBREAKING, 5, Enchantments.MENDING, 1
                    ), douradinha);
            player.getInventory().add(douradinha);

            setCooldown(player.getUUID(), 2592000L * 1000);
            player.sendSystemMessage(Component.literal("Você recebeu seu Kit"), true);

        } else {
            player.sendSystemMessage(Component.literal("Você ainda não pode usar este comando"), true);
        }
        return 1;
    }

    private static boolean checkCooldown(UUID playerUUID) {
        if(cooldowns.containsKey(playerUUID)) {
            long cooldownEnd = cooldowns.get(playerUUID);
            return System.currentTimeMillis() >= cooldownEnd;
        }
        return true;
    }

    private static void setCooldown(UUID playerUUID, long milliseconds) {
        long cooldownEnd = System.currentTimeMillis() + milliseconds;
        cooldowns.put(playerUUID, cooldownEnd);
    }
}
