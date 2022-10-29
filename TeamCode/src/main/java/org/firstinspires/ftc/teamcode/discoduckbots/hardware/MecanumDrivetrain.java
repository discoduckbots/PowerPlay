package org.firstinspires.ftc.teamcode.discoduckbots.hardware;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.discoduckbots.control.PIDF;
import org.firstinspires.ftc.teamcode.discoduckbots.util.NumberUtility;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrivetrain implements DrivetrainInterface {
    private static final float ENCODER_CLICKS_FORWARD_1_INCH = 43.25f;//19.0f 18.75487911f;
    private static final float ENCODER_CLICKS_STRAFE_1_INCH = 44.325f;//25.8944908f;

    public static final int DIRECTION_FORWARD = 0;
    public static final int DIRECTION_REVERSE = 1;
    public static final int DIRECTION_STRAFE_RIGHT = 2;
    public static final int DIRECTION_STRAFE_LEFT = 3;

    private DcMotorEx mFrontLeft;
    private DcMotorEx mFrontRight;
    private DcMotorEx mBackLeft;
    private DcMotorEx mBackRight;
    private Telemetry mTelemetry;
    private LinearOpMode opMode;
    private IMU imu;
    private ColorSensor colorSensor;
    private PIDF headingPID;


    /**
     * @deprecated Use MecanumDrivetrain(telemetry, opMode, imu, colorSensor, frontLeft, frontRight, backLeft, backRight)
     *
     * Creates a mecanum motor using the 4 individual motors passed in as the arguments, but with no imu or color sensor
     * @param telemetry : Telemetry to send messages to the Driver Control
     * @param opMode : the OpMode this is being used in
     * @param frontLeft : Front left motor
     * @param frontRight : Front right motor
     * @param backLeft : Back left motor
     * @param backRight : Back right motor
     */
    public MecanumDrivetrain(Telemetry telemetry,
                             LinearOpMode opMode,
                             DcMotorEx frontLeft, DcMotorEx frontRight,
                             DcMotorEx backLeft, DcMotorEx backRight ) {
        this(telemetry, opMode, null, null, frontLeft, frontRight, backLeft, backRight);
    }

    /**
     * Creates a mecanum motor using the 4 individual motors passed in as the arguments
     * @param telemetry : Telemetry to send messages to the Driver Control
     * @param opMode : the OpMode this is being used in
     * @param imu : the Inertial Measurement Unit from the Rev/Control Hub
     * @param colorSensor : the NormalizedColorSensor
     * @param frontLeft : Front left motor
     * @param frontRight : Front right motor
     * @param backLeft : Back left motor
     * @param backRight : Back right motor
     */
    public MecanumDrivetrain(Telemetry telemetry,
                             LinearOpMode opMode,
                             IMU imu,
                             ColorSensor colorSensor,
                             DcMotorEx frontLeft, DcMotorEx frontRight,
                             DcMotorEx backLeft, DcMotorEx backRight ) {
        mTelemetry = telemetry;
        mFrontLeft = frontLeft;
        mFrontRight = frontRight;
        mBackLeft = backLeft;
        mBackRight = backRight;
        this.opMode = opMode;
        this.imu = imu;
        this.colorSensor = colorSensor;

        setMotorDirection(DIRECTION_FORWARD);

        mBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        headingPID = new PIDF(0.95, 0, 0.01, 0);
        headingPID.setPhase(false);
        headingPID.setPeakOutputForward(1);
        headingPID.setPeakOutputReverse(-1);
        headingPID.setContinuityRange(-Math.PI, Math.PI);
        headingPID.setContinuous(true);
        headingPID.setAtTargetThreshold(Math.toRadians(3));

        mFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        mFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



    }
    public void print() {
        Log.d("FTC", "backLeft: " + mBackLeft.getCurrentPosition() );
        Log.d("FTC", "backRight: " + mBackRight.getCurrentPosition() );
        Log.d("FTC", "frontLeft: " + mFrontLeft.getCurrentPosition() );
        Log.d("FTC", "frontRight: " + mFrontRight.getCurrentPosition() );
    }

    private void setMotorDirection(int direction){
        if (DIRECTION_FORWARD == direction){
            setDirectionFrontLeft(DcMotorSimple.Direction.FORWARD);
            setDirectionFrontRight(DcMotorSimple.Direction.FORWARD);
            setDirectionBackLeft(DcMotorSimple.Direction.FORWARD);
            setDirectionBackRight(DcMotorSimple.Direction.FORWARD);
        }
        else if (DIRECTION_STRAFE_RIGHT == direction){
            setDirectionFrontLeft(DcMotorSimple.Direction.FORWARD);
            setDirectionFrontRight(DcMotorSimple.Direction.REVERSE);
            setDirectionBackLeft(DcMotorSimple.Direction.REVERSE);
            setDirectionBackRight(DcMotorSimple.Direction.FORWARD);
        }
        else if (DIRECTION_STRAFE_LEFT == direction){
            setDirectionFrontLeft(DcMotorSimple.Direction.REVERSE);
            setDirectionFrontRight(DcMotorSimple.Direction.FORWARD);
            setDirectionBackLeft(DcMotorSimple.Direction.FORWARD);
            setDirectionBackRight(DcMotorSimple.Direction.REVERSE);
        }
        else{
            setDirectionFrontLeft(DcMotorSimple.Direction.REVERSE);
            setDirectionFrontRight(DcMotorSimple.Direction.REVERSE);
            setDirectionBackLeft(DcMotorSimple.Direction.REVERSE);
            setDirectionBackRight(DcMotorSimple.Direction.REVERSE);
        }
    }
    static DcMotorSimple.Direction invertDirection(DcMotorSimple.Direction direction) {
        return direction ==
                DcMotorSimple.Direction.FORWARD ?
                DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
    }
    private void setDirectionFrontLeft(DcMotorSimple.Direction direction) {
        mFrontLeft.setDirection(invertDirection(direction));
    }
    private void setDirectionFrontRight(DcMotorSimple.Direction direction) {
        mFrontRight.setDirection((direction));
    }
    private void setDirectionBackLeft(DcMotorSimple.Direction direction) {
        //mBackLeft.setDirection((direction));
        mBackLeft.setDirection((direction));
    }
    private void setDirectionBackRight(DcMotorSimple.Direction direction) {
        mBackRight.setDirection(invertDirection(direction));
        mBackRight.setDirection((direction));
    }


    /**
     * Overload of drive method that allows for throttling of power
     * @param speedX - the x value of the joystick controlling strafe
     * @param speedY - the y value of the joystick controlling the forward/backward motion
     * @param rotation - the x value of the joystick controlling the rotation
     * @param throttle - the amount to throttle the power of the motors
     */
    public void drive(double speedX, double speedY, double rotation, double throttle){
        double throttledX = speedX * throttle;
        double throttledY = speedY * throttle;
        double throttledRotation = rotation * throttle;
        mTelemetry.addData("x:","speedX");
        mTelemetry.addData("y:","speedY");
        mTelemetry.update();
        drive(throttledX, throttledY, throttledRotation);
    }
    public void turnLeft(LinearOpMode opMode, int degree){
        setMotorDirection(DIRECTION_FORWARD);
        drive(0,0,-.5);
        opMode.sleep((long)(495 * 1));
        stop();
    }

    public void turnRight(LinearOpMode opMode, int degree){
        setMotorDirection(DIRECTION_FORWARD);
        drive(0,0,.5);
        opMode.sleep((long)(750 * 1));
        stop();
    }
    /**
     * This function makes the mecanum motor drive using the joystick
     * @param speedX - the x value of the joystick controlling straf
     * @param speedY - the y value of the joystick controlling the forward/backwards motion
     * @param rotation - the x value of the joystick controlling the rotation
     */
    public void drive(double speedX, double speedY, double rotation) {
        mTelemetry.addData("speedX", speedX);
        mTelemetry.addData("speedY", speedY);
        mTelemetry.addData("rotation", rotation);
        mTelemetry.update();

        double fl = speedX + speedY + rotation;
        double fr = -speedX + speedY - rotation;
        double bl= -speedX + speedY + rotation;
        double br = speedX + speedY - rotation;

        double max = NumberUtility.findMax(fl, fr, bl, br);
        if (max > 1) {
            fl = fl / max;
            fr = fr / max;
            bl = bl / max;
            br = br / max;
        }

        mFrontRight.setPower(fr);
        mFrontLeft.setPower(fl);
        mBackRight.setPower(br);
        mBackLeft.setPower(bl);
        Log.d("FTC", "backLeft: " + mBackLeft.getCurrentPosition() );
        Log.d("FTC", "backRight: " + mBackRight.getCurrentPosition() );
        Log.d("FTC", "frontLeft: " + mFrontLeft.getCurrentPosition() );
        Log.d("FTC", "frontRight: " + mFrontRight.getCurrentPosition() );
    }

    /**
     * This function stops the mecanum motor
     */
    public void stop() {
        mFrontLeft.setPower(0);
        mFrontRight.setPower(0);
        mBackLeft.setPower(0);
        mBackRight.setPower(0);
    }

    /**
     * Method to drive a specified distance using motor encoder functionality
     *
     * @param inches - The Number Of Inches to Move
     * @param direction - The Direction to Move
     *                  - Valid Directions:
     *                  - MecanumDrivetrain.DIRECTION_FORWARD
     *                  - MecanumDrivetrain.DIRECTION_REVERSE
     *                  - MecanumDrivetrain.DIRECTION_STRAFE_LEFT
     *                  - MecanumDrivetrain.DIRECTION_STRAFE_RIGHT
     * @param speed - The desired motor power (most accurate at low powers < 0.25)
     */
    public void driveByDistance(int inches, int direction, double speed){
        setMotorDirection(direction);
        driveByRevolution(convertDistanceToTarget(inches, direction), speed);
    }

    /**
     * Method to drive a specified distance using motor encoder functionality (includes telemetry)
     *
     * @param inches - The Number Of Inches to Move
     * @param direction - The Direction to Move
     *                  - Valid Directions:
     *                  - MecanumDrivetrain.DIRECTION_FORWARD
     *                  - MecanumDrivetrain.DIRECTION_REVERSE
     *                  - MecanumDrivetrain.DIRECTION_STRAFE_LEFT
     *                  - MecanumDrivetrain.DIRECTION_STRAFE_RIGHT
     * @param speed - The desired motor power (most accurate at low powers < 0.25)
     */
    public void driveByDistanceWithTolerance(int inches, int direction, double speed){
        setMotorDirection(direction);

        int targetPosition = convertDistanceToTarget(inches, direction);
        mTelemetry.addData("Target Position:", targetPosition);
        mTelemetry.update();

        driveByRevolutionWithTolerance(targetPosition, speed);

        mTelemetry.addData("Ending Positions:",
                "target: " + targetPosition +
                " FL: " + mFrontLeft.getCurrentPosition()
                        + " FR: " + mFrontRight.getCurrentPosition()
                        + " BL: " + mBackLeft.getCurrentPosition()
                        + " BR: " + mBackRight.getCurrentPosition());
        mTelemetry.update();
    }

    public void driveByGyro(double inches, int direction, double baseSpeed, double targetHeading){
        setMotorDirection(direction);

        int targetPosition = convertDistanceToTarget(inches, direction);

        driveByRevolutionWithGyro(targetPosition, baseSpeed, targetHeading, direction);
    }

    public void driveWithColorSensor(int direction, double baseSpeed, double targetHeading){
        setMotorDirection(direction);

        driveUntilColor(baseSpeed, targetHeading, direction);
    }

    private int convertDistanceToTarget(double inches, int direction){
        double target;

        if (DIRECTION_FORWARD == direction || DIRECTION_REVERSE == direction){
            target = inches * ENCODER_CLICKS_FORWARD_1_INCH;
        }
        else{
            target = inches * ENCODER_CLICKS_STRAFE_1_INCH;
        }

        long val = Math.round(target);
        return Integer.parseInt(Long.toString(val));
    }

    /**
     * Returns if atleast one of the wheels is moving
     * @return true if the robot is moving
     */
    public boolean isMoving() {
        return mFrontLeft.isBusy() || mFrontRight.isBusy() || mBackRight.isBusy() || mBackLeft.isBusy();
    }

    private void driveUntilColor(double basePower, double targetHeading, int direction){
        int tolerance = 10;

        mFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        mFrontLeft.setPower(basePower);
        mFrontRight.setPower(basePower);
        mBackLeft.setPower(basePower);
        mBackRight.setPower(basePower);


        double currentPower = basePower;

        int redCount = 0;

        while (redCount < 1 && opMode.opModeIsActive()) {
            /*NormalizedRGBA colors = colorSensor.getNormalizedColors();
            if (isRed(colors)){
                redCount++;
                break;
            }*/

            double gyroAdjustment = imu.computeHeadingAdjustment(targetHeading);

            if (DIRECTION_FORWARD == direction) {
                mFrontLeft.setPower(basePower + gyroAdjustment);
                mBackLeft.setPower(basePower + gyroAdjustment);
                mFrontRight.setPower(basePower - gyroAdjustment);
                mBackRight.setPower(basePower - gyroAdjustment);
            }
            else if (DIRECTION_REVERSE == direction){
                mFrontLeft.setPower(basePower - gyroAdjustment);
                mBackLeft.setPower(basePower - gyroAdjustment);
                mFrontRight.setPower(basePower + gyroAdjustment);
                mBackRight.setPower(basePower + gyroAdjustment);
            }
            else if (DIRECTION_STRAFE_LEFT == direction){
                mBackLeft.setPower(basePower + gyroAdjustment);
                mBackRight.setPower(basePower + gyroAdjustment);
                mFrontRight.setPower(basePower - gyroAdjustment);
                mFrontLeft.setPower(basePower - gyroAdjustment);
            }
            else{ //Strafe Right
                mBackLeft.setPower(basePower - gyroAdjustment);
                mBackRight.setPower(basePower - gyroAdjustment);
                mFrontRight.setPower(basePower + gyroAdjustment);
                mFrontLeft.setPower(basePower + gyroAdjustment);
            }
        }
        stop();
    }

    private boolean isRed(NormalizedRGBA colors){

        if (colors.red > (colors.blue + colors.green)){
            return true;
        }

        return false;
    }

    private void    driveByRevolutionWithGyro(int revolutions, double basePower, double targetHeading, int direction){
        int tolerance = 2;
        int revolutionsRemaining = revolutions;

        mFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        mFrontLeft.setTargetPosition(mFrontLeft.getCurrentPosition() + revolutions);
        mFrontRight.setTargetPosition(mFrontRight.getCurrentPosition() + revolutions);
        mBackLeft.setTargetPosition(mBackLeft.getCurrentPosition() + revolutions);
        mBackRight.setTargetPosition(mBackRight.getCurrentPosition() + revolutions);

        mFrontLeft.setPower(basePower);
        mFrontRight.setPower(basePower);
        mBackLeft.setPower(basePower);
        mBackRight.setPower(basePower);

        int target;
        int current;

        int firstPosition1 = mBackLeft.getCurrentPosition();
        int firstPosition2 = mFrontLeft.getCurrentPosition();
        int firstPosition3 = mBackRight.getCurrentPosition();
        int firstPosition4 = mFrontRight.getCurrentPosition();

        while (opMode.opModeIsActive() && (target = mFrontLeft.getTargetPosition()) > (current = mFrontLeft.getCurrentPosition()) + tolerance){

            double gyroAdjustment = imu.computeHeadingAdjustment(targetHeading);

            double adjustedPower = basePower;
            int distanceToTarget = target - current;

            if (distanceToTarget < 10){
                adjustedPower = basePower * .15;
            }else if (distanceToTarget < 20){
                adjustedPower = basePower * .25;
            }

            if (adjustedPower < 0.2) {
                adjustedPower = 0.2;
            }
            if (DIRECTION_FORWARD == direction) {
                mFrontLeft.setPower(adjustedPower + gyroAdjustment);
                mBackLeft.setPower(adjustedPower + gyroAdjustment);
                mFrontRight.setPower(adjustedPower - gyroAdjustment);
                mBackRight.setPower(adjustedPower - gyroAdjustment);
            }
            else if (DIRECTION_REVERSE == direction){
                mFrontLeft.setPower(adjustedPower - gyroAdjustment);
                mBackLeft.setPower(adjustedPower - gyroAdjustment);
                mFrontRight.setPower(adjustedPower + gyroAdjustment);
                mBackRight.setPower(adjustedPower + gyroAdjustment);
            }
            else if (DIRECTION_STRAFE_LEFT == direction){
                mBackLeft.setPower(adjustedPower + gyroAdjustment);
                mBackRight.setPower(adjustedPower + gyroAdjustment);
                mFrontRight.setPower(adjustedPower - gyroAdjustment);
                mFrontLeft.setPower(adjustedPower - gyroAdjustment);
            }
            else{ //Strafe Right
                mBackLeft.setPower(adjustedPower - gyroAdjustment);
                mBackRight.setPower(adjustedPower - gyroAdjustment);
                mFrontRight.setPower(adjustedPower + gyroAdjustment);
                mFrontLeft.setPower(adjustedPower + gyroAdjustment);
            }

            mTelemetry.addData("In Loop Target Position:", mFrontLeft.getTargetPosition());
            mTelemetry.addData("Current Position: ", mFrontLeft.getCurrentPosition());
            mTelemetry.addData("Current Heading: ", imu.getIMUHeading());
            mTelemetry.addData("Target Heading: ", targetHeading);
            mTelemetry.addData("Gyro Adjustment: " , gyroAdjustment);
            mTelemetry.addData("adjPower: " , adjustedPower);
            //Log.d("FTC", "adjustedPower : " + adjustedPower);
            mTelemetry.update();
        }

        mTelemetry.addData("mBackLeft: " , mBackLeft.getCurrentPosition() - firstPosition1);
        mTelemetry.addData("mFrontLeft: " , mFrontLeft.getCurrentPosition() - firstPosition2);
        mTelemetry.addData("mBackRight: " , mBackRight.getCurrentPosition() - firstPosition3);
        mTelemetry.addData("mFrontRight: " , mFrontRight.getCurrentPosition() - firstPosition4);
        mTelemetry.update();
        stop();
    }

    public void turnToHeading(LinearOpMode opMode, double heading, Runnable whileWaiting){
        headingPID.setSetpoint(heading);
        while (!opMode.isStopRequested()){
            if (whileWaiting != null){
                whileWaiting.run();
            }

            if (Math.abs(headingPID.getLastError()) < Math.toRadians(6)){
                break;
            }

            //Code that Turns Use this to adjust power
            //headingPID.update(imu.getIMUHeading());
        }
    }

    public void gyroTurn(double degrees, double basePower, LinearOpMode opMode) {
        setMotorDirection(DIRECTION_FORWARD);
        double targetHeading = imu.calculateTurnHeading(degrees); // this returns the current heading + degrees
        boolean rotateLeft = degrees < 0;
        double distanceToTarget = Math.abs(degrees);
        double previousDistanceToTarget = 999;

        mTelemetry.addData("Start Gyro Turn:", " " );
        mTelemetry.addData("Current Heading: ", imu.getIMUHeading());
        mTelemetry.addData("Target Heading: ", targetHeading);
        mTelemetry.addData("Turn Left: " , rotateLeft);
        mTelemetry.addData("Previous Distance To Target: " , previousDistanceToTarget);
        mTelemetry.addData("Distance To Target: " , distanceToTarget);
        mTelemetry.addData("Rotate Left: " , rotateLeft);
        mTelemetry.update();

        //opMode.sleep(2000);

        while (distanceToTarget > 1 && opMode.opModeIsActive()){
            mTelemetry.addData("In Loop ", distanceToTarget);
            distanceToTarget = imu.getDistanceToTargetHeading(targetHeading, rotateLeft);

            if ((previousDistanceToTarget + 2) < (distanceToTarget )){
                break;
            }
            previousDistanceToTarget = distanceToTarget;

            /* Slow down when getting closer to target */
            double adjustedPower = basePower;
            if (distanceToTarget < 20){
                adjustedPower = basePower * .25;
            }
            else if (distanceToTarget < 10){
                adjustedPower = basePower * .15;
            }

            if (rotateLeft){
                adjustedPower *= -1;
            }

            mTelemetry.addData("In Loop Gyro Turn:", " " );
            mTelemetry.addData("Current Heading: ", imu.getIMUHeading());
            mTelemetry.addData("Target Heading: ", targetHeading);
            mTelemetry.addData("Turn Left: " , rotateLeft);
            mTelemetry.addData("Previous Distance To Target: " , previousDistanceToTarget);
            mTelemetry.addData("Distance To Target: " , distanceToTarget);
            mTelemetry.addData("Adjusted Power: " , adjustedPower);
            mTelemetry.update();

            mFrontLeft.setPower(adjustedPower);
            mBackLeft.setPower(adjustedPower);
            mFrontRight.setPower(-adjustedPower);
            mBackRight.setPower(-adjustedPower);
        }

        stop();
        mTelemetry.addData("After Loop Gyro Turn:", " " );
        mTelemetry.addData("Current Heading: ", imu.getIMUHeading());
        mTelemetry.addData("Target Heading: ", targetHeading);
        mTelemetry.addData("Turn Left: " , rotateLeft);
        mTelemetry.addData("Previous Distance To Target: " , previousDistanceToTarget);
        mTelemetry.addData("Distance To Target: " , distanceToTarget);
        mTelemetry.update();
        //opMode.sleep(2);
    }

    public void turnLeftGyro(double basePower, IMU imu, double targetHeading,
                          int direction, Telemetry telemetry){
        double startHeading = imu.getIMUHeading();
        telemetry.addData("IMU current: " , imu.getIMUHeading());
        telemetry.update();
        int tolerance = 10;

        mFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        mFrontLeft.setPower(basePower);
        mFrontRight.setPower(basePower);
        mBackLeft.setPower(basePower);
        mBackRight.setPower(basePower);


        double currentPower = basePower;

        ElapsedTime timeoutTimer = new ElapsedTime();
        //Negative Value is Go Right - Positive is Left
        double adjustment = imu.computeHeadingAdjustment(targetHeading);
        while (adjustment != 0 && opMode.opModeIsActive() && timeoutTimer.seconds() < 0.75) {
            telemetry.addData("IMU adjustment: " , " " + startHeading + " " + adjustment);
            telemetry.update();
            if (DIRECTION_FORWARD == direction || DIRECTION_REVERSE == direction) {
                mFrontLeft.setPower(basePower + adjustment);
                mBackLeft.setPower(basePower + adjustment);
                mFrontRight.setPower(basePower - adjustment);
                mBackRight.setPower(basePower - adjustment);
            }
            else{
                mBackLeft.setPower(basePower - adjustment);
                mFrontRight.setPower(basePower + adjustment);
            }

            //Negative Value is Go Right - Positive is Left
            adjustment = imu.computeHeadingAdjustment(targetHeading);

        }

        stop();
    }
    private void driveByRevolutionWithTolerance(int revolutions, double power){
        int tolerance = 10;

        mFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        mFrontLeft.setTargetPosition(mFrontLeft.getCurrentPosition() + revolutions);
        mFrontRight.setTargetPosition(mFrontRight.getCurrentPosition() + revolutions);
        mBackLeft.setTargetPosition(mBackLeft.getCurrentPosition() + revolutions);
        mBackRight.setTargetPosition(mBackRight.getCurrentPosition() + revolutions);

        mFrontLeft.setPower(power);
        mFrontRight.setPower(power);
        mBackLeft.setPower(power);
        mBackRight.setPower(power);

        ElapsedTime startTime = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
        while ((mFrontLeft.getTargetPosition() > mFrontLeft.getCurrentPosition() + tolerance)
        && startTime.time() < 10 && opMode.opModeIsActive()){
            mTelemetry.addData("In Loop Target Position:", mFrontLeft.getTargetPosition());
            mTelemetry.addData("Current Position: ", mFrontLeft.getCurrentPosition());
            mTelemetry.update();
        }

        mFrontLeft.setPower(0);
        mFrontRight.setPower(0);
        mBackLeft.setPower(0);
        mBackRight.setPower(0);
    }

    /**
     * Method will motors a specified number of revolutions at the desired power
     * agnostic of direction.
     *
     * @param revolutions - the number of motor encoder ticks to move
     * @param power - the speed at which to move
     */
    private void driveByRevolution(int revolutions, double power){
        mFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mFrontLeft.setTargetPosition(revolutions);
        mFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        mBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mBackRight.setTargetPosition(revolutions);
        //mBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        mBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mBackLeft.setTargetPosition(revolutions);
        //mBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        mFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mFrontRight.setTargetPosition(revolutions);
        //mFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int brPos = mBackRight.getCurrentPosition();
        int frPos = mFrontRight.getCurrentPosition();
        int blPos = mBackLeft.getCurrentPosition();
        int flPos = mFrontLeft.getCurrentPosition();
        mFrontLeft.setPower(power);
        mBackRight.setPower(power );
        mBackLeft.setPower(power );
        mFrontRight.setPower(power );

        while (mFrontLeft.isBusy()) {
            // wait for the wheels to stop
        }
        stop();

        opMode.telemetry.addData("Target:",revolutions);
        opMode.telemetry.addData("br Pos change: " + mBackRight.getDirection() + " " ,
                mBackRight.getCurrentPosition() - brPos);
        opMode.telemetry.addData("fr Pos change: " + mFrontRight.getDirection() + " ",
                mFrontRight.getCurrentPosition() - frPos);
        opMode.telemetry.addData("bl Pos change: " + mBackLeft.getDirection() + " ",
                mBackLeft.getCurrentPosition() - blPos);
        opMode.telemetry.addData("fl Pos change: " + mFrontLeft.getDirection() + " ",
                mFrontLeft.getCurrentPosition() - flPos);
        opMode.telemetry.update();
        /*
        opMode.telemetry.addData("FL: " , mFrontLeft.getCurrentPosition());
        opMode.telemetry.addData("FR: " , mFrontRight.getCurrentPosition());
        opMode.telemetry.addData("BL: " , mBackLeft.getCurrentPosition());
        opMode.telemetry.addData("BR: " , mBackRight.getCurrentPosition());
        opMode.telemetry.update();

         */
    }

    public void forwardByTime(LinearOpMode opMode, double speed, double time) {
        /*int brPos = mBackRight.getCurrentPosition();
        int frPos = mFrontRight.getCurrentPosition();
        int blPos = mBackLeft.getCurrentPosition();
        int flPos = mFrontLeft.getCurrentPosition();
        mBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
        mBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mBackLeft.setPower(speed);
        mBackRight.setPower(speed);
        mFrontRight.setPower(speed);
        mFrontLeft.setPower(speed);
        opMode.sleep((long)(1000 * time));
       /* opMode.telemetry.addData("br Pos change: " + mBackRight.getDirection() + " " ,
                mBackRight.getCurrentPosition() - brPos);
        opMode.telemetry.addData("fr Pos change: " + mFrontRight.getDirection() + " ",
                mFrontRight.getCurrentPosition() - frPos);
        opMode.telemetry.addData("bl Pos change: " + mBackLeft.getDirection() + " ",
                mBackLeft.getCurrentPosition() - blPos);
        opMode.telemetry.addData("fl Pos change: " + mFrontLeft.getDirection() + " ",
                mFrontLeft.getCurrentPosition() - flPos);
        opMode.telemetry.update();*/


    }

    public void strafeRightByTime(LinearOpMode opMode, double speed, double time) {
        mBackLeft.setPower(-speed);
        mBackRight.setPower(speed);
        mFrontLeft.setPower(speed);
        mFrontRight.setPower(-speed);
        opMode.sleep((long)(1000 * time));
        stop();
    }

    public void strafeLeftByTime(LinearOpMode opMode, double speed, double time) {
        mFrontLeft.setPower(-speed);
        mFrontRight.setPower(speed);
        mBackLeft.setPower(speed);
        mBackRight.setPower(-speed);
        opMode.sleep((long)(1000*time));
        stop();
    }

    public void backwardByTime(LinearOpMode opMode, double speed, double time) {
        mBackLeft.setPower(-speed);
        mBackRight.setPower(-speed);
        mFrontLeft.setPower(-speed);
        mFrontRight.setPower(-speed);
        opMode.sleep((long)(1000 * time));
        stop();
    }
}