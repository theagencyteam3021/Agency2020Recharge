package frc.robot;

import frc.robot.AgencySystem;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class Shooter extends AgencySystem{
    protected Boolean shotRequested = false;
    public enum ShooterAngle{SHORT,LONG};

    private CANSparkMax m_motor1;
    private CANSparkMax m_motor2;
    private CANSparkMax m_angle;


    public Shooter(int deviceID1, int deviceID2, int angleID, String name,
            Boolean debug) {

        this.name = name;
        this.debug = debug;

        m_motor1 = new CANSparkMax(deviceID1, MotorType.kBrushless);
        m_motor2 = new CANSparkMax(deviceID2, MotorType.kBrushless);
        m_angle = new CANSparkMax(angleID, MotorType.kBrushless);

        m_motor1.restoreFactoryDefaults();
        m_motor2.restoreFactoryDefaults();
        m_angle.restoreFactoryDefaults();

    }

    public void requestShot() {

        this.shotRequested = true;

    }

    public void requestStop() {
        this.shotRequested = false;
    }

    public void requestAngleChange(ShooterAngle requestedAngle) {

        //???? por que = requestedAngle;

    }

    public void teleopPeriodic() {
        if (shotRequested) {
            m_motor1.set(.65);
            m_motor2.set(.65);
        }
        else {
            m_motor1.set(0);
            m_motor2.set(0);
        }

    }

    
}