package net.soldadozuero.forthereign.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;


public class StarterKitCommand {
    public StarterKitCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
         dispatcher.register(Commands.literal("kit").then(Commands.literal("starter")
                 .executes((command) -> {
                     return kitStarter(command.getSource());
                 }))
        );
    }

    private int kitStarter(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = (ServerPlayer) source.getEntity();
        Objects.requireNonNull(player).level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP,
                SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat())
                        * 0.7F + 1.0F) * 2.0F);
        player.getInventory().add(new ItemStack(Items.DIAMOND_SWORD));

        return 1;
    }
}
