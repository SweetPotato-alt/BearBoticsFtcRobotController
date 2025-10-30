package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.Servo;



@TeleOp

public class Robotcoding extends OpMode {



    DcMotor left, right, arm;

    Servo myServo;



    double servoPosition = 0.5; // Start at center

    final double SERVO_INCREMENT = 0.01; // Increment value per press



    boolean dpadUpPressedLast = false;

    boolean dpadDownPressedLast = false;



    @Override

    public void init() {

        // Initialize drive motors

        left = hardwareMap.get(DcMotor.class, "left");

        left.setDirection(DcMotorSimple.Direction.REVERSE);



        right = hardwareMap.get(DcMotor.class, "right");

        right.setDirection(DcMotorSimple.Direction.FORWARD);



        // Initialize arm motor

        arm = hardwareMap.get(DcMotor.class, "arm");

        arm.setDirection(DcMotorSimple.Direction.FORWARD);



        // Initialize servo

        myServo = hardwareMap.get(Servo.class, "servo");

        myServo.setPosition(servoPosition);

    }



    @Override

    public void loop() {

        // Arcade Drive using left joystick

        float drive = gamepad1.left_stick_y;

        float turn = -gamepad1.left_stick_x;



        float leftPower = drive + turn;

        float rightPower = drive - turn;



        leftPower = Math.max(-1.0f, Math.min(1.0f, leftPower));

        rightPower = Math.max(-1.0f, Math.min(1.0f, rightPower));



        left.setPower(leftPower);

        right.setPower(rightPower);



        // Arm control using right joystick Y-axis

        arm.setPower(gamepad1.right_stick_y);



        // D-Pad control for servo

        if (gamepad1.dpad_up && !dpadUpPressedLast) {

            servoPosition += SERVO_INCREMENT;

        }

        if (gamepad1.dpad_down && !dpadDownPressedLast) {

            servoPosition -= SERVO_INCREMENT;

        }



        // Save current button states

        dpadUpPressedLast = gamepad1.dpad_up;

        dpadDownPressedLast = gamepad1.dpad_down;



        // Clamp position to [0.0, 1.0]

        servoPosition = Math.max(0.0, Math.min(1.0, servoPosition));

        myServo.setPosition(servoPosition);



        // Telemetry

        telemetry.addData("Drive", drive);

        telemetry.addData("Turn", turn);

        telemetry.addData("Left Power", leftPower);

        telemetry.addData("Right Power", rightPower);

        telemetry.addData("Arm Power", gamepad1.right_stick_y);

        telemetry.addData("Servo Position", servoPosition);

        telemetry.update();

    }

}

