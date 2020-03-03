package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Elevator extends AgencySystem{
    protected Boolean outtakeRequested = false;
    protected Boolean elevateRequested = false;

    public Elevator(int deviceID1, int deviceID2, String name, Boolean debug){
    
        this.name = name;
        this.debug = debug;
    
    }

    public Boolean hasBall() {
        return false;
    }

    public Boolean isDown() {
        return false;
    }

    public void requestOuttake() {

    }

    public void requestElevate() {

    }

    public void teleopPeriodic() {

    }

}
