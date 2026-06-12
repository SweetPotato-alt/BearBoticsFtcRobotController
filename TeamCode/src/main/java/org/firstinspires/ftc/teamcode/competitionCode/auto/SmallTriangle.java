package org.firstinspires.ftc.teamcode.competitionCode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Triangle", group = "Autonomous")
public class SmallTriangle extends LinearOpMode {

    // Declare motors
    DcMotor fleft, fright, rleft, rright;


    @Override
    public void runOpMode() {

        fleft  = hardwareMap.get(DcMotor.class, "fleft");
        fright = hardwareMap.get(DcMotor.class, "fright");
        rleft  = hardwareMap.get(DcMotor.class, "rleft");
        rright = hardwareMap.get(DcMotor.class, "rright");

        fleft.setDirection(DcMotorSimple.Direction.FORWARD);
        rleft.setDirection(DcMotorSimple.Direction.REVERSE);
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        rright.setDirection(DcMotorSimple.Direction.REVERSE);

        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        //initial stop
        fleft.setPower(0);
        fright.setPower(0);
        rleft.setPower(0);
        rright.setPower(0);

        telemetry.addLine("Red Side Small Triangle- Ready; Press start.");
        telemetry.update();

        // wait for the start button
        waitForStart();

        if (opModeIsActive()) {
            telemetry.addLine("Backing up - outside of launch zone.");
            telemetry.update();

            fleft.setPower(0.5);
            fright.setPower(0.5);
            rleft.setPower(0.5);
            rright.setPower(0.5);
            sleep(500);

            fleft.setPower(0);
            fright.setPower(0);
            rleft.setPower(0);
            rright.setPower(0);

            telemetry.addLine("Done.");
            telemetry.update();
        }
    }
}
