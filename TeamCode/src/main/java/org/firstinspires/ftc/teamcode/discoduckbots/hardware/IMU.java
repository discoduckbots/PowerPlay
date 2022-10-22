package org.firstinspires.ftc.teamcode.discoduckbots.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class IMU {

    private static final double MAX_GYRO_ADJUSTMENT = 0.15;

    private BNO055IMU imu;
    private Orientation angles;

    public IMU(BNO055IMU imu) {
        this.imu = imu;
    }

    public void initialize(){
        BNO055IMU.Parameters IMUParameters = new BNO055IMU.Parameters();
        IMUParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu.initialize(IMUParameters);
    }

    public boolean getisGyroCalibrated(){
        return imu.isGyroCalibrated();
    }

    public BNO055IMU.CalibrationStatus getCalibrationStatus(){
        return imu.getCalibrationStatus();
    }

    public double getIMUHeading() {
        double currentHeading;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        currentHeading = angles.firstAngle; //Because REV Hub is upside down
        if (currentHeading > 0) {
            currentHeading = 360 - currentHeading;
        } else {
            currentHeading = -currentHeading;
        }
        return currentHeading;
    }

    public double[] printAngles(){
        double[] values;

        values = new double[3];
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        values[0] = angles.firstAngle;
        values[1] = angles.secondAngle;
        values[2] = angles.thirdAngle;

        return values;
    }

    private double getHeadingError(double targetHeading, double currentHeading){
        if (targetHeading >= 180){
            currentHeading = currentHeading + (360 - targetHeading);
        }
        else{
            currentHeading = currentHeading - targetHeading;
        }

        targetHeading = 0;

        if (currentHeading >= 180.0){
            return 360 - currentHeading;
        }

        return  targetHeading - currentHeading;
    }

    public double computeHeadingAdjustment(double targetHeading){
        double currentHeading = getIMUHeading();
        double headingError = getHeadingError(targetHeading, currentHeading);
        boolean rotateLeft = headingError < 0;
        double adjustment = 0.0;

        headingError = Math.abs(headingError);

        if (headingError < 0.3){
            return 0.0;
        }

        adjustment = (Math.pow((headingError + 2) / 5, 2) + 2)/ 100;
        if (adjustment > MAX_GYRO_ADJUSTMENT){
            adjustment = MAX_GYRO_ADJUSTMENT;
        }

        if (rotateLeft){
            adjustment = adjustment * -1;
        }

        return adjustment;
    }

    public double calculateTurnHeading(double rotationDegrees){
        double resultHeading = getIMUHeading() + rotationDegrees;

        if (resultHeading >= 360){
            return resultHeading - 360;
        }
        else if (resultHeading < 0){
            return resultHeading + 360;
        }

        return resultHeading;
    }

    public double getDistanceToTargetHeading(double targetHeading, boolean rotateLeft){
        double currentHeading = getIMUHeading();

        if(!rotateLeft){
            if (currentHeading > 0){
                targetHeading = targetHeading + (360 - currentHeading);
                currentHeading = 0;
            }

            if (targetHeading - currentHeading >= 360){
                return targetHeading - currentHeading - 360;
            }

            return targetHeading - currentHeading;
        }


        if (currentHeading - targetHeading < 0){
            currentHeading = currentHeading + (360 - targetHeading);
            targetHeading = 0;
        }

        return currentHeading - targetHeading;
    }
}
