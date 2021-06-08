package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MainTeleOp")
public class MainTeleOp extends OpMode {
    private Robot robot;
    private Controller controller1;
    private Controller controller2;

    private final double initialArmPosition = 1.0, initialGripPosition = 0.67;
    private final double[] armPositions = {0.72, 0.18};
    private  double gripPosition = 0.25;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        robot.runUsingEncoders();

        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        robot.armServo.setPosition(initialArmPosition);
        robot.gripServo.setPosition(initialGripPosition);
    }

    private void updateRobot() {
        if (controller1.XOnce()) robot.resetHeading();

        final double x = Math.pow(controller1.left_stick_x, 3.0);
        final double y = Math.pow(controller1.left_stick_y, 3.0);

        final double rotation = Math.pow(controller1.right_stick_x, 3.0);
        final double direction = Math.atan2(x, y) - robot.getHeading();
        final double speed = Math.min(1.0, Math.sqrt(x * x + y * y));

        final double lf = speed * Math.sin(direction + Math.PI / 4.0) - rotation;
        final double rf = speed * Math.cos(direction + Math.PI / 4.0) + rotation;
        final double lr = speed * Math.cos(direction + Math.PI / 4.0) - rotation;
        final double rr = speed * Math.sin(direction + Math.PI / 4.0) + rotation;

        robot.setMotors(lf, lr, rf, rr, (controller1.right_trigger != 0));
    }

    @Override
    public void loop() {
        controller1.update();
        controller2.update();
        robot.loop();

        if (controller2.XOnce()) {
            robot.armServo.setPosition((robot.armServo.getPosition() == initialArmPosition) ?
                    armPositions[0] : initialArmPosition);
        }

        if (controller2.YOnce() && (robot.armServo.getPosition() == armPositions[0])) {
            robot.gripServo.setPosition(gripPosition);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.armServo.setPosition(armPositions[1]);
        }else if(controller2.YOnce() && (robot.armServo.getPosition() == armPositions[1])) {
            robot.armServo.setPosition(armPositions[0]);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.gripServo.setPosition(initialArmPosition);
        }

        if (controller1.A()) {
            robot.shooter.setPower(0.85);
        }else if(!controller1.A()) {
            robot.shooter.setPower(0.0);
        }

        if (controller1.B()) {
            robot.collector.setPower(1.0);
        }else if(!controller1.B()) {
            robot.collector.setPower(0.0);
        }

        if (controller1.XOnce()) {
            robot.pusher.setPosition(0.87);
        }
        if (controller1.YOnce()){
            robot.pusher.setPosition(1.0);
        }

        updateRobot();
    }
}
