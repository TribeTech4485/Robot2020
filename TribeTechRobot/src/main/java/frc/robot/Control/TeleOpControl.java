package frc.robot.Control;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.TribeTech.IterativeControlMethod;
import frc.robot.Subsystem.BallDeliverySystem;
import frc.robot.Subsystem.BallShooterSystem;
import frc.robot.Subsystem.GearShiftSystem;
import frc.robot.Subsystem.HookSystem;
import frc.robot.Subsystem.TankDriveSystem;

public class TeleOpControl extends IterativeControlMethod {

    // Driver controllers
    private XboxController mainController = new XboxController(0);
     // Shooter controllers
    private Joystick auxController = new Joystick(1);    // aux controller setup

    // Declare subsystems used in this method
    private TankDriveSystem drive = new TankDriveSystem();
    private GearShiftSystem gearShift = new GearShiftSystem();
    private BallShooterSystem shooter = new BallShooterSystem();
    private BallDeliverySystem delivery = new BallDeliverySystem();
    private HookSystem hook = new HookSystem();

    private PowerDistributionPanel pdp;

    private List<Double> leftDriveValues = new ArrayList<Double>();
    private List<Double> rightDriveValues = new ArrayList<Double>();

    // Logitech Gamepad axis
    //public static final int kLogiTechAxisLeftX = 1;
    //public static final int kLogiTechAxisLeftY = 2;
    public static final int kLogiTechAxisTriggers = 3; // left trigger only=-1.0, right only=1.0, both=0.0
    //public static final int kLogiTechAxisRightX = 4;
    //public static final int kLogiTechAxisRightY = 5;
    //public static final int kLogiTechAxisDpad = 6;

    // Logitech Gamepad buttons
    public static final int kLogiTechButtonA = 1; // Bottom Button
    public static final int kLogiTechButtonB = 2; // Right Button
    //public static final int kLogiTechButtonX = 3; // Left Button
    //public static final int kLogiTechButtonY = 4; // Top Button
    public static final int kLogiTechBumperLeft = 5; // on front of controller
    public static final int kLogiTechBumperRight = 6;
    //public static final int kLogiTechButtonBack = 7;
    //public static final int kLogiTechButtonStart = 8;
    //public static final int kLogiTechStickLeft = 9;  // on front of controller
    //public static final int kLogiTechStickRight = 10;

    private static boolean auxButtonAToggle = true;  // keeps track of when button pressed and repressed
    private static boolean auxButtonBToggle = true;
    private static boolean conveyorOn = true;	//start with true so 1st press turns on
    private static boolean collectorOn = true;

    @Override
    protected void methodInit() {
        // Register and initialize the subsystems with the robot map
        map.registerSystem(drive);
        map.registerSystem(gearShift);
        map.registerSystem(shooter);
        map.registerSystem(delivery);
        map.registerSystem(hook);  // larry
        
        map.initSystems();

        pdp = new PowerDistributionPanel(62);
    }

    @Override
    protected void methodUpdate() {
        // Get the drive values from the main controller
        double leftDrive = mainController.getY(Hand.kLeft);
        double rightDrive = mainController.getY(Hand.kRight);

        leftDrive = Math.pow(leftDrive, 3) + 0.2 * leftDrive;
        rightDrive = Math.pow(rightDrive, 3) + 0.2 * rightDrive;

        leftDriveValues.add(leftDrive);
        rightDriveValues.add(rightDrive);
        if (leftDriveValues.size() > 10) leftDriveValues.remove(0);
        if (rightDriveValues.size() > 10) rightDriveValues.remove(0);

        // leftDrive = 0;
        // rightDrive = 0;
        // for (Double val : leftDriveValues) {
        //     leftDrive += val;
        // }
        // for (Double val : rightDriveValues) {
        //     rightDrive += val;
        // }
        // leftDrive /= leftDriveValues.size();
        // rightDrive /= rightDriveValues.size();

        // if (gearShift.isHighGear()) {

        // } else {
        //     leftDrive = Math.pow(leftDrive, 3) + 0.1 * leftDrive;
        //     rightDrive = Math.pow(rightDrive, 3) + 0.1 * rightDrive;
        // }

        drive.setTankDriveInput(leftDrive / 2, rightDrive / 2);
        SmartDashboard.putNumber("Left Drive Percent", leftDrive);
        SmartDashboard.putNumber("Right Drive Percent", rightDrive);

        SmartDashboard.putNumber("PDP Current", pdp.getTotalCurrent());

        // *** Larry
        //https://www.chiefdelphi.com/t/java-toggle-button/122156
        boolean auxButtonAPressed = auxController.getRawButtonPressed(kLogiTechButtonA);  // check if button pressed 
        if (auxButtonAToggle && auxButtonAPressed) {  	// Only execute once per Button push
            auxButtonAToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (conveyorOn) {  // Decide which way to set the motor this time through
                conveyorOn = false;
                //conveyorMotor.set(1);
                delivery.turnOnConveyor();    // turn on conveyor
            } else {
                conveyorOn = true;
                //conveyorMotor.set(0);
                delivery.turnOffConveyor();  // turn off conveyor
            }
        } else if (!auxButtonAPressed) { 
            auxButtonAToggle = true; // Button has been released, so allows button re-press to activate code above
        }

        boolean auxButtonBPressed = auxController.getRawButtonPressed(kLogiTechButtonB);  // check if button pressed 
        if (auxButtonBToggle && auxButtonBPressed) {  	// Only execute once per Button push
            auxButtonBToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (collectorOn) {  // Decide which way to set the motor this time through
                collectorOn = false;
                delivery.turnOnCollector();   // turn on collector
            } else {
                collectorOn = true;
                delivery.turnOffCollector();  // turn off collector
            }
        } else if (!auxButtonBPressed) { 
            auxButtonBToggle = true; // Button has been released, so allows button re-press to activate code above
        }
        // *** end Larry ***

        // *** Larry *****
        // control shooter speed with Logitech controller
        if (auxController.getRawButton(kLogiTechBumperLeft)) {
            shooter.setTargetRPM(-1900);
            if (shooter.isOnTarget()) {
                System.out.println("Shooter On Target at -1900");
                //delivery.deliverBalls();
            }
        }
        if (auxController.getRawButton(kLogiTechBumperRight)) {
            shooter.setTargetRPM(-1750);
            if (shooter.isOnTarget()) {
                System.out.println("Shooter On Target at -1750");
                //delivery.deliverBalls();
            }
        }
        // https://www.chiefdelphi.com/t/commands-activated-by-trigger-how/130010/5
        // value>0 if just right trigger pressed, value<0 if just left trigger pressed, both=sum left+right=0
        if (auxController.getRawAxis(kLogiTechAxisTriggers) > 0.4) {
            shooter.setTargetRPM(0);
            System.out.println("Shooter stopped");
        }
        // *** end Larry ***

        // larry - following line not needed now
        //delivery.setCollector(mainController.getBumper(Hand.kLeft));

        if (mainController.getBButton()) {
            gearShift.setGearHigh(true);
        } else if (mainController.getAButton()) {
            gearShift.setGearHigh(false);
        }
    }

    @Override
    protected void methodStop() {
        
    }
    
}