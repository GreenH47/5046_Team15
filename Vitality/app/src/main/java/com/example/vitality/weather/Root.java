package com.example.vitality.weather;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */

//weather的URL下的json 转 java，如下所示
public class Root{
    public Coord coord;
    public ArrayList<Weather> weather;
    public String base;

    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public int dt;
    public Sys sys;
    public int timezone;
    public int id;
    public String name;
    public int cod;


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}



 class Clouds{
    public int all;
}

 class Coord{
    public double lon;
    public double lat;
}


class Sys{
    public int type;
    public int id;
    public String country;
    public int sunrise;
    public int sunset;
}

class Wind{
    public double speed;
    public int deg;
}

