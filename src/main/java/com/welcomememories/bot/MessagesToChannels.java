/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.welcomememories.bot;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Jose Francisco Riffo <jfriffoa@gmail.com>
 */

public class MessagesToChannels extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        
        //Send messages to other channels
        if (rawMessage.startsWith(Main.prefix + "send")){
            String id = rawMessage.substring(rawMessage.indexOf("<") +1, rawMessage.indexOf(">"));
            String message = rawMessage.substring(rawMessage.indexOf(">") + 1);
           
            // Send to a channel
            if (id.charAt(0) == '#') {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Sent by: " + event.getAuthor().getName());
                embed.setColor(Color.RED);
                embed.setAuthor("Pika");
                embed.setDescription("Esta es una descripción, a ver que tal se ve");
                embed.setFooter("_Y este es el footer_");
                
                event.getGuild().getTextChannelById(id.substring(1)).sendMessage(embed.build()).queue();
            }
//            switch(id.charAt(0)){
//                case '#':   // Send to a channel
//                    event.getGuild().getTextChannelById(id.substring(1)).sendMessage(message).queue();
//                    break;
//                case '@':
//                    User user = api.getUserById(id.substring(2)); //Send to a user
//                    System.out.println("ID:" + id.substring(2));
//                    System.out.println(user);
//                    user.openPrivateChannel().queue((channel) -> {
//                        channel.sendMessage(message).queue();
//                    });
//                    break;
//            }
        }
    }
}
