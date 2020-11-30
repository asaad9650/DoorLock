package com.example.doorlock;

public class EntryInfo
{
    private String e_name;
    private String date_time;


    public EntryInfo()
    {

    }

    public EntryInfo(String e_name, String date_time)
    {
        this.e_name = e_name;
        this.date_time = date_time;
    }

    public String getE_name()
    {
        return e_name;
    }

    public void setE_name(String e_name)
    {
        this.e_name = e_name;
    }

    public String getDate_time()
    {
        return date_time;
    }

    public void setDate_time(String date_time)
    {
        this.date_time = date_time;
    }
}
