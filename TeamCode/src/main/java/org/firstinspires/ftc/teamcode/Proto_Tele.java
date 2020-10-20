package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.IMU;
import org.firstinspires.ftc.teamcode.Subsystems.ScaledMecanum;
import org.firstinspires.ftc.teamcode.Subsystems.Odometer;
import org.firstinspires.ftc.teamcode.Subsystems.ThreadedOdometer;

// Two controller teleop mode
@TeleOp(name = "Proto_Tele", group = "Opmode")
//@Disabled
public final class Proto_Tele extends OpMode {
    // Declare OpMode members.
    private IMU imu;
    private ScaledMecanum drivetrain;
//    private Odometer odometer;
    private ThreadedOdometer odometer;

    // Init vars
    private boolean pressLastTime = false;
    private boolean goToCarry = false;

    @Override
    public final void init() {
        // Init subsystems
        imu = new IMU(hardwareMap);
        drivetrain = new ScaledMecanum(imu, hardwareMap, gamepad1, gamepad2);
//        odometer = new Odometer(hardwareMap);
        odometer = new ThreadedOdometer(hardwareMap);
    }

    @Override
    public final void loop() {
        drivetrain.runMecanum();
        odometer.updateOdometry();



        // Telemetry to the driver station. NOTE: %.2f means floating point
        telemetry.addData("Robot","Heading (%.2f)",
                imu.getYaw());
        telemetry.addData("Drive Train","F-Left (%.2f), F-Right (%.2f), B-Left (%.2f), Back Right (%.2f)",
                drivetrain.getDrivetrain().getFrontLeftPower(), drivetrain.getDrivetrain().getFrontRightPower(), drivetrain.getDrivetrain().getBackLeftPower(), drivetrain.getDrivetrain().getBackRightPower());
        telemetry.addData("Drive Train O","X (%.2f), Y (%.2f), Theta (%.2f), TickTime (%.2f)",
                odometer.x, odometer.y, odometer.theta * 57.2, odometer.tickTime);
        telemetry.addData("Drive Train Oi","m1 (%.2f), m2 (%.2f), m3 (%.2f)",
                (double) odometer.m1.getCurrentPosition(), (double) odometer.m2.getCurrentPosition(), (double) odometer.m3.getCurrentPosition());
    }
}