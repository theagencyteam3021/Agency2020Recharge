package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Shooter extends AgencySystem{
    protected Boolean shotRequested = false;
    public enum ShooterAngle{SHORT,LONG};

    private CANSparkMax m_motorL;
    private CANSparkMax m_motorR;
    private CANSparkMax m_angle;


    public Shooter(int leftMotor, int rightMotor, int angleID, String name,
            Boolean debug) {

        this.name = name;
        this.debug = debug;

        m_motorL = new CANSparkMax(leftMotor, MotorType.kBrushless);
        m_motorR = new CANSparkMax(rightMotor, MotorType.kBrushless);
        m_angle = new CANSparkMax(angleID, MotorType.kBrushless);

        m_motorL.restoreFactoryDefaults();
        m_motorR.restoreFactoryDefaults();
        m_angle.restoreFactoryDefaults();

    }

    public void requestStart() {

        this.shotRequested = true;

    }

    public void requestStop() {
        this.shotRequested = false;
    }

    public void requestAngleChange(ShooterAngle requestedAngle) {

        //???? por que = requestedAngle;
        
    }

    public void teleopPeriodic() {
        //TODO: one motor needs to be spinning in reverse
        /*
        if (shotRequested) {
            m_motorL.set(.65);
            m_motorR.set(.65);
        }
        else {
            m_motorL.set(0);
            m_motorR.set(0);
        }
        */

    }

    
}