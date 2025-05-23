package com.gelevla.discordsrvlinkchannel;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.guild.GuildUnavailableEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.guild.GuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import github.scarsz.discordsrv.objects.managers.AccountLinkManager;

public class JDAListener extends ListenerAdapter {
    private final DiscordSRVLinkChannel plugin;

    public JDAListener(DiscordSRVLinkChannel plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildUnavailable(@NotNull GuildUnavailableEvent event) {
        plugin.getLogger().severe(event.getGuild().getName() + " Discord server went unavailable.");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // If the message was sent in the discord linking channel
        if (event.getChannel().getId().equals(plugin.getConfig().getString("LinkingDiscordChannel"))) {
            // Don't process messages sent by ANY bot
            if (!event.getAuthor().isBot()) {
                DiscordSRV.api.callEvent(new DiscordGuildMessageReceivedEvent(event));

                // Get the AccountLinkManager interface instance
                AccountLinkManager accountLinkManager = DiscordSRV.getPlugin().getAccountLinkManager();

                // Process the linking code using the interface method
                String reply = accountLinkManager.process(event.getMessage().getContentRaw(), event.getAuthor().getId());

                if (reply != null) {
                    event.getChannel().sendMessage(reply).queue();
                }
            }

            // Remove messages if configured
            if (plugin.getConfig().getBoolean("RemoveMessages")) {
                event.getChannel().deleteMessageById(event.getMessage().getId()).queueAfter(10, TimeUnit.SECONDS);
            }
        }
    }
}