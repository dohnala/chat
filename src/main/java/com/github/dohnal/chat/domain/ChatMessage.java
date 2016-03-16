package com.github.dohnal.chat.domain;

import java.util.Date;

/**
 * @author dohnal
 */
public class ChatMessage
{
    private String username;

    private String message;

    private Date date;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
