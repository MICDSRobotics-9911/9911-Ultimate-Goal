package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.AutonomousTicondeRobot;
import org.firstinspires.ftc.teamcode.TicondeRobot;

@Autonomous(name = "Autonomous V1 BLUE", group = "Basic")
public class AutonomousMain extends LinearOpMode {
    private AutonomousTicondeRobot robot = new AutonomousTicondeRobot();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.initHardware(hardwareMap);
        robot.intakeRotate.setPosition(1);

        waitForStart();

        robot.moveToPositionPID(130, telemetry, () -> opModeIsActive());
        robot.setDriveTrainMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.setMovement(.016,.016,.016,.016);
        robot.runSpinner();
        robot.setMovement(0,0,0,0);
        robot.runSpinner();



    }
}
