package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Blue Auto - Backup to Wall", group = "Autonomous")
public class Autonomousblue extends LinearOpMode {

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

        telemetry.addLine("Ready on BLUE side — will back up to wall");
        telemetry.update();

        // wait for the start button
        waitForStart();

        if (opModeIsActive()) {
            //backup
            launcher.setPower(1.0);
            sleep(500);
            telemetry.addLine("Backup to launching position");
            telemetry.update();
            left.setPower(1.0);
            right.setPower(1.0);
            sleep(200);
            left.setPower(0.0);
            right.setPower(0.0);

            //launch
            telemetry.addLine("Launching");
            telemetry.update();
            sleep(100);
            feeder.setPower(1.0);
            leftindex.setPower(-1.0);
            rightindex.setPower(1.0);
            sleep(10000);

            leftindex.setPower(0.0);
            rightindex.setPower(0.0);
            feeder.setPower(0.0);
            launcher.setPower(0.0);


            //turn
            telemetry.addLine("Turning...");
            telemetry.update();
            left.setPower(0.7);
            right.setPower(0.3);
            sleep(2600);  // Adjust for your robot’s speed and distance

            //backup
            telemetry.addLine("Backing up...");
            telemetry.update();
            left.setPower(1);
            right.setPower(1);
            sleep(2900);

            //turn
            telemetry.addLine("Turning...");
            telemetry.update();
            left.setPower(-0.5);
            right.setPower(0.5);
            sleep(1300);

            //backup
            telemetry.addLine("Backing up...");
            telemetry.update();
            left.setPower(-1);
            right.setPower(-1);
            sleep(1500);

            //turn
            telemetry.addLine("Backing up...");
            telemetry.update();
            left.setPower(-0.5);
            right.setPower(0.5);
            sleep(1000);

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
