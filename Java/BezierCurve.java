package com.rxleska;

public final class BezierCurve {
    private final Point p0;    // Point 0
    private final Point p1;    // Point 1
    private final Point p2;    // Point 2
    private final Point p3;    // Point 3

    public BezierCurve() {
        this.p0 = new Point();
        this.p1 = new Point();
        this.p2 = new Point();
        this.p3 = new Point();
    }

    public BezierCurve(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Point p0() {
        return this.p0;
    }

    public Point p1() {
        return this.p1;
    }

    public Point p2() {
        return this.p2;
    }

    public Point p3() {
        return this.p3;
    }

    public double curveX(double t) {
        return  (this.p0.x() * Math.pow((1 - t), 3)) +
                (this.p1.x() * 3 * t * Math.pow((1 - t), 2)) +
                (this.p2.x() * 3 * Math.pow(t, 2) * (1 - t)) +
                (this.p3.x() * Math.pow(t, 3));
    }

    public double curveY(double t) {
        return  (this.p0.y() * Math.pow((1 - t), 3)) +
                (this.p1.y() * 3 * t * Math.pow((1 - t), 2)) +
                (this.p2.y() * 3 * Math.pow(t, 2) * (1 - t)) +
                (this.p3.y() * Math.pow(t, 3));
    }

    public Point curvePoint(double t) {
        return new Point(curveX(t), curveY(t));
    }

    // return a string representation of this Bezier Curve
    public String toString() {
        return "(" +
                "(" + p0().x() + ", " + p0().y() + "), " +
                "(" + p1().x() + ", " + p1().y() + "), " +
                "(" + p2().x() + ", " + p2().y() + "), " +
                "(" + p3().x() + ", " + p3().y() + ")" +
        ")";
    }
}
