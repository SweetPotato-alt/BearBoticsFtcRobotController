package org.firstinspires.ftc.teamcode.competitionCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MecanumDrive", group="TeleOp")
public class SkillsON extends OpMode {

    // Drive motors
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    
    // Arm motors and servos
    private DcMotor elbow, armRotation;
    private Servo wrist, hand;

    // Servo positions
    private double wristPos = 0.5;
    private boolean handClosed = false;
    private boolean bPressedLast = false;

    // Motor targets for "holding" position
    private int elbowTarget = 0;
    private int rotationTarget = 0;

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
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        armRotation = hardwareMap.get(DcMotor.class, "arm_rotation");
        wrist = hardwareMap.get(Servo.class, "wrist");
        hand = hardwareMap.get(Servo.class, "hand");

        // Initialize motors to use encoders so they can "hold" position
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        elbow.setTargetPosition(0);
        armRotation.setTargetPosition(0);
        
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        // Power here sets the speed limit for reaching the target
        elbow.setPower(0.6);
        armRotation.setPower(0.5);

        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRotation.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

        // ===== ARM CONTROLS (Hold Position) =====
        
        // Elbow: D-pad Up/Down (changes the target position)
        if (gamepad1.dpad_up) {
            elbowTarget += 15; 
        } else if (gamepad1.dpad_down) {
            elbowTarget -= 15;
        }
        elbow.setTargetPosition(elbowTarget);

        // Arm Rotation: D-pad Left/Right (changes the target position)
        if (gamepad1.dpad_right) {
            rotationTarget += 15;
        } else if (gamepad1.dpad_left) {
            rotationTarget -= 15;
        }
        armRotation.setTargetPosition(rotationTarget);

        // Wrist: Y (Up) and A (Down)
        if (gamepad1.y) {
            wristPos += 0.005;
        } else if (gamepad1.a) {
            wristPos -= 0.005;
        }
        wristPos = Math.max(0, Math.min(1, wristPos));
        wrist.setPosition(wristPos);

        // Hand: B (Toggle Close/Open)
        if (gamepad1.b && !bPressedLast) {
            handClosed = !handClosed;
        }
        bPressedLast = gamepad1.b;

        if (handClosed) {
            hand.setPosition(1.0); // Closed
        } else {
            hand.setPosition(0.0); // Open
        }

        telemetry.addData("Status", "Running");
        telemetry.addData("Boost Mode (X)", gamepad1.x ? "ON" : "OFF");
        telemetry.addData("Elbow Target", elbowTarget);
        telemetry.addData("Wrist Pos", wristPos);
        telemetry.addData("Hand", handClosed ? "CLOSED" : "OPEN");
        telemetry.update();
    }
}
