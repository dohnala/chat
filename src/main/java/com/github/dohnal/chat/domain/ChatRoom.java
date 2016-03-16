package com.github.dohnal.chat.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author dohnal
 */
public class ChatRoom implements Serializable
{
    private String name;

    private Set<String> users = Sets.newHashSet();

    private List<ChatMessage> messages = Lists.newArrayList();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<String> getUsers()
    {
        return users;
    }

    public void setUsers(Set<String> users)
    {
        this.users = users;
    }

    public List<ChatMessage> getMessages()
    {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages)
    {
        this.messages = messages;
    }
}
