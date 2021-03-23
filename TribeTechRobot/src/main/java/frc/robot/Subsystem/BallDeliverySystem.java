package frc.robot.Subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.Joystick;

import frc.TribeTech.Subsystem;

public class BallDeliverySystem extends Subsystem{

    //// Includes both the collector and conveyor systems
    private CANSparkMax collectorMotor;
    private CANSparkMax conveyorMotor;
    private DigitalInput collectorBallSensor;
    private DigitalInput[] conveyorBallSensor = new DigitalInput[map.conveyorBallSensor_DIGITAL.length];
    
    private boolean _collecting = false;
    private boolean _conveying = false;
    private boolean _expelling = false;
    private boolean _delivering = false;
    private boolean _finishedDelivering = false;
    //private int _conveyToPosition = 0;

    private double _collectSpeed = -0.3;
    private double _conveySpeed = 1.0;

    private int _totalBallsCollected = 0;
    private int _totalBallsConveyed = 0;
    
    private boolean[] _ballInPosition = new boolean[map.conveyorBallSensor_DIGITAL.length + 1];
    //private boolean[] _ballInPositionLast = new boolean[_ballInPosition.length];

    private static final Joystick auxController = new Joystick(1);    // aux controller setup
    // Logitech Gamepad buttons
    public static final int kLogiTechButtonA = 1; // Bottom Button
    public static final int kLogiTechButtonB = 2; // Right Button
    //public static final int kLogiTechButtonX = 3; // Left Button
    //public static final int kLogiTechButtonY = 4; // Top Button
    //public static final int kLogiTechBumperLeft = 5; // on front of controller
    //public static final int kLogiTechBumperRight = 6;
    //public static final int kLogiTechStickLeft = 7;  // on front of controller
    //public static final int kLogiTechStickRight = 8;

    private static boolean auxButtonAToggle = true;  // keeps track of when button pressed and repressed
    private static boolean auxButtonBToggle = true;
    private static boolean conveyorOn = true;	//start with true so 1st press turns on
    private static boolean collectorOn = true;

     public int getBallsCollected() {
        return _totalBallsCollected;
    }
    public int getBallsConveyed() {
        return _totalBallsConveyed;
    }
    public boolean isEmpty() {
        boolean empty = true;
        for (boolean position : _ballInPosition) {
            if (position) empty = false;
            break;
        }        
        return empty;
    }
    public boolean isDeliveringBalls() {
        return _delivering;
    }

    public void setCollector(boolean collecting) {
        _collecting = collecting;
    }
    public void setManualConvey(boolean conveying) {
        if (map.deliverySensorsConnected) return;
        _conveying = conveying;
    }
    public void setExpelling(boolean expelling) {
        _expelling = expelling;
    }
    public void deliverBalls() {
        if (_finishedDelivering) return;
        _delivering = true;
    }
    public void cancelDeliverBalls() {
        _delivering = false;
        stopWait();
    }

    @Override
    protected void initSystem() {
        collectorMotor = new CANSparkMax(map.collectorMotor_ID, MotorType.kBrushless);
        conveyorMotor = new CANSparkMax(map.conveyorMotor_ID, MotorType.kBrushless);

        collectorBallSensor = new DigitalInput(map.collectorBallSensor_DIGITAL);
        for (int i = 0; i < conveyorBallSensor.length; i++) {
            conveyorBallSensor[i] = new DigitalInput(map.conveyorBallSensor_DIGITAL[i]);
        }
    }

    @Override
    protected void updateSystem() {

        //** Larry 
        //https://www.chiefdelphi.com/t/java-toggle-button/122156
        boolean auxButtonAPressed = auxController.getRawButtonPressed(kLogiTechButtonA);  // check if button pressed 
        if (auxButtonAToggle && auxButtonAPressed) {  	// Only execute once per Button push
            auxButtonAToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (conveyorOn) {  // Decide which way to set the motor this time through
                conveyorOn = false;
                conveyorMotor.set(_conveySpeed);    // turn on conveyor motor
            } else {
                conveyorOn = true;
                conveyorMotor.set( 0 );     // turn off conveyor motor
            }
        } else if (!auxButtonAPressed) { 
            auxButtonAToggle = true; // Button has been released, so allows button re-press to activate code above
        }

        boolean auxButtonBPressed = auxController.getRawButtonPressed(kLogiTechButtonB);  // check if button pressed 
        if (auxButtonBToggle && auxButtonBPressed) {  	// Only execute once per Button push
            auxButtonBToggle = false;  // Prevents this section of code from being called again until Button is released and re-pressed
            if (collectorOn) {  // Decide which way to set the motor this time through
                collectorOn = false;
                collectorMotor.set(_collectSpeed);  // turn on collector motor
            } else {
                collectorOn = true;
                collectorMotor.set(0);  // turn on collector motor
            }
        } else if (!auxButtonBPressed) { 
            auxButtonBToggle = true; // Button has been released, so allows button re-press to activate code above
        }
     
        /*
        // ***** remove this after test
        auxButtonAPressed = auxController.getRawButtonPressed(kLogiTechButtonA);
        if (auxButtonAPressed) {
            auxButtonACount++;
            if ( auxButtonACount % 2 == 0 ) {   
                conveyorMotor.set(_conveySpeed);    // turn on motor every even count
            } else {
                conveyorMotor.set( 0 );
            }
        }
        //** end Larry

        // Update the last Ball Position array
        _ballInPositionLast = _ballInPosition;
        /*
        // Update the ball position array
        _ballInPosition[0] = collectorBallSensor.get();
        for (int i = 0; i < conveyorBallSensor.length; i++) {
            _ballInPosition[i + 1] = conveyorBallSensor[i].get();   // The ball position is shifted by +1 because of the collector sensor
        }
        
        if (!_ballInPositionLast[0] && _ballInPosition[0]) _totalBallsCollected++;
        if (!_ballInPositionLast[_ballInPositionLast.length - 1] && _ballInPosition[_ballInPosition.length - 1]) _totalBallsConveyed++;

        // Check if we should iterrate the conveyor
        if (_ballInPosition[0]) {
            // Check if there are any free spots
            int openPosition = 0;
            for (int i = 0; i < _ballInPosition.length; i++) {
                // check if the current position is open, also check if there is a ball in the top position
                if (!_ballInPosition[i] && !_ballInPosition[_ballInPosition.length - 1]) openPosition = i;
                break;
            }
            _conveyToPosition = openPosition;
        }
        _conveying = false;
        if (_conveyToPosition > 0 && !_ballInPosition[_conveyToPosition]) {
            _conveying = true;
            _collecting = true;
        } else if (_ballInPosition[0]) {
            _collecting = false;
        }

        if (_delivering) {
            _conveying = true;
            _collecting = true;

            boolean ballsDelivered = true;
            for (boolean position : _ballInPosition) {
                if (position) ballsDelivered = false;
                break;
            }
            if (!map.deliverySensorsConnected) ballsDelivered = true;
            if (ballsDelivered) {
                _delivering = false;
                _finishedDelivering = true;
                startWait(map.deliverySensorsConnected ? 300 : 5000); // continue delivering for a little bit to make sure the balls are gone. If there are no sensors run for a few seconds
            }
        } else {
            _finishedDelivering = false;
        }
        
        if (_expelling) {
            collectorMotor.set(-_collectSpeed);
            conveyorMotor.set(-_conveySpeed);
        } else {
            if (_collecting) collectorMotor.set(_collectSpeed);
            else collectorMotor.set(0);

            if (_conveying) conveyorMotor.set(_conveySpeed);
            else conveyorMotor.set(0);
        }
        */
    }
   
    // larry
    public void turnOnConveyor() {
        conveyorMotor.set(_conveySpeed); 
    }
    public void turnOffConveyor() {
        conveyorMotor.set( 0 ); 
    }
    public void turnOnCollector() {
        collectorMotor.set(_collectSpeed);
    }
    public void turnOffCollector() {
        collectorMotor.set( 0 ); 
    }

    @Override
    protected void stopSystem() {
    
    }

}