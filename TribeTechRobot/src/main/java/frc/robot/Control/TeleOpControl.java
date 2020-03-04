package frc.robot.Control;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.TribeTech.IterativeControlMethod;
import frc.robot.Subsystem.TankDriveSystem;

public class TeleOpControl extends IterativeControlMethod {

    // Driver controllers
    private XboxController mainController = new XboxController(0);

    // Declare subsystems used in this method
    private TankDriveSystem drive = new TankDriveSystem();

    @Override
    protected void methodInit() {
        // Register and initialize the subsystems with the robot map
        map.registerSystem(drive);
        
        map.initSystems();
    }

    @Override
    protected void methodUpdate() {
        // Get the drive values from the main controller
        double leftDrive = mainController.getY(Hand.kLeft);
        double rightDrive = mainController.getY(Hand.kRight);
        drive.setTankDriveInput(leftDrive, rightDrive);
    }

    @Override
    protected void methodStop() {
        
    }
    
}