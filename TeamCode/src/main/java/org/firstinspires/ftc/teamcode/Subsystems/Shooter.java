package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Util.PID;

public class Shooter {
    // Class members
    private DcMotor shooterMotor;
    private PID shooterPID;
    private ElapsedTime veloTimer;

    // State machine
    public final int IDLE = 0;
    public final int SPEEDING_UP = 1;
    public final int READY = 2;
    private int STATE = IDLE;
    private int LAST_STATE = STATE;

    // Info
    public final double kP = 0.005;
    public final double kI = 0;
    public final double kD = 0;
    public final double kF = 0;
    public final double errorSP = 100;
    public final double highVelocity = 100;
    public final double barsVelocity = 150;

    // General Vars
    private double velocitySetpoint = highVelocity;
    private double velocity = 0;
    private int lastPos = 0;
    private double lastTime = 0;

    public Shooter (HardwareMap hardwareMap) {
        // Initialize class members
        shooterMotor = hardwareMap.get(DcMotor.class, "5");
        shooterMotor.setPower(0);
        shooterPID = new PID(kP, kI, kD);
        veloTimer = new ElapsedTime();
    }

    // Call in loop to operate shooter
    public void runShooter() {
        // Get info that the state machine needs to run
        updateVelocity();

        // State machine
        if (STATE == IDLE) {
            shooterMotor.setPower(0);
            shooterPID.clearI();
        } else if (STATE == SPEEDING_UP) {
            runToVelocity();
            if (Math.abs(velocitySetpoint - velocity) < errorSP) {
                STATE = READY;
            }
        } else if (STATE == READY) {
            runToVelocity();
        }
    }

    // Private methods
    // Calculate the velocity of the elevator from the last time this was called
    private void updateVelocity() {
        int pos = shooterMotor.getCurrentPosition();
        double time = veloTimer.milliseconds();
        velocity = Math.abs((pos - lastPos) / (time - lastTime));
        lastTime = time;
        lastPos = pos;
    }
    // Sets the power of the motor based on a feedback controller
    private void runToVelocity() {
        double pidPower = shooterPID.runPID(velocitySetpoint, velocity);
        double power = pidPower + kF;
        shooterMotor.setPower(power);
    }

    // Getter and setter
    public int getSTATE() { return STATE; }
    public double getVelocity() { return velocity; }
    public void setSTATE(int state) { STATE = state; }
    public void setVelocitySetpoint(double velocitySetpoint1) { velocitySetpoint = velocitySetpoint1; }
}
