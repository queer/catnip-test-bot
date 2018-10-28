package gg.amy.test;

import com.google.common.collect.ImmutableSet;
import com.mewna.catnip.Catnip;
import com.mewna.catnip.CatnipOptions;
import com.mewna.catnip.cache.CacheFlag;
import com.mewna.catnip.shard.DiscordEvent;
import com.mewna.catnip.shard.DiscordEvent.Raw;
import com.mewna.catnip.shard.manager.DefaultShardManager;

import java.text.NumberFormat;
import java.util.EnumSet;

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
                        .cacheFlags(EnumSet.of(CacheFlag.DROP_EMOJI, CacheFlag.DROP_GAME_STATUSES))
                        .disabledEvents(ImmutableSet.of(
                                Raw.CHANNEL_CREATE,
                                Raw.CHANNEL_DELETE,
                                Raw.CHANNEL_UPDATE,
                                Raw.CHANNEL_PINS_UPDATE,
                                Raw.GUILD_CREATE,
                                Raw.GUILD_AVAILABLE,
                                Raw.GUILD_DELETE,
                                Raw.GUILD_UNAVAILABLE,
                                Raw.GUILD_UPDATE,
                                Raw.GUILD_EMOJIS_UPDATE,
                                Raw.GUILD_MEMBER_UPDATE,
                                Raw.GUILD_MEMBERS_CHUNK,
                                Raw.GUILD_ROLE_CREATE,
                                Raw.GUILD_ROLE_DELETE,
                                Raw.GUILD_ROLE_UPDATE,
                                Raw.GUILD_INTEGRATIONS_UPDATE,
                                Raw.USER_UPDATE,
                                Raw.MESSAGE_DELETE,
                                Raw.MESSAGE_DELETE_BULK,
                                Raw.MESSAGE_UPDATE,
                                Raw.MESSAGE_EMBEDS_UPDATE,
                                Raw.PRESENCE_UPDATE,
                                Raw.READY,
                                Raw.TYPING_START
                        ))
                        .shardManager(new DefaultShardManager(15))
        );
        
        catnip.on(DiscordEvent.MESSAGE_CREATE, msg -> {
            if(msg.author().id().equals("128316294742147072")) {
                if(msg.content().startsWith("catnip")) {
                    final String cmd = msg.content().replaceFirst("catnip", "").trim();
                    switch(cmd) {
                        case "stats": {
                            final NumberFormat format = NumberFormat.getInstance();
                            final long maxMemory = Runtime.getRuntime().maxMemory();
                            final long totalMemory = Runtime.getRuntime().totalMemory();
                            final long freeMemory = Runtime.getRuntime().freeMemory();
                            final long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                            
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
