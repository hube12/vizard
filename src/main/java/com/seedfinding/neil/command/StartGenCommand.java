package com.seedfinding.neil.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.neil.Main;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class StartGenCommand extends ClientCommand{
    @Override
    public String getName() {
        return "start";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(this::start);
    }

    public int start(CommandContext<ServerCommandSource> context){
        boolean res= Main.genController.start();
        if (res){
            sendFeedback("Started structure gen", Formatting.GREEN, false);
        }else{
            sendFeedback("Can not start structure gen (already running)", Formatting.GREEN, false);
        }
        return 0;
    }

}
