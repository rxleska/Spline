package com.rxleska;

import java.util.ArrayList;

public class CubicSpline {
    private ArrayList<Point> vertices;
    private ArrayList<BezierCurve> cubicSplines;

    public CubicSpline() {
        this.vertices = new ArrayList<Point>();
        this.cubicSplines = new ArrayList<BezierCurve>();
    }

    public void addKnot(double x, double y) {
        Point p = new Point(x, y);
        this.vertices.add(p);
    }

    public void addKnot(Point p) {
        this.vertices.add(p);
    }

    public void removeKnotAt(int index) {
        this.vertices.remove(index);
    }

    public void clearKnots() {
        this.vertices.clear();
    }

    public ArrayList<BezierCurve> getCubicSplines() {
        updateSplines();
        return this.cubicSplines;
    }

    protected void updateSplines() {
        int v;
        ArrayList<Double> xPoints = new ArrayList<Double>();
        ArrayList<Double> yPoints = new ArrayList<Double>();
        this.cubicSplines.clear();

        for(v = 0; v < this.vertices.size(); v++) {
            xPoints.add(this.vertices.get(v).x());
            yPoints.add(this.vertices.get(v).y());
        }

        ArrayList<Double> px1 = new ArrayList<Double>();
        ArrayList<Double> px2 = new ArrayList<Double>();
        ArrayList<Double> py1 = new ArrayList<Double>();
        ArrayList<Double> py2 = new ArrayList<Double>();

        computeControlPoints(xPoints, px1, px2);
        computeControlPoints(yPoints, py1, py2);

        for(v = 0; v < (this.vertices.size() - 1); v++) {
            BezierCurve cubicSpline = new BezierCurve(
                        new Point(xPoints.get(v), yPoints.get(v)),
                        new Point(px1.get(v), py1.get(v)),
                        new Point(px2.get(v), py2.get(v)),
                        new Point(xPoints.get(v + 1), yPoints.get(v + 1))
                    );
            this.cubicSplines.add(cubicSpline);
        }
    }

    protected void computeControlPoints(ArrayList<Double> K, ArrayList<Double> p1, ArrayList<Double> p2) {
        int n = K.size() - 1;
        int i;

        for(i = 0; i < (n + 1); i++) {
            p1.add(0.0);
            p2.add(0.0);
        }

        // RHS Vector - RIGHT HAND SYSTEM VECTOR
        ArrayList<Double> a = new ArrayList<Double>();
        ArrayList<Double> b = new ArrayList<Double>();
        ArrayList<Double> c = new ArrayList<Double>();
        ArrayList<Double> r = new ArrayList<Double>();

        // Left most segment
        a.add(0.0);
        b.add(2.0);
        c.add(1.0);
        r.add(K.get(0) + 2 * K.get(1));

        // Internal Segments
        for (i = 1; i < (n-1); i++) {
            a.add(1.0);
            b.add(4.0);
            c.add(1.0);
            r.add(4.0 * K.get(i) + 2.0 * K.get(i + 1));
        }

        // Right most Segment
        a.add(2.0);
        b.add(7.0);
        c.add(0.0);
        r.add(8.0 * K.get(n - 1) + K.get(n));

        // Solves Ax = b with thomas algorithm
        for( i = 1; i < n; i++ ) {
            double m = a.get(i) / b.get(i - 1);
            double bi = b.get(i) - m * c.get(i - 1);
            double ri = r.get(i) - m * r.get(i - 1);
            b.set(i, bi);
            r.set(i, ri);
        }
        p1.set(n - 1, r.get(n - 1) / b.get(n - 1));
        for (i = n - 2; i >= 0; i--) {
            p1.set(i, (r.get(i) - c.get(i) * p1.get(i + 1)) / b.get(i));
        }
        for(i = 0; i < (n - 1); i++) {
            p2.set(i, 2.0 * K.get(i + 1) - p1.get(i + 1));
        }
        p2.set(n - 1, 0.5 * (K.get(n) + p1.get(n - 1)));
    }
}
