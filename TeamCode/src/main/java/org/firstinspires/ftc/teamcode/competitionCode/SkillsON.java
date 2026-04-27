package org.firstinspires.ftc.teamcode.competitionCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MecanumDrive", group="TeleOp")
public class SkillsON extends OpMode {

    // Drive motors
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    
    // Hex Motor for rotation
    private DcMotor rotate;

    // Arm servos
    private Servo elbow, wrist, hand;

    // Mode control
    private boolean armMode = false;
    private boolean lastA = false;

    // Constants for rotation (Rev Core Hex = 288 ticks per rev)
    final double TICKS_PER_DEGREE = 288.0 / 360.0;
    final int POS_190 = (int)(190 * TICKS_PER_DEGREE);

    // Servo positions
    double elbowPos = 0.5;
    double wristPos = 0.5;
    double handPos = 0.5;

    @Override
    public void init() {

        // Motors
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        // Hex Motor Initialization
        rotate = hardwareMap.get(DcMotor.class, "rotate");
        rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotate.setTargetPosition(0);
        rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotate.setPower(0.5);

        // Flipped directions to fix inversion
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Set motors to BRAKE
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Servos
        elbow = hardwareMap.get(Servo.class, "elbow");
        wrist = hardwareMap.get(Servo.class, "wrist");
        hand  = hardwareMap.get(Servo.class, "hand");
    }

    @Override
    public void loop() {

        // Toggle mode with A button
        if (gamepad1.a && !lastA) {
            armMode = !armMode;
        }
        lastA = gamepad1.a;

        if (!armMode) {
            // ===== DRIVE MODE =====
            double y  = -gamepad1.left_stick_y;
            double x  = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double fl = y + x + rx;
            double bl = y - x + rx;
            double fr = y - x - rx;
            double br = y + x - rx;

            double max = Math.max(Math.abs(fl),
                    Math.max(Math.abs(bl),
                            Math.max(Math.abs(fr), Math.abs(br))));

            double maxPower = gamepad1.x ? 1.0 : 0.5;

            if (max > maxPower) {
                double scale = maxPower / max;
                fl *= scale;
                bl *= scale;
                fr *= scale;
                br *= scale;
            }

            frontLeft.setPower(fl);
            backLeft.setPower(bl);
            frontRight.setPower(fr);
            backRight.setPower(br);

        } else {
            // ===== ARM MODE =====

            // Stop driving
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // 1. Elbow Control (Left Stick Y)
            if (Math.abs(gamepad1.left_stick_y) > 0.05) {
                elbowPos += -gamepad1.left_stick_y * 0.01;
            }
            elbowPos = Math.max(0, Math.min(1, elbowPos));
            elbow.setPosition(elbowPos);

            // 2. Wrist Control (Right Stick Y)
            if (Math.abs(gamepad1.right_stick_y) > 0.05) {
                wristPos += -gamepad1.right_stick_y * 0.01;
            }
            wristPos = Math.max(0, Math.min(1, wristPos));
            wrist.setPosition(wristPos);

            // 3. Rotation Control (D-pad)
            if (gamepad1.dpad_left) {
                rotate.setTargetPosition(-POS_190);
            } else if (gamepad1.dpad_right) {
                rotate.setTargetPosition(POS_190);
            } else if (gamepad1.dpad_down) {
                rotate.setTargetPosition(0);
            }
            rotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rotate.setPower(0.5);

            // 4. Hand Control (Bumpers)
            if (gamepad1.left_bumper) {
                handPos = 0.0; // open
            }
            if (gamepad1.right_bumper) {
                handPos = 1.0; // close
            }
            hand.setPosition(handPos);
        }

        telemetry.addData("Mode", armMode ? "ARM" : "DRIVE");
        telemetry.addData("Rotate Pos", rotate.getCurrentPosition());
        telemetry.addData("Elbow Pos", elbowPos);
        telemetry.addData("Wrist Pos", wristPos);
        telemetry.update();
    }
}