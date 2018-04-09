package com.ardanet_systems.it.croatpool.Models;

import java.util.ArrayList;

public class Pool {
    private int Position = 0;
    private String Name = "";
    private String Api = "";

    public Pool (String Name, String Api )
    {
        this.Name = Name;
        this.Api = Api;
    }

    public Pool ( int Position, String Name, String Api )
    {
        this.Position = Position;
        this.Name = Name;
        this.Api = Api;
    }

    public int Position() {return Position; }
    public void Position(int Position) { this.Position = Position; }

    public String Name() { return Name; }
    public void Name(String Name) { this.Name = Name; }

    public String Api() { return Api; }
    public void Api(String Api) { this.Api = Api; }

    public String toString()
    {
        return(Name);
    }

    public static int selectPoolData(String dataValue, ArrayList<Pool> dataSource) {
        for (int i = 0; i < dataSource.size(); i++) {
            Pool item = dataSource.get(i);
            if (item.Name().equals(dataValue)) {
                return i;
            }
        }
        return -1;
    }
}
