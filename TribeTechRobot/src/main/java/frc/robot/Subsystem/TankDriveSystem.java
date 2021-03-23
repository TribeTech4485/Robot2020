package frc.robot.Subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.TribeTech.Subsystem;

public class TankDriveSystem extends Subsystem {

    private CANSparkMax[] leftDriveMotor = new CANSparkMax[map.leftDriveMotor_ID.length];
    private CANSparkMax[] rightDriveMotor = new CANSparkMax[map.rightDriveMotor_ID.length];

    private double[] rawTankDriveInput = {0.0, 0.0};  // Left, Right


    // TankDrive methods
    public void setTankDriveInput(double left, double right) {
        left = left * 0.60;
        right = right * 0.60;
        rawTankDriveInput[0] = left;
        rawTankDriveInput[1] = right;
    }

    private void setDrive(CANSparkMax[] motors, double setVal) {
        for (int i = 0; i < motors.length; i++) {
            motors[i].set(setVal);
        }
    }


    // Subsystem methods
    @Override
    protected void initSystem() {
        for (int i = 0; i < leftDriveMotor.length; i++) {
            leftDriveMotor[i] = new CANSparkMax(map.leftDriveMotor_ID[i], MotorType.kBrushless);
            leftDriveMotor[i].setOpenLoopRampRate(1);
        }
        for (int i = 0; i < rightDriveMotor.length; i++) {
            rightDriveMotor[i] = new CANSparkMax(map.rightDriveMotor_ID[i], MotorType.kBrushless);
        }
    }

    @Override
    protected void updateSystem() {
        setDrive(leftDriveMotor, rawTankDriveInput[0]);
        setDrive(rightDriveMotor, -rawTankDriveInput[1]);
    }

    @Override
    protected void stopSystem() {
        setDrive(leftDriveMotor, 0);
        setDrive(rightDriveMotor, 0);
    }
    
}