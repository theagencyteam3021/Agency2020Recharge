package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Shooter extends AgencySystem{
    protected Boolean shotRequested = false;
    public enum ShooterAngle{SHORT,LONG};


    public Shooter(int deviceID1, int deviceID2, int deviceID3, String name,
            Boolean debug) {

        this.name = name;
        this.debug = debug;

    }

    public void requestShot() {

    }

    public void requestAngleChange(ShooterAngle requestedAngle) {

    }

    public void teleopPeriodic() {

    }

    
}