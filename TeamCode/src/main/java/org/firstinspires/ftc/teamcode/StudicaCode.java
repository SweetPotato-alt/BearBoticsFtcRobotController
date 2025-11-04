package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class StudicaCode extends OpMode {

    // Motors
    DcMotor left, right, launcher;

    // Servos
    Servo feeder;                // torque servo
    CRServo leftIndex, rightIndex; // continuous rotation servos

    // Toggle states
    boolean launcherOn = false;
    boolean indexActive = false;

    // Button press memory
    boolean aPressedLast = false;
    boolean xPressedLast = false;

    @Override
    public void init() {
        // --- Drive motors ---
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        // --- Launcher ---
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setPower(0);

        // --- Feeder (torque servo) ---
        feeder = hardwareMap.get(Servo.class, "feeder");
        feeder.setPosition(0.5); // neutral start

        // --- Index servos (continuous rotation) ---
        leftIndex = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");
        leftIndex.setPower(0);
        rightIndex.setPower(0);
    }

    @Override
    public void loop() {
        // --- Drive control ---
        float drive = -gamepad1.left_stick_y;
        float turn = gamepad1.left_stick_x;

        float leftPower = drive + turn;
        float rightPower = drive - turn;

        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

        left.setPower(leftPower);
        right.setPower(rightPower);

        // --- Launcher toggle (A button) ---
        if (gamepad1.a && !aPressedLast) {
            launcherOn = !launcherOn;
            launcher.setPower(launcherOn ? 1.0 : 0.0);
        }
        aPressedLast = gamepad1.a;

        // --- Feeder control (right stick Y) ---
        double feederInput = -gamepad1.right_stick_y;
        double feederPos = feeder.getPosition() + feederInput * 0.01;
        feederPos = Math.max(0.0, Math.min(1.0, feederPos));
        feeder.setPosition(feederPos);

        // --- Index toggle (X button) ---
        if (gamepad1.x && !xPressedLast) {
            indexActive = !indexActive;
            double power = indexActive ? 1.0 : 0.0;

            // If one spins opposite, flip one sign below:
            leftIndex.setPower(power);
            rightIndex.setPower(power);

            // Example: if mirrored setup → use rightIndex.setPower(-power);
        }
        xPressedLast = gamepad1.x;

        // --- Telemetry ---
        telemetry.addData("Drive", drive);
        telemetry.addData("Turn", turn);
        telemetry.addData("Launcher", launcherOn ? "ON" : "OFF");
        telemetry.addData("Feeder Pos", feeder.getPosition());
        telemetry.addData("Index", indexActive ? "Spinning" : "Stopped");
        telemetry.update();
    }
}
