package org.firstinspires.ftc.teamcode.competitionCode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="test", group="TeleOp")
public class test extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @java.lang.Override
    public void init() {
        // NOTE: Make sure these names match exactly what you have in the Robot Configuration on your Driver Hub
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left");
        backRight  = hardwareMap.get(DcMotor.class, "back_right");

        // Reverse one side so forward works correctly
        // Usually, the right side is reversed, but this depends on your build
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @java.lang.Override
    public void loop() {

        double fl = gamepad1.left_stick_y;
        double fr = gamepad1.right_stick_y;


        // Set motor power
        frontLeft.setPower(fl);
        frontRight.setPower(fr);
    }
}