/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.welcomememories.bot;

import java.util.Scanner;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * @author Jose Francisco Riffo
 */
public class Main extends ListenerAdapter {

    static String prefix = "!";
    
    /**
     * @param args the command line arguments
     * @throws javax.security.auth.login.LoginException
     */
    public static void main(String[] args) throws LoginException {
        Scanner sc = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream("oath.key"));
        String apiKey = sc.nextLine();
        JDABuilder builder = JDABuilder.createDefault(apiKey);
        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.MEMBER_OVERRIDES);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.watching("Cry baby Cry"));
        
        builder.addEventListeners(new Main());
        
        builder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(">>>Message| " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay());
        System.out.println(">>>Raw|" + event.getMessage().getContentRaw());
        
        //Handle commands
        if (event.getMessage().getContentRaw().equals(prefix + "!ping")){
            event.getChannel().sendMessage("Pong!").queue();
        }
        
        // Send message to another channel
        if (event.getMessage().getContentRaw().startsWith(prefix + "send")){
            JDA api = event.getJDA();
            
            String raw = event.getMessage().getContentRaw();
            String id = raw.substring(raw.indexOf("<") + 1, raw.indexOf(">"));
            String message = raw.substring(raw.indexOf(">") + 1);
            switch(id.charAt(0)){
                case '#':
                    event.getGuild().getTextChannelById(id.substring(1)).sendMessage(message).queue();
                    break;
                case '@':
                    User user = api.getUserById(id.substring(2));
                    System.out.println("ID:" + id.substring(2));
                    System.out.println(user);
                    user.openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(message).queue();
                    });
                    break;
            }
        }
    }
    
    
}
