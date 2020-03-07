package frc.TribeTech;

public abstract class IterativeControlMethod {
    protected RobotMap map = RobotMap.getInstance();
    
    private double _startTime = -1;
    private double _waitEndTime = 0;
    private boolean _running = false;
    
    private int _controlStep = 0;
    
    
    public IterativeControlMethod() {
        
    }

    public void init() {
        _running = true;
        methodInit();
    }
    public void update() {
        if (_startTime < 0) _startTime = System.currentTimeMillis();
        if (!_checkDoUpdate()) return;
        methodUpdate();
    }
    public void stop() {
        _startTime = -1;
        _waitEndTime = 0;
        _running = false;
        _controlStep = 0;
        methodStop();
    }
    
    public double getRunDuration() {
        return System.currentTimeMillis() - _startTime;
    }
    public boolean isRunning() {
        return _running;
    }
    
    private boolean _checkDoUpdate() {
        return (_waitEndTime < getRunDuration());
    }
    
    protected void setWaitDuration(final double duration_milliseconds) {
        _waitEndTime = getRunDuration() + duration_milliseconds;
    }
    // Step control helper functions
    protected void setStep(int step) {
        _controlStep = step;
    }
    protected int getCurrentStep() {
        return _controlStep;
    }
    protected int step() {
        // return the current step and iterrate the current control step
        return _controlStep++;
    } 

    protected abstract void methodInit();
    protected abstract void methodUpdate();
    protected abstract void methodStop();
}