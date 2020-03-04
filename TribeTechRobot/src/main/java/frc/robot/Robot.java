/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.TribeTech.IterativeControlMethod;
import frc.TribeTech.RobotMap;
import frc.robot.Control.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */

    private RobotMap robotMap = RobotMap.getInstance();

    private IterativeControlMethod teleOpControlMethod = new TeleOpControl();

    SendableChooser<IterativeControlMethod> autoChooser = new SendableChooser<IterativeControlMethod>();
    private IterativeControlMethod defaultAutoControlMethod = null;
    private IterativeControlMethod selectedAutoControlMethod = defaultAutoControlMethod;

	@Override
	public void robotInit() {
        // Create an auto selector on the smart dashboard
        autoChooser.setDefaultOption("Default (None)", defaultAutoControlMethod);
        autoChooser.addOption("Auto Test", new AutoTest());
        SmartDashboard.putData("Auto Selector", autoChooser);
    }

	@Override
	public void robotPeriodic() {
        robotMap.updateSystems();
	}

	@Override
	public void autonomousInit() {
        // Get the selected auto method and initialize it
        selectedAutoControlMethod = autoChooser.getSelected();
        if (selectedAutoControlMethod != null) {
            selectedAutoControlMethod.init();
        }

        robotMap.startSystems();
	}

	@Override
	public void autonomousPeriodic() {
        if (selectedAutoControlMethod != null) {
            selectedAutoControlMethod.update();
        }
	}

	@Override
	public void teleopInit() {
        teleOpControlMethod.init();
        robotMap.startSystems();
	}
	@Override
	public void teleopPeriodic() {
        teleOpControlMethod.update();
    }

	@Override
	public void disabledInit() {
        teleOpControlMethod.stop();
        if (selectedAutoControlMethod != null) selectedAutoControlMethod.stop();
        robotMap.stopSystems();
	}

	@Override
	public void disabledPeriodic() {
    }

	@Override
	public void testInit() {
        robotMap.startSystems();
	}

	@Override
	public void testPeriodic() {
	}

}
