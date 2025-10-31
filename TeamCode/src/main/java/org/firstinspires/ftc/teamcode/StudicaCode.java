package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class StudicaCode extends OpMode {

    DcMotor left, right;
    Servo launcher, leftIndex, rightIndex;

    // Servo toggle states
    boolean launcherActive = false;
    boolean leftRightIndexActive = false;

    // Button press tracking (to prevent rapid toggling)
    boolean aPressedLast = false;
    boolean bPressedLast = false;

    // Servo positions
    double launcherOnPos = 1.0;
    double launcherOffPos = 0.0;

    double leftIndexOnPos = 1.0;
    double leftIndexOffPos = 0.0;

    double rightIndexOnPos = 1.0;
    double rightIndexOffPos = 0.0;

    @Override
    public void init() {
        // Initialize drive motors
        left = hardwareMap.get(DcMotor.class, "left");
        left.setDirection(DcMotorSimple.Direction.REVERSE);

        right = hardwareMap.get(DcMotor.class, "right");
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        // Initialize servos
        launcher = hardwareMap.get(Servo.class, "launcher");
        leftIndex = hardwareMap.get(Servo.class, "leftindex");
        rightIndex = hardwareMap.get(Servo.class, "rightindex");

        // Set initial positions
        launcher.setPosition(launcherOffPos);
        leftIndex.setPosition(leftIndexOffPos);
        rightIndex.setPosition(rightIndexOffPos);

        telemetry.addLine("Robot Initialized");
    }

    @Override
    public void loop() {
        // --- DRIVE CONTROL ---
        float drive = gamepad1.left_stick_y;
        float turn = -gamepad1.left_stick_x;

        float leftPower = drive + turn;
        float rightPower = drive - turn;

        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

        left.setPower(leftPower);
        right.setPower(rightPower);

        // --- LAUNCHER TOGGLE (Button A) ---
        if (gamepad1.a && !aPressedLast) {
            launcherActive = !launcherActive;  // toggle state
            if (launcherActive) {
                launcher.setPosition(launcherOnPos);
            } else {
                launcher.setPosition(launcherOffPos);
            }
        }
        aPressedLast = gamepad1.a;

        // --- INDEX TOGGLE (Button B) ---
        if (gamepad1.b && !bPressedLast) {
            leftRightIndexActive = !leftRightIndexActive;
            if (leftRightIndexActive) {
                leftIndex.setPosition(leftIndexOnPos);
                rightIndex.setPosition(rightIndexOnPos);
            } else {
                leftIndex.setPosition(leftIndexOffPos);
                rightIndex.setPosition(rightIndexOffPos);
            }
        }
        bPressedLast = gamepad1.b;

        // --- TELEMETRY ---
        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);
        telemetry.addData("Launcher Active", launcherActive);
        telemetry.addData("Index Active", leftRightIndexActive);
        telemetry.update();
    }
}
