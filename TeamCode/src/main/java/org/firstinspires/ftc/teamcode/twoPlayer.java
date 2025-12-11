package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="twoPlayer", group ="TeleOP")
public class twoPlayer extends LinearOpMode {

    //Hardware Declarations
    private DcMotor left;
    private DcMotor right;

    private DcMotor launcher;
    private CRServo feeder;
    private CRServo leftIndex;
    private CRServo rightIndex;


    //Angle Hold Variables
    private double targetAngle = 0;
    private boolean angleHoldEnabled = false;

    //Driver Variables
    private final double PRECISION_SCALE = 0.3; //Level of parking mode motor power

    //Toggle State Variables
    boolean flywheelState = false;
    boolean lastFlywheelButton = false;
    boolean parkingMode = false;
    boolean lastParking = false;

    @Override
    public void runOpMode(){
        //Hardware Mapping
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        launcher = hardwareMap.get(DcMotor.class, "launcher");
        feeder = hardwareMap.get(CRServo.class, "feeder");
        leftIndex = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");


        waitForStart();

        while(opModeIsActive()){
            //-----------------------------
            //Driver 1 - Driving Controls
            //-----------------------------
            float drive = gamepad1.left_stick_y;
            float turn = gamepad1.left_stick_x;

            float leftPower = drive - turn;
            float rightPower = drive + turn;

            leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));
            rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));

            left.setPower(leftPower);
            right.setPower(rightPower);

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

            left.setPower(leftPower);
            right.setPower(rightPower);

            //-----------------------------
            //Driver 2 - Shooting Controls
            //-----------------------------

            boolean currentFlywheel = gamepad2.left_bumper;
            if (currentFlywheel && !lastFlywheelButton){
                flywheelState = !flywheelState;
                launcher.setPower(flywheelState ? 1.0 : 0.0);
            }
            lastFlywheelButton = currentFlywheel;

            float feederInput = -gamepad2.right_stick_y;
            if (Math.abs(feederInput) < 0.1) {
                feederInput = 0;
            }
            feeder.setPower(feederInput);


            if (gamepad2.right_bumper){
                leftIndex.setPower(-1.0);
                rightIndex.setPower(1.0);
            }
            else {
                leftIndex.setPower(0.0);
                rightIndex.setPower(0.0);
            }

            //Telemetry
            telemetry.addData("Driver Mode", parkingMode ? "Precision" : "Full");
            telemetry.addData("Target Angle", targetAngle);
            telemetry.addData("Angle Hold Enabled", angleHoldEnabled);
            telemetry.update();
        }
    }
}

