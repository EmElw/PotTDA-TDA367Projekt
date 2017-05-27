package com.pottda.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.pottda.game.model.Constants;
import com.pottda.game.model.Obstacle;

import javax.vecmath.Vector2f;

public class ObstacleTextureFactory {
    public static Texture getRectangularObstacleTexture(Vector2f sizeMeters){
        return getRectangularObstacleTexture(sizeMeters.x, sizeMeters.y);
    }

    public static Texture getRectangularObstacleTexture(float widthMeters, float heightMeters){
        int width = Math.round(widthMeters / Constants.WIDTH_RATIO);
        int height = Math.round(heightMeters / Constants.HEIGHT_RATIO);

        Pixmap tempPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        tempPixmap.setColor(Color.BLACK);
        tempPixmap.fillRectangle(0, 0, width, height);
        tempPixmap.setColor(Color.WHITE);
        tempPixmap.drawRectangle(0, 0, width, height);

        tempPixmap.dispose();

        return new Texture(tempPixmap);
    }

    public static Texture getCircularObstacleTexture(float radiusMeters){
        int radius = Math.round(radiusMeters / Constants.WIDTH_RATIO);

        Pixmap tempPixMap = new Pixmap(radius * 2 + 1, radius * 2 + 1, Pixmap.Format.RGBA8888);

        tempPixMap.setColor(Color.BLACK);
        tempPixMap.fillCircle(radius, radius, radius);
        tempPixMap.setColor(Color.WHITE);
        tempPixMap.drawCircle(radius, radius, radius);

        tempPixMap.dispose();

        return new Texture(tempPixMap);
    }
}
