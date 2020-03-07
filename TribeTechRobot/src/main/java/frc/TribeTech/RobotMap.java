package frc.TribeTech;

import java.util.List;

import java.util.ArrayList;

public class RobotMap {
    //////////////////////////////////////////////////////////////////////DON'T MODIFY//////////////////////////////////////////////////////////////////////
    // Static instance of robot map
    private static RobotMap instance = null;
    public static RobotMap getInstance() {
        if (instance == null) {
            instance = new RobotMap();
        }
        return instance;
    }

    // Subsystem methods
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
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////// Quick change hardware IDs //////////////////////////////////////////////////////////////////////
    //// Motor IDs
    public int[] leftDriveMotor_ID = {4, 5, 6};
    public int[] rightDriveMotor_ID = {1, 2, 3};

    public int[] shooterMotor_ID = {8, 9};   // Left, Right

    public int collectorMotor_ID = 7;
    public int conveyorMotor_ID = 10;

    //// Pneumatic IDs
    public int pcm_ID = 0;
    public int[] gearShiftSolenoid_ID = {0, 1};   // in, out

    //// Digital Sensor IDs

    //// Analog Sensor IDs

    //// I2C Sensor addresses
    
    // TODO add hardware ids for other subsystems
}