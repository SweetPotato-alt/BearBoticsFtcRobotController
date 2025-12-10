package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="IMU Angle Hold Test")
public class IMUAngleHoldTest extends LinearOpMode {

    private IMU imu;

    @Override
    public void runOpMode() {

        // Get IMU from config named: "imu"
        imu = hardwareMap.get(IMU.class, "imu");

        // Define Hub orientation on robot
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

        imu.initialize(new IMU.Parameters(orientation));

        waitForStart();

        while (opModeIsActive()) {

            double heading = imu.getRobotYawPitchRollAngles()
                    .getYaw(AngleUnit.DEGREES);

            telemetry.addData("Heading", heading);
            telemetry.update();
        }
    }
}
