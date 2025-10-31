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
    boolean feederActive = false;
    boolean indexActive = false;

    // --- Button press tracking ---
    boolean aPressedLast = false;
    boolean bPressedLast = false;
    boolean xPressedLast = false;

    // --- Servo positions ---
    double feederOnPos = 1.0;
    double feederOffPos = 0.0;

    double indexOnPos = 1.0;
    double indexOffPos = 0.0;

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
        feeder = hardwareMap.get(Servo.class, "feeder");
        index = hardwareMap.get(Servo.class, "index");

        // Initial positions
        feeder.setPosition(feederOffPos);
        index.setPosition(indexOffPos);

        telemetry.addLine("Robot Initialized");
    }

    @Override
    public void loop() {
        // --- DRIVE ---
        float drive = gamepad1.left_stick_y;
        float turn = -gamepad1.left_stick_x;

        float leftPower = drive + turn;
        float rightPower = drive - turn;

        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

        left.setPower(leftPower);
        right.setPower(rightPower);

        // --- TOGGLE LAUNCHER (Button A) ---
        if (gamepad1.a && !aPressedLast) {
            launcherActive = !launcherActive;
            launcher.setPower(launcherActive ? 1.0 : 0.0);
        }
        aPressedLast = gamepad1.a;

        // --- TOGGLE FEEDER (Button B) ---
        if (gamepad1.b && !bPressedLast) {
            feederActive = !feederActive;
            feeder.setPosition(feederActive ? feederOnPos : feederOffPos);
        }
        bPressedLast = gamepad1.b;

        // --- TOGGLE INDEX (Button X) ---
        if (gamepad1.x && !xPressedLast) {
            indexActive = !indexActive;
            index.setPosition(indexActive ? indexOnPos : indexOffPos);
        }
        xPressedLast = gamepad1.x;

        // --- TELEMETRY ---
        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);
        telemetry.addData("Launcher", launcherActive ? "ON" : "OFF");
        telemetry.addData("Feeder", feederActive ? "ON" : "OFF");
        telemetry.addData("Index", indexActive ? "ON" : "OFF");
        telemetry.update();
    }
}
