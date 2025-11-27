package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class sensortest extends OpMode{
    private DistanceSensor distance;
    private NormalizedColorSensor color;

    @Override
    public void init() {
        distance = hardwareMap.get(DistanceSensor.class, "distanceSensor");
        color = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
    }

    @Override
    public void loop(){
            NormalizedRGBA colors = color.getNormalizedColors();
            double dist = distance.getDistance(DistanceUnit.CM);

        int r = (int)(colors.red * 255);
        int g = (int)(colors.green * 255);
        int b = (int)(colors.blue * 255);

        //to learn artifacts' color
        float aa = colors.red;
        float bb = colors.green;
        float cc = colors.blue;
        float dd = colors.alpha;

        //convert to HSV
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        float hue = hsv[0];

        String detectedColor = "Unknown";

        if ((hue >= 0 && hue < 30) || (hue > 330 && hue <= 360)) detectedColor = "Red";
        else if (hue >= 30 && hue < 60) detectedColor = "Orange";
        else if (hue >= 60 && hue < 90) detectedColor = "Yellow";
        else if (hue >= 90 && hue < 150) detectedColor = "Green";
        else if (hue >= 150 && hue < 210) detectedColor = "Cyan";
        else if (hue >= 210 && hue < 270) detectedColor = "Blue";
        else if (hue >= 270 && hue < 330) detectedColor = "Purple";

        telemetry.addData("Distance", "%.2f", dist);
        telemetry.addData("Hue", hue);
        telemetry.addData("Detected color", detectedColor);
        telemetry.addLine("Raw data: ");
        telemetry.addData("Red", "%.3f", aa);
        telemetry.addData("Green", "%.3f", bb);
        telemetry.addData("Blue", "%.3f", cc);
        telemetry.addData("Alpha", "%.3f", dd);
        telemetry.update();

    }


}
