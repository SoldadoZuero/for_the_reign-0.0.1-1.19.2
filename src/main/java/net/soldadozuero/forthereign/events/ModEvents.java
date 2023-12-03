package net.soldadozuero.forthereign.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import net.soldadozuero.forthereign.ForTheReign;
import net.soldadozuero.forthereign.commands.StarterKitCommand;
import net.soldadozuero.forthereign.commands.VipKitCommand;
import net.soldadozuero.forthereign.commands.VipPlusKitCommand;


@Mod.EventBusSubscriber(modid = ForTheReign.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new StarterKitCommand(event.getDispatcher());
        new VipKitCommand(event.getDispatcher());
        new VipPlusKitCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
