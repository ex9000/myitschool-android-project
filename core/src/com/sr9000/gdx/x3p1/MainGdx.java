package com.sr9000.gdx.x3p1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sr9000.gdx.x3p1.business.AppBusiness;
import com.sr9000.gdx.x3p1.business.draw.DrawHelper;
import com.sr9000.gdx.x3p1.business.part.struct.x3p1Circle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MainGdx extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont gdxFont;
    ShapeRenderer w1Dr, kDr, w2Dr, bDr, oDr;
    ShapeRenderer[] renders;
    Texture img;
    BasicColor bc;
    DrawHelper d;

    public MainGdx(BasicColor bc) {
        this.bc = bc;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        gdxFont = generator.generateFont(parameter); // font size 32
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        img = new Texture("sa-logo.png");
        d = new DrawHelper();

        w1Dr = new ShapeRenderer();
        kDr = new ShapeRenderer();
        w2Dr = new ShapeRenderer();
        bDr = new ShapeRenderer();
        oDr = new ShapeRenderer();

        renders = new ShapeRenderer[]{w1Dr, kDr, w2Dr, oDr, bDr};
    }

    @Override
    public void render() {

        // get app state
        x3p1Circle[] all_circles;
        synchronized (AppBusiness.INSTANCE) {
            if (!AppBusiness.INSTANCE.is_resumed()) {
                return;
            }
            all_circles = AppBusiness.INSTANCE.getPhysics().get_circles();
        }

        // render state
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        ScreenUtils.clear(bc.r, bc.g, bc.b, 1);
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int x0 = width / 2;
        int y0 = height / 2;
        int r0 = Math.min(width, height);

        int xx = x0 - r0 / 2;
        int yy = y0 - r0 / 2;
        w1Dr.begin(ShapeRenderer.ShapeType.Filled);
        w1Dr.rectLine(xx, yy, xx, yy + r0, r0 * 0.01f);
        w1Dr.rectLine(xx, yy, xx + r0, yy, r0 * 0.01f);
        w1Dr.rectLine(xx + r0, yy, xx + r0, yy + r0, r0 * 0.01f);
        w1Dr.rectLine(xx, yy + r0, xx + r0, yy + r0, r0 * 0.01f);
        w1Dr.end();

        r0 = r0 / 3 * 2;
        d.init(width, height, AppBusiness.INSTANCE.getPhysics().get_scale());

        for (ShapeRenderer r : renders) {
            r.begin(ShapeRenderer.ShapeType.Filled);
        }

        w1Dr.setColor(Color.WHITE);
        w2Dr.setColor(Color.WHITE);
        kDr.setColor(Color.BLACK);
        oDr.setColor(Color.ORANGE);
        bDr.setColor(Color.CYAN);

        int i = 0;
        for (x3p1Circle circle : all_circles) {
            i += 1;
            float r = circle.real_r;
            r = d.s(r);

            float x = d.x(circle.x);
            float y = d.y(circle.y);

            w1Dr.circle(x, y, r * 1.00f, 64);
            kDr.circle(x, y, r - 0.006f * r0, 64);
            w2Dr.circle(x, y, r - 0.018f * r0, 64);

            if (circle.color % 2 == 0) {
                oDr.circle(x, y, r - 0.024f * r0, 64);
            } else {
                bDr.circle(x, y, r - 0.024f * r0, 64);
            }


        }

        for (ShapeRenderer r : renders) {
            r.end();
        }

        batch.begin();
        for (x3p1Circle circle : all_circles) {
            float x = d.x(circle.x);
            float y = d.y(circle.y);

            Label.LabelStyle labelStyle = new Label.LabelStyle(gdxFont, Color.BLACK);
            Label l = new Label(String.format("%,d", circle.number), labelStyle);
            l.setWrap(false);
            l.setEllipsis(false);

            float w = l.getPrefWidth();
            float h = l.getPrefHeight();

            float factor = fit_rect_in_circle(d.s(circle.real_r) - 0.024f * r0, w, h);
            l.setFontScale(factor);
            l.setPosition(x - 0.5f * l.getPrefWidth(), y, Align.center | Align.left);
            l.draw(batch, 1);

//                font.draw(batch, String.valueOf(circle.number), x, y, 100f, Align.bottomLeft, false);
        }
        batch.end();


        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

    }

    private float fit_rect_in_circle(float r, float w, float h) {
        float omega = h / w;
        omega = omega * omega;

        float cosine = (1 - omega) / (1 + omega);

        return ((float) Math.sqrt(2 * r * r * (1 + cosine))) / w;
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        for (ShapeRenderer r : renders) {
            r.dispose();
        }
    }
}
