package org.firstinspires.ftc.teamcode.discoduckbots.opmode.powerPlay;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.discoduckbots.hardware.ConeArm;
import org.firstinspires.ftc.teamcode.discoduckbots.hardware.HardwareStore;
import org.firstinspires.ftc.teamcode.discoduckbots.hardware.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.discoduckbots.sensors.TensorFlow;

@Autonomous(name="tfAutonomousRed", group="Robot")
public class TfAutonomousRed extends LinearOpMode{

        private ElapsedTime runtime = new ElapsedTime();
        private MecanumDrivetrain mecanumDrivetrain = null;
        private ConeArm coneArm = null;
        TensorFlow tensorFlow = null;



        private static final double AUTONOMOUS_SPEED = 0.4;

        private static final double ROTATION_SPEED = 0.4;
        private static final int WOBBLE_GRABBER_REVOLUTIONS = 6250;

        private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
        private static final String[] LABELS = {
                "Ball",
                "Cube",
                "Duck",
                "Marker"
        };

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code on the next line, between the double quotes.
         */
        private static final String VUFORIA_KEY =
                "AYSk32L/////AAABmbhg8GZe7kWfmZUNbwUuIPIF4dklwa5nY6Be4MuPWPpva8SYxSc/pUq/kc9kdl8Bh7w7t8PjWaJGfLRGug7l/wswCDj2V2Ag+hsG2zUDnAY55qbbiTzIjyt2qJzfYIK5Ipojsz7KmEiAWC7DUf9C64jez6LEDJEYYwtR+W2RrTl0DRRYpVmMGk31aF5ZbHC77dTEvpT5xCGAC35F2R53bYW9eUbDMiQWnfKTKOxLA8oEsA5pI42IJhZvFqfSFYsTaLp7DymS8b3QVwn4jOvWMh+sdloU9f1fE14yolR4wcIzbiFcSA2eJTGYfwUcopLlpZsE4A3XdKRx/AIExFADF5qAaAW02wMILYxWQYXDBQ4m";

        /**
         * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
         * localization engine.
         */
        private VuforiaLocalizer vuforia;

        /**
         * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
         * Detection engine.
         */
        private TFObjectDetector tfod;

        @Override
        public void runOpMode() {
            HardwareStore hardwareStore = new HardwareStore(hardwareMap, telemetry, this);
            mecanumDrivetrain = hardwareStore.getMecanumDrivetrain();
            DcMotor coneLift = hardwareStore.getConeLift();
            DcMotor coneTurret = hardwareStore.getConeTurret();
            Servo coneGrabber = hardwareStore.getConeGrabber();
            ConeArm coneArm = new ConeArm(coneLift, coneGrabber, coneTurret);


            // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
            // first.
            //initVuforia();
            //initTfod();

            /**
             * Activate TensorFlow Object Detection before we wait for the start command.
             * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
             **/
       /* if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(2.5, 16.0/9.0);
        }
*/
            /** Wait for the game to begin */
            telemetry.addData(">", "Press Play to start op mode");
            telemetry.update();
            waitForStart();

            if (opModeIsActive()) {
                coneArm.close();
                sleep(300);
                mecanumDrivetrain.driveByGyro(45, mecanumDrivetrain.DIRECTION_FORWARD, AUTONOMOUS_SPEED, 0);

            /* mecanumDrivetrain.driveByGyro(3, mecanumDrivetrain.DIRECTION_FORWARD, AUTONOMOUS_SPEED, 0);
            //tensorflow
            sleep(300);
            mecanumDrivetrain.driveByGyro(9, mecanumDrivetrain.DIRECTION_STRAFE_RIGHT, AUTONOMOUS_SPEED, 0);
            //spin carousel
            sleep(200);
            //carouselSpinner.setPower(0.7);
            sleep(1000);
            //carouselSpinner.setPower(0);
            mecanumDrivetrain.driveByGyro(18, mecanumDrivetrain.DIRECTION_STRAFE_LEFT, AUTONOMOUS_SPEED, 0);
            mecanumDrivetrain.driveByGyro(5, mecanumDrivetrain.DIRECTION_FORWARD, AUTONOMOUS_SPEED, 0);
            //drop cube
            sleep(200);
            //This distance depends on if we are going over the barrier or through the gap
            mecanumDrivetrain.driveByGyro(3, mecanumDrivetrain.DIRECTION_REVERSE, AUTONOMOUS_SPEED, 0);
            mecanumDrivetrain.driveByGyro(21, mecanumDrivetrain.DIRECTION_STRAFE_LEFT, AUTONOMOUS_SPEED, 0);
*/
                telemetry.addData("end","");
                telemetry.update();
            }
        }

        /**
         * Initialize the Vuforia localization engine.
         */
        private void initVuforia() {
            /*
             * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
             */
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");

            //  Instantiate the Vuforia engine
            vuforia = ClassFactory.getInstance().createVuforia(parameters);

            // Loading trackables is not necessary for the TensorFlow Object Detection engine.
        }

        /**
         * Initialize the TensorFlow Object Detection engine.
         */
        private void initTfod() {
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minResultConfidence = 0.8f;
            tfodParameters.isModelTensorFlow2 = true;
            tfodParameters.inputSize = 320;
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        }
    }


