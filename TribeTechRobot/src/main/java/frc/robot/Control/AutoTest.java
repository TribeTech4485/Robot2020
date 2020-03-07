package frc.robot.Control;

import frc.TribeTech.IterativeControlMethod;
import frc.robot.Subsystem.TankDriveSystem;

public class AutoTest extends IterativeControlMethod {

    private TankDriveSystem drive = new TankDriveSystem();

    @Override
    protected void methodInit() {
        map.registerSystem(drive);

        map.initSystems();
    }

    @Override
    protected void methodUpdate() {
        switch (step()) {
        case 0:
            // Drive forward at 10% speed for 250 ms
            drive.setTankDriveInput(1, 1);
            setWaitDuration(500);
            break;
        case 1:
            // Drive backward at 10% speed for 250 ms
            drive.setTankDriveInput(0, 0);
            setWaitDuration(2000);
            break;
        case 2:
            drive.setTankDriveInput(-1, -1);
            setWaitDuration(500);
            break;
        default:
            drive.setTankDriveInput(0, 0);
        }
    }

    @Override
    protected void methodStop() {
        System.out.println("Auto Stop");
    }
    
}