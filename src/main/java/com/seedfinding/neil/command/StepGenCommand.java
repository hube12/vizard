package com.seedfinding.neil.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.neil.GenController;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class StepGenCommand extends ClientCommand{
    @Override
    public String getName() {
        return "step";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(this::step);
    }

    public int step(CommandContext<ServerCommandSource> context){
        GenController.step();
        sendFeedback("Stepped structure gen once", Formatting.GREEN, false);
        return 0;
    }

}
