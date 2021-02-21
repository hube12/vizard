package com.seedfinding.neil.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.seedfinding.neil.Instance;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class TimeoutCommand extends ClientCommand {
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        builder.executes(this::getTime).then(
                CommandManager.argument("n", integer(0, Integer.MAX_VALUE)).
                        executes(ctx -> this.setTime(getInteger(ctx, "n")))
        );
    }

    public int setTime(int n) {
        int res = Instance.genController.stop();
        Instance.genController.setTime(n);
        if (res == 0) {
            Instance.genController.start();
        }
        ClientCommand.sendFeedback("Time between pieces has been set to : " + Instance.genController.getTime(), Formatting.DARK_GREEN, false);
        return 0;
    }

    public int getTime(CommandContext<ServerCommandSource> context) {
        ClientCommand.sendFeedback("Time between pieces is : " + Instance.genController.getTime(), Formatting.DARK_GREEN, false);
        return 0;
    }

}
