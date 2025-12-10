package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


@TeleOp
public class TeleOpwithIMU extends OpMode {

    DcMotor left, right, launcher;
    CRServo feeder;
    CRServo leftIndex, rightIndex;
    DistanceSensor distance;
    private IMU imu;
    private DistanceSensor distSensor;
    private Orientation angles;
    boolean launcherOn = false;
    boolean indexActive = false;
    boolean aPressedLast = false;
    private double targetAngle = 0;
    private boolean holdAngle = false;
    //boolean xPressedLast = false;

    @Override
    public void init() {
        //drive motors
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        //launcher
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);
        launcher.setPower(0);

        //feeder
        feeder = hardwareMap.get(CRServo.class, "feeder");
        feeder.setPower(0);

        //index
        leftIndex = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");
        leftIndex.setPower(0);
        rightIndex.setPower(0);

        //sensors
        distance = hardwareMap.get(DistanceSensor.class, "distanceSensor");

        //Gyro
        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

        imu.initialize(new IMU.Parameters(orientation));

    }

    @Override
    public void loop() {
        //sensors + gyro
        double dist = distance.getDistance(DistanceUnit.CM);

        //drive control
        float drive = gamepad1.left_stick_y;
        float turn = gamepad1.left_stick_x;


        double angles = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double currentAngle = angles;


        if (gamepad1.y) {
            if (!holdAngle) {
                targetAngle = currentAngle;
                holdAngle = true;
            }

            double error = (int) Math.floor(targetAngle) - Math.floor(currentAngle);
            turn = (float)(0.01 * error);
            turn = Math.max(Math.min(turn, 0.5f), -0.5f);

            drive = -0.5f;

            if (dist <= 80) {
                drive = 0;
                //turn  = 0;
            }
        } else {
            holdAngle = false;
        }

        float leftPower = drive - turn;
        float rightPower = drive + turn;

        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

        left.setPower(leftPower);
        right.setPower(rightPower);

        //Launcher toggle - a button
        if (gamepad1.a && !aPressedLast) {
            launcherOn = !launcherOn;
            launcher.setPower(launcherOn ? 1.0 : 0.0);
        }
        aPressedLast = gamepad1.a;

        //feeder
        float feederInput = -gamepad1.right_stick_y;  // invert so pushing up = positive power

        //deadzone
        if (Math.abs(feederInput) < 0.1) {
            feederInput = 0;
        }
        feeder.setPower(feederInput);
        //if (gamepad1.y){
        //   feeder.setPower(1);
        //}
        //else {
        //    feeder.setPower(0);
        //}

        // --- Index toggle (X button) ---
//        if (gamepad1.right_bumper && !xPressedLast) {
//            indexActive = !indexActive;
//            double power = indexActive ? 1.0 : 0.0;
//
//            // If one servo spins the wrong way, flip one power
//            leftIndex.setPower(-power);
//            rightIndex.setPower(power);
//        }
//        xPressedLast = gamepad1.right_bumper;
        if (gamepad1.right_bumper){
            leftIndex.setPower(-1.0);
            rightIndex.setPower(1.0);
        }
        else {
            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
        }

        // --- Telemetry ---
        telemetry.addData("Drive", "L: %.2f  R: %.2f", leftPower, rightPower);
        telemetry.addData("Launcher", launcherOn ? "ON" : "OFF");
        telemetry.addData("Index", indexActive ? "SPINNING" : "STOPPED");
        telemetry.addData("Distance", "%.2f", dist);
        telemetry.addData("Current Angle", currentAngle);
        telemetry.addData("Target Angle", targetAngle);
        telemetry.update();
    }
}
