package frc.TribeTech.PID;


public class PIDController {
	
	private SPID pid;
	
	double lastReturn = 0;
	
	// Delay for consistency
	private double delay = 10;	//10 ms
	private double startTime = -1;
	private double duration = -1;
	
	public PIDController() {
		pid = new SPID();
		duration = delay;
		checkTime();
	}
	
	public void setSPID(SPID _pid) {
		pid = _pid;
	}
	
	public SPID getSPID() {
		return pid;
	}
	
	private boolean checkTime() {
		if (startTime < 0) startTime = System.currentTimeMillis();
		duration = System.currentTimeMillis() - startTime;
		if (duration >= delay) {
			duration = -1;
			startTime = -1;
			return true;
		}
		return false;
	}
	
	public double update(double error, double position) {
		if (!checkTime()) return lastReturn;
		//else System.out.println("PID update tick!");
		double pTerm,
			dTerm, iTerm;
		
		pTerm = pid.pGain * error;	// Calculate the proportional term
		
		pid.iState += error;	// Check to make sure this isn't resetting every cycle
		if (pid.iState > pid.iMax) pid.iState = pid.iMax;
		if (pid.iState < pid.iMin) pid.iState = pid.iMin;
		iTerm = pid.iGain * pid.iState;	// Calculate the integral term
		
		dTerm = pid.dGain * (position - pid.dState);
		pid.dState = position;	// Also check to make sure this isn't resetting every cycle
		
		double returnVal = pTerm + iTerm - dTerm;
		lastReturn = returnVal;
		return returnVal;
	}
	
}
