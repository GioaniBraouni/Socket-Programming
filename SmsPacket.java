package server;

import java.io.Serializable;

public class SmsPacket implements Serializable
{
    private String phoneNumber;
    private String recipientName;
    private String smsChoice;
    private String homeAddress;

    public void setPhoneNumber(String number)
    {
        this.phoneNumber = number;
    }

    public void setRecipientName(String name)
    {
        this.recipientName = name;
    }

    public void setSmsChoice(String choice)
    {
        this.smsChoice = choice;
    }

    public void setHomeAddress(String address)
    {
        this.homeAddress = address;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getRecipientName()
    {
        return recipientName;
    }

    public String getSmsChoice()
    {
        return smsChoice;
    }

    public String getHomeAddress()
    {
        return homeAddress;
    }

    public String toString()
    {
        return "Μετακινηση " + smsChoice + " "+ phoneNumber + " " +  homeAddress;
    }
}
