package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Example", group="Linear Opmode")
public class Example extends LinearOpMode {

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private Servo servo = null;

    @Override
    public void runOpMode() {
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotorName");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotorName");
        servo = hardwareMap.get(Servo.class, "servoName");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            double leftPower = 0;
            double rightPower = 0;

            double divePower = gamepad1.left_stick_y;
            double turnPower = gamepad1.right_stick_x;

            leftPower = divePower - turnPower;
            rightPower = divePower + turnPower;

            if (leftPower > 1) {
                leftPower = 1;
            }
            if (leftPower < -1) {
                leftPower = -1;
            }
            if (rightPower > 1) {
                rightPower = 1;
            }
            if (rightPower < -1) {
                rightPower = -1;
            }
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);

            if (gamepad1.a) {
                servo.setPosition(1);
            } else if(gamepad1.b) {
                servo.setPosition(0);
            } else {
                servo.setPosition(gamepad1.right_trigger);
            }

            telemetry.addData("Robot Date", "Right Power (%.2f), Left Power (%.2f)", rightPower, leftPower);
            telemetry.update();
        }
    }
}
