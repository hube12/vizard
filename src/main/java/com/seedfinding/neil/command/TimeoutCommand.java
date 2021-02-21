package com.seedfinding.neil.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.neil.GenController;
import com.seedfinding.neil.Main;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class SetTimeoutCommand extends ClientCommand{
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.then(argument("n",integer(200,Integer.MAX_VALUE))).
                executes(ctx->start(ctx,getInteger(ctx,"n")));
    }

    public int start(CommandContext<ServerCommandSource> context,int n){
        Main.genController.stop();
        Main.genController.setTime(n);
        Main.genController.start();
        return 0;
    }

}
