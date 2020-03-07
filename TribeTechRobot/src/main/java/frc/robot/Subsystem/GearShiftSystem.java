package frc.robot.Subsystem;

import edu.wpi.first.wpilibj.Solenoid;
import frc.TribeTech.Subsystem;

public class GearShiftSystem extends Subsystem {

    private Solenoid gearShiftSolenoid[] = new Solenoid[2];

    private boolean _highGear = false;

    public void setGearHigh(boolean high) {
        _highGear = false;
    }
    public boolean isHighGear() {
        return _highGear;
    }

    @Override
    protected void initSystem() {
        gearShiftSolenoid[0] = new Solenoid(map.pcm_ID, map.gearShiftSolenoid_ID[0]);
        gearShiftSolenoid[1] = new Solenoid(map.pcm_ID, map.gearShiftSolenoid_ID[1]);
    }

    @Override
    protected void updateSystem() {
        gearShiftSolenoid[0].set(!_highGear);
        gearShiftSolenoid[1].set(_highGear);
    }

    @Override
    protected void stopSystem() {
        
    }
    
}