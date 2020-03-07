package frc.robot.Subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.TribeTech.PID.*;
import frc.TribeTech.Subsystem;

public class BallShooterSystem extends Subsystem {

    private int numShooterMotors = map.shooterMotor_ID.length;

    private SPID defaultSPID[] = new SPID[numShooterMotors];

    private CANSparkMax shooterMotor[] = new CANSparkMax[numShooterMotors];
    private CANEncoder shooterEncoder[] = new CANEncoder[numShooterMotors];
    private PIDController shooterPID[] = new PIDController[numShooterMotors];

    private double _targetRPM = 0;

    public void setTargetRPM(double target) {
        _targetRPM = target;
    }

    @Override
    protected void initSystem() {
        for (int i = 0; i < numShooterMotors; i++) {
            defaultSPID[i] = new SPID();
            defaultSPID[i].iMin = -1.0;
            defaultSPID[i].iMax = -1.0;
            defaultSPID[i].pGain = 0.0003;
            defaultSPID[i].iGain = 0.893562;
            defaultSPID[i].dGain = 0;
            shooterPID[i] = new PIDController();
            shooterPID[i].setSPID(defaultSPID[i]);
        
            shooterMotor[i] = new CANSparkMax(map.shooterMotor_ID[i], MotorType.kBrushless);
            shooterEncoder[i] = new CANEncoder(shooterMotor[i]);
        }
    }

    @Override
    protected void updateSystem() {
        for (int i = 0; i < numShooterMotors; i++) {
            double setVal = 0;

            if (_targetRPM != 0) {
                double targetVelocity = _targetRPM;
                if (i % 2 == 1) targetVelocity *= -1;

                double currentVelocity = shooterEncoder[i].getVelocity();
                double error = targetVelocity - currentVelocity;

                // Live PID Tuning hack start
                SPID shooterSPID = shooterPID[i].getSPID();
                shooterSPID.iGain = Math.abs(targetVelocity) * 0.000163236;
                shooterPID[i].setSPID(shooterSPID);

                setVal = shooterPID[i].update(error, currentVelocity);
                if ((setVal / Math.abs(setVal)) / (targetVelocity / Math.abs(targetVelocity)) != 1) setVal = 0;

                if (setVal > 1) setVal = 1;
                if (setVal < -1) setVal = -1;
            }

            shooterMotor[i].set(setVal);
        }
    }

    @Override
    protected void stopSystem() {
        for (int i = 0; i < numShooterMotors; i++) {
            shooterMotor[i].set(0);
        }
    }
    
}