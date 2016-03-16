package com.github.dohnal.chat.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author dohnal
 */
public class ChatRoom implements Serializable
{
    private String name;

    private Set<String> users = Sets.newHashSet();

    private List<Message> messages = Lists.newArrayList();

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

    public List<Message> getMessages()
    {
        return messages;
    }

    public void setMessages(List<Message> messages)
    {
        this.messages = messages;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
