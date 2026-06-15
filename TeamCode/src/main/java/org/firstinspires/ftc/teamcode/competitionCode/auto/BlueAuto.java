package org.firstinspires.ftc.teamcode.competitionCode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "BLUE", group = "Autonomous")
public class BlueAuto extends LinearOpMode {

    // Drive motors
    private DcMotor fleft = null;
    private DcMotor fright = null;
    private DcMotor rleft = null;
    private DcMotor rright = null;

    // Launcher
    private DcMotor launcher = null;

    // Servos
    private CRServo feeder = null;
    private CRServo leftIndex = null;
    private CRServo rightIndex = null;

    @Override
    public void runOpMode() {

        // Drive motors
        fleft = hardwareMap.get(DcMotor.class, "fleft");
        fright = hardwareMap.get(DcMotor.class, "fright");
        rleft = hardwareMap.get(DcMotor.class, "rleft");
        rright = hardwareMap.get(DcMotor.class, "rright");

        fleft.setDirection(DcMotorSimple.Direction.FORWARD);
        rleft.setDirection(DcMotorSimple.Direction.REVERSE);
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        rright.setDirection(DcMotorSimple.Direction.REVERSE);

        fleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rleft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rright.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Launcher
        launcher = hardwareMap.get(DcMotor.class, "launcher");
        launcher.setDirection(DcMotorSimple.Direction.FORWARD);
        launcher.setPower(0);

        // Feeder
        feeder = hardwareMap.get(CRServo.class, "feeder");
        feeder.setPower(0);

        // Index
        leftIndex = hardwareMap.get(CRServo.class, "leftindex");
        rightIndex = hardwareMap.get(CRServo.class, "rightindex");
        leftIndex.setPower(0);
        rightIndex.setPower(0);

        telemetry.addLine("Blue Side - Ready; Press start.");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {

            // Spin launcher
            launcher.setPower(1.0);
            sleep(1500);

            // Backup to launching position
            telemetry.addLine("Backup to launching position");
            telemetry.update();

            fleft.setPower(0.3);
            fright.setPower(0.0);
            rleft.setPower(0.3);
            rright.setPower(0.0);
            sleep(250);

            fleft.setPower(0.2);
            fright.setPower(0.2);
            rleft.setPower(0.2);
            rright.setPower(0.2);
            sleep(200);

            fleft.setPower(0.0);
            fright.setPower(0.0);
            rleft.setPower(0.0);
            rright.setPower(0.0);

            // Launch
            telemetry.addLine("Launching");
            telemetry.update();

            feeder.setPower(-1.0);
            sleep(500);

            feeder.setPower(1.0);
            sleep(3000);

            leftIndex.setPower(-1.0);
            rightIndex.setPower(1.0);
            sleep(1200);

            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
            sleep(1000);

            leftIndex.setPower(-1.0);
            rightIndex.setPower(1.0);
            sleep(7000);

            // Shake robot

            fleft.setPower(-0.2);
            fright.setPower(-0.2);
            rleft.setPower(-0.2);
            rright.setPower(-0.2);
            sleep(100);

            fleft.setPower(0.2);
            fright.setPower(0.2);
            rleft.setPower(0.2);
            rright.setPower(0.2);
            sleep(100);

            fleft.setPower(-0.2);
            fright.setPower(-0.2);
            rleft.setPower(-0.2);
            rright.setPower(-0.2);
            sleep(100);

            fleft.setPower(0.2);
            fright.setPower(0.2);
            rleft.setPower(0.2);
            rright.setPower(0.2);
            sleep(100);

            fleft.setPower(0.0);
            fright.setPower(0.0);
            rleft.setPower(0.0);
            rright.setPower(0.0);

            sleep(7000);

            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);
            feeder.setPower(0.0);
            launcher.setPower(0.0);

            // Backup
            telemetry.addLine("Backing up...");
            telemetry.update();

            fleft.setPower(0.3);
            fright.setPower(0.3);
            rleft.setPower(0.3);
            rright.setPower(0.3);
            sleep(1000);

            // Turn
            telemetry.addLine("Turning...");
            telemetry.update();

            fleft.setPower(-0.2);
            rleft.setPower(-0.2);
            fright.setPower(0.2);
            rright.setPower(0.2);
            sleep(1400);

            // Backup
            telemetry.addLine("Backing up...");
            telemetry.update();

            fleft.setPower(0.3);
            fright.setPower(0.3);
            rleft.setPower(0.3);
            rright.setPower(0.3);
            sleep(1500);

            // Stop
            fleft.setPower(0.0);
            fright.setPower(0.0);
            rleft.setPower(0.0);
            rright.setPower(0.0);

            feeder.setPower(0.0);
            leftIndex.setPower(0.0);
            rightIndex.setPower(0.0);

            telemetry.addLine("Done.");
            telemetry.update();
        }
    }
}