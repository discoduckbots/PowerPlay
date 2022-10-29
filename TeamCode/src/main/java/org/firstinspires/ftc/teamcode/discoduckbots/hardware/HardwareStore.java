package org.firstinspires.ftc.teamcode.discoduckbots.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class HardwareStore {
    private MecanumDrivetrain mecanumDrivetrain;
    private ConeArm coneArm;
    private IMU imu;
    private ColorSensor colorSensor = null;
    private TouchSensor touchSensor = null;
    private DistanceSensor distanceSensor = null;
    private DistanceSensor distanceSensor2 = null;
    private DistanceSensor blockSensor = null;
    private TouchSensor armStoppingSensor = null;
    private WebcamName webcamName = null;

    public DcMotorEx frontLeft ;
    public DcMotorEx frontRight ;
    public DcMotorEx backRight ;
    public DcMotorEx backLeft ;
    //public DcMotor intakeMotor;
    public DcMotor coneTurret;
    public DcMotor coneLift;
    public Servo coneGrabber;
   // public Servo intakePusher;
    //public RevBlinkinLedDriver ledDriver;

    public HardwareStore(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode) {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
         frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
         backRight = hardwareMap.get(DcMotorEx.class, "backRight");
         backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
         coneTurret = hardwareMap.get(DcMotor.class, "coneTurret");
         coneLift = hardwareMap.get(DcMotor.class, "coneLift");
         coneGrabber= hardwareMap.get(Servo.class, "coneGrabber");

        // frontRight.setDirection(DcMotorEx.Direction.FORWARD);
       //  frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        //webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //cargoGrabber = new CargoGrabber(cargoMotor, cargoServo);
       // DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        //intakePusher = hardwareMap.get(Servo.class, "intakePusher");
       /* intake = new Intake(intakeMotor, intakePusher);

        DcMotorEx shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        Servo pusherServo = hardwareMap.get(Servo.class, "pusher");
        shooter = new Shooter(shooterMotor, pusherServo);

        DcMotor wobbleMoverMotor = hardwareMap.get(DcMotor.class, "wobbleMover");
        Servo wobbleGrabber = hardwareMap.get(Servo.class, "wobbleGrabber");
        wobbleMover = new WobbleMover(wobbleMoverMotor, wobbleGrabber);

*/

        BNO055IMU gyro = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new IMU(gyro);
        imu.initialize();

        mecanumDrivetrain = createDrivetrain(telemetry, opMode, imu, colorSensor, frontLeft, frontRight, backLeft, backRight);
    }

    protected MecanumDrivetrain createDrivetrain(Telemetry telemetry,
                                 LinearOpMode opMode,
                                 IMU imu,
                                 ColorSensor colorSensor,
                                                 DcMotorEx frontLeft,
                                                 DcMotorEx frontRight,
                                                 DcMotorEx backLeft,
                                                 DcMotorEx backRight){
        return new MecanumDrivetrain(telemetry, opMode, imu, colorSensor, frontLeft, frontRight, backLeft, backRight);
    }

    public MecanumDrivetrain getMecanumDrivetrain() {
        return mecanumDrivetrain;
    }


    public IMU getImu(){
        return imu;
    }

    public ColorSensor getColorSensor(){
        return colorSensor;
    }

    public TouchSensor getArmStoppingSensor() { return armStoppingSensor; }
    public DistanceSensor getDistanceSensor() { return distanceSensor; }
    public DistanceSensor getDistanceSensor2() { return distanceSensor2; }
    public DistanceSensor getBlockSensor() { return blockSensor; }


    public ConeArm getConeArm() { return coneArm;}

    public WebcamName getWebcamName() {return webcamName;}

   // public RevBlinkinLedDriver getLedDriver() { return ledDriver;}

    public DcMotor getConeLift() {
        return coneLift;
    }

    public DcMotor getConeTurret() {
        return coneTurret;
    }

    public Servo getConeGrabber() {
        return coneGrabber;
    }
}
