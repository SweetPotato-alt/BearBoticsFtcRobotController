package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class StudicaCode extends OpMode {

    // --- Hardware ---
    DcMotor left, right, launcher;
    Servo feeder, index;

    // --- Toggle states ---
    boolean launcherActive = false;

    // --- Button press tracking ---
    boolean aPressedLast = false;  // launcher toggle

    @Override
    public void init() {
        // Drive motors
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        // Launcher motor
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);
        launcher.setPower(0);

        // Servos
        feeder = hardwareMap.get(Servo.class, "feeder");  // torque servo
        index = hardwareMap.get(Servo.class, "index");    // continuous rotation servo

        // Initial positions/power
        feeder.setPosition(0.5);  // center start
        index.setPosition(0.5);   // stop

        telemetry.addLine("Robot Initialized");
    }

    @Override
    public void start() {
        // Stop all motors at start
        left.setPower(0);
        right.setPower(0);
        launcher.setPower(0);
        index.setPosition(0.5);

        // Reset toggle states
        launcherActive = false;
    }

    @Override
    public void loop() {
        // --- DRIVE (arcade) ---
        float drive = gamepad1.left_stick_y;
        float turn = gamepad1.left_stick_x;

        float leftPower = drive + turn;
        float rightPower = drive - turn;

        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

        left.setPower(leftPower);
        right.setPower(rightPower);

        // --- LAUNCHER TOGGLE (Button A) ---
        if (gamepad1.a && !aPressedLast) {
            launcherActive = !launcherActive;
            launcher.setPower(launcherActive ? 1.0 : 0.0);
        }
        aPressedLast = gamepad1.a;

        // --- FEEDER (torque servo, joystick) ---
        double feederSpeed = gamepad1.right_stick_y; // -1 to 1
        double currentFeederPos = feeder.getPosition();
        double newFeederPos = currentFeederPos + feederSpeed * 0.01; // small increment per loop
        newFeederPos = Math.max(0.0, Math.min(1.0, newFeederPos)); // clamp
        feeder.setPosition(newFeederPos);

        // --- INDEX (continuous rotation servo, hold X to spin) ---
        if (gamepad1.x) {
            index.setPosition(1.0); // spin forward
        } else {
            index.setPosition(0.5); // stop
        }

        // --- TELEMETRY ---
        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);
        telemetry.addData("Launcher", launcherActive ? "ON" : "OFF");
        telemetry.addData("Feeder Pos", feeder.getPosition());
        telemetry.addData("Index", gamepad1.x ? "SPINNING" : "STOPPED");
        telemetry.update();
    }
}
