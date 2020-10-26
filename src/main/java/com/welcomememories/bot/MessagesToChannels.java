/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.welcomememories.bot;

import java.awt.Color;
import java.util.ArrayList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Jose Francisco Riffo <jfriffoa@gmail.com>
 */

public class MessagesToChannels extends ListenerAdapter {

    
    // !send -c <channel> -u <author> -m <message>
    // Si no se especifica autor, se usa por defecto
    // Si no se especifica canal, no se envia nada
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        
        //Send messages to other channels
        if (rawMessage.startsWith(Main.prefix + "send")){
            System.out.println(rawMessage);
            ArrayList<String> parameters = new ArrayList<>();
            
            //Get Parameters
            Boolean checking = true;
            while (checking){
                int startIdx = rawMessage.indexOf("-");
                int endIdx = rawMessage.indexOf("-", startIdx + 1);
                
                //If we can't find the next parameter. End the check
                if (endIdx == -1) {
                    checking = false;
                    endIdx = rawMessage.length();
                }
                
                //Add the parameter to the list
                if (startIdx == -1){
                    sendMessage(event, "To see how to use the command, use: `!send -h`");
                    return;
                }
                
                parameters.add(rawMessage.substring(startIdx, endIdx));
                rawMessage = rawMessage.substring(endIdx);
            }
            
            
            String id = "";
            String author = event.getAuthor().getName();
            String message = "";
            
            //Parse Parameters
            for (String parameter : parameters) {
                if (parameter.charAt(1) == 'h'){
                    sendMessage(event, "To send a message, use the following command: `!send -c #channel -u author -m message`\nThis bot accepts multi-line messages too");
                    return;
                }
                
                String content = parameter.substring(3);
                switch(parameter.charAt(1)){
                    case 'c':
                        id = content.substring(content.indexOf("<") + 1, content.lastIndexOf(">"));
                        break;
                    case 'u':
                        author = content;
                        break;
                    case 'm':
                        message = content;
                        break;
                }
            }
            
            //Check if everything is right
            if (id.isEmpty()){
                sendMessage(event, "Please Specify a channel. Try again using `!send -c #channel -m message`");
                return;
            }
           
            // Send to a channel
            if (id.charAt(0) == '#') {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Sent by: " + author);
                embed.setColor(Color.PINK);
                embed.setDescription(message);
                
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
    
    void sendMessage(MessageReceivedEvent event, String message){
        event.getChannel().sendMessage(message).queue();
    }
}
