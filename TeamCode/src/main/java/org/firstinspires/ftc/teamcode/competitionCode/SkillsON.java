package org.firstinspires.ftc.teamcode.competitionCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MecanumDrive", group="TeleOp")
public class SkillsON extends OpMode {

    // Drive motors
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // Arm servos
    private Servo elbow, wrist, hand;

    // Mode control
    private boolean armMode = false;
    private boolean lastA = false;

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

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Servos (names must match config)
        elbow = hardwareMap.get(Servo.class, "elbow");
        wrist = hardwareMap.get(Servo.class, "wrist");
        hand  = hardwareMap.get(Servo.class, "hand");
    }

    @Override
    public void loop() {

        // Toggle mode with A button (edge detection)
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

            // Scale so max is 0.5
            if (max > 0.5) {
                double scale = 0.5 / max;
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

            // Stop driving while using arm
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);

            // Move arm up/down
            double armInput = -gamepad1.left_stick_y * 0.01;

            elbowPos += armInput;
            elbowPos = Math.max(0, Math.min(1, elbowPos));

            elbow.setPosition(elbowPos);

            // Wrist control (right stick Y)
            wristPos += -gamepad1.right_stick_y * 0.01;
            wristPos = Math.max(0, Math.min(1, wristPos));
            wrist.setPosition(wristPos);

            // Hand open/close (D-pad)
            if (gamepad1.dpad_left) {
                handPos = 0.0; // open
            }
            if (gamepad1.dpad_right) {
                handPos = 1.0; // close
            }
            hand.setPosition(handPos);
        }

        telemetry.addData("Mode", armMode ? "ARM" : "DRIVE");
        telemetry.update();
    }
}