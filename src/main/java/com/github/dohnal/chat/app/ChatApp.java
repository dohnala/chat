package com.github.dohnal.chat.app;

import com.github.dohnal.chat.ChatRuntime;
import com.github.dohnal.chat.domain.ChatRepository;
import com.github.dohnal.chat.domain.ChatService;
import com.github.dohnal.chat.read.ChatRepositoryAkka;
import com.github.dohnal.chat.write.ChatServiceAkka;
import com.google.common.collect.Lists;

/**
 * @author dohnal
 */
public class ChatApp
{
    public static void main(String[] args) throws InterruptedException
    {
        final ChatRuntime chatRuntime = new ChatRuntime(0, Lists.newArrayList("fuck"));
        final ChatService chatService = new ChatServiceAkka(chatRuntime);
        final ChatRepository chatRepository = new ChatRepositoryAkka(chatRuntime);

        // Join room as John
        chatService.joinRoom("John");

        // Send messages
        chatService.sendMessage("John", "Hello!");
        chatService.sendMessage("John", "Fuck! ... nobody here");

        // Wait for view synchronization
        Thread.sleep(1000);

        // Print current view state
        System.out.println(chatRepository.getChatRoom());
    }
}
