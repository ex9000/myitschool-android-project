package com.sr9000.gdx.x3p1.business.part.struct;

import com.badlogic.gdx.math.Vector2;

public class x3p1Circle {
    public float x, y, original_r, real_r;
    public int color;
    public long number, hops;

    public x3p1Circle() {
    }

    public x3p1Circle(float x, float y, float r, int color, long number, long hops) {
        this.x = x;
        this.y = y;
        this.original_r = r;
        this.real_r = r;
        this.color = color;
        this.number = number;
        this.hops = hops;
    }

    public x3p1Circle(x3p1Circle circle) {
        this.x = circle.x;
        this.y = circle.y;
        this.original_r = circle.original_r;
        this.color = circle.color;
        this.number = circle.number;
        this.hops = circle.hops;
        this.real_r = circle.real_r;
    }

    public Vector2 get_position() {
        return new Vector2(x, y);
    }

    public void set_position(Vector2 pos) {
        x = pos.x;
        y = pos.y;
    }
}
