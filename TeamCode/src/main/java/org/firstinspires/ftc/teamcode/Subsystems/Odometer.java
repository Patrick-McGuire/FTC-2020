package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/*
IMPORTANT INFO:
M1 is left side encoder
M2 is right side encoder
M3 is back encoder

ALL UNITS ARE INCHES
 */

public class Odometer {
    public DcMotor m1;
    public DcMotor m2;
    public DcMotor m3;

    private ElapsedTime time;
    private double lastTime = 0;
    public double tickTime = 0;

    // Constants
    private final double ticksPerRev = 8192;
    private final double wheelDiamiter = 1.88976;
    private final double r1 = 7;  // Positive
    private final double r2 = -6.75;  // Negative
    private final double r3 = -7;  // Negative

    private double m1LastPos = 0;
    private double m2LastPos = 0;
    private double m3LastPos = 0;

    public double x = 0;
    public double y = 0;
    public double theta = 0;

    public Odometer(HardwareMap hardwareMap) {
        m1 = hardwareMap.get(DcMotor.class, "1");
        m2 = hardwareMap.get(DcMotor.class, "2");
        m3 = hardwareMap.get(DcMotor.class, "3");
        time = new ElapsedTime();
        time.reset();

    }

    public void updateOdometry() {
        ////// Get the traveled by each wheel //////
        // Get the motors current position
        double m1In = m1.getCurrentPosition();
        double m2In = -m2.getCurrentPosition();
        double m3In = m3.getCurrentPosition();
        // Figure out how far we have gone
        double m1Delta = m1In - m1LastPos;
        double m2Delta = m2In - m2LastPos;
        double m3Delta = m3In - m3LastPos;
        // Reset reference vars for next tick
        m1LastPos = m1In;
        m2LastPos = m2In;
        m3LastPos = m3In;
        // Get the distance each wheel has traveled in inches
        double m1Dist = (m1Delta / ticksPerRev) * Math.PI * wheelDiamiter;
        double m2Dist = (m2Delta / ticksPerRev) * Math.PI * wheelDiamiter;
        double m3Dist = (m3Delta / ticksPerRev) * Math.PI * wheelDiamiter;
        ////// Odometer calculations //////
        double deltaXRobot = (m1Dist + m2Dist) / 2;
        double deltaTheta = (m2Dist - deltaXRobot) / r2;
        double deltaYRobot = m3Dist + r3 * deltaTheta;
        deltaTheta *= -1;
        
        double deltaXMap = deltaXRobot * Math.cos(theta) - deltaYRobot * Math.sin(theta);
        double deltaYMap = deltaXRobot * Math.sin(theta) + deltaYRobot * Math.cos(theta);

        // Update odometer
        y += deltaYMap;
        x += deltaXMap;
        theta += deltaTheta;

        tickTime = time.milliseconds() - lastTime;
        lastTime = time.milliseconds();
    }
}
