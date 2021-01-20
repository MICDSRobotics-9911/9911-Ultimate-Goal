package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RapidFire;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

@Autonomous(name = "AutoRedCenter", group = "Concept")
//@Disabled
public class AutoRedCenter extends LinearOpMode {
    private Robot robot;
    private MecanumDrive mecanumDrive;
    private DcMotor shooter1;
    private DcMotor shooter2;
    double firepower = 0.5;
    private DcMotor arm;
    private Servo claw;
    boolean clawstate = false;
    boolean hopstate = false;
    double firepowerstate = 0.0;
    private DcMotor intake;
    private Servo hopperpush;
    private int storedRings = 0;
    private int fieldRings;
    private ColorSensor groundSensor;

    private enum AutoMode {
        PICK_UP_WOBBLE,
        MOVE_TO_LINE,
        FIRE_POWER_SHOT,
        PICKUP_A,
        PICKUP_B,
        PICKUP_C,
        FIRE_GOAL,
        DROP_WOBBLE,
        PARK_LINE
    };
    private enum FieldMode {
        A, // Wobble zone is on bottom, 0 rings
        B, // Wobble zone is in the middle, 1 ring
        C, // Wobble zone is on top, 4 rings
    }

    private AutoMode autoMode;
    private FieldMode fieldMode;
    private AutoMode pickupConfig;

    @Override
    public void runOpMode() {
        this.robot = new Robot(hardwareMap);
        this.mecanumDrive = (MecanumDrive) this.robot.getDrivetrain();
        this.autoMode = AutoMode.MOVE_TO_LINE;
        this.fieldMode = FieldMode.A;
        this.shooter1 = hardwareMap.get(DcMotor.class, "shooter1");
        this.shooter2 = hardwareMap.get(DcMotor.class, "shooter2");
        this.arm = hardwareMap.get(DcMotor.class, "arm");
        this.claw = hardwareMap.get(Servo.class, "claw");
        claw.setPosition(0);
        this.hopperpush = hardwareMap.get(Servo.class, "hopperpush");
        this.intake = hardwareMap.get(DcMotor.class, "intake");
        hopperpush.setPosition(0.5);
        this.groundSensor = hardwareMap.get(ColorSensor.class, "groundSensor");




        switch(this.fieldMode) {
            case A:
                this.fieldRings = 0;
                this.pickupConfig = AutoMode.PICKUP_A;
                break;
            case B:
                this.fieldRings = 1;
                this.pickupConfig = AutoMode.PICKUP_B;
                break;
            case C:
                this.fieldRings = 4;
                this.pickupConfig = AutoMode.PICKUP_C;
                break;
        }

        waitForStart();

        /** Pseudocode for Auto */
        /*

        //Drive to center line
        //Assess what configuration the field is in

        if (field_config == "A") { <-- LUCAS IS DOING THIS BOI
            // Square is in lower right
            // 0 rings
            //pick up wobble goal
            //drive almost to launch line
            //shoot the three loaded rings into the power-shots
            //rotate 90 degrees to the right
            //strafe left 4 in.
            //drive forward 12 in.
            //deposit wobble goal
        }
        else if (field_config == "B") { //<-- BAZ IS DOING
            // Square is in middle
            // 1 ring
            //pick up wobble goal

            //WOBBLE GOAL PICKUP SEQUENCE:
            arm.setPower(-1);
            sleep(150);
            arm.setPower(0);
            this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(),1,0);
            sleep(50);
            this.mecanumDrive.stopMoving();

            //drive almost to launch line
            this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(),1,0);
            sleep(500);
            this.mecanumDrive.stopMoving();

            //shoot the three pre-loaded rings into the power-shots

            RapidFire.rapidFire(shooter1, shooter2, hopperpush, this.fieldRings);

            //drive forward to pick up 1 ring

            this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(),1,0);
            sleep(500);
            this.mecanumDrive.stopMoving();


            //shoot 1 ring into the top goal

            this.shooter1.setPower(1);
            this.shooter2.setPower(1);
            sleep(500);
            this.shooter1.setPower(0);
            this.shooter1.setPower(0);

            //rotate 90 degrees to the right

            this.mecanumDrive.complexDrive(MecanumDrive.Direction.RIGHT.angle(),0,1);
            sleep(1000);
            this.mecanumDrive.stopMoving();

            //drive forward a few inches

            this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(),1,0);
            sleep(500);
            this.mecanumDrive.stopMoving();

            //deposit wobble goal
        }
        else if (field_config == "C") {
            // Square is in the top right
            // Four rings
            //pick up wobble goal
            //drive almost to the launch line
            //shoot the three pre-loaded rings into the power-shots
            //drive forward and pick up three rings
            //shoot 3 rings into the top goal
            //pick up last ring
            //shoot 1 ring into the top goal
            //drive forward the last few inches
            //deposit wobble goal
        }*/



        /** Actual code for Auto */
        while (opModeIsActive()) {
            switch (this.autoMode) {
                case PICK_UP_WOBBLE:
                    arm.setPower(-1);
                    sleep(150);
                    arm.setPower(0);
                    this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(),1,0);
                    sleep(50);
                    this.mecanumDrive.stopMoving();
                    this.autoMode = AutoMode.MOVE_TO_LINE;
                    break;
                case MOVE_TO_LINE:
                    this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(), 1, 0);
                    sleep(100); // Change to reflect actual distance
                    this.mecanumDrive.stopMoving();
                    this.autoMode = AutoMode.FIRE_POWER_SHOT;
                    break;
                case FIRE_POWER_SHOT:
                    // TODO: Make powershot algorithm
                    RapidFire.rapidFire(shooter1,shooter2,hopperpush,storedRings);
                    this.autoMode = pickupConfig;
                    break;
                case PICKUP_A:
                    this.autoMode = AutoMode.FIRE_GOAL;
                    break;
                case PICKUP_B:
                    while (storedRings < 1) {
                        this.intake.setPower(0.5);
                        sleep(50);
                    }
                    this.autoMode = AutoMode.FIRE_GOAL;
                    break;
                case PICKUP_C:
                    while (storedRings < 3) {
                        this.intake.setPower(0.5);
                        sleep(50);
                    }
                    this.autoMode = AutoMode.FIRE_GOAL;
                    this.fieldRings = 1;
                    break;
                case FIRE_GOAL:
                    while (storedRings != 0) {
                        RapidFire.rapidFire(shooter1,shooter2,hopperpush,storedRings); // Shoots the rings based off the number of rings
                    }
                    if (this.fieldRings == 1) {
                        this.autoMode = AutoMode.PICKUP_B;
                    } else {
                        this.autoMode = AutoMode.DROP_WOBBLE;
                    }
                    break;
                case DROP_WOBBLE:
                    // TODO: Drop wobble
                    this.arm.setPower(1);
                    sleep(150);
                    this.arm.setPower(0);
                    this.mecanumDrive.complexDrive(MecanumDrive.Direction.DOWN.angle(),1,0);
                    sleep(50);
                    this.mecanumDrive.stopMoving();
                    this.autoMode = AutoMode.PARK_LINE;
                    break;
                case PARK_LINE:
                    // TODO: Move to the line
                    while (this.groundSensor.blue() < 200) {
                        this.mecanumDrive.complexDrive(MecanumDrive.Direction.DOWN.angle(), 1, 0);
                        telemetry.addLine(String.valueOf(this.groundSensor.blue()));
                        telemetry.update();
                        sleep(50);
                    }
                    sleep(50);
                    this.mecanumDrive.stopMoving();
                    break;
            }
        }
    }

}



/*



        this.mecanumDrive.complexDrive(MecanumDrive.Direction.UP.angle(),1,0);
        sleep(500);
        this.mecanumDrive.stopMoving();

        this.mecanumDrive.complexDrive(MecanumDrive.Direction.RIGHT.angle(),0,1);
        sleep(600);
        this.mecanumDrive.stopMoving();

        this.mecanumDrive.complexDrive(MecanumDrive.Direction.DOWN.angle(),1,0);
        sleep(500);
        this.mecanumDrive.stopMoving();

        this.mecanumDrive.complexDrive(MecanumDrive.Direction.LEFT.angle(),0,-1);
        sleep(600);
        this.mecanumDrive.stopMoving();

*/