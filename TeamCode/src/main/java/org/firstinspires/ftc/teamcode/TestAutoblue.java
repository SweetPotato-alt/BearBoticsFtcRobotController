package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "BlueTest", group = "Autonomous")
public class TestAutoblue extends LinearOpMode {

    // Declare motors
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor launcher = null;

    // Declare CRServos
    private CRServo feeder = null;
    private CRServo leftindex = null;
    private CRServo rightindex = null;

    @Override
    public void runOpMode() {

        //define
        left = hardwareMap.get(DcMotor.class, "left");
        right = hardwareMap.get(DcMotor.class, "right");
        launcher = hardwareMap.get(DcMotor.class, "launcher");

        feeder = hardwareMap.get(CRServo.class, "feeder");
        leftindex = hardwareMap.get(CRServo.class, "leftindex");
        rightindex = hardwareMap.get(CRServo.class, "rightindex");

        //motor define
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);

        //servo define
        feeder.setDirection(DcMotorSimple.Direction.FORWARD);
        leftindex.setDirection(DcMotorSimple.Direction.FORWARD);
        rightindex.setDirection(DcMotorSimple.Direction.REVERSE);

        //initial stop
        left.setPower(0);
        right.setPower(0);
        launcher.setPower(0);
        feeder.setPower(0);
        leftindex.setPower(0);
        rightindex.setPower(0);

        telemetry.addLine("Test");
        telemetry.update();

        // wait for the start button
        waitForStart();

        if (opModeIsActive()) {
            //backup
            launcher.setPower(1.0);
            sleep(1500);
            telemetry.addLine("Backup to launching position");
            telemetry.update();
            //left.setPower(0);
            right.setPower(0.5);
            sleep(250);
            left.setPower(0.5);
            right.setPower(0.5);
            sleep(150);
            left.setPower(0.0);
            right.setPower(0.0);


            //launch
            telemetry.addLine("Launching");
            telemetry.update();
            feeder.setPower(1.0);
            sleep(3000);
            leftindex.setPower(-1.0);
            rightindex.setPower(1.0);
            sleep(5000);
            leftindex.setPower(0.0);
            rightindex.setPower(0.0);
            sleep(10);
            leftindex.setPower(0.5);
            rightindex.setPower(-0.5);
            sleep(100);
            leftindex.setPower(0.0);
            rightindex.setPower(0.0);
            sleep(10);
            leftindex.setPower(-1.0);
            rightindex.setPower(1.0);
            sleep(8000);

            leftindex.setPower(0.0);
            rightindex.setPower(0.0);
            feeder.setPower(0.0);
            launcher.setPower(0.0);


            //backup
            telemetry.addLine("Backing up...");
            telemetry.update();
            left.setPower(1);
            right.setPower(1);
            sleep(4000);

            //turn
            telemetry.addLine("Turning...");
            telemetry.update();
            left.setPower(-0.5);
            right.setPower(0.5);
            sleep(1500);

            //backup
            telemetry.addLine("Backing up...");
            telemetry.update();
            left.setPower(-1);
            right.setPower(-1);
            sleep(3000);

            //stop
            left.setPower(0.0);
            right.setPower(0.0);
            telemetry.addLine("Stopped");
            telemetry.update();


            //stop the servo
            feeder.setPower(0);
            leftindex.setPower(0);
            rightindex.setPower(0);

            telemetry.addLine("Done.");
            telemetry.update();
        }
    }
}
