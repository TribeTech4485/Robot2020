/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.TribeTech.Subsystem;

public class HookSystem extends Subsystem {
    // motors for Hook subsystem
    public CANSparkMax hookMotorLower;    // Declare hook motors
    public CANSparkMax hookMotorRaise;
    public CANEncoder hookMotorLowerEncoder;   // Declare hook encoders
    public CANEncoder hookMotorRaiseEncoder;

    public static final double encoderTicksPerRevolution = 42.0;
    public static final int kHookTickHigh = 4500;
    public static final int kHookTickLow = 0;

    private static final Joystick auxController = new Joystick(1);    // aux controller setup

    // Logitech Gamepad axis
    //public static final int kLogiTechAxisLeftStickX = 1;
    //public static final int kLogiTechAxisLeftStickY = 2;
    //public static final int kLogiTechAxisShoulder = 3;
    //public static final int kLogiTechAxisRightStickX = 4;
    //public static final int kLogiTechAxisRightStickY = 5;
    //public static final int kLogiTechAxisDpad = 6;
    
    // Logitech Gamepad buttons
    //public static final int kLogiTechButtonA = 1; // Bottom Button
    //public static final int kLogiTechButtonB = 2; // Right Button
    public static final int kLogiTechButtonX = 3; // Left Button
    public static final int kLogiTechButtonY = 4; // Top Button
    public static final int kLogiTechBumperLeft = 5; // on front of controller
    public static final int kLogiTechBumperRight = 6;
    //public static final int kLogiTechStickLeft = 7;  // on front of controller
    //public static final int kLogiTechStickRight = 8;
    //public static final int kLogiTechButtonBack = 9;  // Small button top left
    //public static final int kLogiTechButtonStart = 10; // Small button top right

    private static boolean auxButtonXToggle = true; // keeps track of when button pressed and repressed
    private static boolean auxButtonYToggle = true;
    private static boolean auxBumperLeftToggle = true;
    //private static boolean auxBumperRightToggle = true;
    private static boolean hookMotorRaiseOn = true;	// start with true so 1st press turns on
    private static boolean hookMotorLowerOn = true;
    private static boolean hookMotorRaiseReverseOn = true;
    //private static boolean hookMotorLowerReverseOn = true;

    private static int kAmpsMax = 40;

    @Override
    protected void initSystem() {
        // Initialize HookSubsystem motors
        hookMotorLower = new CANSparkMax(map.hookLowerMotor_ID, MotorType.kBrushless);
        hookMotorRaise = new CANSparkMax(map.hookRaiseMotor_ID, MotorType.kBrushless);
        hookMotorLower.restoreFactoryDefaults();
        hookMotorRaise.restoreFactoryDefaults();
    
        hookMotorLower.setInverted(false);    // Set direction
        hookMotorRaise.setInverted(false);
        //hookMotorLower.setIdleMode(IdleMode.kCoast);    // Set idle mode
        //hookMotorRaise.setIdleMode(IdleMode.kCoast);
        hookMotorLower.setIdleMode(IdleMode.kBrake);    // Set idle mode
        hookMotorRaise.setIdleMode(IdleMode.kBrake);

        hookMotorLower.setSmartCurrentLimit(kAmpsMax);
        hookMotorRaise.setSmartCurrentLimit(kAmpsMax);
    
        // Initialize encoders
        hookMotorLowerEncoder = new CANEncoder(hookMotorLower);
        hookMotorRaiseEncoder = new CANEncoder(hookMotorRaise);
        hookMotorLowerEncoder.setPositionConversionFactor(encoderTicksPerRevolution);
        hookMotorRaiseEncoder.setPositionConversionFactor(encoderTicksPerRevolution);
        hookMotorLowerEncoder.setPosition(0);
        hookMotorRaiseEncoder.setPosition(0);
    }

    @Override
    protected void updateSystem() {

        //https://www.chiefdelphi.com/t/java-toggle-button/122156
        // Logitech Button X controls motor to raise hook
        boolean auxButtonXPressed = auxController.getRawButtonPressed(kLogiTechButtonX);  // check if button pressed 
        if (auxButtonXToggle && auxButtonXPressed) {  	// Only execute once per Button push
            auxButtonXToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (hookMotorRaiseOn) {  // Decide which way to set the motor this time through
                hookMotorRaiseOn = false;
                hookMotorRaise.set(0.20);      // turn on raise motor
                hookMotorLower.setIdleMode(IdleMode.kCoast); // Set idle mode so can be lowered
            } else {
                hookMotorRaiseOn = true;
                hookMotorRaise.set(0);
            }
        } else if (!auxButtonXPressed) { 
            auxButtonXToggle = true; // Button has been released, so allows button re-press to activate code above
        }

        // Logitech Button Y controls lowering of raise motor to lower plastic pipe
        boolean auxButtonYPressed = auxController.getRawButtonPressed(kLogiTechButtonY);  // check if button pressed 
        if (auxButtonYToggle && auxButtonYPressed) {  	// Only execute once per Button push
            auxButtonYToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (hookMotorRaiseReverseOn) {  // Decide which way to set the motor this time through
                hookMotorRaiseReverseOn = false;
                //hookMotorRaise.setInverted(true);  
                hookMotorRaise.set(-0.50);  // reverse motor so will lower plastic pipe
            } else {
                hookMotorRaiseReverseOn = true;
                hookMotorRaise.set(0);
            }
        } else if (!auxButtonYPressed) { 
            auxButtonYToggle = true; // Button has been released, so allows button re-press to activate code above
        }

        // Logitech left bumper controls motor to lower hook which raises entire robot
        boolean auxBumperLeftPressed = auxController.getRawButtonPressed(kLogiTechBumperLeft);  // check if button pressed 
        if (auxBumperLeftToggle && auxBumperLeftPressed) {  	// Only execute once per Button push
            auxBumperLeftToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (hookMotorLowerOn) {  // Decide which way to set the motor this time through
                hookMotorLowerOn = false;
                hookMotorLower.set(0.50);   // turn on lower motor
                hookMotorRaise.setIdleMode(IdleMode.kCoast); // Set idle mode so can be lowered
            } else {
                hookMotorLowerOn = true;
                hookMotorLower.set(0);
            }
        } else if (!auxBumperLeftPressed) {
            auxBumperLeftToggle = true; // Button has been released, so allows button re-press to activate code above
        }

        /*
        // Logitech right bumper controls motor to raise to be lowered after match - disabled by Dylan request
        boolean auxBumperRightPressed = auxController.getRawButtonPressed(kLogiTechBumperRight);  // check if button pressed 
        if (auxBumperRightToggle && auxBumperRightPressed) {  	// Only execute once per Button push
            auxBumperRightToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (hookMotorLowerReverseOn) {  // Decide which way to set the motor this time through
                hookMotorLowerReverseOn = false;
                hookMotorLower.set(-0.50);   // turn on lower motor
            } else {
                hookMotorLowerReverseOn = true;
                hookMotorLower.set(0);
            }
        } else if (!auxBumperRightPressed) {
            auxBumperRightToggle = true; // Button has been released, so allows button re-press to activate code above
        }
        */

        // check voltage, temperature
        SmartDashboard.putNumber("Voltage Lower", hookMotorLower.getBusVoltage());
        SmartDashboard.putNumber("Temp Lower", hookMotorLower.getMotorTemperature());
        SmartDashboard.putNumber("Output Lower", hookMotorLower.getAppliedOutput());
        SmartDashboard.putNumber("Current Lower", hookMotorLower.getOutputCurrent());

    }

    @Override
    protected void stopSystem() {
      
    }
}
