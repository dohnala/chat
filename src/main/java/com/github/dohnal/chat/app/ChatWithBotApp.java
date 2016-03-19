package com.github.dohnal.chat.app;

import com.github.dohnal.chat.ChatRuntime;
import com.google.common.collect.Lists;

/**
 * @author dohnal
 */
public class ChatWithBotApp
{
    public static void main(String[] args) throws InterruptedException
    {
        new ChatRuntime(5, Lists.newArrayList("fuck"));
    }
}
