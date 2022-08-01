package com.sr9000.gdx.x3p1.business.part;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.business.part.struct.x3p1Circle;
import com.sr9000.gdx.x3p1.business.state.IPreferencesProvider;

public class Physics extends ProtoPausable {

    Array<Body> circles;

    Thread t;
    World world;
    boolean keep_going;

    final float scale, velocity, rmax;
    final int limit;

    public Physics() {
        scale = 100;
        velocity = 25;

        limit = 10;
        rmax = 0.5f * (float) Math.sqrt((scale * scale / (limit * 3.0)));
    }

    @Override
    protected void sanitize() {
        circles = new Array<>();
    }

    @Override
    protected void save_state(IPreferencesProvider p) {
        keep_going = false;
        try {
            t.join(1000);
        } catch (InterruptedException e) {
            // nop
        }

        p.getCircleReader().save_circles(get_circles());
    }

    @Override
    protected void load_state(IPreferencesProvider p) {
        Box2D.init();
        world = new World(new Vector2(0, 0), false);
        world.setContinuousPhysics(true);

//        for (int i = 0; i < 10; ++i) {
//            push_number(i * 113, (i + 1) * 100, (i % 2 == 0) ? "orange" : "blue");
//        }

        for (x3p1Circle c : p.getCircleReader().load_circles()) {
            add_x3p1_circle(c);
        }
        normalize_circles_size();

        // walls
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        Body wall = world.createBody(bodyDef);
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(0, 0, scale, 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = edgeShape;
        wall.createFixture(fixtureDef);

        wall = world.createBody(bodyDef);
        edgeShape = new EdgeShape();
        edgeShape.set(scale, 0, scale, scale);

        fixtureDef.shape = edgeShape;
        wall.createFixture(fixtureDef);

        wall = world.createBody(bodyDef);
        edgeShape = new EdgeShape();
        edgeShape.set(scale, scale, 0, scale);

        fixtureDef.shape = edgeShape;
        wall.createFixture(fixtureDef);

        wall = world.createBody(bodyDef);
        edgeShape = new EdgeShape();
        edgeShape.set(0, scale, 0, 0);

        fixtureDef.shape = edgeShape;
        wall.createFixture(fixtureDef);


        keep_going = true;
        t = new Thread(() -> {
            try {
                long begin = System.nanoTime();
                while (keep_going) {
                    Thread.sleep(1);
                    synchronized (AppBusiness.INSTANCE) {
                        if (!AppBusiness.INSTANCE.is_resumed()) {
                            continue;
                        }
                        long end = System.nanoTime();
                        float delta = 1.0e-9f * (end - begin);
                        begin = end;

//                    float w = AppBusiness.INSTANCE.getMotionProvider().getAngular()[2];
//                    w /= 6.28f;
//                    if (Math.abs(w) < 0.1f) {
//                        w = 0;
//                    }

                        float[] fva = AppBusiness.INSTANCE.getMotionProvider().get_acceleration();
                        Vector2 va = new Vector2(fva[0], fva[1]);
                        va = va.scl(scale * 0.03f);
                        if (va.len() / scale < 0.1f) {
                            va = new Vector2(0, 0);
                        }

                        if (va.len() > 0) {
//                        if (false){
//                        Vector2 cen = new Vector2(0.5f * scale, 0.5f * scale);

//                            Array<Body> bodies = new Array<>();
//                            world.getBodies(bodies);

                            for (Body b : circles) {
//                            float l1 = w * p0.dst(cen);
//                            Vector2 rotComp = p0.sub(cen).rotate90((int) Math.signum(w)).setLength(l1);
//                            Vector2 impulse = rotComp.add(va).scl(0.6f);
                                Vector2 impulse = new Vector2().add(va).nor().scl(velocity);
//                                b.applyLinearImpulse(impulse.scl(b.getMass()).scl(0), p0, true);
                                b.setLinearVelocity(impulse.add(new Vector2().setToRandomDirection().scl(0.33f * velocity)));
                            }
                        }

                        world.step(delta, 10, 10);
                        for (Body b : circles) {
                            ((x3p1Circle) b.getUserData()).set_position(b.getPosition());
                        }
                    }
                }
            } catch (InterruptedException e) {
                // nop
            }
        });
        t.start();
    }

    public float get_scale() {
        return scale;
    }

    public void push_number(long number, long hops, String color) {
        if (circles.size >= limit) {
            // drop_min_if_smaller(hops);
            drop_min_if_max_is_smaller(hops);
        }

        if (circles.size < limit) {
            add_new_circle(number, hops, color);
            normalize_circles_size();
        }
    }

    private void drop_min_if_smaller(long hops) {
        int imin = 0;
        for (int i = 1; i < circles.size; i++) {
            if (((x3p1Circle) circles.get(i).getUserData()).hops < ((x3p1Circle) circles.get(imin).getUserData()).hops) {
                imin = i;
            }
        }

        if (hops > ((x3p1Circle) circles.get(imin).getUserData()).hops) {
            world.destroyBody(circles.get(imin));
            circles.removeIndex(imin);
        }
    }

    private void drop_min_if_max_is_smaller(long hops) {
        int imax = 0;
        int imin = 0;
        for (int i = 1; i < circles.size; i++) {
            if (((x3p1Circle) circles.get(i).getUserData()).hops > ((x3p1Circle) circles.get(imax).getUserData()).hops) {
                imax = i;
            }
            if (((x3p1Circle) circles.get(i).getUserData()).hops < ((x3p1Circle) circles.get(imin).getUserData()).hops) {
                imin = i;
            }
        }

        if (hops > ((x3p1Circle) circles.get(imax).getUserData()).hops) {
            world.destroyBody(circles.get(imin));
            circles.removeIndex(imin);
        }
    }

    private void normalize_circles_size() {
        if (circles.size <= 0) {
            return;
        }

        float rlargest = 0;
        for (Body b : circles) {
            rlargest = Math.max(rlargest, ((x3p1Circle) b.getUserData()).original_r);
        }

        float factor = rmax / rlargest;
        for (Body b : circles) {
            x3p1Circle circle = (x3p1Circle) b.getUserData();
            float r = circle.original_r * factor;
            float d = circle.hops / (r * r);

            circle.real_r = r;
            Fixture f = b.getFixtureList().first();

            f.setDensity(1);
            f.getShape().setRadius(r);

            b.resetMassData();
        }
    }

    private void add_new_circle(long number, long hops, String color) {
        x3p1Circle x3p1circle = new x3p1Circle(
                scale / 2, scale / 2, (2 + hops) * (2 + hops),//(float) Math.sqrt(2 + hops),//MathUtils.log2(2 + hops),
                (color.equals("orange")) ? 0 : 1,
                number, hops
        );

        add_x3p1_circle(x3p1circle);
    }

    private void add_x3p1_circle(x3p1Circle x3p1circle) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();

        // circles
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x3p1circle.x, x3p1circle.y);
        bodyDef.fixedRotation = true;

        Body circle = world.createBody(bodyDef);

        fixtureDef.restitution = 1.0f;
        fixtureDef.friction = 0;
        fixtureDef.density = 1;

        circleShape.setRadius(1);
        fixtureDef.shape = circleShape;

        circle.createFixture(fixtureDef);

        circle.setLinearVelocity(new Vector2().setToRandomDirection().scl(velocity));
        circle.setUserData(new x3p1Circle(x3p1circle));

        circles.add(circle);
    }

    public x3p1Circle[] get_circles() {
        x3p1Circle[] res = new x3p1Circle[circles.size];
        int i = 0;
        for (Body b : circles) {
            res[i] = new x3p1Circle((x3p1Circle) b.getUserData());

//                res[i].setPosition(b.getPosition());
//                res[i].original_r = b.getFixtureList().first().getShape().getRadius();

            i++;
        }
        return res;
    }
}
