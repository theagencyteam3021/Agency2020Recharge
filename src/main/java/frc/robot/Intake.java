package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Intake extends AgencySystem{
    protected Boolean advanceRequested = false;

    public Intake(int deviceID1, int deviceID2, String name, Boolean debug){
    
        this.name = name;
        this.debug = debug;
    
    }

    public void requestStart() {
    
    }
    public void requestStop() {

    }

    public Boolean hasBall() {
        return false;
    }

    //advance only if there is a ball, separate the speed control groups
    //maybe make them both have the same forward limit switch instead of speed control groups

    public void advance() {
        advanceRequested = true;
    }

    public void teleopPeriodic() {

    }

}