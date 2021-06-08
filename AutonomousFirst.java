package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Autonomous")
public class AutonomousFirst extends OpMode {
    private Robot robot;

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY =
            "AYFidhj/////AAABmYAYnHVX/E5avxwXiVlmN9Nc4gOT++zRpd/1ESldDdjyrN2eEJpV1OtYWGV0imEhVRFUtgdwm1Qp2mv02ECXAZvvMCWEGM1PSc0R0FPs4sXeUn9a9J0j1VVrKPHsW1sIPDRsMo+GKZQFUyEsMrRqiumyN4UCCow8rylNC9L3EoCo2BdpSBePXLCkNyVOV1hVXVE1j4aliLD7WD0MyTvy2KWhdTh6u2sRhiq/efMayRFVy0h5qKDn/b1hq3atEluSC4yZQgB3DmSYOcyw8LqHk2sqcDu4vFy/gQdP9RKFM9Xmhs4r8TKn6gwlyrEl7H14EnZy+LUr9Q7JazztJJVC8SOw+3fsobN6QUgrFcZO/D9y";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        initVuforia();
        initTfod();
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    @Override
    public void init_loop() {
        super.init_loop();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(2.5, 16.0/9.0);
        }

        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                }
                telemetry.update();
            }
        }
    }



    @Override
    public void loop() {
        ///
    }

    //    ColorSensor sensorColor;
//    Robot robot;
//    boolean merge = true;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        sensorColor = hardwareMap.get(ColorSensor.class, "culoare");
//        robot = new Robot(hardwareMap, telemetry);
//
//        waitForStart();
//
//        float hsvValues[] = {0F, 0F, 0F};
//        final float values[] = hsvValues;
//        final double SCALE_FACTOR = 255;
//
//        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
//        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
//
//        while(sensorColor.red() < 80 && sensorColor.blue() < 80 && sensorColor.green() < 80) {
//
//            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
//                    (int) (sensorColor.green() * SCALE_FACTOR),
//
//                    (int) (sensorColor.blue() * SCALE_FACTOR),
//                    hsvValues);
//
//            robot.setMotors(1, 1, 1, 1, false);
//            telemetry.addLine()
//                    .addData("Red", sensorColor.red())
//                    .addData("Green", sensorColor.green())
//                    .addData("Blue", sensorColor.blue());
//            telemetry.update();
//
//            relativeLayout.post(new Runnable() {
//                public void run() {
//                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
//                }
//            });
//
//        }
//
//        robot.setMotors(0,0,0,0, false);

}
