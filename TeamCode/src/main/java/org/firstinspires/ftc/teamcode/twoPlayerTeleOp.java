package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DistanceSensor;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name="Two Driver TeleOP", group ="TeleOP")
public class twoPlayerTeleOp extends LinearOpMode {

    //Hardware Declarations
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private DcMotor flywheel;
    private DcMotor feeder;
    private DcMotor indexer;

    private BNO055IMU imu;

    //Angle Hold Variables
    private double targetAngle = 0;
    private boolean angleHoldEnabled = false;

    //Driver Variables
    private final double PRECISION_SCALE = 0.5; //Level of parking mode motor power

    //Toggle State Variables
    boolean flywheelState = false;
    boolean lastFlywheelButton = false;
    boolean parkingMode = false;
    boolean lastParking = false;

    @Override
    public void runOpMode(){
        //Hardware Mapping
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        feeder = hardwareMap.get(DcMotor.class, "feeder");
        indexer = hardwareMap.get(DcMotor.class, "indexer");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(params);

        telemetry.addLine("IMU Initialized");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            //-----------------------------
            //Driver 1 - Driving Controls
            //-----------------------------
            double leftPower = gamepad1.left_stick_y;
            double rightPower = gamepad1.right_stick_y;

            //Toggling touchpad parking mode on/off
            boolean currentParking = gamepad1.touchpad;

            if(currentParking&& !lastParking){
                parkingMode = !parkingMode;
            }

            lastParking = currentParking;

            if(parkingMode){
                leftPower *=PRECISION_SCALE;
                rightPower *= PRECISION_SCALE;
            }

            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);

            //-----------------------------
            //Driver 2 - Shooting Controls
            //-----------------------------

            //Flywheel toggle (left bumper)
            boolean currentFlywheel = gamepad2.left_bumper;
            if (currentFlywheel && !lastFlywheelButton){
                flywheelState = !flywheelState;
            }
            lastFlywheelButton = currentFlywheel;

            //Feeder toggle (right bumper)
            if(gamepad2.right_bumper){
                feeder.setPower(1.0);
            } else {
                feeder.setPower(0);
            }

            //Angle Hold Toggle (Triangle)
            if (gamepad2.triangle){
                if (!angleHoldEnabled) {
                    targetAngle = getHeading();
                    angleHoldEnabled = true;
                }
            } else {
                angleHoldEnabled = false;
            }

            //Indexer Control (right joystick)
            double indexerPower = gamepad2.right_stick_y;
            indexer.setPower(indexerPower);

            //Angle Hold Logic

            if (angleHoldEnabled) {
                double currentAngle = getHeading();
                double error = targetAngle - currentAngle;

                double kP = 0.02; //proportional constant
                double correction = error * kP;

                leftMotor.setPower(leftPower + correction);
                rightMotor.setPower(rightPower - correction);
            }

            //Telemetry
            telemetry.addData("Driver Mode", parkingMode ? "Precision" : "Full");
            telemetry.addData("Current Angle", getHeading());
            telemetry.addData("Target Angle", targetAngle);
            telemetry.addData("Angle Hold Enabled", angleHoldEnabled);
            telemetry.update();
        }
    }

    private double getHeading(){
        Orientation orientaion = imu.getAngularOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.DEGREES
        );
        return orientaion.firstAngle;
    }
}


