package frc.robot.Control;

import frc.TribeTech.IterativeControlMethod;
import frc.robot.Subsystem.BallDeliverySystem;
import frc.robot.Subsystem.BallShooterSystem;
import frc.robot.Subsystem.TankDriveSystem;

public class AutoShooterTest extends IterativeControlMethod {

    private TankDriveSystem drive = new TankDriveSystem();
    private BallShooterSystem shooter = new BallShooterSystem();
    private BallDeliverySystem ballDelivery = new BallDeliverySystem();

    @Override
    protected void methodInit() {
        map.registerSystem(drive);
        map.registerSystem(shooter);
        map.registerSystem(ballDelivery);

        map.initSystems();
    }

    @Override
    protected void methodUpdate() {
        switch (step()) {
        case 0:
            drive.setTankDriveInput(-0.05, -0.05);
            setWaitDuration(1000);
            break;
        case 1:
            drive.setTankDriveInput(0, 0);
            break;
        case 2:
            // Spin up the shooter
            shooter.setTargetRPM(3000);
            if (!shooter.isOnTarget()) revertStep();    // Wait until the shooter starts
            break;
        case 3:
            ballDelivery.deliverBalls();
            break;
        case 4:
            if (ballDelivery.isDeliveringBalls()) revertStep();     // Wait until the delivery system is done
            if (!ballDelivery.isEmpty()) revertStep(2);
            break;
        case 5:
            shooter.setTargetRPM(0);
            break;
        default:
            break;
        }
    }

    @Override
    protected void methodStop() {
    }
    
}