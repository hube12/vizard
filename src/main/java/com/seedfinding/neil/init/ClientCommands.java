package com.seedfinding.neil.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.seedfinding.neil.command.StartGenCommand;
import com.seedfinding.neil.command.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientCommands {

    public static final String PREFIX = "gen";
    public static final List<ClientCommand> COMMANDS = new ArrayList<>();

    public static StartGenCommand START;
    public static StopGenCommand STOP;
    public static StepGenCommand STEP;
    public static TimeoutCommand TIME;

    static {
        COMMANDS.add(START = new StartGenCommand());
        COMMANDS.add(STOP = new StopGenCommand());
        COMMANDS.add(STEP = new StepGenCommand());
        COMMANDS.add(TIME = new TimeoutCommand());
    }

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        COMMANDS.forEach(clientCommand -> clientCommand.register(dispatcher));
    }

    public static boolean isClientSideCommand(String[] args) {
        if(args.length < 2)return false;
        if(!PREFIX.equals(args[0]))return false;

        for(ClientCommand command: COMMANDS) {
            if(command.getName().equals(args[1])) {
                return true;
            }
        }

        return false;
    }

    public static void executeCommand(StringReader reader) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        try {
            assert player != null;
            player.networkHandler.getCommandDispatcher().execute(reader, new FakeCommandSource(player));
        } catch(CommandException e) {
            ClientCommand.sendFeedback("Command exited weirdly", Formatting.RED, false);
            e.printStackTrace();
        } catch(CommandSyntaxException e) {
            ClientCommand.sendFeedback("Syntax was incorrect", Formatting.RED, false);
            e.printStackTrace();
        } catch(Exception e) {
            ClientCommand.sendFeedback("That's not supposed to happen, please report it", Formatting.RED, false);
            e.printStackTrace();
        }

    }

    /**
     * Magic class by Earthcomputer.
     * https://github.com/Earthcomputer/clientcommands/blob/fabric/src/main/java/net/earthcomputer/clientcommands/command/FakeCommandSource.java
     * */
    public static class FakeCommandSource extends ServerCommandSource {
        public FakeCommandSource(ClientPlayerEntity player) {
            super(player, player.getPos(), player.getRotationClient(), null, 0, player.getEntityName(), player.getName(), null, player);
        }

        @Override
        public Collection<String> getPlayerNames() {
            return Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList()
                    .stream().map(e -> e.getProfile().getName()).collect(Collectors.toList());
        }
    }

}
