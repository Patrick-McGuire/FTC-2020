package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ThreadedOdometer {
    HardwareMap hardwareMap;
    private ElapsedTime time;
    public double tickTime = 0;

    public double x = 0;
    public double y = 0;
    public double theta = 0;

    public DcMotor m1;
    public DcMotor m2;
    public DcMotor m3;

    private OdometerThread odometerThread;

    public ThreadedOdometer(HardwareMap hardwareMap1) {
        hardwareMap = hardwareMap1;
        time = new ElapsedTime();
        time.reset();
        odometerThread = new OdometerThread();
        odometerThread.start();
    }

    public class OdometerThread extends Thread {
        private boolean runThread = true;

        // Constants
        private final double ticksPerRev = 8192;
        private final double wheelDiameter = 1.88976;
        private final double r1 = 7.3125;  // Positive
        private final double r2 = -7.3125;  // Negative
        private final double r3 = -6.625;  // Negative

        private double m1LastPos = 0;
        private double m2LastPos = 0;
        private double m3LastPos = 0;

        public double x = 0;
        public double y = 0;
        public double theta = 0;

        private double lastTime = 0;
        public double tickTime = 0;

        @Override
        public void run() {
            m1 = hardwareMap.get(DcMotor.class, "1");
            m2 = hardwareMap.get(DcMotor.class, "2");
            m3 = hardwareMap.get(DcMotor.class, "3");
            m1LastPos = m1.getCurrentPosition();
            m2LastPos = -m2.getCurrentPosition();
            m3LastPos = m3.getCurrentPosition();
            while (runThread) {
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
                double m1Dist = (m1Delta / ticksPerRev) * Math.PI * wheelDiameter;
                double m2Dist = (m2Delta / ticksPerRev) * Math.PI * wheelDiameter;
                double m3Dist = (m3Delta / ticksPerRev) * Math.PI * wheelDiameter;
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
        public void disableThread() { runThread = false; }


    }

    public void updateOdometry() {
        x = odometerThread.x;
        y = odometerThread.y;
        theta = odometerThread.theta;
        tickTime = odometerThread.tickTime;
    }
}
