package com.rxleska;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        CubicSpline spline = new CubicSpline();

        spline.addKnot(0, 0);
        spline.addKnot(100, 200);
        spline.addKnot(150, -100);
        spline.addKnot(200, 0);
        spline.addKnot(240, -50);

        ArrayList<BezierCurve> curves = spline.getCubicSplines();

        int c;
        for(c = 0; c < curves.size(); c++) {
            for( double pt = 0.0; pt <= .9; pt+=0.1) {

                System.out.println(curves.get(c).curvePoint(pt).toString());
            }

        }
    }
}
