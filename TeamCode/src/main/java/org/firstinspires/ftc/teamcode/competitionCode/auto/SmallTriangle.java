package org.firstinspires.ftc.teamcode.competitionCode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "RED triangle", group = "Autonomous")
public class SmallTriangle extends LinearOpMode {

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
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);

        //servo define
        feeder.setDirection(DcMotorSimple.Direction.FORWARD);
        leftindex.setDirection(DcMotorSimple.Direction.FORWARD);
        rightindex.setDirection(DcMotorSimple.Direction.FORWARD);

        //initial stop
        left.setPower(0);
        right.setPower(0);
        launcher.setPower(0);
        feeder.setPower(0);
        leftindex.setPower(0);
        rightindex.setPower(0);

        telemetry.addLine("Red Side Small Triangle- Ready; Press start.");
        telemetry.update();

        // wait for the start button
        waitForStart();

        if (opModeIsActive()) {
            telemetry.addLine("Backing up - outside of launch zone.");
            telemetry.update();

            left.setPower(0.5);
            right.setPower(0.5);
            sleep(500);

            left.setPower(0.0);
            right.setPower(0.0);

            telemetry.addLine("Done.");
            telemetry.update();
        }
    }
}
