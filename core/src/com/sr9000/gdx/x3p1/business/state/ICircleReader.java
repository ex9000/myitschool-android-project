package com.sr9000.gdx.x3p1.business.state;

import com.sr9000.gdx.x3p1.business.part.struct.x3p1Circle;

import java.util.ArrayList;

public interface ICircleReader {

    void save_circles(x3p1Circle[] circles);

    ArrayList<x3p1Circle> load_circles();

}
