package net.soldadozuero.forthereign.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class VipKitCommand {
    private static final Map<UUID, Long> cooldowns = new HashMap<>();

    public VipKitCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
         dispatcher.register(Commands.literal("kit").then(Commands.literal("vip")
                 .executes((command) -> {
                     return kitStarter(command.getSource());
                 }))
        );
    }

    private int kitStarter(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = (ServerPlayer) source.getEntity();

        assert player != null;
        if(checkCooldown(player.getUUID())) {
            Objects.requireNonNull(player).level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((
                            player.getRandom().nextFloat() - player.getRandom().nextFloat()
                    ) * 0.7F + 1.0F) * 2.0F);
            player.getInventory().add(new ItemStack(Items.ENCHANTING_TABLE, 1));
            player.getInventory().add(new ItemStack(Items.TOTEM_OF_UNDYING, 2));
            player.getInventory().add(new ItemStack(Items.ENDER_CHEST, 2));
            player.getInventory().add(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("alexsmobs", "centipede_leggings")), 1));
            player.getInventory().add(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("waystones", "mossy_waystone")), 2));

            setCooldown(player.getUUID(), 2592000L * 1000);
            player.sendSystemMessage(Component.literal("Você recebeu seu Kit"), true);

        } else {
            UUID playerUUID = player.getUUID();
            long cooldownEnd = cooldowns.get(playerUUID);
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
