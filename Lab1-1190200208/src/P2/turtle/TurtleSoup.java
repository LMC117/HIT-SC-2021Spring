/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TurtleSoup {

    /**
     * Draw a square.
     *
     * @param turtle     the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        for (double i = 0; i < 360; i = i + 90) {
            turtle.forward(sideLength);
            turtle.turn(90);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * <p>
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     *
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        double doubleSides = sides;
        return (180 * (doubleSides - 2)) / doubleSides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * <p>
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     *
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        int i = 3;
        for (; ((i * angle) / (i - 2)) - 180 > Math.pow(10, -2); i++) ;
        return i;
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * <p>
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     *
     * @param turtle     the turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        // ????????????????????????
        double ang = calculateRegularPolygonAngle(sides);
        // ??????
        for (int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(180 - ang);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * <p>
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360.
     * <p>
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     *
     * @param currentBearing current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     * must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        double ang = 0;
        double delta_x = targetX - currentX;
        int delta_y = targetY - currentY;
        if (delta_y == 0) {
            if (delta_x > 0)
                ang = 90 - currentBearing;
            else
                ang = 270 - currentBearing;
            if (ang <= 0)
                ang += 360;
            return ang;
        }
        if (delta_x == 0) {
            if (delta_y > 0)
                ang = 360 - currentBearing;
            else
                ang = 180 - currentBearing;
            if (ang <= 0)
                ang += 360;
            if (ang == 360)
                ang = 0;
            return ang;
        }
        double tan = delta_y / delta_x;
        double relative_ang = Math.abs(Math.toDegrees(Math.atan(tan)));
        // ????????????
        if (delta_x > 0 && delta_y > 0)
            ang = 90 - relative_ang - currentBearing;
        if (delta_x > 0 && delta_y < 0)
            ang = 90 + relative_ang - currentBearing;
        if (delta_x < 0 && delta_y < 0)
            ang = 270 - relative_ang - currentBearing;
        if (delta_x < 0 && delta_y > 0)
            ang = 270 + relative_ang - currentBearing;
        if (ang <= 0)
            ang += 360;
        if (ang == 360)
            ang = 0;
        return ang;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * <p>
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     *
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     * otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        List<Double> angles = new ArrayList<Double>();
        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        int targetX, targetY;
        double angle = 0;
        for (int i = 1; i < xCoords.size(); i++) { // ??????n-1???
            targetX = xCoords.get(i);
            targetY = yCoords.get(i);
            angle = calculateBearingToPoint(angle, currentX, currentY, targetX, targetY);
            angles.add(angle);
            currentX = targetX;
            currentY = targetY;
        }
        return angles;
    }

    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and
     * there are other algorithms too.
     *
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        // ?????????????????????3???????????????points
        if (points.size() <= 3)
            return points;
        // ????????????????????????
        Set<Point> minpoints = new HashSet<>();
        // ???points????????????list?????????????????????
        List<Point> pointsList = new ArrayList<>(points);
        // ????????????y????????????????????????????????????????????????????????????????????????
        Point initial = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        for (Point point : pointsList)
            if (point.y() < initial.y())
                initial = point;
        minpoints.add(initial);
        // ????????????????????????????????????????????????????????????????????????????????????????????????
        Point current = initial;
        Point target;
        Point temp;
        double bearing = 0;
        double angle, temp_angle;
        double distance, temp_distance;
        int i;

        if (pointsList.get(0) != current) // ?????????target,?????????current??????
            target = pointsList.get(0);
        else
            target = pointsList.get(1);

        while (target != initial) {
            angle = 360;
            temp_angle = 0;
            distance = Double.MAX_VALUE;
            temp_distance = 0;
            i = 0;
            for (; i < pointsList.size(); i++) { // ????????????????????????????????????????????????????????????i????????????point???target
                temp = pointsList.get(i);
                if (temp != current) {
                    temp_angle = calculateBearingToPoint(bearing, (int) current.x(), (int) current.y(), (int) temp.x(), (int) temp.y()); // ??????current???temp?????????
                    temp_distance = Math.pow(current.x() - temp.x(), 2) + Math.pow(current.y() - temp.y(), 2);  // ??????current???temp?????????
                    if (temp_angle < angle) { // ??????angle??????????????????target???????????????????????????
                        angle = temp_angle;
                        distance = temp_distance;
                        target = temp;
                    } else if (temp_angle == angle) { // ???????????????????????????
                        if (temp_distance > distance) // ??????????????????????????????????????????
                            target = temp;
                    }
                }
            }
            current = target; // ???????????????????????????
            bearing += angle;
            if (target != initial)
                minpoints.add(target);
        }
        return minpoints;
    }

    /**
     * Draw your personal, custom art.
     * <p>
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     *
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        int number;
        for (int i = 1; i <= 800; i++) {
            turtle.forward(2 * i);
            turtle.turn(235);
            number = i % 3;
            switch (number) {
                case 1:
                    turtle.color(PenColor.BLUE);
                    break;
                case 2:
                    turtle.color(PenColor.RED);
                    break;
                default:
                    turtle.color(PenColor.GREEN);
                    break;
            }
        }
    }

    /**
     * Main method.
     * <p>
     * This is the method that runs when you run "java TurtleSoup".
     *
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        //drawSquare(turtle, 40);
        //drawRegularPolygon(turtle, 6, 40);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
