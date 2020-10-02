package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.IMU;
import org.firstinspires.ftc.teamcode.Subsystems.ScaledMecanum;

// Two controller teleop mode
@TeleOp(name = "Proto_Tele", group = "Opmode")
//@Disabled
public final class Proto_Tele extends OpMode {
    // Declare OpMode members.
    private IMU imu;
    private ScaledMecanum drivetrain;
    private DigitalChannel digitalTouch;
    private ElapsedTime runtime;

    // Init vars
    private boolean pressLastTime = false;
    private boolean goToCarry = false;

    @Override
    public final void init() {
        // Init subsystems
        imu = new IMU(hardwareMap);
        drivetrain = new ScaledMecanum(imu, hardwareMap, gamepad1, gamepad2);
        digitalTouch = hardwareMap.get(DigitalChannel.class, "ls");
        runtime = new ElapsedTime();
    }

    @Override
    public final void loop() {
        if (false) {
            drivetrain.disableYeet();
        } else {
            drivetrain.enableYeet();
        }

        drivetrain.runMecanum();


        // Telemetry to the driver station. NOTE: %.2f means floating point
        telemetry.addData("Robot","Heading (%.2f)",
                imu.getYaw());
        telemetry.addData("Drive Train","F-Left (%.2f), F-Right (%.2f), B-Left (%.2f), Back Right (%.2f)",
                drivetrain.getDrivetrain().getFrontLeftPower(), drivetrain.getDrivetrain().getFrontRightPower(), drivetrain.getDrivetrain().getBackLeftPower(), drivetrain.getDrivetrain().getBackRightPower());
    }
}