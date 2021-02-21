package com.seedfinding.neil.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.neil.Main;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class StopGenCommand extends ClientCommand{
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(this::start);
    }

    public int start(CommandContext<ServerCommandSource> context){
        int res= Main.genController.stop();
        if (res==0){
            sendFeedback("Stopped structure gen", Formatting.GREEN, false);
        }else if (res==-1){
            sendFeedback("Can not stop structure gen (nothing has started)", Formatting.RED, false);
        }else{
            sendFeedback("Can not stop structure gen (nothing running)", Formatting.DARK_RED, false);
        }
        return 0;
    }

}
