package org.firstinspires.ftc.teamcode.competitionCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MecanumDrive", group="TeleOp")
public class SkillsON extends OpMode {

    // Drive motors
    private DcMotor frontLeft, frontRight, backLeft, backRight;

    // Arm hardware
    private DcMotor armRotation;
    private Servo elbow, wrist, hand;

    // Servo positions
    private double elbowPos = 0.0;
    private double wristPos = 0.0;
    private double handPos  = 0.7; // Start closed

    @Override
    public void init() {
        // Drive Motors
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Arm Hardware
        armRotation = hardwareMap.get(DcMotor.class, "arm_rotation");
        elbow = hardwareMap.get(Servo.class, "elbow");
        wrist = hardwareMap.get(Servo.class, "wrist");
        hand = hardwareMap.get(Servo.class, "hand");

        // IMPORTANT: switch to manual control mode
        armRotation.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armRotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initial Servo positions
        elbow.setPosition(elbowPos);
        wrist.setPosition(wristPos);
        hand.setPosition(handPos);
    }

    @Override
    public void loop() {

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


        // 25% normal speed, 100% when holding right trigger
        double maxPower = gamepad1.right_trigger > 0.1 ? 1.0 : 0.25;


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

        // ===== ARM CONTROLS =====

        // Elbow: D-pad Up/Down
        if (gamepad1.dpad_up) {
            elbowPos -= 0.0015;
        } else if (gamepad1.dpad_down) {
            elbowPos += 0.0015;
        }
        elbowPos = Math.max(0, Math.min(1, elbowPos));
        elbow.setPosition(elbowPos);

        // Arm Rotation: HOLD = move, RELEASE = stop instantly
        if (gamepad1.dpad_right) {
            armRotation.setPower(0.2);
        } else if (gamepad1.dpad_left) {
            armRotation.setPower(-0.2);
        } else {
            armRotation.setPower(0); // immediate stop
        }

        // Wrist: X (up), A (down)
        if (gamepad1.x) {
            wristPos += 0.0025;
        } else if (gamepad1.a) {
            wristPos -= 0.0025;
        }
        wristPos = Math.max(0, Math.min(1, wristPos));
        wrist.setPosition(wristPos);

        // Hand: Y (open), B (close)
        if (gamepad1.y) {
            handPos -= 0.0025;
        } else if (gamepad1.b) {
            handPos += 0.0025;
        }
        handPos = Math.max(0, Math.min(1, handPos));
        hand.setPosition(handPos);

        // Telemetry
        telemetry.addData("Status", "Running");
        telemetry.addData("Speed Mode", gamepad1.right_trigger > 0.1 ? "BOOST" : "NORMAL");
        telemetry.addData("Elbow Pos", elbowPos);
        telemetry.addData("Wrist Pos", wristPos);
        telemetry.addData("Hand Pos", handPos);
        telemetry.addData("Arm Power", armRotation.getPower());
        telemetry.update();
    }
}