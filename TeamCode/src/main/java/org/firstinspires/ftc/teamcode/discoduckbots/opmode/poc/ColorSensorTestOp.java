package org.firstinspires.ftc.teamcode.discoduckbots.opmode.poc;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@TeleOp(name = "Color Sensor Test Op", group = "Sensor")
@Disabled
public class ColorSensorTestOp extends LinearOpMode {

    NormalizedColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        float[] hsvValues = new float[3];
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

        int redCount = 0;

        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(false);
        }

        waitForStart();

        boolean wasRed = false;

        while (opModeIsActive()){
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            boolean isRed = isRed(colors);

            if (isRed && !wasRed){
                redCount++;
            }
            wasRed = isRed;

            boolean isWhite = isWhite(colors);

            telemetry.addLine()
                    .addData("Is Red", isRed)
                    .addData("Is White", isWhite);

            telemetry.addLine()
                    .addData("a", "%.3f", colors.alpha)
                    .addData("r", "%.3f", colors.red)
                    .addData("g", "%.3f", colors.green)
                    .addData("b", "%.3f", colors.blue);

            telemetry.addLine()
                    .addData("Red Line Count", redCount);

            telemetry.update();

        }
    }

    private boolean isRed(NormalizedRGBA colors){

        if (colors.red > (colors.blue + colors.green)){
            return true;
        }

        return false;
    }

    private boolean isWhite(NormalizedRGBA colors){

        if (colors.red > .2 && colorsEqual(colors.red, colors.green, colors.blue, 0.075f)){
            return true;
        }

        return false;
    }


    private boolean colorsEqual(float color1, float color2, float color3, float tolerance){
        return Math.abs(color1-color2) < tolerance && Math.abs(color1-color3) < tolerance && Math.abs(color2-color3) < tolerance;
    }

}
