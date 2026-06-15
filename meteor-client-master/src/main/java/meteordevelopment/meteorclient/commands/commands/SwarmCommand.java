/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.commands.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.ModuleArgumentType;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import meteordevelopment.meteorclient.pathing.PathManagers;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
// import meteordevelopment.meteorclient.systems.modules.misc.swarm.Swarm; // AUTO-REMOVED
// import meteordevelopment.meteorclient.systems.modules.misc.swarm.SwarmConnection; // AUTO-REMOVED
// import meteordevelopment.meteorclient.systems.modules.misc.swarm.SwarmWorker; // AUTO-REMOVED
// import meteordevelopment.meteorclient.systems.modules.world.InfinityMiner; // AUTO-REMOVED
import meteordevelopment.meteorclient.utils.misc.text.MeteorClickEvent;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class SwarmCommand extends Command {

//     private final static SimpleCommandExceptionType SWARM_NOT_ACTIVE = new SimpleCommandExceptionType(Component.literal("The swarm module must be active to use this command.")); // AUTO-REMOVED
    private @Nullable ObjectIntPair<String> pendingConnection;

    public SwarmCommand() {
//         super("swarm", "Sends commands to connected swarm workers."); // AUTO-REMOVED
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(literal("disconnect").executes(_ -> {
//             Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//             if (swarm.isActive()) { // AUTO-REMOVED
//                 swarm.close(); // AUTO-REMOVED
            } else {
                throw SWARM_NOT_ACTIVE.create();
            }

            return SINGLE_SUCCESS;
        }));

        builder.then(literal("join")
            .then(argument("ip", StringArgumentType.string())
                .then(argument("port", IntegerArgumentType.integer(0, 65535))
                    .executes(context -> {
                        String ip = StringArgumentType.getString(context, "ip");
                        int port = IntegerArgumentType.getInteger(context, "port");

                        pendingConnection = new ObjectIntImmutablePair<>(ip, port);

                        info("Are you sure you want to connect to '%s:%s'?", ip, port);
                        info(Component.literal("Click here to confirm").setStyle(Style.EMPTY
                            .applyFormats(ChatFormatting.UNDERLINE, ChatFormatting.GREEN)
//                             .withClickEvent(new MeteorClickEvent(".swarm join confirm")) // AUTO-REMOVED
                        ));

                        return SINGLE_SUCCESS;
                    })
                )
            )
            .then(literal("confirm").executes(_ -> {
                if (pendingConnection == null) {
//                     error("No pending swarm connections."); // AUTO-REMOVED
                    return SINGLE_SUCCESS;
                }

//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 swarm.enable(); // AUTO-REMOVED

//                 swarm.close(); // AUTO-REMOVED
//                 swarm.mode.set(Swarm.Mode.Worker); // AUTO-REMOVED
//                 swarm.worker = new SwarmWorker(pendingConnection.left(), pendingConnection.rightInt()); // AUTO-REMOVED

                pendingConnection = null;

                try {
//                     info("Connected to (highlight)%s.", swarm.worker.getConnection()); // AUTO-REMOVED
                } catch (NullPointerException _) {
//                     error("Error connecting to swarm host."); // AUTO-REMOVED
//                     swarm.close(); // AUTO-REMOVED
//                     swarm.toggle(); // AUTO-REMOVED
                }

                return SINGLE_SUCCESS;
            }))
        );

        builder.then(literal("connections").executes(_ -> {
//             Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//             if (swarm.isActive()) { // AUTO-REMOVED
//                 if (swarm.isHost()) { // AUTO-REMOVED
//                     if (swarm.host.getConnectionCount() > 0) { // AUTO-REMOVED
//                         ChatUtils.info("--- Swarm Connections (highlight)(%s/%s)(default) ---", swarm.host.getConnectionCount(), swarm.host.getConnections().length); // AUTO-REMOVED

//                         for (int i = 0; i < swarm.host.getConnections().length; i++) { // AUTO-REMOVED
//                             SwarmConnection connection = swarm.host.getConnections()[i]; // AUTO-REMOVED
                            if (connection != null)
                                ChatUtils.info("(highlight)Worker %s(default): %s.", i, connection.getConnection());
                        }
                    } else {
                        warning("No active connections");
                    }
//                 } else if (swarm.isWorker()) { // AUTO-REMOVED
//                     info("Connected to (highlight)%s", swarm.worker.getConnection()); // AUTO-REMOVED
                }
            } else {
                throw SWARM_NOT_ACTIVE.create();
            }

            return SINGLE_SUCCESS;
        }));

        builder.then(literal("follow").executes(context -> {
//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 if (swarm.isActive()) { // AUTO-REMOVED
//                     if (swarm.isHost()) { // AUTO-REMOVED
//                         swarm.host.sendMessage(context.getInput() + " " + mc.player.getName().getString()); // AUTO-REMOVED
//                     } else if (swarm.isWorker()) { // AUTO-REMOVED
                        error("The follow host command must be used by the host.");
                    }
                } else {
                    throw SWARM_NOT_ACTIVE.create();
                }

                return SINGLE_SUCCESS;
            }).then(argument("player", PlayerArgumentType.create()).executes(context -> {
                Player playerEntity = PlayerArgumentType.get(context);

//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 if (swarm.isActive()) { // AUTO-REMOVED
//                     if (swarm.isHost()) { // AUTO-REMOVED
//                         swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                     } else if (swarm.isWorker() && playerEntity != null) { // AUTO-REMOVED
                        PathManagers.get().follow(entity -> entity.getName().getString().equalsIgnoreCase(playerEntity.getName().getString()));
                    }
                } else {
                    throw SWARM_NOT_ACTIVE.create();
                }
                return SINGLE_SUCCESS;
            }))
        );

        builder.then(literal("goto")
            .then(argument("x", IntegerArgumentType.integer())
                .then(argument("z", IntegerArgumentType.integer()).executes(context -> {
//                     Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                     if (swarm.isActive()) { // AUTO-REMOVED
//                         if (swarm.isHost()) { // AUTO-REMOVED
//                             swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                         } else if (swarm.isWorker()) { // AUTO-REMOVED
                            int x = IntegerArgumentType.getInteger(context, "x");
                            int z = IntegerArgumentType.getInteger(context, "z");

                            PathManagers.get().moveTo(new BlockPos(x, 0, z), true);
                        }
                    } else {
                        throw SWARM_NOT_ACTIVE.create();
                    }
                    return SINGLE_SUCCESS;
                }))
            )
        );

        builder.then(literal("infinity-miner").executes(context -> {
//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 if (swarm.isActive()) { // AUTO-REMOVED
//                     if (swarm.isHost()) { // AUTO-REMOVED
//                         swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                     } else if (swarm.isWorker()) { // AUTO-REMOVED
                        runInfinityMiner();
                    }
                } else {
                    throw SWARM_NOT_ACTIVE.create();
                }
                return SINGLE_SUCCESS;
            })
            .then(argument("target", BlockStateArgument.block(REGISTRY_ACCESS)).executes(context -> {
//                     Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                     if (swarm.isActive()) { // AUTO-REMOVED
//                         if (swarm.isHost()) { // AUTO-REMOVED
//                             swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                         } else if (swarm.isWorker()) { // AUTO-REMOVED
//                             Modules.get().get(InfinityMiner.class).targetBlocks.set(List.of(context.getArgument("target", BlockInput.class).getState().getBlock())); // AUTO-REMOVED
                            runInfinityMiner();
                        }
                    } else {
                        throw SWARM_NOT_ACTIVE.create();
                    }
                    return SINGLE_SUCCESS;
                })
                .then(argument("repair", BlockStateArgument.block(REGISTRY_ACCESS)).executes(context -> {
//                     Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                     if (swarm.isActive()) { // AUTO-REMOVED
//                         if (swarm.isHost()) { // AUTO-REMOVED
//                             swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                         } else if (swarm.isWorker()) { // AUTO-REMOVED
//                             Modules.get().get(InfinityMiner.class).targetBlocks.set(List.of(context.getArgument("target", BlockInput.class).getState().getBlock())); // AUTO-REMOVED
//                             Modules.get().get(InfinityMiner.class).repairBlocks.set(List.of(context.getArgument("repair", BlockInput.class).getState().getBlock())); // AUTO-REMOVED
                            runInfinityMiner();
                        }
                    } else {
                        throw SWARM_NOT_ACTIVE.create();
                    }
                    return SINGLE_SUCCESS;
                })))
            .then(literal("logout").then(argument("logout", BoolArgumentType.bool()).executes(context -> {
//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 if (swarm.isActive()) { // AUTO-REMOVED
//                     if (swarm.isHost()) { // AUTO-REMOVED
//                         swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                     } else if (swarm.isWorker()) { // AUTO-REMOVED
//                         Modules.get().get(InfinityMiner.class).logOut.set(BoolArgumentType.getBool(context, "logout")); // AUTO-REMOVED
                    }
                } else {
                    throw SWARM_NOT_ACTIVE.create();
                }
                return SINGLE_SUCCESS;
            })))
            .then(literal("walkhome").then(argument("walkhome", BoolArgumentType.bool()).executes(context -> {
//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 if (swarm.isActive()) { // AUTO-REMOVED
//                     if (swarm.isHost()) { // AUTO-REMOVED
//                         swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                     } else if (swarm.isWorker()) { // AUTO-REMOVED
//                         Modules.get().get(InfinityMiner.class).walkHome.set(BoolArgumentType.getBool(context, "walkhome")); // AUTO-REMOVED
                    }
                } else {
                    throw SWARM_NOT_ACTIVE.create();
                }
                return SINGLE_SUCCESS;
            }))));

        builder.then(literal("mine")
            .then(argument("block", BlockStateArgument.block(REGISTRY_ACCESS)).executes(context -> {
//                 Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                 if (swarm.isActive()) { // AUTO-REMOVED
//                     if (swarm.isHost()) { // AUTO-REMOVED
//                         swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                     } else if (swarm.isWorker()) { // AUTO-REMOVED
//                         swarm.worker.target = context.getArgument("block", BlockInput.class).getState().getBlock(); // AUTO-REMOVED
                    }
                } else {
                    throw SWARM_NOT_ACTIVE.create();
                }
                return SINGLE_SUCCESS;
            }))
        );

        builder.then(literal("toggle")
            .then(argument("module", ModuleArgumentType.create())
                .executes(context -> {
//                     Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                     if (swarm.isActive()) { // AUTO-REMOVED
//                         if (swarm.isHost()) { // AUTO-REMOVED
//                             swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                         } else if (swarm.isWorker()) { // AUTO-REMOVED
                            Module module = ModuleArgumentType.get(context);
                            module.toggle();
                        }
                    } else {
                        throw SWARM_NOT_ACTIVE.create();
                    }
                    return SINGLE_SUCCESS;
                }).then(literal("on")
                    .executes(context -> {
//                         Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                         if (swarm.isActive()) { // AUTO-REMOVED
//                             if (swarm.isHost()) { // AUTO-REMOVED
//                                 swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                             } else if (swarm.isWorker()) { // AUTO-REMOVED
                                Module m = ModuleArgumentType.get(context);
                                m.enable();
                            }
                        } else {
                            throw SWARM_NOT_ACTIVE.create();
                        }
                        return SINGLE_SUCCESS;
                    })).then(literal("off")
                    .executes(context -> {
//                         Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//                         if (swarm.isActive()) { // AUTO-REMOVED
//                             if (swarm.isHost()) { // AUTO-REMOVED
//                                 swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                             } else if (swarm.isWorker()) { // AUTO-REMOVED
                                Module m = ModuleArgumentType.get(context);
                                m.disable();
                            }
                        } else {
                            throw SWARM_NOT_ACTIVE.create();
                        }
                        return SINGLE_SUCCESS;
                    })
                )
            )
        );

        builder.then(literal("scatter").executes(context -> {
//             Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//             if (swarm.isActive()) { // AUTO-REMOVED
//                 if (swarm.isHost()) { // AUTO-REMOVED
//                     swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                 } else if (swarm.isWorker()) { // AUTO-REMOVED
                    scatter(100);
                }
            } else {
                throw SWARM_NOT_ACTIVE.create();
            }
            return SINGLE_SUCCESS;
        }).then(argument("radius", IntegerArgumentType.integer()).executes(context -> {
//             Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//             if (swarm.isActive()) { // AUTO-REMOVED
//                 if (swarm.isHost()) { // AUTO-REMOVED
//                     swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                 } else if (swarm.isWorker()) { // AUTO-REMOVED
                    scatter(IntegerArgumentType.getInteger(context, "radius"));
                }
            } else {
                throw SWARM_NOT_ACTIVE.create();
            }
            return SINGLE_SUCCESS;
        })));

        builder.then(literal("stop").executes(context -> {
//             Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//             if (swarm.isActive()) { // AUTO-REMOVED
//                 if (swarm.isHost()) { // AUTO-REMOVED
//                     swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                 } else if (swarm.isWorker()) { // AUTO-REMOVED
                    PathManagers.get().stop();
                }
            } else {
                throw SWARM_NOT_ACTIVE.create();
            }
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("exec").then(argument("command", StringArgumentType.greedyString()).executes(context -> {
//             Swarm swarm = Modules.get().get(Swarm.class); // AUTO-REMOVED
//             if (swarm.isActive()) { // AUTO-REMOVED
//                 if (swarm.isHost()) { // AUTO-REMOVED
//                     swarm.host.sendMessage(context.getInput()); // AUTO-REMOVED
//                 } else if (swarm.isWorker()) { // AUTO-REMOVED
                    ChatUtils.sendPlayerMsg(StringArgumentType.getString(context, "command"));
                }
            } else {
                throw SWARM_NOT_ACTIVE.create();
            }
            return SINGLE_SUCCESS;
        })));
    }

    private void runInfinityMiner() {
//         InfinityMiner infinityMiner = Modules.get().get(InfinityMiner.class); // AUTO-REMOVED
//         infinityMiner.disable(); // AUTO-REMOVED
//        infinityMiner.smartModuleToggle.set(true);
//         infinityMiner.enable(); // AUTO-REMOVED
    }

    private void scatter(int radius) {
        Random random = new Random();

        double a = random.nextDouble() * 2 * Math.PI;
        double r = radius * Math.sqrt(random.nextDouble());
        double x = mc.player.getX() + r * Math.cos(a);
        double z = mc.player.getZ() + r * Math.sin(a);

        PathManagers.get().stop();
        PathManagers.get().moveTo(new BlockPos((int) x, 0, (int) z), true);
    }
}
