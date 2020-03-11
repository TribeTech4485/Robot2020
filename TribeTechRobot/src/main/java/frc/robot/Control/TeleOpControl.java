package frc.robot.Control;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.TribeTech.IterativeControlMethod;
import frc.robot.Subsystem.BallDeliverySystem;
import frc.robot.Subsystem.BallShooterSystem;
import frc.robot.Subsystem.GearShiftSystem;
import frc.robot.Subsystem.TankDriveSystem;

public class TeleOpControl extends IterativeControlMethod {

    // Driver controllers
    private XboxController mainController = new XboxController(0);

    // Declare subsystems used in this method
    private TankDriveSystem drive = new TankDriveSystem();
    private GearShiftSystem gearShift = new GearShiftSystem();
    private BallShooterSystem shooter = new BallShooterSystem();
    private BallDeliverySystem delivery = new BallDeliverySystem();

    private PowerDistributionPanel pdp;

    private List<Double> leftDriveValues = new ArrayList<Double>();
    private List<Double> rightDriveValues = new ArrayList<Double>();

    @Override
    protected void methodInit() {
        // Register and initialize the subsystems with the robot map
        map.registerSystem(drive);
        map.registerSystem(gearShift);
        map.registerSystem(shooter);
        map.registerSystem(delivery);
        
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

        if (mainController.getBumper(Hand.kRight)) {
            shooter.setTargetRPM(5000);
            if (shooter.isOnTarget()) {
                System.out.println("Shooter On Target");
                //delivery.deliverBalls();
            }
        } else {
            delivery.cancelDeliverBalls();
            shooter.setTargetRPM(0);
        }

        delivery.setCollector(mainController.getBumper(Hand.kLeft));

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