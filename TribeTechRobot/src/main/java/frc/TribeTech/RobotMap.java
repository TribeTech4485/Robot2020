package frc.TribeTech;

import java.util.List;
import java.util.ArrayList;

public class RobotMap {
    private static RobotMap instance = null;
    public static RobotMap getInstance() {
        if (instance == null) {
            instance = new RobotMap();
        }
        return instance;
    }

    private List<Subsystem> subsystems = new ArrayList<Subsystem>();
    public void registerSystem(Subsystem system) {
        subsystems.add(system);
    }
    public void destroySystems() {
        subsystems.clear();
    }
    public void initSystems() {
        for (Subsystem system : subsystems) {
            system.init();
        }
    }
    public void startSystems() {
        for (Subsystem system : subsystems) {
            system.start();
        }
    }
    public void updateSystems() {
        for (Subsystem system : subsystems) {
            system.update();
        }
    }
    public void stopSystems() {
        for (Subsystem system : subsystems) {
            system.stop();
        }
    }

    public int[] leftDriveMotor_ID = {1, 2, 3};
    public int[] rightDriveMotor_ID = {4, 5, 6};

    public int[] shooterMotor_ID = {8, 9};   // Left, Right
    
}