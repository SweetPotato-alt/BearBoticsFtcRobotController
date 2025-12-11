package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="Two Driver TeleOP with IMU", group ="TeleOP")
public class twoPlayerIMU extends LinearOpMode {

    //Hardware
    private DcMotor left, right, launcher;
    private CRServo feeder, leftIndex, rightIndex;
    private DistanceSensor distance;
    private IMU imu;

    //Angle Hold
    private double targetAngle = 0;
    private boolean angleHoldEnabled = false;

    //Precision
    private final double PRECISION_SCALE = 0.3;

    //Toggle
    boolean flywheelState = false;
    boolean lastFlywheelButton = false;
    boolean parkingMode = false;
    boolean lastParking = false;

    @Override
    public void runOpMode() {

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

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );
        imu.initialize(new IMU.Parameters(orientation));

        waitForStart();

        while(opModeIsActive()) {

            // -----------------------
            // Gamepad 1 (Driver) - Driving + Angle Hold
            // -----------------------
            float drive = gamepad1.left_stick_y;
            float turn = gamepad1.left_stick_x;

            double currentAngle = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            // Toggle precision/parking mode
            boolean currentParking = gamepad1.touchpad;
            if(currentParking && !lastParking){
                parkingMode = !parkingMode;
            }
            lastParking = currentParking;

            // Angle hold toggle
            if (gamepad1.y) {
                if (!angleHoldEnabled) {
                    targetAngle = currentAngle;
                    angleHoldEnabled = true;
                }
            } else {
                angleHoldEnabled = false;
            }

            // Apply angle hold correction if enabled
            if (angleHoldEnabled) {
                double error = normalize(targetAngle - currentAngle);
                float correctionTurn = (float)(0.01 * error);
                correctionTurn = Math.max(Math.min(correctionTurn, 0.5f), -0.5f);

                float correctionDrive = drive;
                if (parkingMode) correctionDrive *= PRECISION_SCALE;

                left.setPower(correctionDrive - correctionTurn);
                right.setPower(correctionDrive + correctionTurn);
            } else {
                // Normal drive mode
                if (parkingMode) {
                    drive *= PRECISION_SCALE;
                    turn *= PRECISION_SCALE;
                }
                float leftPower = drive - turn;
                float rightPower = drive + turn;

                leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
                rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

                left.setPower(leftPower);
                right.setPower(rightPower);
            }

            // -----------------------
            // Gamepad 2 (Shooter)
            // -----------------------

            //Launcher toggle
            boolean currentFlywheel = gamepad2.left_bumper;
            if (currentFlywheel && !lastFlywheelButton){
                flywheelState = !flywheelState;
                launcher.setPower(flywheelState ? 1.0 : 0.0);
            }
            lastFlywheelButton = currentFlywheel;

            //Feeder toggle
            float feederInput = -gamepad2.right_stick_y;
            if (Math.abs(feederInput) < 0.1) feederInput = 0;
            feeder.setPower(feederInput);

            //Index toggle
            if (gamepad2.right_bumper){
                leftIndex.setPower(-1.0);
                rightIndex.setPower(1.0);
            } else {
                leftIndex.setPower(0.0);
                rightIndex.setPower(0.0);
            }

            // -----------------------
            // Telemetry
            // -----------------------
            telemetry.addData("Drive Mode", parkingMode ? "Precision" : "Full");
            telemetry.addData("Angle Hold Enabled", angleHoldEnabled);
            telemetry.addData("Target Angle", targetAngle);
            telemetry.addData("Current Angle", currentAngle);
            telemetry.update();
        }
    }

    // --- NORMALIZE ANGLE TO (-180, 180] ---
    private double normalize(double angle) {
        while (angle > 180) angle -= 360;
        while (angle <= -180) angle += 360;
        return angle;
    }
}
