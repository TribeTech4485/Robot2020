package frc.TribeTech;

public abstract class Subsystem {
    // Get the robot map instance
    protected RobotMap map = RobotMap.getInstance();

    //
    private boolean _started = false;
    protected boolean enabled = true;
    protected boolean running = false;
    protected boolean waiting = false;
    protected boolean stopped = false;

    // Error Values
    protected boolean error = false;
    protected int numErrors = 0;

    // Time values
    private double _timeStart = -1;
    private double _timeDuration = 0;

    private double _timeWaitEnd = 0;

    
    public Subsystem() {
        _timeStart = -1;
        _timeDuration = 0;
    }
    
    protected void startWait(double duration_millisecond) {
        _timeWaitEnd = _timeDuration + duration_millisecond;
    }
    protected void stopWait() {
        _timeWaitEnd = 0;
    }
    private void updateTime() {
        if (_timeStart < 0) return;
        _timeDuration = System.currentTimeMillis() - _timeStart; 

        waiting = (_timeWaitEnd < _timeDuration);
    }
    private boolean checkDoUpdate() {
        return (_started && running && enabled && !waiting && !stopped);
    }

    public void init() {
        if (!enabled) return;

        start();
        initSystem();
    }
    public void start() {
        _started = true;
        running = true;
        _timeStart = System.currentTimeMillis();
    }
    public void update() {
        updateTime();

        checkDoUpdate();
        updateSystem();
    }
    public void stop() {
        if (!enabled) return;
        stopped = true;

        stopSystem();
    }

    protected abstract void initSystem();
    protected abstract void updateSystem();
    protected abstract void stopSystem();

}