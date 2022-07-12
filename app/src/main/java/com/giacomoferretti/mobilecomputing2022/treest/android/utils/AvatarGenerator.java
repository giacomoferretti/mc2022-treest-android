package com.giacomoferretti.mobilecomputing2022.treest.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.giacomoferretti.mobilecomputing2022.treest.android.R;

public class AvatarGenerator {
    private final Context context;
    private String character;
    private int size = 512;
    private int textSize = 96;

    public AvatarGenerator(@NonNull Context context) {
        this.context = context;
    }

    public AvatarGenerator setLabel(String label) {
        // Extract first character (emojis supported)
        int codePoint = label.trim().codePointAt(0);
        int upperCaseCodePoint = Character.toUpperCase(codePoint);

        this.character = new String(Character.toChars(upperCaseCodePoint));
        return this;
    }

    public AvatarGenerator setSize(int size) {
        this.size = size;
        return this;
    }

    public AvatarGenerator setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    private Bitmap generateBitmap() {
        long startTime = System.currentTimeMillis();

        // Create text painter
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize * context.getResources().getDisplayMetrics().scaledDensity);
        textPaint.setColor(Color.WHITE);

        // Create painter
        Paint painter = new Paint();
        painter.setAntiAlias(true);
        painter.setColor(ContextCompat.getColor(context, R.color.md_theme_light_primary));

        // Create bitmap
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Calculate bounds
        Rect areaRect = new Rect(0, 0, size, size);
        RectF bounds = new RectF(areaRect);
        bounds.right = textPaint.measureText(character);
        bounds.bottom = textPaint.descent() - textPaint.ascent();
        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;

        // Draw
        canvas.drawCircle(size / 2.0f, size / 2.0f, size / 2.0f, painter);
        canvas.drawText(character, bounds.left, bounds.top - textPaint.ascent(), textPaint);

        Log.d("AvatarGenerator", (System.currentTimeMillis() - startTime) + "ms");

        return bitmap;
    }

    public Bitmap build() {
        return generateBitmap();
    }

    public BitmapDrawable buildDrawable() {
        return new BitmapDrawable(context.getResources(), generateBitmap());
    }

    enum Shape {
        CIRCLE,
        SQUARE
    }
}
