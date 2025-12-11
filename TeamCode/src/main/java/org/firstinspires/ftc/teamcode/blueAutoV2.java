package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="BackupThenLaunchAuto", group="Autonomous")
public class blueAutoV2 extends LinearOpMode {

    // --- Hardware Declarations ---
    private DcMotor left, right, launcher;
    private CRServo feeder, leftIndex, rightIndex;
    private DistanceSensor distance;
    private IMU imu;

    // --- Motor and wheel constants ---
    private static final double TICKS_PER_MOTOR_REV = 460.8; // motor encoder ticks per rev
    private static final double GEAR_RATIO = 19.2; // motor gearbox ratio
    private static final double WHEEL_DIAMETER_INCH = 4.0; // wheel diameter
    private static final double COUNTS_PER_INCH = (TICKS_PER_MOTOR_REV * GEAR_RATIO) / (Math.PI * WHEEL_DIAMETER_INCH);

    // --- Angle hold / IMU ---
    private double targetAngle = 0;
    private boolean angleHoldEnabled = false;

    @Override
    public void runOpMode() {

        // --- Hardware Mapping ---
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        launcher = hardwareMap.get(DcMotor.class, "launcher");
        feeder = hardwareMap.get(CRServo.class, "feeder");
        leftIndex = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");

        distance = hardwareMap.get(DistanceSensor.class, "distanceSensor");
        imu = hardwareMap.get(IMU.class, "imu");

        // Initialize IMU orientation
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );
        imu.initialize(new IMU.Parameters(orientation));

        // Reset motor encoders
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Ready to start");
        telemetry.update();

        // Wait for start button
        waitForStart();

        if (opModeIsActive()) {

            // === STEP 1: Back up from the wall ===
            telemetry.addLine("Backing up from wall...");
            telemetry.update();

            // Set motors to move backwards slowly
            launcher.setPower(1.0);
            left.setPower(0.3);
            right.setPower(0.3);

            // Keep moving until distance sensor reads 70cm
            while (opModeIsActive() && distance.getDistance(DistanceUnit.CM) < 25) {
                telemetry.addData("Distance to wall", distance.getDistance(DistanceUnit.CM));
                telemetry.update();
            }


            // Stop motors
            left.setPower(0.0);
            right.setPower(0.0);
            telemetry.addLine("Backup complete");
            telemetry.update();
            sleep(500);

            // === STEP 2: Launch sequence ===
            telemetry.addLine("Launching...");
            telemetry.update();

            // Feeder starts
            feeder.setPower(-1.0);
            sleep(500);

            // Reverse feeder briefly
            feeder.setPower(1.0);
            sleep(3000);

            // First indexer spin
            leftIndex.setPower(-1.0);
            rightIndex.setPower(1.0);
            sleep(1200);

            // Stop indexers briefly
            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
            sleep(1000);

            // Second indexer spin
            leftIndex.setPower(-1.0);
            rightIndex.setPower(1.0);
            sleep(10000);

            // Small "wiggle" to settle robot
//            left.setPower(-1.0);
//            right.setPower(-1.0);
//            sleep(100);
//            left.setPower(1.0);
//            right.setPower(1.0);
//            sleep(100);
//            left.setPower(-1.0);
//            right.setPower(-1.0);
//            sleep(100);
//            left.setPower(1.0);
//            right.setPower(1.0);
//            sleep(100);
//            left.setPower(0.0);
//            right.setPower(0.0);
//            sleep(500);

            // Stop all servos and launcher
            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
            feeder.setPower(0.0);
            launcher.setPower(0.0);

            moveStraight(2, 0.3);

            // === STEP 3: Autonomous driving after launch ===
            // Example: Turn left 120 degrees using IMU
            turnIMU(120,  0.4);

            // Example: Move forward 24 inches using encoders
            moveStraight(24, 0.5);

            telemetry.addLine("Autonomous sequence complete");
            telemetry.update();
        }
    }

    // --- Helper: move straight using encoder counts ---
    private void moveStraight(double inches, double power) {
        int moveCounts = (int)(inches * COUNTS_PER_INCH);
        int leftTarget = left.getCurrentPosition() + moveCounts;
        int rightTarget = right.getCurrentPosition() + moveCounts;

        left.setTargetPosition(leftTarget);
        right.setTargetPosition(rightTarget);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(Math.abs(power));
        right.setPower(Math.abs(power));

        // Wait until motors reach target
        while (opModeIsActive() && (left.isBusy() || right.isBusy())) {
            telemetry.addData("LeftPos", left.getCurrentPosition());
            telemetry.addData("RightPos", right.getCurrentPosition());
            telemetry.update();
        }

        // Stop motors
        left.setPower(0);
        right.setPower(0);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // --- Helper: turn using IMU ---
    private void turnIMU(double targetAngle, double power) {
        double startAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double desiredAngle = normalizeAngle(startAngle + targetAngle);

        while (opModeIsActive()) {
            double currentAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double error = normalizeAngle(desiredAngle - currentAngle);

            if (Math.abs(error) < 2.0) break; // stop when within 2 degrees

            double turnPower = Math.signum(error) * power;
            left.setPower(-turnPower);
            right.setPower(turnPower);

            telemetry.addData("Current Angle", currentAngle);
            telemetry.addData("Target Angle", desiredAngle);
            telemetry.addData("Error", error);
            telemetry.update();
        }

        left.setPower(0);
        right.setPower(0);
    }

    // --- Normalize angle to (-180, 180] ---
    private double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle <= -180) angle += 360;
        return angle;
    }
}
