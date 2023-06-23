package com.example.boxbox.objects;

import android.graphics.Canvas;

import static com.example.boxbox.utils.Utils.*;

import com.example.boxbox.Game;

public class Particles {

    public static double MAX_SIZE = 100, MIN_SIZE = 40;
    public static double MAX_NUM = 5, MIN_NUM = 3;

    boolean spreadX, spreadY;

    Particle[] particles = new Particle[0];

    public Particles(double minX, double maxX, double minY, double maxY, boolean spreadX, boolean spreadY) {
        this.spreadX = spreadX;
        this.spreadY = spreadY;

        int numOfParticles = randInt((int) MIN_NUM, (int) (MAX_NUM+1));
        particles = new Particle[numOfParticles];
        int size, x, y, xVel, yVel;

        // generating the individual particles
        for (int i = 0; i < particles.length; i++) {
            size = randInt((int) MIN_SIZE, (int) (MAX_SIZE+1));
            x = randInt((int) minX, (int) (minX+1 - size));
            y = randInt((int) minY, (int) (maxY+1 - size));

            if (spreadX) {
                xVel = 20;
                xVel *= randInt(0, 2) == 1 ? 1 : -1;
            }
            else {
                xVel = 0;
            }
            if (spreadY) {
                yVel = 20;
                yVel *= randInt(0, 2) == 1 ? 1 : -1;
            }
            else {
                yVel = 0;
            }

            Particle p = new Particle(x, y, size, xVel, yVel);
            particles = append(p, particles);

        }


    }


    public void draw(Canvas canvas) {
        for (Particle particle : particles) {
            particle.draw(canvas);
        }
    }

    public void update() {
        for (Particle particle : particles) {
            particle.update();
        }
    }


    public Particles[] appendTo(Particles[] particlesHolder) {
        Particles[] out = new Particles[particles.length+1];

        for (int i = 0; i < out.length; i++) {
            out[i] = particlesHolder[i];
        }

        out[out.length-1] = this;

        return out;
    }



    class Particle {

        int x, y, xVel, yVel;
        double size;

        Particle (int x, int y, int size, int xVel, int yVel) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.xVel = xVel;
            this.yVel = yVel;
        }

        public void draw(Canvas canvas) {

        }


        public void update() {
            this.x += xVel *Game.dt;
            this.y += yVel *Game.dt;
            this.size -= 0.3 * Game.dt;

        }
    }


    private Particle[] append(Particle item, Particle[] array) {
        Particle[] out = new Particle[array.length+1];

        for (int i = 0; i < out.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }

}
