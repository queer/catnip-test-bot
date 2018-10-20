package gg.amy.test;

import com.mewna.catnip.Catnip;
import com.mewna.catnip.CatnipOptions;
import com.mewna.catnip.shard.DiscordEvent;
import com.mewna.catnip.shard.manager.DefaultShardManager;

import java.text.NumberFormat;

/**
 * @author amy
 * @since 10/20/18.
 */
public final class Test {
    private static final int MB = 1024 * 1024;
    
    private Test() {
    }
    
    public static void main(final String[] args) {
        final Catnip catnip = Catnip.catnip(
                new CatnipOptions(System.getenv("TOKEN"))
        );
        
        catnip.on(DiscordEvent.MESSAGE_CREATE, msg -> {
            if(msg.author().id().equals("128316294742147072")) {
                if(msg.content().startsWith("catnip")) {
                    final String cmd = msg.content().replaceFirst("catnip", "").trim();
                    switch(cmd) {
                        case "stats": {
                            final var format = NumberFormat.getInstance();
                            final var maxMemory = Runtime.getRuntime().maxMemory();
                            final var totalMemory = Runtime.getRuntime().totalMemory();
                            final var freeMemory = Runtime.getRuntime().freeMemory();
                            final var usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                            
                            catnip.rest().channel().sendMessage(msg.channelId(),
                                    "```\n" +
                                            "CACHE STATS\n" +
                                            "===========\n" +
                                            "  Guilds: " + catnip.cache().guilds().size() + '\n' +
                                            "   Users: " + catnip.cache().users().size() + '\n' +
                                            "Channels: " + catnip.cache().channels().size() + '\n' +
                                            " Members: " + catnip.cache().members().size() + '\n' +
                                            
                                            '\n' +
                                            
                                            "MEMORY STATS\n" +
                                            "============\n" +
        
                                            " used: " + format.format(usedMemory / MB) + "MB\n" +
                                            " free: " + format.format(freeMemory / MB) + "MB\n" +
                                            "total: " + format.format(totalMemory / MB) + "MB\n" +
                                            "  max: " + format.format(maxMemory / MB) + "MB\n" +
                                            
                                            "```");
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        });
        
        catnip.startShards();
    }
}
