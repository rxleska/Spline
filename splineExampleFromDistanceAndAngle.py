#!/usr/bin/env python3
import turtle
import math

drawing = turtle.Turtle()
drawing.hideturtle()
vertices = []
splines = []
dpc = 10 #Dots Per Curve

def createKnot(x , y):
    vertices.append((x, y))
    drawing.pu()
    drawing.setposition(x, y)
    drawing.pd()
    drawing.fillcolor("red")
    drawing.begin_fill()
    drawing.circle(3)
    drawing.end_fill()

def updateSplines():
    xpoints = []
    ypoints = []
    for v in vertices:
        xpoints.append(v[0])
        ypoints.append(v[1])
    px = computeControlPoints(xpoints)
    py = computeControlPoints(ypoints)
    for i in range(0, len(vertices) - 1):
        cubicSpline = (xpoints[i], ypoints[i], px[0][i], py[0][i], px[1][i], py[1][i], xpoints[i + 1], ypoints[i + 1])
        splines.append(cubicSpline)

def computeControlPoints(K):
    p1 = []
    p2 = []
    n = len(K) - 1
    
    for i in range(1, n + 1):
        p1.append(0)
        p2.append(0)

    #RHS Vector RIGHT HAND SYSTEM VECTOR
    a = []
    b = []
    c = []
    r = []
    
    # Left most segment 
    a.append(0)
    b.append(2)
    c.append(1)
    r.append(K[0] + 2 * K[1])

    #Internal Segments
    for i in range(1, n - 1):
        a.append(1)
        b.append(4)
        c.append(1)
        r.append(4 * K[i] + 2 * K[i + 1])

    #Right Segment
    a.append(2)
    b.append(7)
    c.append(0)
    r.append(8 * K[n-1] + K[n])

    #Solves Ax = b with thomas algorithem 
    for i in range(1, n):
        m = a[i] / b[i - 1]
        b[i] = b[i] - m * c[i - 1]
        r[i] = r[i] - m * r[i - 1]
    
    p1[n - 1] = r[n - 1] /b[n - 1]
    for i in range(n - 2, -1, -1):
        p1[i] = (r[i] - c[i] * p1[i + 1]) / b[i]
    for i in range(0, n - 1):
        p2[i] = 2 * K[i + 1] - p1[i + 1]
    p2[n-1] = 0.5 * (K[n] + p1[n-1])

    return (p1, p2)

def bezierCurveX(t, x0, x1, x2, x3):
    return (x0 * ((1 - t)**3)) + (x1 * 3 * t * ((1 - t)**2)) + (x2 * 3 * (t**2) * (1 - t)) + (x3 * (t**3))

def bezierCurveY(t, y0, y1, y2, y3):
    return (y0 * ((1 - t)**3)) + (y1 * 3 * t * ((1 - t)**2)) + (y2 * 3 * (t**2) * (1 - t)) + (y3 * (t**3))

def drawBezier(dt, pt0x, pt0y, pt1x, pt1y, pt2x, pt2y, pt3x, pt3y):
    points = []
    for t in range(0, dpc, dt): 
        point = (bezierCurveX(t / dpc, pt0x, pt1x, pt2x, pt3x), bezierCurveY(t / dpc, pt0y, pt1y, pt2y, pt3y))
        points.append(point)
    point = (bezierCurveX(1, pt0x, pt1x, pt2x, pt3x), bezierCurveY(1, pt0y, pt1y, pt2y, pt3y))
    points.append(point)
    drawing.setposition(points[0][0], points[0][1])
    for drawnPoint in points:
        drawing.setposition(drawnPoint[0], drawnPoint[1])

createKnot(0,0)

distanceToTarget = 260
angleToTarget = 9.6

drawing.setposition(200,0)
drawing.setposition(0,0)


verticalPointOnPlane = distanceToTarget * math.tan(math.radians(angleToTarget)) 

createKnot((distanceToTarget/2),(verticalPointOnPlane+20))

createKnot(distanceToTarget,verticalPointOnPlane)


drawing.pu()
drawing.setposition(0,0)
drawing.pd()
updateSplines()
for spline in splines:
    drawBezier(1, spline[0], spline[1], spline[2], spline[3], spline[4], spline[5], spline[6], spline[7])
input("press return")